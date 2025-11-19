package xyz.akhiltay.lab4fullstack.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.akhiltay.lab4fullstack.domain.Owner;

import java.util.Optional;

public interface OwnerRepository extends CrudRepository<Owner, Long> {
    Optional<Owner> findByFirstname(String firstname);
}