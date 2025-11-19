package xyz.akhiltay.lab4fullstack.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.akhiltay.lab4fullstack.domain.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
