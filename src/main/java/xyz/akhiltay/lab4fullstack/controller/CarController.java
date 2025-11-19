package xyz.akhiltay.lab4fullstack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.akhiltay.lab4fullstack.domain.Car;
import xyz.akhiltay.lab4fullstack.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // GET all cars
    @GetMapping("/cars")
    public Iterable<Car> getAllCars() {
        return carRepository.findAll();
    }

    // GET car by ID
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create new car
    @PostMapping("/cars")
    public Car createCar(@RequestBody Car car) {
        return carRepository.save(car);
    }

    // PUT update car
    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        Optional<Car> carData = carRepository.findById(id);

        if (carData.isPresent()) {
            Car car = carData.get();
            car.setBrand(carDetails.getBrand());
            car.setModel(carDetails.getModel());
            car.setColor(carDetails.getColor());
            car.setRegistrationNumber(carDetails.getRegistrationNumber());
            car.setModelYear(carDetails.getModelYear());
            car.setPrice(carDetails.getPrice());
            car.setOwner(carDetails.getOwner());

            return ResponseEntity.ok(carRepository.save(car));
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE car
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET cars by brand
    @GetMapping("/cars/search/findByBrand")
    public List<Car> getCarsByBrand(@RequestParam String brand) {
        return carRepository.findByBrand(brand);
    }

    // GET cars by color
    @GetMapping("/cars/search/findByColor")
    public List<Car> getCarsByColor(@RequestParam String color) {
        return carRepository.findByColor(color);
    }

    // GET cars by model year
    @GetMapping("/cars/search/findByModelYear")
    public List<Car> getCarsByModelYear(@RequestParam int modelYear) {
        return carRepository.findByModelYear(modelYear);
    }

    // GET cars by brand and model
    @GetMapping("/cars/search/findByBrandAndModel")
    public List<Car> getCarsByBrandAndModel(@RequestParam String brand, @RequestParam String model) {
        return carRepository.findByBrandAndModel(brand, model);
    }
}