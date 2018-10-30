package pbt.solution4;

import pbt.solution4.CircularBufferActions.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

import static org.assertj.core.api.Assertions.*;

class CircularBufferProperties {

	@Property
	//@Report(Reporting.GENERATED)
	void checkBuffer(@ForAll("sequences") ActionSequence<Model> sequence) {

		Invariant<Model> sizeMustNotBeNegative =
				model -> assertThat(model.buffer.size())
								 .as("Size must not be negative")
								 .isGreaterThanOrEqualTo(0);

		Invariant<Model> sizeMustNotExceedCapacity =
				model -> assertThat(model.buffer.size())
								 .as("Size must not exceed capacity")
								 .isLessThanOrEqualTo(model.capacity);

		sequence
				.withInvariant(sizeMustNotBeNegative)
				.withInvariant(sizeMustNotExceedCapacity)
				.run(new Model());

		// Display the actual run sequence
		// System.out.println(sequence.runSequence());
	}

	@Provide
	Arbitrary<ActionSequence<Model>> sequences() {
		return Arbitraries.sequences(CircularBufferActions.actions());
	}

}
