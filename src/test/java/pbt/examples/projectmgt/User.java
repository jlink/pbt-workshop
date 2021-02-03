package pbt.examples.projectmgt;

public class User {

	private final String email;

	public User(String email) {
		this.email = email;
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
}
