package app.dto.responses;

import java.time.Instant;

import app.entities.Country;

public class CountryResponseDTO {

	private Long countryId;

	private String name;

	private Instant lastUpdate;

	public CountryResponseDTO(Country country) {
		this.countryId = country.getCountryId();
		this.name = country.getName();
		this.lastUpdate = country.getLastUpdate();
	}

	public Long getCountryId() {
		return countryId;
	}

	public String getName() {
		return name;
	}

	public Instant getLastUpdate() {
		return lastUpdate;
	}

}
