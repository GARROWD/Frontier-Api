package ru.frontierspb.controllers.customers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.models.Customer;
import ru.frontierspb.services.BookingService;
import ru.frontierspb.services.CustomerService;
import ru.frontierspb.services.PreviousPasswordsService;
import ru.frontierspb.services.ReferentService;
import ru.frontierspb.services.validators.CustomerValidationService;
import ru.frontierspb.util.exceptions.*;

@RestController
@RequestMapping("/api/customer/edit")
@RequiredArgsConstructor
public class CustomerEditController {
    private final ModelMapper modelMapper;
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final ReferentService referentService;
    private final PreviousPasswordsService previousPasswordsService;
    private final CustomerValidationService customerValidationService;

    /* TODO У меня честно это было два метода, один для редактирования username, другой для password. Но чет мне
        захотелось совместить, а то там тавтология была (/api/customer/edit/username?username=Hibana) */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateUsername(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password)
            throws CustomerValidationException, CustomerAlreadyExistsException, CustomerNotFoundException,
            PreviousPasswordsException {
        Customer customer = customerService.findById(getCustomerFromContext().getId());
        if(! username.isBlank()) {
            customer.setUsername(username);
            customerValidationService.validateCustomer(customer);
            customerService.checkUniqueToUpdate(customer);
            customerService.update(customer);
        }
        if (! password.isBlank()){
            previousPasswordsService.checkUniqueToUpdate(customer, password);
            customer.setPassword(password);
            customerValidationService.validateCustomer(customer);
            customerService.update(customer);
        }
    }

    @PutMapping("/assign-referrer")
    @ResponseStatus(HttpStatus.OK)
    public void assignReferrer(@RequestParam("username") String username)
            throws CustomerNotFoundException, CustomerReferentException {
        referentService.assignReferrerById(getCustomerFromContext().getId(), username);
    }

    /*@PostMapping("/booking")
    @ResponseStatus(HttpStatus.OK)
    public void createBooking(@RequestBody BookingRequest bookingRequest)
            throws BookingAlreadyExistsException {
        bookingService.create(getCustomerFromContext().getId(), modelMapper.map(bookingRequest, Booking.class));
    }*/

    /*@DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(HttpServletRequest request, HttpServletResponse response)
            throws CustomerNotFoundException {
        customerService.delete(getCustomerFromContext().getId());
        new SecurityContextLogoutHandler().logout(request, response,
                                                  SecurityContextHolder.getContext().getAuthentication());
    }*/

    private Customer getCustomerFromContext() {
        return (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
