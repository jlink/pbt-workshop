package pbt.exercise4;

import pbt.exercise4.CircularBufferActions.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

class CircularBufferProperties {

	@Property
	// @Report(Reporting.GENERATED)
	void checkBuffer(@ForAll("sequences") ActionSequence<Model> sequence) {
		sequence.run(new Model());
	}

	@Provide
	Arbitrary<ActionSequence<Model>> sequences() {
		return Arbitraries.sequences(pbt.exercise4.CircularBufferActions.actions());
	}

}
