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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PreviousPasswordsService {
    private final PreviousPasswordsRepository previousPasswordsRepository;

    public List<PreviousPassword> findByCustomer(Customer customer, Pageable pageable) {
        return previousPasswordsRepository.findAllByCustomer(customer, pageable).toList();
    }

    public List<PreviousPassword> findByCustomer(Customer customer) {
        return previousPasswordsRepository.findAllByCustomer(customer);
    }

    @Transactional
    public void checkUniqueToUpdate(Customer customer, String password)
            throws PreviousPasswordsException {
        Map<String, String> errors = new HashMap<>();

        if(customer.getPassword().equals(password)){
            errors.put("message.password.same", ExceptionsMessages.getMessage("message.password.same"));
            throw new PreviousPasswordsException(errors);
        }

        List<PreviousPassword> previousPasswords = findByCustomer(customer);

        if(previousPasswords.stream().anyMatch(
                previousPassword -> previousPassword.getPassword().equals(password))) {
            errors.put("message.password.notUnique", ExceptionsMessages.getMessage("message.password.notUnique"));
            throw new PreviousPasswordsException(errors);
        }

        create(customer);
    }

    @Transactional
    public void create(Customer customer) {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        previousPasswordsRepository.deleteAll(
                previousPasswordsRepository.findAllByCustomer(customer).stream().filter(
                        previousPassword -> previousPassword.getDate().isBefore(sixMonthsAgo)).toList());

        previousPasswordsRepository.save(
                new PreviousPassword(0, customer, LocalDateTime.now(), customer.getPassword()));
    }
}
