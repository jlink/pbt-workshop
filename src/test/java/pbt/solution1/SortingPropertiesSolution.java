package pbt.solution1;

import java.util.*;

import org.assertj.core.api.*;

import net.jqwik.api.*;

class SortingPropertiesSolution {

	@Property
	boolean numberOfElementsRemainsTheSame(@ForAll List<Integer> unsorted) {
		return unsorted.size() == sort(unsorted).size();
	}

	@Property
	void allElementsStayInSortedList(@ForAll List<Integer> unsorted) {
		List<Integer> sorted = sort(unsorted);
		Assertions.assertThat(unsorted).allMatch(sorted::contains);
	}

	@Property
	void sortingIsIdempotent(@ForAll List<Integer> unsorted) {
		List<Integer> sorted = sort(unsorted);
		List<Integer> sortedTwice = sort(sorted);
		Assertions.assertThat(sorted).isEqualTo(sortedTwice);
	}

	@Property
	boolean sortedListIsReallySorted(@ForAll List<Integer> unsorted) {
		return isSorted(sort(unsorted));
	}

	private boolean isSorted(List<Integer> sorted) {
		if (sorted.size() <= 1) return true;
		return sorted.get(0) <= sorted.get(1)
					   && isSorted(sorted.subList(1, sorted.size()));
	}

	private <T extends Comparable<? super T>> List<T> sort(List<T> original) {
		List<T> clone = new ArrayList<>(original);
		Collections.sort(clone);
		return clone;
	}

}
