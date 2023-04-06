package ru.frontierspb.controllers.customers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.responses.*;
import ru.frontierspb.models.Customer;
import ru.frontierspb.services.*;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;

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

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getInfo()
            throws CustomerNotFoundException {
        return modelMapper.map(customerService.findById(getCustomerFromContext().getId()), CustomerResponse.class);
    }

    @GetMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public List<SessionResponse> getSessions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return sessionService.findByCustomer(getCustomerFromContext().getId(), PageRequest.of(page, size)).stream().map(
                session -> modelMapper.map(session, SessionResponse.class)).toList();
    }

    @GetMapping("/referrals")
    @ResponseStatus(HttpStatus.OK)
    public List<ReferralResponse> getReferrals(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return referentService.findReferralsByReferrerId(getCustomerFromContext().getId(), PageRequest.of(page, size)).stream().map(
                referent -> modelMapper.map(referent, ReferralResponse.class)).toList();
    }

    @GetMapping("/referrers")
    @ResponseStatus(HttpStatus.OK)
    public List<ReferrerResponse> getReferrers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return referentService.findReferrersByReferralId(getCustomerFromContext().getId(), PageRequest.of(page, size)).stream().map(
                referent -> modelMapper.map(referent, ReferrerResponse.class)).toList();
    }

    @GetMapping("/points-history")
    @ResponseStatus(HttpStatus.OK)
    public List<PointsResponse> getPointsHistory(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return pointsService.findByCustomer(getCustomerFromContext().getId(), PageRequest.of(page, size)).stream().map(
                points -> modelMapper.map(points, PointsResponse.class)).toList();
    }

    /*@GetMapping("/booking-status")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse getBookingStatus()
            throws BookingNotFoundException {
        return modelMapper.map(bookingService.findByCustomerId(getCustomerFromContext().getId()), BookingResponse.class);
    }*/

    private Customer getCustomerFromContext() {
        return (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

