package pbt.examples;

import java.math.*;
import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;

class FluentInterfaceExamples {

	@Property
	@Report(Reporting.GENERATED)
	void everythingFlows(
			@ForAll("fluentString") String string,
			@ForAll("fluentDecimal") BigDecimal decimal,
			@ForAll("fluentList") List<Integer> list

	) { }

	@Provide
	StringArbitrary fluentString() {
		return Arbitraries.strings()
						  .alpha()
						  .numeric()
						  .withChars('?', '!', '.')
						  .ofMinLength(2)
						  .ofMaxLength(10);
	}

	@Provide
	BigDecimalArbitrary fluentDecimal() {
		return Arbitraries.bigDecimals()
				.between(BigDecimal.ONE, BigDecimal.TEN)
				.ofScale(3);
	}

	@Provide
	SizableArbitrary<List<Integer>> fluentList() {
		return Arbitraries.integers()
						  .greaterOrEqual(42)
						  .lessOrEqual(99)
						  .list().ofMaxSize(10);
	}
}
