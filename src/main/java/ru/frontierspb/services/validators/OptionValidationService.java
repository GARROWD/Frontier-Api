package ru.frontierspb.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.frontierspb.dto.requests.OptionRequest;
import ru.frontierspb.util.exceptions.OptionValidationException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionValidationService {
    public void validateOptionRequest(OptionRequest optionRequest)
            throws OptionValidationException {
        Map<String, String> errors = new HashMap<>();

        if(optionRequestFieldsIsNull(optionRequest)) {
            errors.put("message.option.nullFields", ExceptionsMessages.getMessage("message.option.nullFields"));
            throw new OptionValidationException(errors);
        }
    }

    private boolean optionRequestFieldsIsNull(OptionRequest optionRequest) {
        if(Optional.ofNullable(optionRequest).isEmpty()) {
            return true;
        }

        if(Optional.ofNullable(optionRequest.getName()).isEmpty()) {
            return true;
        }

        if(Float.compare(optionRequest.getPrice(), 0) <= 0) {
            return true;
        }

        return false;
    }
}
