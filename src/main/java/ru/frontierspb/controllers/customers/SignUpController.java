package ru.frontierspb.controllers.customers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.requests.SignUpRequest;
import ru.frontierspb.models.Customer;
import ru.frontierspb.services.SignUpService;
import ru.frontierspb.services.validators.CustomerValidationService;
import ru.frontierspb.util.exceptions.CustomerAlreadyExistsException;
import ru.frontierspb.util.exceptions.CustomerValidationException;

@RestController
@RequestMapping("/api/sign-up")
@RequiredArgsConstructor
public class SignUpController {
    private final ModelMapper modelMapper;
    private final SignUpService signUpService;
    private final CustomerValidationService customerValidationService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@RequestBody SignUpRequest request)
            throws CustomerValidationException, CustomerAlreadyExistsException {
        customerValidationService.validateSignUpRequest(request);
        signUpService.signUp(modelMapper.map(request, Customer.class));
    }
}
