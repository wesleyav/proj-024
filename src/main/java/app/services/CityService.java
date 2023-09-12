package app.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.dto.requests.CityRequestDTO;
import app.dto.updates.CityUpdateDTO;
import app.entities.City;
import app.entities.Country;
import app.repositories.CityRepository;
import app.repositories.CountryRepository;
import app.services.exceptions.CityAlreadyExistsException;
import app.services.exceptions.DataIntegrityException;
import app.services.exceptions.InvalidPaginationParametersException;
import app.services.exceptions.PaginationDataAccessException;
import app.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Transactional
	public Optional<City> getCityById(Long id) {
		Optional<City> optional = cityRepository.findById(id);

		if (optional.isPresent()) {
			return Optional.of(optional.get());
		} else {
			throw new EntityNotFoundException("City not found with ID: " + id);
		}
	}

	@Transactional
	public Page<City> getAllCities(Integer pageNumber, Integer pageSize) throws PaginationDataAccessException {
		try {
			if (pageNumber < 0 || pageSize <= 0) {
				throw new InvalidPaginationParametersException("Parâmetros de paginação inválidos.");
			}
			Pageable pageable = PageRequest.of(pageNumber, pageSize);

			return cityRepository.findAll(pageable);
		} catch (InvalidPaginationParametersException e) {
			throw new InvalidPaginationParametersException("Erro ao recuperar cidades paginados: ");
		} catch (DataAccessException e) {
			throw new PaginationDataAccessException("Erro ao recuperar cidades paginados.");
		}

	}

	@Transactional
	public City createCity(CityRequestDTO cityRequestDTO) throws CityAlreadyExistsException {

//		if (cityRepository.existsByCity(cityRequestDTO.getCity())) {
//			throw new CityAlreadyExistsException();
//		}

		if (cityRepository.existsByName(cityRequestDTO.getName())) {
			throw new CityAlreadyExistsException();
		}

		try {
			Country existingCountry = cityRequestDTO.getCountry().convertToDTO(cityRequestDTO.getCountry());

			if (existingCountry == null) {
				Country newCountry = new Country();
				newCountry.setName(cityRequestDTO.getCountry().getName());
				newCountry.setLastUpdate(Instant.now());
				existingCountry = countryRepository.save(newCountry);
			}

			City city = cityRequestDTO.convertToDTO(cityRequestDTO);
			city.setName(cityRequestDTO.getName());
			city.setLastUpdate(Instant.now());
			city.setCountryId(existingCountry);

			return cityRepository.save(city);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Erro de integridade de dados ao criar a cidade com o país .", e);
		}

	}

	@Transactional
	public void deleteCityById(Long id) throws ResourceNotFoundException, DataIntegrityException {

		try {

			Optional<City> optional = cityRepository.findById(id);

			if (!optional.isPresent()) {
				throw new ResourceNotFoundException("Cidade não encontrada.");
			}

			cityRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro de integridade de dados ao excluir a cidade.", e);
		}
	}

	@Transactional
	public City updateCityById(Long id, CityUpdateDTO cityUpdateDTO) throws ResourceNotFoundException {
		Optional<City> optional = cityRepository.findById(id);

		if (!optional.isPresent()) {
			throw new ResourceNotFoundException("Cidade não encontrada.");
		}

		try {
			City existingCity = optional.get();

			existingCity.setName(cityUpdateDTO.getName());
			existingCity.setLastUpdate(Instant.now());

			return cityRepository.save(existingCity);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Erro ao acessar dados ao atualizar a cidade.", e);
		}
	}

	public City getOrCreateCity(CityRequestDTO cityRequestDTO, Country country) {
		String cityName = cityRequestDTO.getName();

		City existingCity = cityRepository.findByName(cityName);

		if (existingCity != null) {
			return existingCity;
		} else {
			City newCity = new City();
			newCity.setName(cityName);
			newCity.setLastUpdate(Instant.now());
			newCity.setCountryId(country);

			return cityRepository.save(newCity);
		}
	}
}
