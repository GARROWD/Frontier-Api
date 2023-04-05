package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.exceptions.AdminAlreadyExistsException;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.messages.ExceptionsMessages;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminService {
    private final CustomerService customerService;

    @Transactional
    public void assignAdmin(long id)
            throws CustomerNotFoundException, AdminAlreadyExistsException {
        Map<String, String> errors = new HashMap<>();

        Customer customer = customerService.findById(id);

        if(customer.getRole().equals("ROLE_ADMIN")) {
            errors.put("message.customer.alreadyAdmin", ExceptionsMessages.getMessage("message.customer.alreadyAdmin"));
            throw new AdminAlreadyExistsException(errors);
        }

        customer.setRole("ROLE_ADMIN");
        customerService.updateWithoutChecks(customer);

        log.info("User with ID {} is assigned to administrator role", id);
    }
}