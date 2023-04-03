package ru.frontierspb.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frontierspb.models.Customer;
import ru.frontierspb.repositories.CustomersRepository;
import ru.frontierspb.util.exceptions.CustomerAlreadyExistsException;
import ru.frontierspb.util.exceptions.CustomerNotFoundException;
import ru.frontierspb.util.exceptions.PointsInsufficientException;
import ru.frontierspb.util.messages.ExceptionsMessages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CustomerService {
    private final CustomersRepository customersRepository;

    public Customer findById(long id)
            throws CustomerNotFoundException {
        Optional<Customer> foundCustomer = customersRepository.findById(id);

        if(foundCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return foundCustomer.get();
    }

    public void existsById(long id)
            throws CustomerNotFoundException {
        if(customersRepository.findById(id).isEmpty()) {
            throw new CustomerNotFoundException();
        }
    }

    public Customer findByUsername(String username)
            throws CustomerNotFoundException {
        Optional<Customer> foundCustomer = customersRepository.findByUsername(username);

        if(foundCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return foundCustomer.get();
    }

    public Customer findByPhoneNumber(String phoneNumber)
            throws CustomerNotFoundException {
        Optional<Customer> foundCustomer = customersRepository.findByPhoneNumber(phoneNumber);

        if(foundCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return foundCustomer.get();
    }

    public Customer findByUsernameAndPhoneNumber(String username, String phoneNumber)
            throws CustomerNotFoundException {
        Optional<Customer> foundCustomer = customersRepository.findByUsernameAndPhoneNumber(username, phoneNumber);

        if(foundCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        return foundCustomer.get();
    }

    public List<Customer> findAll(Pageable pageable) {
        return customersRepository.findAll(pageable).toList();
    }

    public List<Customer> findAllByPhoneNumberEndingWith(String phoneNumber, Pageable pageable){
        return customersRepository.findAllByPhoneNumberEndingWith(phoneNumber, pageable);
    }

    public void checkUniqueToCreate(Customer customer)
            throws CustomerAlreadyExistsException {
        Map<String, String> errors = new HashMap<>();

        if(customersRepository.findByUsername(customer.getUsername()).isPresent()) {
            errors.put("message.username.notUnique", ExceptionsMessages.getMessage("message.username.notUnique"));
        }

        if(customersRepository.findByPhoneNumber(customer.getPhoneNumber()).isPresent()) {
            errors.put("message.phoneNumber.notUnique", ExceptionsMessages.getMessage("message.phoneNumber.notUnique"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerAlreadyExistsException(errors);
        }
    }

    public void checkUniqueToUpdate(Customer customer)
            throws CustomerAlreadyExistsException {
        Map<String, String> errors = new HashMap<>();

        if(customersRepository.findByUsernameAndIdIsNot(customer.getUsername(), customer.getId()).isPresent()) {
            errors.put("message.username.notUnique", ExceptionsMessages.getMessage("message.username.notUnique"));
        }

        if(customersRepository.findByPhoneNumberAndIdIsNot(customer.getPhoneNumber(), customer.getId()).isPresent()) {
            errors.put("message.phoneNumber.notUnique", ExceptionsMessages.getMessage("message.phoneNumber.notUnique"));
        }

        if(! errors.isEmpty()) {
            throw new CustomerAlreadyExistsException(errors);
        }
    }

    @Transactional
    public void create(Customer customer) {
        customer.setRole("ROLE_USER");
        customersRepository.save(customer);
        log.info("User with ID {} is created", customer.getId());
    }

    @Transactional
    public void update(Customer customer)
            throws CustomerAlreadyExistsException, CustomerNotFoundException {
        existsById(customer.getId());
        checkUniqueToUpdate(customer);
        customersRepository.save(customer);
    }

    @Transactional
    public void deductPoints(Customer customer, int points)
            throws PointsInsufficientException {
        Map<String, String> errors = new HashMap<>();

        if(customer.getPoints() < points) {
            errors.put("message.points.insufficient", ExceptionsMessages.getMessage("message.points.insufficient"));
            throw new PointsInsufficientException(errors);
        }

        customer.deductPoints(points);
        updateWithoutChecks(customer);
    }

    @Transactional
    public void accruePoints(Customer customer, int points){
        customer.accruePoints(points);
        updateWithoutChecks(customer);
    }

    @Transactional
    public void updateWithoutChecks(Customer customer) {
        customersRepository.save(customer);
    }

    @Transactional
    public void delete(long id)
            throws CustomerNotFoundException {
        existsById(id);
        /* TODO Странное использование existsById, он же выкидывает исключение, а не возвращает boolean */
        customersRepository.deleteById(id);
        log.info("User with ID {} is deleted", id);
    }
}
