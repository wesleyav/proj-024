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

import app.dto.requests.CountryRequestDTO;
import app.dto.updates.CountryUpdateDTO;
import app.entities.Country;
import app.services.CountryService;
import app.services.exceptions.CountryAlreadyExistsException;
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
@Tag(name = "Country")
public class CountryController {

	@Autowired
	private CountryService countryService;

	@Operation(summary = "Endpoint para listar um país por id")
	@GetMapping(value = "/countries/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCountryById(@PathVariable Long id) {
		try {
			Country country = countryService.getCountryById(id).get();
			return new ResponseEntity<Country>(country, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			String errorMessage = "Country not found with ID: " + id;
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, errorMessage));
		}
	}

	@Operation(summary = "Endpoint para listar todos os países")
	@GetMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCountries(@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "10") Integer pageSize) {

		try {
			if (pageNumber < 0 || pageSize <= 0) {
				throw new InvalidPaginationParametersException();
			}

			Page<Country> page = countryService.getAllCountries(pageNumber, pageSize);

			if (page.isEmpty()) {
				throw new ResourceEmptyException();
			}

			return ResponseEntity.status(HttpStatus.OK).body(page);
		} catch (InvalidPaginationParametersException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetros de paginação inválidos.");
		} catch (ResourceEmptyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, "Nenhum registro encontrado."));
		} catch (PaginationDataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao recuperar países paginados.");
		}
	}

	@Operation(summary = "Endpoint para criar um país")
	@PostMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCountry(@RequestBody CountryRequestDTO countryRequestDTO)
			throws DataIntegrityViolationException {
		try {
			Country createdCountry = countryService.createCountry(countryRequestDTO);
			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(createdCountry.getCountryId())
					.toUri();

			return ResponseEntity.created(location).body(createdCountry);
		} catch (CountryAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("O país já existe.");
		} catch (DataIntegrityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro de integridade de dados ao criar o país.");
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar dados ao criar o país.");
		}
	}

	@Operation(summary = "Endpoint para remover um país por id")
	@DeleteMapping(value = "/countries/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteCountryById(@PathVariable Long id) {
		try {
			countryService.deleteCountryById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("País não encontrado.");
		} catch (DataIntegrityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Erro de integridade de dados ao excluir o país.");
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar dados ao excluir o país.");
		}
	}

	@Operation(summary = "Endpoint para atualizar um país por id")
	@PutMapping(value = "/countries/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCountry(@PathVariable Long id, @RequestBody CountryUpdateDTO countryUpdateDTO) {
		try {
			Country updatedCountry = countryService.updateCountry(id, countryUpdateDTO);
			return ResponseEntity.ok(updatedCountry);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("País não encontrado.");
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar dados ao atualizar o país.");
		}
	}

}
