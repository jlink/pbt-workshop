package pbt.examples;

import org.assertj.core.api.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.data.Percentage.*;

class ConstrainingValuesExamples {


	// Expected to fail since there is no sqrt of negative numbers
	// @Property
	void squareOfRootIsOriginalValue(@ForAll double aNumber) {
		double sqrt = Math.sqrt(aNumber);
		Assertions.assertThat(sqrt * sqrt).isCloseTo(aNumber, withPercentage(10));
	}



	// Use annotation to constrain generated numbers

	@Property
	void squareOfRootIsOriginalValue_1(@ForAll @Positive double aNumber) {
		double sqrt = Math.sqrt(aNumber);
		Assertions.assertThat(sqrt * sqrt).isCloseTo(aNumber, withPercentage(1));
	}



	// Provide arbitrary through builder methods

	@Property
	void squareOfRootIsOriginalValue_2(@ForAll("positiveDoubles") double aNumber) {
		double sqrt = Math.sqrt(aNumber);
		Assertions.assertThat(sqrt * sqrt).isCloseTo(aNumber, withPercentage(1));
	}

	@Provide
	Arbitrary<Double> positiveDoubles() {
		return Arbitraries.doubles().between(0, Double.MAX_VALUE);
	}




	// Use assumption to constrain execution of checks

	@Property
	void squareOfRootIsOriginalValue_3(@ForAll double aNumber) {
		Assume.that(aNumber > 0);
		double sqrt = Math.sqrt(aNumber);
		Assertions.assertThat(sqrt * sqrt).isCloseTo(aNumber, withPercentage(1));
	}

}
