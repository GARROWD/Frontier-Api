package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.VerificationException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final CustomerService customerService;
    private final CustomerVerificationService customerVerificationService;

    public void signIn(String username, String phoneNumber)
            throws CustomerNotFoundException {
        Customer customer = customerService.findByUsernameAndPhoneNumber(username, phoneNumber);

        customerVerificationService.sendCode(customer);
    }

    public UsernamePasswordAuthenticationToken verifyAndGetAuthenticationToken(String code)
            throws VerificationException, CustomerNotFoundException {
        customerVerificationService.verifyCode(code);

        Customer customer = customerVerificationService.getCustomerAndDeleteCache(code);

        return new UsernamePasswordAuthenticationToken(
                customerService.findByUsername(customer.getUsername()), null,
                Collections.singleton(new SimpleGrantedAuthority(customer.getRole())));
    }
}
