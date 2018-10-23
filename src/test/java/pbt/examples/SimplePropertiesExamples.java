package pbt.examples;

import java.util.*;

import org.assertj.core.api.*;

import net.jqwik.api.*;

class SimplePropertiesExamples {

	@Property(tries = 10)
	@Report(Reporting.GENERATED)
	boolean generateIntegers(@ForAll int aNumber) {
		return true;
		// return aNumber % 2 == 0;
	}

	@Property(tries = 20)
	@Report(Reporting.GENERATED)
	void generateListOfStrings(@ForAll List<String> listOfStrings) {
		// Assertions.assertThat(listOfStrings).isNotEmpty();
	}
}
