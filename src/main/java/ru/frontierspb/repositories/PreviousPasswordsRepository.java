package ru.frontierspb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.frontierspb.models.PreviousPassword;

import java.util.List;

public interface PreviousPasswordsRepository extends JpaRepository<PreviousPassword, Long> {
    Page<PreviousPassword> findAllByCustomerId(long customerId, Pageable pageable);
    List<PreviousPassword> findAllByCustomerId(long customerId);
}
