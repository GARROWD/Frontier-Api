package ru.frontierspb.controllers.customers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.requests.SignInRequest;
import ru.frontierspb.dto.requests.VerificationRequest;
import ru.frontierspb.services.SignInService;
import ru.frontierspb.services.validators.CustomerValidationService;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.CustomerValidationException;
import ru.frontierspb.util.exceptions.VerificationException;

import java.io.IOException;

@RestController
@RequestMapping("/api/sign-in")
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService;
    private final CustomerValidationService customerValidationService;

    @PostMapping("/send-code")
    @ResponseStatus(HttpStatus.OK)
    public void sendCode(@RequestBody SignInRequest request)
            throws CustomerValidationException, CustomerNotFoundException, IOException, InterruptedException {
        customerValidationService.validateSignInRequest(request);
        signInService.signIn(request.getUsername(), request.getPhoneNumber());
    }

    @PostMapping("/verify-code")
    @ResponseStatus(HttpStatus.OK)
    public void verifyCode(@RequestBody VerificationRequest request, HttpSession session)
            throws VerificationException, CustomerValidationException, CustomerNotFoundException {
        customerValidationService.validateVerificationRequest(request);
        UsernamePasswordAuthenticationToken token =
                signInService.verifyAndGetAuthenticationToken(request.getCode());

        /* Сорян, придется это делать здесь. Вообще в принципе процесс собственноручной аутентификация ооочень
        сложный. Я очень надеюсь, что это не прям говно код говно кодов, ибо изучение всего этого
        дерьма при выходе новой Spring Security 6 было невыносимым. Короче грежу мечтами, что я узнаю как это
        правильно реализовывается и сделаю по красоте */

        SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().getContext();
        securityContext.setAuthentication(token);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}

