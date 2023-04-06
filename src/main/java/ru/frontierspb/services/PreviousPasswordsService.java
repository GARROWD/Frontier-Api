package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.PreviousPassword;
import ru.frontierspb.repositories.PreviousPasswordsRepository;
import ru.frontierspb.util.exceptions.PreviousPasswordsException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PreviousPasswordsService {
    private final PreviousPasswordsRepository previousPasswordsRepository;

    public List<PreviousPassword> findByCustomer(long customerId, Pageable pageable) {
        return previousPasswordsRepository.findAllByCustomerId(customerId, pageable).toList();
    }

    public List<PreviousPassword> findByCustomer(long customerId) {
        return previousPasswordsRepository.findAllByCustomerId(customerId);
    }

    @Transactional
    public void checkUniqueToUpdate(Customer customer, String password)
            throws PreviousPasswordsException {
        if(customer.getPassword().equals(password)) {
            throw new PreviousPasswordsException(
                    Map.of("message.password.same", ExceptionsMessages.getMessage("message.password.same")));
        }

        List<PreviousPassword> previousPasswords = findByCustomer(customer.getId());

        if(previousPasswords.stream().anyMatch(
                previousPassword -> previousPassword.getPassword().equals(password))) {
            throw new PreviousPasswordsException(
                    Map.of("message.password.notUnique", ExceptionsMessages.getMessage("message.password.notUnique")));
        }

        create(customer);
    }

    @Transactional
    public void create(Customer customer) {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        previousPasswordsRepository.deleteAll(
                previousPasswordsRepository.findAllByCustomerId(customer.getId()).stream().filter(
                        previousPassword -> previousPassword.getDate().isBefore(sixMonthsAgo)).toList());

        previousPasswordsRepository.save(
                new PreviousPassword(0, customer.getId(), LocalDateTime.now(), customer.getPassword()));
    }
}
