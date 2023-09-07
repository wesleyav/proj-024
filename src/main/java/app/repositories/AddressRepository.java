package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
