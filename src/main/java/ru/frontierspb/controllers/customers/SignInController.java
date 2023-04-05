package ru.frontierspb.controllers.customers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.requests.SignInRequest;
import ru.frontierspb.services.SignInService;
import ru.frontierspb.services.validators.CustomerValidationService;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.CustomerValidationException;

@RestController
@RequestMapping("/api/sign-in")
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;
    private final CustomerValidationService customerValidationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void signIn(@RequestBody SignInRequest request, HttpSession session)
            throws CustomerValidationException, CustomerNotFoundException {
        customerValidationService.validateSignInRequest(request);
        UsernamePasswordAuthenticationToken token = signInService.getAuthenticationToken(request.getUsername(), request.getPassword());
        SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().getContext();
        securityContext.setAuthentication(token);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

