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

	/**
	 * A simple metamorphic property: Given the result of sorting one list,
	 * sorting a shuffled original list has the same result.
	 */
	@Property
	void shuffledListHasSameSortedResult(@ForAll List<Integer> unsorted, @ForAll Random random) {
		List<Integer> sorted = sort(unsorted);
		Collections.shuffle(unsorted, random);
		List<Integer> sortedShuffled = sort(unsorted);
		Assertions.assertThat(sorted).isEqualTo(sortedShuffled);
	}

	@Property
	boolean sortedListIsReallySorted(@ForAll List<Integer> unsorted) {
		return isSorted(sort(unsorted));
	}

	private boolean isSorted(List<Integer> sorted) {
		if (sorted.size() < 2) return true;
		return sorted.get(0) <= sorted.get(1)
				&& isSorted(sorted.subList(1, sorted.size()));
	}

	@Property
	<T extends Comparable<T>> void anyListOfComparablesCanBeSorted(@ForAll List<T> aList) {
		Assertions.assertThat(sort(aList)).isNotNull();
	}

	private <T extends Comparable<? super T>> List<T> sort(List<T> original) {
		List<T> clone = new ArrayList<>(original);
		Collections.sort(clone);
		return clone;
	}

}
