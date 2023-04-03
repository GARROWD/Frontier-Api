package ru.frontierspb.controllers.customers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.models.Option;
import ru.frontierspb.services.OptionService;
import ru.frontierspb.services.SessionService;
import ru.frontierspb.util.exceptions.OptionNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
@Slf4j
public class DataController {
    private final OptionService optionService;
    private final SessionService sessionService;

    @GetMapping("/options")
    @ResponseStatus(HttpStatus.OK)
    public List<Option> getOptions(){
        return optionService.findAll();
    }

    @GetMapping("/option")
    @ResponseStatus(HttpStatus.OK)
    public float getOptionValue(@RequestParam String name)
            throws OptionNotFoundException {
        return optionService.findByName(name).getValue();
    }

    @GetMapping("/sessions-count")
    @ResponseStatus(HttpStatus.OK)
    public long getActiveSessionsCount() {
        return sessionService.activeCount();
    }
}
