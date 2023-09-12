package app.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

import app.dto.requests.CityRequestDTO;
import app.dto.updates.CityUpdateDTO;
import app.entities.City;
import app.services.CityService;
import app.services.exceptions.CityAlreadyExistsException;
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
@Tag(name = "City")
public class CityController {

	@Autowired
	private CityService cityService;

	@Operation(summary = "Endpoint para listar uma cidade por id")
	@GetMapping(value = "/cities/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCityById(@PathVariable Long id) {
		try {
			City city = cityService.getCityById(id).get();
			return new ResponseEntity<City>(city, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			String errorMessage = "City not found with ID: " + id;
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorResponse(HttpStatus.NOT_FOUND, errorMessage));
		}
	}

	@Operation(summary = "Endpoint para listar todas as cidades")
	@GetMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCities(@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "10") Integer pageSize) {

		try {
			if (pageNumber < 0 || pageSize <= 0) {
				throw new InvalidPaginationParametersException();
			}

			Page<City> page = cityService.getAllCities(pageNumber, pageSize);

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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao recuperar cidades paginados.");
		}
	}

	@Operation(summary = "Endpoint para criar uma cidade com país")
	@PostMapping(value = "/cities", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createCityWithCountry(@RequestBody CityRequestDTO cityRequestDTO) {
		try {
			City createdCity = cityService.createCity(cityRequestDTO);

			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(createdCity.getCityId())
					.toUri();

			return ResponseEntity.created(location).body(createdCity);
		} catch (CityAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("A cidade já existe.");
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar dados ao criar a cidade.");
		}
	}

	@Operation(summary = "Endpoint para deletar uma cidade")
	@DeleteMapping(value = "/cities/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteCityById(@PathVariable Long id) {
		try {
			cityService.deleteCityById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cidade não encontrada.");
		} catch (DataIntegrityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Erro de integridade de dados ao excluir a cidade.");
		}
	}

	@Operation(summary = "Endpoint para atualizar uma cidade por id")
	@PutMapping(value = "/cities/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCityById(@PathVariable Long id, @RequestBody CityUpdateDTO cityUpdateDTO) {
		try {
			City updatedCity = cityService.updateCityById(id, cityUpdateDTO);
			return ResponseEntity.status(HttpStatus.OK).body(updatedCity);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cidade não encontrada.");
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar dados ao atualizar a cidade.");
		}
	}

}
