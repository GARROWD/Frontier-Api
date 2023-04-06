package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String username, String password)
            throws CustomerNotFoundException {
        Customer customer = customerService.findByUsernameAndPassword(username, passwordEncoder.encode(password));

        return new UsernamePasswordAuthenticationToken(customer, null,
                Collections.singleton(new SimpleGrantedAuthority(customer.getRole())));
    }
}
