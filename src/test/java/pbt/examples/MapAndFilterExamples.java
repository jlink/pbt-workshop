package pbt.examples;

import net.jqwik.api.*;

class MapAndFilterExamples {

	@Property
	boolean evenNumbersAreEven(@ForAll("even") int anInt) {
		return anInt % 2 == 0;
	}

	@Provide
	Arbitrary<Integer> even() {
		return Arbitraries.integers().filter(i -> i % 2 == 0);
	}

	@Provide
	Arbitrary<Integer> evenUpTo10000() {
		return Arbitraries.integers().between(1, 10000).filter(i -> i % 2 == 0);
	}

	@Provide
	Arbitrary<Integer> _evenUpTo10000() {
		return Arbitraries.integers().between(1, 5000).map(i -> i * 2);
	}

}
