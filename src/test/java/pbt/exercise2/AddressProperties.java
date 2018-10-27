package pbt.exercise2;

import org.assertj.core.api.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

class AddressProperties {

	//@Property
	@Label("Address instances are valid")
	void addressInstancesAreValid(@ForAll Address anAddress) {
		assertThat(anAddress).is(anyOf(
				instanceOf(StreetAddress.class),
				instanceOf(PostOfficeBox.class)
		));
		assertThat(anAddress.city()).isNotEmpty();
		if (anAddress.zipCode().isPresent()) {
			isValidGermanZipCode(anAddress.zipCode().get());
		}
	}

	private void isValidGermanZipCode(@ForAll String germanZipcode) {
		assertThat(germanZipcode.chars()).allMatch(c -> c >= '0' && c <= '9');
	}

	private Condition<? super Address> instanceOf(final Class<?> expectedType) {
		return new Condition<Address>() {
			@Override
			public boolean matches(Address value) {
				return value.getClass() == expectedType;
			}
		};
	}

}
