package pbt.exercise2;

public class PostOfficeBox extends Address {

	private final String identifier;

	public PostOfficeBox(Country country, String city, String germanZipcode, String identifier) {
		super(country, city, germanZipcode);

		if (identifier == null || identifier.trim().isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.identifier = identifier.trim();
	}
}
