package pbt.examples.reverse;

import java.util.ArrayList;
import java.util.*;

import net.jqwik.api.*;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

@Label("Collections.reverse(List)")
class ReverseListTests {

	@Example
	@Label("empty list")
	void emptyList() {
		List<Integer> aList = Collections.emptyList();
		assertThat(reverse(aList)).isEmpty();
	}

	@Example
	@Label("one element")
	void oneElement() {
		List<Integer> aList = Collections.singletonList(1);
		assertThat(reverse(aList)).containsExactly(1);
	}

	@Example
	@Label("many elements")
	void manyElements() {
		List<Integer> aList = asList(1, 2, 3, 4, 5, 6);
		assertThat(reverse(aList)).containsExactly(6, 5, 4, 3, 2, 1);
	}

	@Example
	@Label("duplicate elements")
	void duplicateElements() {
		List<Integer> aList = asList(1, 2, 2, 4, 6, 6);
		assertThat(reverse(aList)).containsExactly(6, 6, 4, 2, 2, 1);
	}

	private <T> List<T> reverse(List<T> original) {
		List<T> clone = new ArrayList<>(original);
		// Should produce failing properties:
		// List<T> clone = new ArrayList<>(new HashSet<>(original));
		Collections.reverse(clone);
		return clone;
	}

}
