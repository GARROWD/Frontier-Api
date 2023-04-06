package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Points;
import ru.frontierspb.models.Referent;
import ru.frontierspb.repositories.PointsRepository;
import ru.frontierspb.util.enums.ReferentLevel;
import ru.frontierspb.util.enums.TransactionType;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.OptionNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PointsService {
    private final OptionService optionService;
    private final ReferentService referentService;
    private final CustomerService customerService;
    private final PointsRepository pointsRepository;

    public List<Points> findByCustomer(long customerId, Pageable pageable) {
        return pointsRepository.findAllByCustomerId(customerId, pageable);
    }

    @Transactional
    public void create(Points points) {
        pointsRepository.save(points);
    }

    @Transactional
    public void incrementToReferrers(long customerId, float price) {
        List<Referent> referrers = referentService.findReferrersByReferralId(customerId);
        for(Referent referrer : referrers) {
            try {
                incrementIfPresent(referrer.getReferrer().getId(), price, referrer.getLevel());
            } catch(CustomerNotFoundException | OptionNotFoundException exception) {
                // TODO ОШИБКИ ОБРАБОТАЙ
                log.warn("ЧЕЛ ХУЙНЯ ЧЕТ У ТЕБЯ");
            }
        }
    }

    private void incrementIfPresent(long id, float price, ReferentLevel level)
            throws CustomerNotFoundException, OptionNotFoundException {
        if(! Objects.equals(id, 0)) {
            float levelMultiple = optionService.findByName(level.name()).getValue();
            Customer foundCustomer = customerService.findById(id);
            int points = (int) (price / 100 * levelMultiple);
            create(new Points(0, foundCustomer.getId(), LocalDateTime.now(), TransactionType.ACCRUED, points));
            customerService.incrementPoints(foundCustomer, points);
        }
    }
}
