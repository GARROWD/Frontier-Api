package ru.frontierspb.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomersRepository
        extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsernameAndPhoneNumber(String username, String phoneNumber);
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    Optional<Customer> findByUsernameAndIdIsNot(String username, long id);
    Optional<Customer> findByPhoneNumberAndIdIsNot(String phoneNumber, long id);
    List<Customer> findAllByPhoneNumberEndingWith(String phoneNumber, Pageable pageable);
    // Можно писать простые sql команды просто создав метод без реализации, вау...
}
