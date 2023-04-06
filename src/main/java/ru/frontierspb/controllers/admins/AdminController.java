package ru.frontierspb.controllers.admins;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.responses.CustomerResponse;
import ru.frontierspb.services.AdminService;
import ru.frontierspb.services.BookingService;
import ru.frontierspb.services.CustomerService;
import ru.frontierspb.util.exceptions.AdminAlreadyExistsException;
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

    @GetMapping("/customers")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerResponse> getAllCustomersByLastFourDigitsOfPhoneNumber(
            @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        if(phoneNumber.isEmpty()) {
            return customerService.findAll(PageRequest.of(page, size)).stream().map(
                    customer -> modelMapper.map(customer, CustomerResponse.class)).toList();
        }
        else {
            return customerService.findAllByPhoneNumberEndingWith(phoneNumber, PageRequest.of(page, size)).stream().map(
                    customer -> modelMapper.map(customer, CustomerResponse.class)).toList();
        }
    }

    /* TODO Где-то здесь еще должна быть реализована возможность управлять бронированиями, но мне оооооочень
        лень продумывать логику без конкретных бизнес требований
    @GetMapping("/booking-status")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponse bookingStatus(@RequestParam long id)
            throws BookingNotFoundException {
        return modelMapper.map(bookingService.findByCustomerId(id), BookingResponse.class);
    }*/

    @PutMapping("/assign-admin")
    @ResponseStatus(HttpStatus.OK)
    public void assignAdmin(@RequestParam("id") long id)
            throws CustomerNotFoundException, AdminAlreadyExistsException {
        adminService.assignAdmin(id);
    }
}
