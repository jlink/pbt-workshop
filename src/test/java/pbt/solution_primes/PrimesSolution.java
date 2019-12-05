package pbt.solution_primes;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static org.assertj.core.api.Assertions.*;

class PrimesSolution {

	@Property
	void prime_is_factored_to_list_of_itself(@ForAll("primes") int prime) {
		assertThat(factorize(prime)).containsExactly(prime);
	}

	@Property
	void prime_squared_is_factored_to_list_of_twice_itself(@ForAll("primes") int prime) {
		assertThat(factorize(prime * prime)).containsExactly(prime, prime);
	}

	@Property
	void prime_powered_to_n_is_factored_to_list_of_n_times_itself(
			@ForAll("primes") int prime,
			@ForAll @IntRange(min = 1, max = 5) int power
	) {
		int number = (int) Math.pow(prime, power);
		assertThat(factorize(number)).containsOnly(prime);
		assertThat(factorize(number)).hasSize(power);
	}

	@Property
	void product_of_two_primes_is_factored_to_list_of_both(
			@ForAll("primes") int prime1,
			@ForAll("primes") int prime2
	) {
		Assume.that(prime1 < prime2);
		assertThat(factorize(prime1 * prime2)).containsExactly(prime1, prime2);
	}

	@Property
	void product_of_list_of_primes_is_factored_to_original_list(
			@ForAll("listOfPrimes") List<Integer> primes
	) {
		primes.sort(Integer::compareTo);
		int product = product(primes);
		Assume.that(product > 1);
		assertThat(factorize(product)).isEqualTo(primes);
	}

	@Property
	boolean any_number_above_1_can_be_factorized(
			@ForAll @IntRange(min = 2) int number
	) {
		List<Integer> factors = factorize(number);
		int product = product(factors);

		return product == number;
	}

	@Provide
	Arbitrary<List<Integer>> listOfPrimes() {
		return primes().list().ofMinSize(1).ofMaxSize(7);
	}

	@Provide
	Arbitrary<Integer> primes() {
		return Arbitraries.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);
	}

	@Provide
	Arbitrary<Integer> primesNaive() {
		return Arbitraries.integers().between(2, Integer.MAX_VALUE).filter(i -> isPrime(i));
	}

	private boolean isPrime(int number) {
		return false;
	}

	private int product(List<Integer> primes) {
		return primes.stream().reduce(1, (i1, i2) -> i1 * i2);
	}

	static List<Integer> factorize(int number) {
		ArrayList<Integer> factors = new ArrayList<>();

		int candidate = 2;
		while (number >= candidate) {
			while (number % candidate != 0) {
				if (Math.sqrt(number) < candidate) {
					candidate = number;
				} else {
					candidate++;
				}
			}
			factors.add(candidate);
			number /= candidate;
		}
		return factors;
	}

}
