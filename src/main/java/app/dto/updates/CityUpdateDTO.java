package app.dto.updates;

import app.entities.City;

public class CityUpdateDTO {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City convertToDTO(CityUpdateDTO dto) {
		City city = new City();

		city.setName(dto.getName());
		return city;
	}

}
