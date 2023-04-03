package ru.frontierspb.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Points;

import java.util.List;

@Repository
public interface PointsRepository
        extends JpaRepository<Points, Long> {
    List<Points> findAllByCustomer(Customer customer, Pageable pageable);
}
