package ru.frontierspb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Customer;
import ru.frontierspb.models.Referent;

import java.util.List;

@Repository
public interface ReferentRepository
        extends JpaRepository<Referent, Long> {
    Page<Referent> findAllByReferrerOrderByLevel(Customer customer, Pageable pageable);
    List<Referent> findAllByReferrerOrderByLevel(Customer customer);
    Page<Referent> findAllByReferralOrderByLevel(Customer customer, Pageable pageable);
    List<Referent> findAllByReferralOrderByLevel(Customer customer);
}
