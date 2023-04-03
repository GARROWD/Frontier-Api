package ru.frontierspb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.frontierspb.models.Option;

import java.util.Optional;

@Repository
public interface OptionsRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByName(String name);
}
