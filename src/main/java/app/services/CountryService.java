package app.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.dto.requests.CountryRequestDTO;
import app.dto.updates.CountryUpdateDTO;
import app.entities.City;
import app.entities.Country;
import app.repositories.CityRepository;
import app.repositories.CountryRepository;
import app.services.exceptions.CountryAlreadyExistsException;
import app.services.exceptions.DataIntegrityException;
import app.services.exceptions.InvalidPaginationParametersException;
import app.services.exceptions.PaginationDataAccessException;
import app.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private CityRepository cityRepository;

	@Transactional
	public Optional<Country> getCountryById(Long id) {
		Optional<Country> optional = countryRepository.findById(id);

		if (optional.isPresent()) {
			return Optional.of(optional.get());
		} else {
			throw new EntityNotFoundException("Country not found with ID: " + id);
		}
	}

	@Transactional
	public Page<Country> getAllCountries(Integer pageNumber, Integer pageSize) throws PaginationDataAccessException {
		try {
			if (pageNumber < 0 || pageSize <= 0) {
				throw new InvalidPaginationParametersException("Parâmetros de paginação inválidos.");
			}
			Pageable pageable = PageRequest.of(pageNumber, pageSize);

			return countryRepository.findAll(pageable);
		} catch (InvalidPaginationParametersException e) {
			throw new InvalidPaginationParametersException("Erro ao recuperar países paginados: ");
		} catch (DataAccessException e) {
			throw new PaginationDataAccessException("Erro ao recuperar países paginados.");
		}

	}

	@Transactional
	public Country createCountry(CountryRequestDTO countryRequestDTO)
			throws CountryAlreadyExistsException, DataIntegrityException, DataIntegrityViolationException {
//		if (countryRepository.existsByCountry(countryRequestDTO.getCountry())) {
//			throw new CountryAlreadyExistsException();
//		}

		if (countryRepository.existsByName(countryRequestDTO.getName())) {
			throw new CountryAlreadyExistsException();
		}

		try {
			Country newCountry = new Country();
			newCountry.setName(countryRequestDTO.getName());
			newCountry.setLastUpdate(Instant.now());

			return countryRepository.save(newCountry);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Erro de integridade de dados ao criar o país.", e);
		}
	}

	@Transactional
	public Country updateCountry(Long id, CountryUpdateDTO countryUpdateDTO) throws ResourceNotFoundException {

		if (!countryRepository.existsById(id)) {
			throw new ResourceNotFoundException("País não encontrado.");
		}

		try {
			Country existingCountry = countryRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("País não encontrado."));
			existingCountry.setName(countryUpdateDTO.getName());
			existingCountry.setLastUpdate(Instant.now());
			return countryRepository.save(existingCountry);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Erro ao acessar dados ao atualizar o país.", e);
		}
	}

	@Transactional
	public void deleteCountryById(Long id) throws ResourceNotFoundException, DataIntegrityException {

		try {
			Optional<Country> optional = countryRepository.findById(id);

			if (!optional.isPresent()) {
				throw new ResourceNotFoundException("País não encontrado.");
			}

			List<City> citiesToDelete = cityRepository.findByCountryId(optional.get());
			cityRepository.deleteAll(citiesToDelete);
			countryRepository.deleteById(id);

			countryRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro de integridade de dados ao excluir o país.", e);
		}
	}

	public Country getOrCreateCountry(CountryRequestDTO countryRequestDTO) {
		String countryName = countryRequestDTO.getName();

		Country existingCountry = countryRepository.findByName(countryName);

		if (existingCountry != null) {
			return existingCountry;
		} else {
			Country newCountry = new Country();
			newCountry.setName(countryName);
			newCountry.setLastUpdate(Instant.now());

			return countryRepository.save(newCountry);
		}
	}

}
