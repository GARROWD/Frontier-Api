package ru.frontierspb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Referent;

import java.util.List;

@Repository
public interface ReferentRepository
        extends JpaRepository<Referent, Long> {
    Page<Referent> findAllByReferrerIdOrderByLevel(long customerId, Pageable pageable);
    List<Referent> findAllByReferrerIdOrderByLevel(long customerId);
    Page<Referent> findAllByReferralIdOrderByLevel(long customerId, Pageable pageable);
    List<Referent> findAllByReferralIdOrderByLevel(long customerId);
}
