package ru.frontierspb.controllers.admins;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.responses.BookingResponse;
import ru.frontierspb.dto.responses.CustomerResponse;
import ru.frontierspb.services.AdminService;
import ru.frontierspb.services.BookingService;
import ru.frontierspb.services.CustomerService;
import ru.frontierspb.util.exceptions.AdminAlreadyExistsException;
import ru.frontierspb.util.exceptions.BookingNotFoundException;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ModelMapper modelMapper;
    private final AdminService adminService;
    private final BookingService bookingService;
    private final CustomerService customerService;

    @GetMapping("/customer/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAllCustomers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return customerService.findAll(PageRequest.of(page, size)).stream().map(
                customer -> modelMapper.map(customer, CustomerResponse.class)).toList();
    }

    @GetMapping("/customer/all-by")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAllCustomersByLastFourDigitsOfPhoneNumber(
            @RequestParam String phoneNumber,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return customerService.findAllByPhoneNumberEndingWith(phoneNumber, PageRequest.of(page, size)).stream().map(
                customer -> modelMapper.map(customer, CustomerResponse.class)).toList();
    }

    /* TODO Где-то здесь еще должна быть реализована возможность управлять бронированиями, но мне оооооочень
        лень продумывать логику без конкретных бизнес требований */
    @GetMapping("/booking-status")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse bookingStatus(@RequestParam long id)
            throws CustomerNotFoundException, BookingNotFoundException {
        return modelMapper.map(bookingService.findByCustomer(customerService.findById(id)), BookingResponse.class);
    }

    @PutMapping("/assign/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void assignAdmin(@PathVariable("id") long id)
            throws CustomerNotFoundException, AdminAlreadyExistsException {
        adminService.assignAdmin(id);
    }
}
