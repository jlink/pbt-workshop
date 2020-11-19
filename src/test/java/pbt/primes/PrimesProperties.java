package pbt.primes;

import java.util.*;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

class PrimesProperties {

	@Example
	void just2() {
		List<Integer> factors = Primes.factorize(2);
		assertThat(factors).containsExactly(2);
	}

}
