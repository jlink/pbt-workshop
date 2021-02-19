package pbt.solution2;

import org.assertj.core.api.*;
import pbt.exercise2.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Label("Addresses")
class AddressPropertiesSolution {

	@Property
	@Label("are valid")
	void addressInstancesAreValid(@ForAll("validAddress") Address anAddress) {
		assertThat(anAddress).is(anyOf(
				instanceOf(StreetAddress.class),
				instanceOf(PostOfficeBox.class)
		));
		assertThat(anAddress.city()).isNotEmpty();
		if (anAddress.zipCode().isPresent()) {
			isValidGermanZipCode(anAddress.zipCode().get());
		}
	}

	@Provide
	Arbitrary<Address> validAddress() {
		return Arbitraries.oneOf(streetAddress(), pob());
	}

	private Arbitrary<Address> streetAddress() {
		Arbitrary<Country> country = Arbitraries.of(Country.class);
		Arbitrary<String> city = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30);
		Arbitrary<String> street = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20);
		Arbitrary<String> houseNumber = Arbitraries.strings().withCharRange('0', '9').ofMinLength(1).ofMaxLength(4);
		Arbitrary<String> addendum = Arbitraries.oneOf(
				Arbitraries.just(""),
				Arbitraries.integers().between(1, 9).map(i -> Integer.toString(i))
		);

		return Combinators.combine(country, city, germanZipCode(), street, houseNumber, addendum)
						  .as((co, ci, z, s, h, a) -> {
							  String fullHouseNumber = (a != null) ? h + "/" + a : h;
							  return new StreetAddress(co, ci, z, s, fullHouseNumber);
						  });
	}

	private Arbitrary<Address> pob() {
		Arbitrary<Country> country = Arbitraries.of(Country.class);
		Arbitrary<String> city = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30);
		Arbitrary<String> pobIdentifier = Arbitraries.strings().alpha().numeric().ofMinLength(1).ofMaxLength(10).map(String::toUpperCase);

		return Combinators.combine(country, city, germanZipCode(), pobIdentifier)
						  .as(PostOfficeBox::new);
	}

	private Arbitrary<String> germanZipCode() {
		return Arbitraries.strings()
						  .withCharRange('0', '9')
						  .ofLength(5)
						  .filter(z -> !z.startsWith("00"));
	}

	private void isValidGermanZipCode(@ForAll String germanZipcode) {
		assertThat(germanZipcode.chars()).allMatch(c -> c >= '0' && c <= '9');
		assertThat(germanZipcode).doesNotStartWith("00");
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
