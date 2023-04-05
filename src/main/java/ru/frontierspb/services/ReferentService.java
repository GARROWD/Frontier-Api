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

    public List<Referent> findReferralsByReferrer(Customer customer, Pageable pageable) {
        return referentRepository.findAllByReferrerOrderByLevel(customer, pageable).toList();
    }
    public List<Referent> findReferrersByReferral(Customer customer, Pageable pageable) {
        return referentRepository.findAllByReferralOrderByLevel(customer, pageable).toList();
    }

    public List<Referent> findReferralsByReferrer(Customer customer) {
        return referentRepository.findAllByReferrerOrderByLevel(customer);
    }

    public List<Referent> findReferrersByReferral(Customer customer) {
        return referentRepository.findAllByReferralOrderByLevel(customer);
    }

    @Transactional
    public void assignReferrerById(Customer customer, String referrerUsername)
            throws CustomerNotFoundException, CustomerReferentException {
        Map<String, String> errors = new HashMap<>();

        Customer referral = customerService.findById(customer.getId());
        Customer referrer = customerService.findByUsername(referrerUsername);

        if(! findReferrersByReferral(referral).isEmpty()) {
            errors.put("message.customer.alreadyHaveReferrer",
                       ExceptionsMessages.getMessage("message.customer.alreadyHaveReferrer"));
        }

        if(Objects.equals(referral.getId(), referrer.getId())) {
            errors.put("message.customer.referredYourself",
                       ExceptionsMessages.getMessage("message.customer.referredHimself"));
        }

        if(!errors.isEmpty()){
            throw new CustomerReferentException(errors);
        }

        if(findReferralsByReferrer(referral).stream().anyMatch(
                referent -> Objects.equals(referent.getReferral().getId(), referrer.getId()))) {
            errors.put("message.customer.referredReferral",
                       ExceptionsMessages.getMessage("message.customer.referredReferral"));
            throw new CustomerReferentException(errors);
        }

        List<Referent> newReferrers =
                Stream.concat(Stream.of(new Referent(0, referrer, referral, LEVEL_ONE)),
                              findReferrersByReferral(referrer).stream().map(referent -> {
                                  Referent newReferent = new Referent();
                                  newReferent.setReferrer(referent.getReferrer());
                                  newReferent.setReferral(referral);
                                  newReferent.setLevel(upgradeLevel(referent.getLevel()));
                                  return newReferent;}))
                      .collect(Collectors.toCollection(ArrayList::new));

        newReferrers.removeIf(referent -> referent.getLevel().equals(LEVEL_NULL));
        referentRepository.saveAll(newReferrers);
        log.info("User with ID {} assigned referrer with username {}", referral.getId(), referrerUsername);
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
