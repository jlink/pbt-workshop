package pbt.exercise2;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

class ZipCodeProperties {

	//@Property
	@Label("German zipcode is valid")
	void germanZipcodeIsValid(@ForAll String germanZipcode) {
		assertThat(germanZipcode).hasSize(5);
		isValidGermanZipCode(germanZipcode);
	}

	private void isValidGermanZipCode(@ForAll String germanZipcode) {
		assertThat(germanZipcode.chars()).allMatch(c -> c >= '0' && c <= '9');
	}
}
