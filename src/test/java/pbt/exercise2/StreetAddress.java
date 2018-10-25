package pbt.exercise2;

public class StreetAddress extends Address {

	private final String street;
	private final String houseNumber;

	public StreetAddress(Country country, String city, String germanZipcode, String street, String houseNumber) {
		super(country, city, germanZipcode);

		if (street == null || street.trim().isEmpty() || street.trim().length() > 30) {
			throw new IllegalArgumentException();
		}
		this.street = street.trim();

		if (!isNullOrValidHouseNumber(houseNumber)) {
			throw new IllegalArgumentException();
		}
		this.houseNumber = houseNumber;
	}

	private boolean isNullOrValidHouseNumber(String houseNumber) {
		if (houseNumber == null) {
			return true;
		}
		if (houseNumber.trim().isEmpty()) {
			return false;
		}
		return houseNumber.chars().allMatch(c -> "1234567890/".contains(Character.toString((char) c)));
	}

	@Override
	public String toString() {
		String houseNumberString = houseNumber != null ? " " + houseNumber : "";
		return String.format("%s%s, %s", street, houseNumberString, super.toString());
	}
}
