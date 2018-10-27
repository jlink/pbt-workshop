package pbt.solution2;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;

@Label("German zipcode")
class ZipCodePropertiesSolution {

	@Property
	@Label("is valid")
	void germanZipcodeIsValid(@ForAll @StringLength(5) @CharRange(from = '0', to = '9') String germanZipcode) {
		assertThat(germanZipcode).hasSize(5);
		isValidGermanZipCode(germanZipcode);
	}

	private void isValidGermanZipCode(@ForAll String germanZipcode) {
		assertThat(germanZipcode.chars()).allMatch(c -> c >= '0' && c <= '9');
	}
}
