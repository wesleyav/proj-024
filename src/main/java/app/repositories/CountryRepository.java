package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

	@Query("SELECT c FROM Country c WHERE c.name = :name")
	Country findByName(String name);

	boolean existsByName(String name);

}
