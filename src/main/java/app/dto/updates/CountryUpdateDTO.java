package app.dto.updates;

import app.entities.Country;

public class CountryUpdateDTO {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country convertToDTO(CountryUpdateDTO dto) {
		Country country = new Country();

		country.setName(dto.getName());

		return country;
	}

}
