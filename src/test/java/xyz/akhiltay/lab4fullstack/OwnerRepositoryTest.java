package xyz.akhiltay.lab4fullstack;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.akhiltay.lab4fullstack.domain.Owner;
import xyz.akhiltay.lab4fullstack.repository.OwnerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository repository;

    @Test
    @DisplayName("Save owner and verify it can be found")
    void saveOwner() {
        repository.save(new Owner("Lucy", "Smith"));
        assertThat(repository.findByFirstname("Lucy").isPresent()).isTrue();
    }

    @Test
    @DisplayName("Delete all owners and verify count is zero")
    void deleteOwners() {
        repository.save(new Owner("Lisa", "Morrison"));
        repository.deleteAll();
        assertThat(repository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("Create and find owner by ID")
    void findOwnerById() {
        Owner owner = repository.save(new Owner("John", "Doe"));
        assertThat(repository.findById(owner.getOwnerid())).isPresent();
    }

    @Test
    @DisplayName("Update owner information")
    void updateOwner() {
        Owner owner = repository.save(new Owner("Jane", "Smith"));
        owner.setFirstname("Janet");
        owner.setLastname("Smithson");
        Owner updated = repository.save(owner);

        assertThat(updated.getFirstname()).isEqualTo("Janet");
        assertThat(updated.getLastname()).isEqualTo("Smithson");
    }
}