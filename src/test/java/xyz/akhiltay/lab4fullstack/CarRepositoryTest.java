package xyz.akhiltay.lab4fullstack;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.akhiltay.lab4fullstack.domain.Car;
import xyz.akhiltay.lab4fullstack.domain.Owner;
import xyz.akhiltay.lab4fullstack.repository.CarRepository;
import xyz.akhiltay.lab4fullstack.repository.OwnerRepository;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    @DisplayName("Save car and verify it exists")
    void saveCar() {
        Owner owner = ownerRepository.save(new Owner("John", "Doe"));
        Car car = new Car("Toyota", "Corolla", "Blue", "ABC-123", 2023, 25000, owner);
        carRepository.save(car);

        assertThat(carRepository.findById(car.getId())).isPresent();
    }

    @Test
    @DisplayName("Find cars by brand")
    void findCarsByBrand() {
        Owner owner = ownerRepository.save(new Owner("Jane", "Smith"));
        carRepository.save(new Car("Honda", "Civic", "Red", "XYZ-789", 2022, 22000, owner));
        carRepository.save(new Car("Honda", "Accord", "Black", "DEF-456", 2023, 28000, owner));
        carRepository.save(new Car("Toyota", "Camry", "White", "GHI-321", 2021, 24000, owner));

        List<Car> hondaCars = carRepository.findByBrand("Honda");
        assertThat(hondaCars).hasSize(2);
        assertThat(hondaCars).allMatch(car -> car.getBrand().equals("Honda"));
    }

    @Test
    @DisplayName("Find cars by color")
    void findCarsByColor() {
        Owner owner = ownerRepository.save(new Owner("Bob", "Johnson"));
        carRepository.save(new Car("BMW", "X5", "Black", "BMW-001", 2023, 55000, owner));
        carRepository.save(new Car("Audi", "A4", "Black", "AUD-002", 2022, 45000, owner));

        List<Car> blackCars = carRepository.findByColor("Black");
        assertThat(blackCars).hasSize(2);
    }

    @Test
    @DisplayName("Find cars by model year")
    void findCarsByModelYear() {
        Owner owner = ownerRepository.save(new Owner("Alice", "Brown"));
        carRepository.save(new Car("Tesla", "Model 3", "White", "TES-123", 2023, 45000, owner));
        carRepository.save(new Car("Tesla", "Model Y", "Black", "TES-456", 2023, 52000, owner));
        carRepository.save(new Car("Tesla", "Model S", "Red", "TES-789", 2022, 75000, owner));

        List<Car> cars2023 = carRepository.findByModelYear(2023);
        assertThat(cars2023).hasSize(2);
    }

    @Test
    @DisplayName("Find cars by brand and model")
    void findCarsByBrandAndModel() {
        Owner owner = ownerRepository.save(new Owner("Charlie", "Davis"));
        carRepository.save(new Car("Ford", "Mustang", "Red", "FOR-001", 2023, 35000, owner));
        carRepository.save(new Car("Ford", "Focus", "Blue", "FOR-002", 2022, 22000, owner));

        List<Car> mustangs = carRepository.findByBrandAndModel("Ford", "Mustang");
        assertThat(mustangs).hasSize(1);
        assertThat(mustangs.get(0).getModel()).isEqualTo("Mustang");
    }

    @Test
    @DisplayName("Delete car and verify it's removed")
    void deleteCar() {
        Owner owner = ownerRepository.save(new Owner("Eve", "Wilson"));
        Car car = carRepository.save(new Car("Mazda", "CX-5", "Gray", "MAZ-111", 2021, 28000, owner));
        Long carId = car.getId();

        carRepository.deleteById(carId);

        assertThat(carRepository.findById(carId)).isEmpty();
    }

    @Test
    @DisplayName("Update car information")
    void updateCar() {
        Owner owner = ownerRepository.save(new Owner("Frank", "Miller"));
        Car car = carRepository.save(new Car("Nissan", "Altima", "Silver", "NIS-222", 2020, 23000, owner));

        car.setPrice(21000);
        car.setColor("Blue");
        Car updatedCar = carRepository.save(car);

        assertThat(updatedCar.getPrice()).isEqualTo(21000);
        assertThat(updatedCar.getColor()).isEqualTo("Blue");
    }

    @Test
    @DisplayName("Find cars by brand ordered by model year ascending")
    void findCarsByBrandOrderedByYear() {
        Owner owner = ownerRepository.save(new Owner("Grace", "Taylor"));
        carRepository.save(new Car("Volkswagen", "Golf", "White", "VW-001", 2023, 25000, owner));
        carRepository.save(new Car("Volkswagen", "Passat", "Black", "VW-002", 2021, 28000, owner));
        carRepository.save(new Car("Volkswagen", "Tiguan", "Gray", "VW-003", 2022, 32000, owner));

        List<Car> vwCars = carRepository.findByBrandOrderByModelYearAsc("Volkswagen");
        assertThat(vwCars).hasSize(3);
        assertThat(vwCars.get(0).getModelYear()).isEqualTo(2021);
        assertThat(vwCars.get(2).getModelYear()).isEqualTo(2023);
    }
}