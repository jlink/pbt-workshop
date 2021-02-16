package pbt.examples.projectmgt;

public class User {

	private final String email;

	public User(String email) {
		assertValidEmail(email);
		// This is actually a bug because local part of email address might be case sensitive
		this.email = email.toLowerCase();
	}

	private void assertValidEmail(String email) {
		long countAt = email.chars().filter(c -> c == '@').count();
		if (countAt != 1) {
			throw new IllegalArgumentException(String.format("Email <%s> should contain exactly one '@' sign", email));
		}
	}

	public String getEmail() {
		return email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return email.equals(user.email);
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}

	@Override
	public String toString() {
		return String.format("User<%s>", email);
	}
}
