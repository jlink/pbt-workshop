package pbt.examples;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

class MetamorphicPropertyExamples {

	@Property
	boolean summingAdditionalNumber(
			@ForAll @Size(max = 100) List<Integer> addends,
			@ForAll int x
	) {
		long sum = sumUp(addends);
		addends.add(x);
		return sum + x == sumUp(addends);
	}

	private long sumUp(final List<Integer> addends) {
		return addends.stream().mapToLong(i -> i).sum();
	}
}
