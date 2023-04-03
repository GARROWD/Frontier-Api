package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.exceptions.CustomerAlreadyExistsException;
import ru.frontierspb.util.exceptions.VerificationException;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final CustomerService customerService;
    private final CustomerVerificationService customerVerificationService;

    public void signUp(Customer customer)
            throws CustomerAlreadyExistsException {
        customerService.checkUniqueToCreate(customer);

        customerVerificationService.sendCode(customer);
    }

    public void verify(String code)
            throws VerificationException {
        customerVerificationService.verifyCode(code);

        customerService.create(customerVerificationService.getCustomerAndDeleteCache(code));
    }
}
