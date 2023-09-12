package app.dto.requests;

import app.entities.Address;

public class AddressRequestDTO {

	private String address;

	private String address2;

	private String district;

	private String postalCode;

	private String phone;

	private CityRequestDTO cityId;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public CityRequestDTO getCityId() {
		return cityId;
	}

	public void setCityId(CityRequestDTO cityId) {
		this.cityId = cityId;
	}

	public Address convertToDTO(AddressRequestDTO dto) {
		Address address = new Address();

		address.setAddress(dto.getAddress());
		address.setAddress2(dto.getAddress2());
		address.setDistrict(dto.getDistrict());
		address.setPostalCode(dto.getPostalCode());
		address.setPhone(dto.getPhone());
		address.setCityId(CityRequestDTO.convertToDTO(getCityId()));

		return address;
	}

}
