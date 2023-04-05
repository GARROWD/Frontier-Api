package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.exceptions.CustomerAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final CustomerService customerService;

    public void signUp(Customer customer)
            throws CustomerAlreadyExistsException {
        customerService.checkUniqueToCreate(customer);

        // TODO Можно сделать подтверждение номера, а то так можно любой нерабочий номер засунуть
        //customerVerificationService.sendCode();
        //customerVerificationService.verifyCode();

        customerService.create(customer);
    }
}
