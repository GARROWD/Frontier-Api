package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Referent;
import ru.frontierspb.repositories.ReferentRepository;
import ru.frontierspb.util.enums.ReferentLevel;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.CustomerReferentException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.frontierspb.util.enums.ReferentLevel.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReferentService {
    private final CustomerService customerService;
    private final ReferentRepository referentRepository;

    @Transactional
    public void create(Referent referrers) {
        referentRepository.save(referrers);
    }

    public List<Referent> findReferralsByReferrerId(long referrerId, Pageable pageable) {
        return referentRepository.findAllByReferrerIdOrderByLevel(referrerId, pageable).toList();
    }

    public List<Referent> findReferrersByReferralId(long referralId, Pageable pageable) {
        return referentRepository.findAllByReferralIdOrderByLevel(referralId, pageable).toList();
    }

    public List<Referent> findReferralsByReferrerId(long referrerId) {
        return referentRepository.findAllByReferrerIdOrderByLevel(referrerId);
    }

    public List<Referent> findReferrersByReferralId(long referralId) {
        return referentRepository.findAllByReferralIdOrderByLevel(referralId);
    }

    @Transactional
    public void assignReferrerById(long customerId, String referrerUsername)
            throws CustomerNotFoundException, CustomerReferentException {
        Map<String, String> errors = new HashMap<>();

        /* customerService.existsById(customerId);
        TODO Вот опять же я не понимаю, надо ли еще раз проверять на существование,
         если до этого я взял customerId из контекста и он точно существует?
        */
        Customer referral = customerService.findById(customerId);
        Customer referrer = customerService.findByUsername(referrerUsername);

        if(! findReferrersByReferralId(customerId).isEmpty()) {
            errors.put("message.customer.alreadyHaveReferrer",
                       ExceptionsMessages.getMessage("message.customer.alreadyHaveReferrer"));
        }

        if(Objects.equals(customerId, referrer.getId())) {
            errors.put("message.customer.referredYourself",
                       ExceptionsMessages.getMessage("message.customer.referredHimself"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerReferentException(errors);
        }

        if(findReferralsByReferrerId(customerId).stream().anyMatch(
                referent -> Objects.equals(referent.getReferral().getId(), referrer.getId()))) {
            errors.put("message.customer.referredReferral",
                       ExceptionsMessages.getMessage("message.customer.referredReferral"));
            throw new CustomerReferentException(errors);
        }

        List<Referent> newReferrers =
                Stream.concat(Stream.of(new Referent(0, referrer, referral, LEVEL_ONE)),
                              findReferrersByReferralId(referrer.getId()).stream().map(referent -> {
                                  Referent newReferent = new Referent();
                                  newReferent.setReferrer(referent.getReferrer());
                                  newReferent.setReferral(referral);
                                  newReferent.setLevel(upgradeLevel(referent.getLevel()));
                                  return newReferent;
                              }))
                      .collect(Collectors.toCollection(ArrayList::new));

        newReferrers.removeIf(referent -> referent.getLevel().equals(LEVEL_NULL));
        referentRepository.saveAll(newReferrers);
        log.info("User with ID {} assigned referrer with username {}", customerId, referrerUsername);
    }

    private ReferentLevel upgradeLevel(ReferentLevel level) {
        return switch(level) {
            case LEVEL_ONE -> LEVEL_TWO;
            case LEVEL_TWO -> LEVEL_THREE;
            case LEVEL_THREE -> LEVEL_NULL;
            default -> level;
        };
    }
}
