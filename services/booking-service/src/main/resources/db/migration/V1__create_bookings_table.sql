-- V1__create_bookings_table.sql
-- HireSphere Booking Service schema
-- Flyway managed migration

CREATE TABLE IF NOT EXISTS bookings (
    id                      VARCHAR(36)     PRIMARY KEY,
    candidate_id            VARCHAR(255)    NOT NULL,
    interviewer_id          VARCHAR(255)    NOT NULL,
    slot_id                 VARCHAR(255)    NOT NULL,
    start_time              TIMESTAMPTZ     NOT NULL,
    end_time                TIMESTAMPTZ     NOT NULL,
    status                  VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    amount_paid             DECIMAL(10, 2)  NOT NULL,
    stripe_payment_intent_id VARCHAR(255),
    session_token           VARCHAR(255),
    created_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_status CHECK (status IN ('PENDING','CONFIRMED','CANCELLED','COMPLETED'))
);

CREATE INDEX idx_bookings_candidate    ON bookings(candidate_id);
CREATE INDEX idx_bookings_interviewer  ON bookings(interviewer_id);
CREATE INDEX idx_bookings_status       ON bookings(status);
CREATE INDEX idx_bookings_start_time   ON bookings(start_time);
