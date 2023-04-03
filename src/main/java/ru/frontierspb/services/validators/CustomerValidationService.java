package ru.frontierspb.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.frontierspb.dto.requests.SignInRequest;
import ru.frontierspb.dto.requests.SignUpRequest;
import ru.frontierspb.dto.requests.VerificationRequest;
import ru.frontierspb.models.Customer;
import ru.frontierspb.util.exceptions.CustomerValidationException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CustomerValidationService {
    public void validateCustomer(Customer customer)
            throws CustomerValidationException {
        Map<String, String> errors = new HashMap<>();

        if(customerFieldsIsNull(customer)) {
            errors.put("message.customer.nullFields", ExceptionsMessages.getMessage("message.customer.nullFields"));
            throw new CustomerValidationException(errors);
        }

        if(usernameIsInvalid(customer.getUsername())) {
            errors.put("message.username.invalid", ExceptionsMessages.getMessage("message.username.invalid"));
        }

        if(phoneNumberIsInvalid(customer.getPhoneNumber())) {
            errors.put("message.phoneNumber.invalid", ExceptionsMessages.getMessage("message.phoneNumber.invalid"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerValidationException(errors);
        }

        customer.setPhoneNumber(
                customer.getPhoneNumber().replaceFirst("\\+7", "8"));
    }

    public void validateSignUpRequest(SignUpRequest signUpRequest)
            throws CustomerValidationException {
        Map<String, String> errors = new HashMap<>();

        if(signUpRequestFieldsIsNull(signUpRequest)) {
            errors.put("message.customer.nullFields", ExceptionsMessages.getMessage("message.customer.nullFields"));
            throw new CustomerValidationException(errors);
        }

        if(usernameIsInvalid(signUpRequest.getUsername())) {
            errors.put("message.username.invalid", ExceptionsMessages.getMessage("message.username.invalid"));
        }

        if(phoneNumberIsInvalid(signUpRequest.getPhoneNumber())) {
            errors.put("message.phoneNumber.invalid", ExceptionsMessages.getMessage("message.phoneNumber.invalid"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerValidationException(errors);
        }

        signUpRequest.setPhoneNumber(
                signUpRequest.getPhoneNumber().replaceFirst("\\+7", "8"));
    }

    public void validateSignInRequest(SignInRequest signInRequest)
            throws CustomerValidationException {
        Map<String, String> errors = new HashMap<>();

        if(signInRequestFieldsIsNull(signInRequest)) {
            errors.put("message.customer.nullFields", ExceptionsMessages.getMessage("message.customer.nullFields"));
            throw new CustomerValidationException(errors);
        }

        if(usernameIsInvalid(signInRequest.getUsername())) {
            errors.put("message.username.invalid", ExceptionsMessages.getMessage("message.username.invalid"));
        }

        if(phoneNumberIsInvalid(signInRequest.getPhoneNumber())) {
            errors.put("message.phoneNumber.invalid", ExceptionsMessages.getMessage("message.phoneNumber.invalid"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerValidationException(errors);
        }
    }

    public void validateVerificationRequest(VerificationRequest verificationRequest)
            throws CustomerValidationException {
        Map<String, String> errors = new HashMap<>();

        if(verificationRequestFieldsIsNull(verificationRequest)) {
            errors.put("message.verificationRequest.nullFields",
                       ExceptionsMessages.getMessage("message.verificationRequest.nullFields"));
            throw new CustomerValidationException(errors);
        }

        if(codeIsInvalid(verificationRequest.getCode())) {
            errors.put("message.verificationCode.invalid",
                       ExceptionsMessages.getMessage("message.verificationCode.invalid"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerValidationException(errors);
        }
    }

    private boolean usernameIsInvalid(String username) {
        return ! Pattern.compile("^[a-zA-Zа-яА-Я][a-zA-Zа-яА-Я0-9-_.]{1,29}$").matcher(username).find();
    }

    private boolean phoneNumberIsInvalid(String phoneNumber) {
        return ! Pattern.compile("^(\\+7|8)[0-9]{10}$").matcher(phoneNumber).find();
    }

    private boolean codeIsInvalid(String code) {
        // TODO Определиться с размером кода
        return ! Pattern.compile("^[0-9]{2,10}$").matcher(code).find();
    }

    private boolean customerFieldsIsNull(Customer customer) {
        if(Optional.ofNullable(customer).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(customer.getUsername()).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(customer.getPhoneNumber()).isEmpty()) {
            return true;
        }

        return false;
    }

    private boolean signUpRequestFieldsIsNull(SignUpRequest signUpRequest) {
        if(Optional.ofNullable(signUpRequest).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(signUpRequest.getUsername()).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(signUpRequest.getPhoneNumber()).isEmpty()) {
            return true;
        }

        return false;
    }

    private boolean signInRequestFieldsIsNull(SignInRequest signInRequest) {
        if(Optional.ofNullable(signInRequest).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(signInRequest.getUsername()).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(signInRequest.getPhoneNumber()).isEmpty()) {
            return true;
        }

        return false;
    }

    private boolean verificationRequestFieldsIsNull(VerificationRequest verificationRequest) {
        if(Optional.ofNullable(verificationRequest).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(verificationRequest.getCode()).isEmpty()) {
            return true;
        }

        return false;
    }
}
