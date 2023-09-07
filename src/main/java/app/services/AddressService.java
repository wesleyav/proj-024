package app.services;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Address;
import app.repositories.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;

	public List<Address> findAll() {
		return addressRepository.findAll();
	}

	public Address save(Address address) {
		address.setLastUpdate(Instant.now());
		return addressRepository.save(address);
	}

	public Address findById(Long id) {
		return addressRepository.findById(id).get();
	}

	public Address update(Long id, Address obj) {
		Address address = addressRepository.getReferenceById(id);

		address.setAddress(obj.getAddress());
		address.setAddress2(obj.getAddress2());
		address.setDistrict(obj.getDistrict());
		address.setPhone(obj.getPhone());
		address.setLastUpdate(Instant.now());

		return addressRepository.save(address);
	}

	public void deleteById(Long id) {
		addressRepository.deleteById(id);
	}
}
