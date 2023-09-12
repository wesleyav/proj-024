package app.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import app.dto.requests.AddressRequestDTO;
import app.dto.responses.AddressResponseDTO;
import app.dto.updates.AddressUpdateDTO;
import app.entities.Address;
import app.services.AddressService;
import app.services.exceptions.AddressAlreadyExistsException;
import app.services.exceptions.DataIntegrityException;
import app.services.exceptions.InvalidPaginationParametersException;
import app.services.exceptions.PaginationDataAccessException;
import app.services.exceptions.ResourceEmptyException;
import app.services.exceptions.ResourceNotFoundException;
import app.util.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/api/v1")
@Tag(name = "Address")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Operation(summary = "Endpoint para listar um endereço por id")
	@GetMapping(value = "/addresses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAddressById(@PathVariable Long id) {
		try {
			Address address = addressService.getAddressById(id).get();
			return new ResponseEntity<Address>(address, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			String errorMessage = "Address not found with ID: " + id;
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, errorMessage));
		}
	}

	@Operation(summary = "Endpoint para listar todos os endereços")
	@GetMapping(value = "/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<AddressResponseDTO>> getAllAddresses(
			@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize)
			throws PaginationDataAccessException {

		try {
			if (pageNumber < 0 || pageSize <= 0) {
				throw new InvalidPaginationParametersException();
			}

			Page<Address> list = addressService.getAllAddressess(pageNumber, pageSize);

			if (list.isEmpty()) {
				throw new ResourceEmptyException();
			}

			Page<AddressResponseDTO> listDTO = list.map(obj -> new AddressResponseDTO(obj));
			return new ResponseEntity<>(listDTO, HttpStatus.OK);
		} catch (InvalidPaginationParametersException e) {
			return ResponseEntity.badRequest().build();
		} catch (ResourceEmptyException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Endpoint para criar um endereço")
	@PostMapping(value = "/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAddress(@RequestBody AddressRequestDTO addressRequestDTO)
			throws DataIntegrityViolationException, AddressAlreadyExistsException, DataIntegrityException {
		try {
			Address createdAddress = addressService.createAddress(addressRequestDTO);
			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(createdAddress.getAddressId())
					.toUri();

			return ResponseEntity.created(location).body(createdAddress);
		} catch (AddressAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("O endereço já existe.");
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar dados ao criar o endereço.");
		}
	}
	
	@Operation(summary = "Endpoint para excluir um endereço por id")
	@DeleteMapping(value = "/addresses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteAddressById(@PathVariable Long id) {
	    try {
	        addressService.deleteAddressById(id);
	        return ResponseEntity.noContent().build();
	    } catch (ResourceNotFoundException e) {
	        return ResponseEntity.notFound().build();
	    } catch (DataIntegrityException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@Operation(summary = "Endpoint para atualizar um endereço por id")
    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddressById(
            @PathVariable Long id,
            @RequestBody AddressUpdateDTO addressUpdateDTO) throws DataIntegrityException {
        try {
            Address updatedAddress = addressService.updateAddressById(id, addressUpdateDTO);
            AddressResponseDTO responseDTO = new AddressResponseDTO(updatedAddress);
            return ResponseEntity.ok(responseDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
