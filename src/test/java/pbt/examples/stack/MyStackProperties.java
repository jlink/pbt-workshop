package pbt.examples.stack;

import org.assertj.core.api.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

class MyStackProperties {

	@Property(tries = 10)
	void checkMyStackMachine(@ForAll("sequences") ActionSequence<MyStringStack> sequence) {
		Invariant<MyStringStack> sizeNeverNegative = stack -> {
			Assertions.assertThat(stack.size()).isGreaterThanOrEqualTo(0);
		};

		sequence
				.withInvariant(sizeNeverNegative)
				.run(new MyStringStack());
	}

	@Provide
	Arbitrary<ActionSequence<MyStringStack>> sequences() {
		return Arbitraries.sequences(MyStringStackActions.actions());
	}

	@Property
	@Label("are equal after same sequence of pushes")
	boolean equality(@ForAll("pushes") ActionSequence<MyStringStack> sequence) {
		MyStringStack stack1 = sequence.run(new MyStringStack());
		MyStringStack stack2 = sequence.run(new MyStringStack());
		return stack1.equals(stack2);
	}

	@Provide
	Arbitrary<ActionSequence<MyStringStack>> pushes() {
		return Arbitraries.sequences(MyStringStackActions.push());
	}

}
