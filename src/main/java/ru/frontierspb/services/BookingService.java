package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Booking;
import ru.frontierspb.models.Customer;
import ru.frontierspb.repositories.BookingRepository;
import ru.frontierspb.util.enums.BookingStatus;
import ru.frontierspb.util.exceptions.BookingAlreadyExistsException;
import ru.frontierspb.util.exceptions.BookingNotFoundException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {
    /* TODO В принципе, весь сервис бронирования это одна сплошная заглушка, потому что я не могу взять из головы и
        придумать логику бронирования */
    private final BookingRepository bookingRepository;

    public Booking findByCustomer(Customer customer)
            throws BookingNotFoundException {
        Optional<Booking> foundBooking = bookingRepository.findByCustomerAndStatusIs(customer, BookingStatus.ACTIVE);

        if(foundBooking.isEmpty()) {
            throw new BookingNotFoundException();
        }

        return foundBooking.get();
    }

    /* TODO Бронирование можно сделать с подтверждением, но надо ли? */
    @Transactional
    public void create(Customer customer, Booking booking)
            throws BookingAlreadyExistsException {
        Map<String, String> errors = new HashMap<>();

        if(bookingRepository.findByCustomerAndStatusIs(customer, BookingStatus.ACTIVE).isPresent()) {
            errors.put("message.booking.alreadyExists", ExceptionsMessages.getMessage("message.booking.alreadyExists"));
            throw new BookingAlreadyExistsException(errors);
        }

        booking.setCustomer(customer);
        booking.setRequestDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);
    }
}