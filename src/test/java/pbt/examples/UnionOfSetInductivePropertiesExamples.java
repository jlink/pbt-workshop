package pbt.examples;

import java.util.*;

import org.assertj.core.api.*;

import net.jqwik.api.*;

class UnionOfSetInductivePropertiesExamples {

	@Property
	void unionOfSetWithEmptySetIsOriginalSet(@ForAll Set<Integer> set) {
		Set<Integer> union = union(set, Collections.emptySet());
		Assertions.assertThat(union).isEqualTo(set);
	}

	@Property
	void unionOfTwoSetsContainsAllElementsOfBoth(
			@ForAll Set<Integer> first,
			@ForAll Set<Integer> second
	) {
		Set<Integer> union = union(first, second);
		Assertions.assertThat(union).containsAll(first);
		Assertions.assertThat(union).containsAll(second);
	}

	private <T> Set<T> union(final Set<T> left, final Set<T> right) {
		Set<T> union = new HashSet<>(left);
		union.addAll(right);
		return union;
	}
}
