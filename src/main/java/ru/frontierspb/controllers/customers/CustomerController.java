package ru.frontierspb.controllers.customers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.requests.BookingRequest;
import ru.frontierspb.dto.responses.BookingResponse;
import ru.frontierspb.dto.responses.CustomerResponse;
import ru.frontierspb.dto.responses.PointsResponse;
import ru.frontierspb.dto.responses.SessionResponse;
import ru.frontierspb.models.Booking;
import ru.frontierspb.models.Customer;
import ru.frontierspb.services.*;
import ru.frontierspb.services.validators.CustomerValidationService;
import ru.frontierspb.util.exceptions.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final ModelMapper modelMapper;
    private final PointsService pointsService;
    private final SessionService sessionService;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final ReferentService referentService;
    private final CustomerValidationService customerValidationService;

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getInfo() {
        return modelMapper.map(getCustomerFromContext(), CustomerResponse.class);
    }

    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public List<SessionResponse> getSessions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size)
            throws CustomerNotFoundException {
        return sessionService.findByCustomer(getCustomerFromContext(), PageRequest.of(page, size)).stream().map(
                session -> modelMapper.map(session, SessionResponse.class)).toList();
    }

    @GetMapping("/points-history")
    @ResponseStatus(HttpStatus.OK)
    public List<PointsResponse> getPointsHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size)
            throws CustomerNotFoundException {
        return pointsService.findByCustomer(getCustomerFromContext(), PageRequest.of(page, size)).stream().map(
                points -> modelMapper.map(points, PointsResponse.class)).toList();
    }

    @GetMapping("/booking-status")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse getBookingStatus()
            throws BookingNotFoundException {
        return modelMapper.map(bookingService.findByCustomer(getCustomerFromContext()), BookingResponse.class);
    }

    @PutMapping("/update-username")
    @ResponseStatus(HttpStatus.OK)
    public void updateUsername(@RequestParam String username)
            throws CustomerValidationException, CustomerAlreadyExistsException, CustomerNotFoundException {
        /*
        Customer customer = getCustomerFromContext();
        customer.setUsername(username);
        customerValidationService.validateCustomer(customer);
        customerService.update(customer);
        */
    }

    @PutMapping("/assign-referrer")
    @ResponseStatus(HttpStatus.OK)
    public void assignReferrer(@RequestParam String username)
            throws CustomerNotFoundException, CustomerReferentException {
        referentService.assignReferrerById(getCustomerFromContext(), username);
    }

    @PostMapping("/booking")
    @ResponseStatus(HttpStatus.OK)
    public void createBooking(@RequestBody BookingRequest bookingRequest)
            throws BookingAlreadyExistsException {
        bookingService.create(getCustomerFromContext(), modelMapper.map(bookingRequest, Booking.class));
    }

    /*
    @PutMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    private void editPhoneNumber(@RequestParam String phoneNumber)
            throws CustomerValidationException, CustomerAlreadyExistsException {
        Customer customer = getCustomerFromContext();
        customer.setPhoneNumber(phoneNumber);
        customersValidationService.validateCustomer(customer);
        customersService.saveAndCheckUnique(customer);
    }
    */

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(HttpServletRequest request, HttpServletResponse response)
            throws CustomerNotFoundException {
        customerService.delete(getCustomerFromContext().getId());
        new SecurityContextLogoutHandler().logout(request, response,
                                                  SecurityContextHolder.getContext().getAuthentication());
    }

    private Customer getCustomerFromContext() {
        return (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
