package ru.frontierspb.controllers.admins;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.requests.OptionRequest;
import ru.frontierspb.models.Option;
import ru.frontierspb.services.OptionService;
import ru.frontierspb.services.validators.OptionValidationService;
import ru.frontierspb.util.exceptions.OptionNotFoundException;
import ru.frontierspb.util.exceptions.OptionValidationException;

@RestController
@RequestMapping("/api/admin/option")
@RequiredArgsConstructor
public class OptionController {
    private final ModelMapper modelMapper;
    private final OptionService optionService;
    private final OptionValidationService optionValidationService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody OptionRequest optionRequest)
            throws OptionNotFoundException, OptionValidationException {
        optionValidationService.validateOptionRequest(optionRequest);
        optionService.update(modelMapper.map(optionRequest, Option.class));
    }
}