package pbt.primes;

import java.util.*;

class Primes {

	static List<Integer> factorize(int number) {
		List<Integer> factors = new ArrayList<>();
		int candidate = 2;
		while (number >= candidate) {
			while (number % candidate != 0) {
				if (candidate > Math.sqrt(number)) {
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
