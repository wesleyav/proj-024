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

import app.dto.requests.AddressRequestDTO;
import app.dto.updates.AddressUpdateDTO;
import app.entities.Address;
import app.entities.City;
import app.entities.Country;
import app.repositories.AddressRepository;
import app.services.exceptions.AddressAlreadyExistsException;
import app.services.exceptions.DataIntegrityException;
import app.services.exceptions.InvalidPaginationParametersException;
import app.services.exceptions.PaginationDataAccessException;
import app.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private CityService cityService;

	@Autowired
	private CountryService countryService;

	@Transactional
	public Optional<Address> getAddressById(Long id) {
		Optional<Address> optional = addressRepository.findById(id);

		if (optional.isPresent()) {
			return Optional.of(optional.get());
		} else {
			throw new EntityNotFoundException("Address not found with ID: " + id);
		}
	}

	@Transactional
	public Page<Address> getAllAddressess(Integer pageNumber, Integer pageSize) throws PaginationDataAccessException {
		try {
			if (pageNumber < 0 || pageSize <= 0) {
				throw new InvalidPaginationParametersException("Parâmetros de paginação inválidos.");
			}
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			return addressRepository.findAll(pageable);
		} catch (InvalidPaginationParametersException e) {
			throw new InvalidPaginationParametersException("Erro ao recuperar endereços paginados: ");
		} catch (DataAccessException e) {
			throw new PaginationDataAccessException("Erro ao recuperar endereços paginados.");
		}
	}

	@Transactional
	public Address createAddress(AddressRequestDTO addressRequestDTO) throws AddressAlreadyExistsException {

//		if (addressRepository.existsByAddress(addressRequestDTO.getAddress())) {
//			throw new AddressAlreadyExistsException("O endereço já existe.");
//		}

		Country existingCountry = countryService.getOrCreateCountry(addressRequestDTO.getCityId().getCountry());
		Country country = new Country();
		country.setName(existingCountry.getName());
		country.setLastUpdate(Instant.now());

		City existingCity = cityService.getOrCreateCity(addressRequestDTO.getCityId(), existingCountry);
		City city = new City();
		city.setName(existingCity.getName());
		city.setLastUpdate(Instant.now());
		city.setCountryId(existingCountry);

		// Crie o endereço e associa-o à cidade e ao país
		Address address = addressRequestDTO.convertToDTO(addressRequestDTO);
		address.setAddress(addressRequestDTO.getAddress());
		address.setAddress2(addressRequestDTO.getAddress2());
		address.setDistrict(addressRequestDTO.getDistrict());
		address.setPostalCode(addressRequestDTO.getPostalCode());
		address.setPhone(addressRequestDTO.getPhone());
		address.setLastUpdate(Instant.now());
		address.setCityId(existingCity);

		return addressRepository.save(address);
	}

	@Transactional
	public void deleteAddressById(Long id) throws ResourceNotFoundException, DataIntegrityException {

		try {

			Optional<Address> optional = addressRepository.findById(id);

			if (!optional.isPresent()) {
				throw new ResourceNotFoundException("Endereço não encontrado.");
			}

			addressRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Erro de integridade de dados ao excluir o endereço.", e);
		}
	}

	@Transactional
	public Address updateAddressById(Long id, AddressUpdateDTO addressUpdateDTO) throws ResourceNotFoundException {
		Optional<Address> optional = addressRepository.findById(id);

		if (!optional.isPresent()) {
			throw new ResourceNotFoundException("Endereço não encontrado.");
		}

		try {
			Address existingAddress = optional.get();

			existingAddress.setAddress2(addressUpdateDTO.getAddress2());
			existingAddress.setDistrict(addressUpdateDTO.getDistrict());
			existingAddress.setPhone(addressUpdateDTO.getPhone());
			existingAddress.setLastUpdate(Instant.now());

			// Consulta o banco de dados para obter os valores atuais de City e Country
//	        City existingCity = existingAddress.getCityId();
//	        Country existingCountry = existingCity.getCountryId();

			// Salve as alterações no endereço atualizado
			Address updatedAddress = addressRepository.save(existingAddress);

//	        updatedAddress.setCityId(existingCity);
//	        updatedAddress.getCityId().setCountryId(existingCountry);

			return addressRepository.save(updatedAddress);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Erro ao acessar dados ao atualizar a cidade.", e);
		}
	}
}
