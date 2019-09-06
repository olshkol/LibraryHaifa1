package telran.library.dto;

public class PublisherAuthor {
	String name;
	String country;
	public String getName() {
		return name;
	}
	public String getCountry() {
		return country;
	}
	public PublisherAuthor(String name, String country) {
		super();
		this.name = name;
		this.country = country;
	}
	public PublisherAuthor() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PublisherAuthor other = (PublisherAuthor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
