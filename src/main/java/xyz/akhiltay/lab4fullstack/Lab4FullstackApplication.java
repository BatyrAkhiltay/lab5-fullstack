package xyz.akhiltay.lab4fullstack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xyz.akhiltay.lab4fullstack.domain.AppUser;
import xyz.akhiltay.lab4fullstack.domain.Car;
import xyz.akhiltay.lab4fullstack.domain.Owner;
import xyz.akhiltay.lab4fullstack.repository.AppUserRepository;
import xyz.akhiltay.lab4fullstack.repository.CarRepository;
import xyz.akhiltay.lab4fullstack.repository.OwnerRepository;

@SpringBootApplication
public class Lab4FullstackApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Lab4FullstackApplication.class);

    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;
    private final AppUserRepository userRepository;

    public Lab4FullstackApplication(
            CarRepository carRepository,
            OwnerRepository ownerRepository,
            AppUserRepository userRepository) {
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Lab4FullstackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Add owners
        Owner owner1 = new Owner("John", "Johnson");
        Owner owner2 = new Owner("Mary", "Robinson");
        ownerRepository.save(owner1);
        ownerRepository.save(owner2);

        // Add cars
        carRepository.save(new Car("Ford", "Mustang", "Red", "ADF-1121", 2023, 59000, owner1));
        carRepository.save(new Car("Nissan", "Leaf", "White", "SSJ-3002", 2020, 29000, owner2));
        carRepository.save(new Car("Toyota", "Prius", "Silver", "KKO-0212", 2022, 39000, owner2));

        // Log cars
        for (Car car : carRepository.findAll()) {
            logger.info("brand: {}, model: {}", car.getBrand(), car.getModel());
        }

        // Add test users
        // Username: user, password: user
        userRepository.save(new AppUser("user",
                encoder.encode("user"), "USER"));

        // Username: admin, password: admin
        userRepository.save(new AppUser("admin",
                encoder.encode("admin"), "ADMIN"));

        logger.info("Test users created: user/user and admin/admin");
    }
}
