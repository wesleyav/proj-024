package app.dto.requests;

import app.entities.Country;

public class CountryRequestDTO {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Country convertToDTO(CountryRequestDTO dto) {
		Country country = new Country();

		country.setName(dto.getName());

		return country;
	}

}
