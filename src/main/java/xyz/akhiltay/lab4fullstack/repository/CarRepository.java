package xyz.akhiltay.lab4fullstack.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import xyz.akhiltay.lab4fullstack.domain.Car;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {

    // fetch cars by brand
    List<Car> findByBrand(String brand);

    // fetch cars by color
    List<Car> findByColor(String color);

    // fetch cars by model year
    List<Car> findByModelYear(int modelYear);

    // fetch cars by brand and model
    List<Car> findByBrandAndModel(String brand, String model);

    // fetch cars by brand or color
    List<Car> findByBrandOrColor(String brand, String color);

    // fetch cars by brand and sort by year
    List<Car> findByBrandOrderByModelYearAsc(String brand);

    // fetch cars by brand using sql
    @Query("select c from Car c where c.brand like %?1")
    List<Car> findByBrandEndsWith(String brand);
}