package ru.frontierspb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Referent;

@Repository
public interface ReferentRepository
        extends JpaRepository<Referent, Long> {
}
