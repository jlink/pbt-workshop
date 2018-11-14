package pbt.solution2;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;

@Label("German zip code")
class ZipCodePropertiesSolution {

	@Property
	@Label("is valid")
	void germanZipCodeIsValid(@ForAll @StringLength(5) @CharRange(from = '0', to = '9') String germanZipCode) {
		Assume.that(!germanZipCode.matches("00\\d+"));
		assertThat(germanZipCode).hasSize(5);
		isValidGermanZipCode(germanZipCode);
	}

	private void isValidGermanZipCode(@ForAll String germanZipcode) {
		assertThat(germanZipcode.chars()).allMatch(c -> c >= '0' && c <= '9');
		assertThat(germanZipcode).doesNotStartWith("00");
	}
}
