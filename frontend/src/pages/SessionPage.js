import React, { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';

/**
 * Live Session Page — WebRTC peer connection via Spring Boot WebSocket signaling
 *
 * Flow:
 * 1. Open WebSocket to /ws/session/{bookingId}
 * 2. Send JOIN message
 * 3. On PEER_JOINED: create RTCPeerConnection, send OFFER
 * 4. On OFFER: create answer, send ANSWER
 * 5. ICE candidates exchanged via TURN relay (Coturn)
 */
const TURN_URL = process.env.REACT_APP_TURN_URL ?? 'turn:localhost:3478';
const WS_BASE  = process.env.REACT_APP_WS_URL  ?? 'ws://localhost:8086';

export default function SessionPage({ user }) {
  const { bookingId }  = useParams();
  const userId         = user?.userId ?? user?.username;

  const localVideoRef  = useRef(null);
  const remoteVideoRef = useRef(null);
  const canvasRef      = useRef(null);
  const wsRef          = useRef(null);
  const pcRef          = useRef(null);

  const [connected, setConnected] = useState(false);
  const [drawing, setDrawing]     = useState(false);
  const [lastPos, setLastPos]     = useState(null);

  useEffect(() => {
    initSession();
    return () => cleanup();
  }, [bookingId]);

  async function initSession() {
    // Get local camera + mic
    const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
    localVideoRef.current.srcObject = stream;

    // Connect WebSocket to signaling server
    const ws = new WebSocket(`${WS_BASE}/ws/session/${bookingId}`);
    wsRef.current = ws;

    ws.onopen = () => {
      ws.send(JSON.stringify({ type: 'JOIN', bookingId, userId }));
      setConnected(true);
    };

    ws.onmessage = async ({ data }) => {
      const msg = JSON.parse(data);
      switch (msg.type) {
        case 'PEER_JOINED':
          await createOffer(stream);
          break;
        case 'OFFER':
          await handleOffer(msg.sdp, stream);
          break;
        case 'ANSWER':
          await pcRef.current?.setRemoteDescription(new RTCSessionDescription(msg.sdp));
          break;
        case 'ICE_CANDIDATE':
          await pcRef.current?.addIceCandidate(new RTCIceCandidate(msg.candidate));
          break;
        case 'WHITEBOARD':
          drawRemoteStroke(msg);
          break;
      }
    };

    ws.onclose = () => setConnected(false);
  }

  function createPeerConnection(stream) {
    const pc = new RTCPeerConnection({
      iceServers: [{ urls: TURN_URL, username: 'hiresphere', credential: 'hiresphere' }],
    });
    pcRef.current = pc;

    stream.getTracks().forEach(track => pc.addTrack(track, stream));

    pc.ontrack = ({ streams }) => {
      remoteVideoRef.current.srcObject = streams[0];
    };

    pc.onicecandidate = ({ candidate }) => {
      if (candidate) {
        wsRef.current?.send(JSON.stringify({
          type: 'ICE_CANDIDATE', bookingId, userId, candidate
        }));
      }
    };

    return pc;
  }

  async function createOffer(stream) {
    const pc    = createPeerConnection(stream);
    const offer = await pc.createOffer();
    await pc.setLocalDescription(offer);
    wsRef.current?.send(JSON.stringify({ type: 'OFFER', bookingId, userId, sdp: offer }));
  }

  async function handleOffer(sdp, stream) {
    const pc = createPeerConnection(stream);
    await pc.setRemoteDescription(new RTCSessionDescription(sdp));
    const answer = await pc.createAnswer();
    await pc.setLocalDescription(answer);
    wsRef.current?.send(JSON.stringify({ type: 'ANSWER', bookingId, userId, sdp: answer }));
  }

  // ── Collaborative Whiteboard ─────────────────────────────────────────────
  function startDraw(e) {
    setDrawing(true);
    setLastPos(getPos(e));
  }

  function draw(e) {
    if (!drawing) return;
    const pos = getPos(e);
    const ctx = canvasRef.current?.getContext('2d');
    if (!ctx || !lastPos) return;
    ctx.beginPath();
    ctx.moveTo(lastPos.x, lastPos.y);
    ctx.lineTo(pos.x, pos.y);
    ctx.strokeStyle = '#1A3C5E';
    ctx.lineWidth = 2;
    ctx.stroke();
    wsRef.current?.send(JSON.stringify({
      type: 'WHITEBOARD', bookingId, userId,
      from: lastPos, to: pos
    }));
    setLastPos(pos);
  }

  function drawRemoteStroke({ from, to }) {
    const ctx = canvasRef.current?.getContext('2d');
    if (!ctx) return;
    ctx.beginPath();
    ctx.moveTo(from.x, from.y);
    ctx.lineTo(to.x, to.y);
    ctx.strokeStyle = '#B45309';
    ctx.lineWidth = 2;
    ctx.stroke();
  }

  function getPos(e) {
    const rect = canvasRef.current.getBoundingClientRect();
    return { x: e.clientX - rect.left, y: e.clientY - rect.top };
  }

  function cleanup() {
    wsRef.current?.send(JSON.stringify({ type: 'LEAVE', bookingId, userId }));
    wsRef.current?.close();
    pcRef.current?.close();
  }

  return (
    <div style={{ padding: 16, maxWidth: 1200, margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
        <h2 style={{ margin: 0 }}>Live Interview Session</h2>
        <span style={{ color: connected ? 'green' : 'red' }}>
          {connected ? '● Connected' : '○ Connecting...'}
        </span>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 16 }}>
        <div>
          <p style={{ margin: '0 0 4px', fontSize: 12, color: '#666' }}>You</p>
          <video ref={localVideoRef} autoPlay muted playsInline
                 style={{ width: '100%', borderRadius: 8, background: '#000' }} />
        </div>
        <div>
          <p style={{ margin: '0 0 4px', fontSize: 12, color: '#666' }}>Interviewer</p>
          <video ref={remoteVideoRef} autoPlay playsInline
                 style={{ width: '100%', borderRadius: 8, background: '#000' }} />
        </div>
      </div>

      <div>
        <p style={{ margin: '0 0 4px', fontSize: 12, color: '#666' }}>Collaborative Whiteboard (draw here)</p>
        <canvas ref={canvasRef} width={800} height={400}
                style={{ border: '1px solid #ddd', borderRadius: 8, cursor: 'crosshair', width: '100%' }}
                onMouseDown={startDraw}
                onMouseMove={draw}
                onMouseUp={() => setDrawing(false)}
                onMouseLeave={() => setDrawing(false)} />
        <button onClick={() => {
          const ctx = canvasRef.current?.getContext('2d');
          ctx?.clearRect(0, 0, 800, 400);
        }} style={{ marginTop: 8 }}>
          Clear whiteboard
        </button>
      </div>
    </div>
  );
}
