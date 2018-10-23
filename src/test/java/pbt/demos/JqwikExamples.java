package pbt.demos;

import net.jqwik.api.*;

class JqwikExamples {

	@Property(tries = 10)
	@Report(Reporting.GENERATED)
	boolean generateIntegers(@ForAll int aNumber) {
		return true;
		// return aNumber % 2 == 0;
	}
}
