package app.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "city")
public class City implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "city_id")
	private Long cityId;

	@Column(name = "city", nullable = false, length = 50)
	private String name;

	@Column(name = "last_update", nullable = false)
	private Instant lastUpdate;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private Country countryId;

	@OneToMany(mappedBy = "cityId")
	@JsonIgnore
	private List<Address> addresses = new ArrayList<>();

	public City() {
	}

	public City(Long cityId, String name, Instant lastUpdate) {
		this.cityId = cityId;
		this.name = name;
		this.lastUpdate = lastUpdate;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Instant lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Country getCountryId() {
		return countryId;
	}

	public void setCountryId(Country countryId) {
		this.countryId = countryId;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

}
