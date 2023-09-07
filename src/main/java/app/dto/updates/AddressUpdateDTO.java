package app.dto.updates;

import app.entities.Address;

public class AddressUpdateDTO {

	private String address;

	private String address2;

	private String district;

	private String phone;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address convertToDTO(AddressUpdateDTO dto) {
		Address address = new Address();

		address.setAddress(dto.getAddress());
		address.setAddress2(dto.getAddress2());
		address.setDistrict(dto.getDistrict());
		address.setPhone(dto.getPhone());

		return address;
	}

}
