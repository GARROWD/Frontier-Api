package ru.frontierspb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.frontierspb.models.Booking;
import ru.frontierspb.util.enums.BookingStatus;

import java.util.Optional;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {
    Optional<Booking> findByCustomerIdAndStatusIs(long customerId, BookingStatus status);
}
