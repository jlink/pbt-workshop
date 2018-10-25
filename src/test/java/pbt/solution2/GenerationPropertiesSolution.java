package pbt.solution2;

import org.assertj.core.api.*;
import pbt.exercise2.*;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;

class GenerationPropertiesSolution {

	@Property
	@Label("German zipcode is valid")
	void germanZipcodeIsValid(@ForAll @StringLength(5) @CharRange(from = '0', to = '9') String germanZipcode) {
		assertThat(germanZipcode).hasSize(5);
		isValidGermanZipCode(germanZipcode);
	}

	@Property
	@Report({Reporting.GENERATED})
	@Label("String.substring() never throws exception")
	void stringSubstringWorks(@ForAll("substringParams") Tuple3<String, Integer, Integer> substringParams) {
		String initialString = substringParams.get1();
		int beginIndex = substringParams.get2();
		int endIndex = substringParams.get3();

		assertThatCode(() -> initialString.substring(beginIndex, endIndex)).doesNotThrowAnyException();
	}

	@Provide
	Arbitrary<Tuple3<String, Integer, Integer>> substringParams() {
		Arbitrary<String> string = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(50);
		return string.flatMap(s -> {
			Arbitrary<Integer> begin = Arbitraries.integers().between(0, s.length() - 1);
			return begin.flatMap(
					b -> {
						Arbitrary<Integer> end = Arbitraries.integers().between(b, s.length() - 1);
						return end.map(e -> Tuple.of(s, b, e));
					}
			);
		});
	}

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
