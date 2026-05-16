package com.hiresphere.bookingservice.grpc;

import com.hiresphere.booking.grpc.*;
import com.hiresphere.bookingservice.model.Booking;
import com.hiresphere.bookingservice.repository.BookingRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * gRPC server implementation for the Booking service.
 * Called by the Live Session Service to verify a booking
 * exists and is confirmed before allowing a session to start.
 */
@GrpcService
@RequiredArgsConstructor
@Slf4j
public class BookingGrpcService extends BookingServiceGrpc.BookingServiceImplBase {

    private final BookingRepository bookingRepository;

    @Override
    public void verifyBooking(VerifyBookingRequest request,
                               StreamObserver<VerifyBookingResponse> responseObserver) {
        log.debug("gRPC verifyBooking: bookingId={}", request.getBookingId());

        var response = bookingRepository.findById(request.getBookingId())
            .filter(b -> b.getCandidateId().equals(request.getCandidateId())
                      || b.getInterviewerId().equals(request.getInterviewerId()))
            .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED)
            .map(b -> VerifyBookingResponse.newBuilder()
                .setValid(true)
                .setSessionToken(b.getSessionToken())
                .build())
            .orElse(VerifyBookingResponse.newBuilder()
                .setValid(false)
                .build());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBooking(GetBookingRequest request,
                            StreamObserver<GetBookingResponse> responseObserver) {
        log.debug("gRPC getBooking: bookingId={}", request.getBookingId());

        bookingRepository.findById(request.getBookingId()).ifPresentOrElse(
            b -> {
                com.hiresphere.booking.grpc.Booking grpcBooking =
                    com.hiresphere.booking.grpc.Booking.newBuilder()
                        .setBookingId(b.getId())
                        .setCandidateId(b.getCandidateId())
                        .setInterviewerId(b.getInterviewerId())
                        .setAmountPaid(b.getAmountPaid())
                        .setSessionToken(b.getSessionToken() != null ? b.getSessionToken() : "")
                        .build();
                responseObserver.onNext(
                    GetBookingResponse.newBuilder().setBooking(grpcBooking).setFound(true).build());
            },
            () -> responseObserver.onNext(
                GetBookingResponse.newBuilder().setFound(false).build())
        );
        responseObserver.onCompleted();
    }
}
