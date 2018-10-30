package pbt.exercise4;

import java.util.*;

import pbt.solution4.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

import static org.assertj.core.api.Assertions.*;

class CircularBufferActions {

	static class Model {
		int capacity;
		CircularBuffer<Object> buffer;
		List<Object> contents = new ArrayList<>();

		void initialize(int capacity) {
			this.capacity = capacity;
			this.buffer = new CircularBuffer<>(capacity);
			this.contents.clear();
		}

		@Override
		public String toString() {
			return buffer.toString();
		}
	}

	static Arbitrary<Action<Model>> actions() {
		return Arbitraries.oneOf(create());
	}

	private static Arbitrary<Action<Model>> create() {
		return Arbitraries.integers().between(0, 100).map(NewAction::new);
	}

	private static Arbitrary<Action<Model>> put() {
		return null;
	}

	private static Arbitrary<Action<Model>> get() {
		return null;
	}

	private static Arbitrary<Action<Model>> size() {
		return null;
	}

	private static class NewAction implements Action<Model> {

		private final int capacity;

		private NewAction(int capacity) {
			this.capacity = capacity;
		}

		@Override
		public Model run(Model model) {
			model.initialize(capacity);
			assertThat(model.buffer.size()).isEqualTo(0);
			return model;
		}

		@Override
		public String toString() {
			return String.format("new(%s)", capacity);
		}

	}

}
