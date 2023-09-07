package app.dto.responses;

import java.time.Instant;

import app.entities.Address;

public class AddressResponseDTO {

	private String address;

	private String address2;

	private String district;

	private String postalCode;

	private String phone;

	private Instant lastUpdate;

	public AddressResponseDTO(Address address) {
		this.address = address.getAddress();
		this.address2 = address.getAddress2();
		this.district = address.getDistrict();
		this.postalCode = address.getPostalCode();
		this.phone = address.getPhone();
		this.lastUpdate = address.getLastUpdate();
	}

	public String getAddress() {
		return address;
	}

	public String getAddress2() {
		return address2;
	}

	public String getDistrict() {
		return district;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public Instant getLastUpdate() {
		return lastUpdate;
	}

}
