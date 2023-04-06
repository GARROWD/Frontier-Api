package ru.frontierspb.controllers.admins;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.frontierspb.dto.responses.CustomerCheckInResponse;
import ru.frontierspb.dto.responses.CustomerCheckOutResponse;
import ru.frontierspb.dto.responses.CustomerSessionResponse;
import ru.frontierspb.services.SessionService;
import ru.frontierspb.util.enums.Extras;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.OptionNotFoundException;
import ru.frontierspb.util.exceptions.PointsInsufficientException;
import ru.frontierspb.util.exceptions.SessionException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class SessionController {
    private final ModelMapper modelMapper;
    private final SessionService sessionService;

    @GetMapping("/sessions")
    public List<CustomerSessionResponse> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return sessionService.findAll(PageRequest.of(page, size)).stream().map(
                session -> modelMapper.map(session, CustomerSessionResponse.class)).toList();
    }

    // TODO Может методы сверху и снизу тоже обьеденить...?

    @GetMapping("/sessions/active")
    public List<CustomerSessionResponse> findAllActive(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        return sessionService.findAllActive(PageRequest.of(page, size)).stream().map(
                session -> modelMapper.map(session, CustomerSessionResponse.class)).toList();
    }

    @PostMapping("/check-in")
    public CustomerCheckInResponse checkInById(@RequestParam(name = "id") long id)
            throws SessionException, CustomerNotFoundException {
        return modelMapper.map(sessionService.checkInById(id), CustomerCheckInResponse.class);
    }

    @PostMapping("/check-out")
    public CustomerCheckOutResponse checkOutById(
            @RequestParam(name = "id") long id,
            @RequestParam(name = "points", defaultValue = "0") int points)
            throws SessionException, CustomerNotFoundException, OptionNotFoundException, PointsInsufficientException {
        return modelMapper.map(sessionService.checkOutById(id, points), CustomerCheckOutResponse.class);
    }

    @PostMapping("/check-in/guest")
    public CustomerCheckInResponse checkInByGuestUsername(@RequestParam(name = "username") String username)
            throws SessionException {
        return modelMapper.map(sessionService.checkInByGuestUsername(username), CustomerCheckInResponse.class);
    }

    @PostMapping("/check-out/guest")
    public CustomerCheckOutResponse checkOutByGuestUsername(@RequestParam(name = "username") String username)
            throws SessionException, OptionNotFoundException {
        return modelMapper.map(sessionService.checkOutByGuestUsername(username), CustomerCheckOutResponse.class);
    }

    @PostMapping("/check-night-stay")
    public CustomerSessionResponse checkNightStayById(
            @RequestParam(name = "id") long id,
            @RequestParam(name = "points", defaultValue = "0") int points,
            @RequestParam(name = "extras", defaultValue = Extras.DEFAULT_VALUE) Extras extras)
            throws OptionNotFoundException, PointsInsufficientException, CustomerNotFoundException {
        return modelMapper.map(sessionService.checkNightStayById(id, points, extras), CustomerSessionResponse.class);
    }

    @PostMapping("/check-night-stay/guest")
    public CustomerSessionResponse checkNightStayByGuestUsername(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "extras", defaultValue = Extras.DEFAULT_VALUE) Extras extras)
            throws OptionNotFoundException {
        return modelMapper.map(sessionService.checkNightStayByGuestUsername(username, extras), CustomerSessionResponse.class);
    }
}
