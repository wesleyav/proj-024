package app.dto.requests;

import app.entities.City;

public class CityRequestDTO {

	private String name;

	private CountryRequestDTO country;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CountryRequestDTO getCountry() {
		return country;
	}

	public void setCountry(CountryRequestDTO country) {
		this.country = country;
	}

	public static City convertToDTO(CityRequestDTO dto) {
		City city = new City();

		city.setName(dto.getName());
		city.setCountryId(CountryRequestDTO.convertToDTO(dto.getCountry()));

		return city;
	}

}
