package pbt.exercise1;

import java.util.*;

import org.assertj.core.api.*;

import net.jqwik.api.*;

class SortingProperties {

	@Property
	void anyListOfIntegersCanBeSorted(@ForAll List<Integer> aList) {
		Assertions.assertThat(sort(aList)).isNotNull();
	}

	// Exercise: Write property methods for all properties you have identified

	private <T extends Comparable<? super T>> List<T> sort(List<T> original) {
		List<T> clone = new ArrayList<>(original);
		Collections.sort(clone);
		return clone;
	}

}
