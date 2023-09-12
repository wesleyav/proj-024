package app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.entities.City;
import app.entities.Country;

public interface CityRepository extends JpaRepository<City, Long> {

	boolean existsByName(String name);

	List<City> findByCountryId(Country countryId);

	@Query("SELECT c FROM City c WHERE c.name = :name")
	City findByName(String name);
}
