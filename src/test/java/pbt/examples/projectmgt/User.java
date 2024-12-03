package pbt.examples.projectmgt;

public record User(String email) {

	public User(String email) {
		assertValidEmail(email);
		// This is actually a bug because local part of email address might be case-sensitive
		this.email = email.toLowerCase();
	}

	private void assertValidEmail(String email) {
		long countAt = email.chars().filter(c -> c == '@').count();
		if (countAt != 1) {
			throw new IllegalArgumentException(String.format("Email <%s> should contain exactly one '@' sign", email));
		}
	}

	@Override
	public String toString() {
		return String.format("User<%s>", email);
	}
}
