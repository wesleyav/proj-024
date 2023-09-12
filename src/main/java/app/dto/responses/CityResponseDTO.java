package app.dto.responses;

import java.time.Instant;

import app.entities.City;

public class CityResponseDTO {

	private Long cityId;

	private String name;

	private Instant lastUpdate;

	private CountryResponseDTO country;

	public CityResponseDTO(City city) {
		this.cityId = city.getCityId();
		this.name = city.getName();
		this.lastUpdate = city.getLastUpdate();
		this.country = new CountryResponseDTO(city.getCountryId());
	}

	public Long getCityId() {
		return cityId;
	}

	public String getName() {
		return name;
	}

	public Instant getLastUpdate() {
		return lastUpdate;
	}

	public CountryResponseDTO getCountry() {
		return country;
	}

}
