package ru.frontierspb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Session;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionsRepository
        extends JpaRepository<Session, Long> {
    Optional<Session> findByCustomerAndOutDateIsNull(Customer customer);
    Optional<Session> findByCustomerUsernameAndOutDateIsNull(String username);
    Page<Session> findAllByCustomerOrderByInDateDesc(Customer customer, Pageable pageable);
    List<Session> findByOutDateIsNull();
    List<Session> findAllByOrderByInDateDesc(Pageable pageable);
    List<Session> findAllByOutDateIsNullOrderByInDateDesc(Pageable pageable);
    long countAllByOutDateIsNull();
}
