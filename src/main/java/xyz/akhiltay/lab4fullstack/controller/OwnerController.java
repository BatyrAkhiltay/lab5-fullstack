package xyz.akhiltay.lab4fullstack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.akhiltay.lab4fullstack.domain.Owner;
import xyz.akhiltay.lab4fullstack.repository.OwnerRepository;

import java.util.Optional;


@RestController
@RequestMapping("/api")
public class OwnerController {

    private final OwnerRepository ownerRepository;

    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @GetMapping("/owners")
    public Iterable<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    @GetMapping("/owners/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        Optional<Owner> owner = ownerRepository.findById(id);
        return owner.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/owners")
    public Owner createOwner(@RequestBody Owner owner) {
        return ownerRepository.save(owner);
    }

    @PutMapping("/owners/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner ownerDetails) {
        Optional<Owner> ownerData = ownerRepository.findById(id);

        if (ownerData.isPresent()) {
            Owner owner = ownerData.get();
            owner.setFirstname(ownerDetails.getFirstname());
            owner.setLastname(ownerDetails.getLastname());

            return ResponseEntity.ok(ownerRepository.save(owner));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/owners/{id}")
    public ResponseEntity<?> deleteOwner(@PathVariable Long id) {
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
