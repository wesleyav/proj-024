package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	boolean existsByAddress(String address);

	@Query("SELECT a FROM Address a WHERE a.address = :address")
	Address findByAddress(String address);

}
