package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Session;
import ru.frontierspb.repositories.SessionsRepository;
import ru.frontierspb.util.enums.Extras;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.OptionNotFoundException;
import ru.frontierspb.util.exceptions.PointsInsufficientException;
import ru.frontierspb.util.exceptions.SessionException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {
    private final CustomerService customerService;
    private final SessionsRepository sessionsRepository;
    private final CalculationService calculationService;
    private final PointsService pointsService;

    public List<Session> findAll(Pageable pageable) {
        return sessionsRepository.findAllByOrderByInDateDesc(pageable);
    }

    public List<Session> findAllActive(Pageable pageable) {
        return sessionsRepository.findAllByOutDateIsNullOrderByInDateDesc(pageable);
    }

    public long activeCount() {
        return sessionsRepository.countAllByOutDateIsNull();
    }

    public List<Session> findByCustomer(long customerId, Pageable pageable) {
        return sessionsRepository.findAllByCustomerIdOrderByInDateDesc(customerId, pageable).toList();
    }

    /* TODO Я не могу понять, хорошо ли это, что тут методы явно дублируют друг друга.
        Они очень похожи, но различаются в мелочах, и вот я не знаю что с ними делать
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////*/

    @Transactional
    public Session checkInById(long id)
            throws SessionException, CustomerNotFoundException {
        Customer customer = customerService.findById(id);

        if(sessionsRepository.findByCustomerIdAndOutDateIsNull(id).isPresent()) {
            throw new SessionException(Map.of("message.session.alreadyCheckIn",
                                              ExceptionsMessages.getMessage("message.session.alreadyCheckIn")));
        }

        Session session = new Session();
        session.setCustomerId(id);
        session.setCustomerUsername(customer.getUsername());
        session.setInDate(LocalDateTime.now());
        sessionsRepository.save(session);

        return session;
    }

    @Transactional
    public Session checkOutById(long id, int points)
            throws CustomerNotFoundException, SessionException, OptionNotFoundException, PointsInsufficientException {
        Customer customer = customerService.findById(id);

        Optional<Session> activeSession = sessionsRepository.findByCustomerIdAndOutDateIsNull(id);

        if(activeSession.isEmpty()) {
            throw new SessionException(
                    Map.of("message.session.notCheckIn", ExceptionsMessages.getMessage("message.session.notCheckIn")));
        }

        Session session = activeSession.get();
        LocalDateTime outDate = LocalDateTime.now();

        float price = calculationService.calculatePrice(session.getInDate(), outDate);
        int pointsSpent = calculationService.calculatePointsSpent(price, points);

        customerService.deductPoints(customer, pointsSpent);
        pointsService.accrueToReferrers(id, price);

        session.setPrice(price);
        session.setOutDate(outDate);
        session.setPointsSpent(pointsSpent);
        sessionsRepository.save(session);

        return session;
    }

    @Transactional
    public Session checkInByGuestUsername(String username)
            throws SessionException {
        if(sessionsRepository.findByCustomerUsernameAndOutDateIsNull(username).isPresent()) {
            throw new SessionException(Map.of("message.session.alreadyCheckIn",
                                              ExceptionsMessages.getMessage("message.session.alreadyCheckIn")));
        }

        Session session = new Session();
        session.setCustomerUsername(username);
        session.setInDate(LocalDateTime.now());
        sessionsRepository.save(session);

        return session;
    }

    @Transactional
    public Session checkOutByGuestUsername(String username)
            throws SessionException, OptionNotFoundException {
        Optional<Session> activeSession = sessionsRepository.findByCustomerUsernameAndOutDateIsNull(username);

        if(activeSession.isEmpty()) {
            throw new SessionException(
                    Map.of("message.session.notCheckIn", ExceptionsMessages.getMessage("message.session.notCheckIn")));
        }

        Session session = activeSession.get();
        LocalDateTime outDate = LocalDateTime.now();

        float price = calculationService.calculatePrice(session.getInDate(), outDate);
        session.setPrice(price);
        session.setOutDate(outDate);
        sessionsRepository.save(session);

        return session;
    }

    @Transactional
    public Session checkNightStayById(long id, int points, Extras extras)
            throws CustomerNotFoundException, OptionNotFoundException, PointsInsufficientException {
        Customer customer = customerService.findById(id);

        float price = calculationService.calculatePrice(extras);
        int pointsSpent = calculationService.calculatePointsSpent(price, points);

        customerService.deductPoints(customer, points);
        pointsService.accrueToReferrers(id, price);

        /*LocalDateTime inDate = LocalDateTime.now();
        LocalDateTime outDate;

        if(inDate.getHour() >= 22) {
            outDate = inDate.plusDays(1).withHour(6).withMinute(0).withSecond(0).withNano(0);
        }
        else {
            outDate = inDate.withHour(6).withMinute(0).withSecond(0).withNano(0);
        }*/

        Session session = new Session();
        session.setCustomerId(customer.getId());
        session.setCustomerUsername(customer.getUsername());
        session.setInDate(LocalDateTime.now());
        session.setPrice(price);
        session.setPointsSpent(pointsSpent);
        sessionsRepository.save(session);

        return session;
    }

    @Transactional
    public Session checkNightStayByGuestUsername(String username, Extras extras)
            throws OptionNotFoundException {
        float price = calculationService.calculatePrice(extras);

        Session session = new Session();
        session.setCustomerUsername(username);
        session.setInDate(LocalDateTime.now());
        session.setPrice(price);
        sessionsRepository.save(session);

        return session;
    }

    @Scheduled(cron = "0 6 * * 6,7")
    @Transactional
    void checkNightStayAfterSixInMorning() {
        List<Session> activeSessions = sessionsRepository.findByOutDateIsNull();

        activeSessions.forEach(session -> session.setOutDate(
                LocalDateTime.now().withHour(6).withMinute(0).withSecond(0).withNano(0)));

        sessionsRepository.saveAll(activeSessions);
    }

    /* TODO Ставит session out у всех пользователей LocalDateTime.now после 0:00. Надо ли?
    @Scheduled(cron = "0 0 * * ?")
    @Transactional
    void checkOutAllCustomersAfterMidnight() {
        List<Session> activeSessions = sessionsRepository.findByOutDateIsNull();

        activeSessions.forEach(session -> session.setOutDate(LocalDateTime.now()));

        sessionsRepository.saveAll(activeSessions);
    }*/
}
