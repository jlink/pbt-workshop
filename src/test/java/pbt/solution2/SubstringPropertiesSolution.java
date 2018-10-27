package pbt.solution2;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.*;

import static org.assertj.core.api.Assertions.*;

@Label("String.substring()")
class SubstringPropertiesSolution {

	@Property
	@Label("never throws exception")
	void stringSubstringWorks(@ForAll("substringParams") Tuple3<String, Integer, Integer> substringParams) {
		String initialString = substringParams.get1();
		int beginIndex = substringParams.get2();
		int endIndex = substringParams.get3();

		assertThatCode(() -> initialString.substring(beginIndex, endIndex)).doesNotThrowAnyException();
	}

	@Provide
	Arbitrary<Tuple3<String, Integer, Integer>> substringParams() {
		Arbitrary<String> string = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(50);
		return string.flatMap(s -> {
			Arbitrary<Integer> begin = Arbitraries.integers().between(0, s.length() - 1);
			return begin.flatMap(
					b -> {
						Arbitrary<Integer> end = Arbitraries.integers().between(b, s.length() - 1);
						return end.map(e -> Tuple.of(s, b, e));
					}
			);
		});
	}

}
