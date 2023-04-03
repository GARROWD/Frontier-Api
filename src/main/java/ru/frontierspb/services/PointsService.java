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
    private final CustomerService customerService;
    private final PointsRepository pointsRepository;

    public List<Points> findByCustomer(Customer customer, Pageable pageable)
            throws CustomerNotFoundException {
        customerService.existsById(customer.getId());
        return pointsRepository.findAllByCustomer(customer, pageable);
    }

    @Transactional
    public void create(Points points) {
        pointsRepository.save(points);
    }

    @Transactional
    public void accrueToReferrers(Customer customer, float price) {
        List<Referent> referrers = customer.getReferrers();
        for(Referent referrer : referrers) {
            try {
                accrueIfPresent(referrer.getReferrer().getId(), price, referrer.getLevel());
            } catch(CustomerNotFoundException | OptionNotFoundException exception) {
                // TODO ОШИБКИ ОБРАБОТАЙ
                log.warn("ЧЕЛ ХУЙНЯ ЧЕТ У ТЕБЯ");
            }
        }
    }

    private void accrueIfPresent(long id, float price, ReferentLevel level)
            throws CustomerNotFoundException, OptionNotFoundException {
        if(! Objects.equals(id, 0)) {
            float levelMultiple = optionService.findByName(level.name()).getValue();
            Customer foundCustomer = customerService.findById(id);
            int points = (int) (price / 100 * levelMultiple);
            create(new Points(0, foundCustomer, LocalDateTime.now(), TransactionType.ACCRUED, points));
            customerService.accruePoints(foundCustomer, points);
        }
    }
}
