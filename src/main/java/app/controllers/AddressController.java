package app.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import app.dto.requests.AddressRequestDTO;
import app.dto.responses.AddressResponseDTO;
import app.dto.updates.AddressUpdateDTO;
import app.entities.Address;
import app.services.AddressService;

@RestController
@RequestMapping(value = "/api/v1")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@GetMapping(value = "/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Address>> findAll() {
		List<Address> lista = addressService.findAll();
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@GetMapping(value = "/addresses-with-response", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<AddressResponseDTO>> findAllWithResponse() {
		List<Address> lista = addressService.findAll();
		List<AddressResponseDTO> dtos = lista.stream().map(AddressResponseDTO::new).collect(Collectors.toList());
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@GetMapping(value = "/addresses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AddressResponseDTO> findById(@PathVariable Long id) {
		Address address = addressService.findById(id);
		AddressResponseDTO dto = new AddressResponseDTO(address);
		return new ResponseEntity<AddressResponseDTO>(dto, HttpStatus.OK);
	}

	@PostMapping(value = "/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AddressResponseDTO> save(@RequestBody AddressRequestDTO addressRequestDTO) {
		Address address = addressRequestDTO.convertToDTO(addressRequestDTO);
		Address savedAddress = addressService.save(address);

		AddressResponseDTO addressResponseDTO = new AddressResponseDTO(savedAddress);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedAddress.getAddressId()).toUri();

		return ResponseEntity.created(uri).body(addressResponseDTO);
	}

	@DeleteMapping(value = "/addresses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		addressService.deleteById(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@PutMapping(value = "/addresses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AddressResponseDTO> update(@PathVariable Long id,
			@RequestBody AddressUpdateDTO addressUpdateDTO) {
		Address existingAddress = addressService.findById(id);

		if (existingAddress == null) {
			return ResponseEntity.notFound().build();
		}

		if (addressUpdateDTO.getAddress() != null) {
			existingAddress.setAddress(addressUpdateDTO.getAddress());
		}

		if (addressUpdateDTO.getAddress2() != null) {
			existingAddress.setAddress2(addressUpdateDTO.getAddress2());
		}

		if (addressUpdateDTO.getDistrict() != null) {
			existingAddress.setDistrict(addressUpdateDTO.getDistrict());
		}

		if (addressUpdateDTO.getPhone() != null) {
			existingAddress.setPhone(addressUpdateDTO.getPhone());
		}

		Address updatedAddress = addressService.save(existingAddress);

		AddressResponseDTO addressResponseDTO = new AddressResponseDTO(updatedAddress);

		return ResponseEntity.ok(addressResponseDTO);
	}

}
