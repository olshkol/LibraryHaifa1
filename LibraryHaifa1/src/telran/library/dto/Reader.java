package telran.library.dto;

import java.time.LocalDate;

import lombok.Setter;
@Setter
public class Reader {
	long id;
	String fullName;
	String phone;
	String email;
	String address;
	LocalDate birthDate;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reader other = (Reader) obj;
		if (id != other.id)
			return false;
		return true;
	}
	public Reader() {
	}
	public Reader(long id, String fullName, String phone, String email, String address, LocalDate birthDate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.birthDate = birthDate;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getId() {
		return id;
	}
	public String getFullName() {
		return fullName;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}

}
