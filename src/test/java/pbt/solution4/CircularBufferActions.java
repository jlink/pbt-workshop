package pbt.solution4;

import java.util.*;

import net.jqwik.api.*;
import net.jqwik.api.stateful.*;

import static org.assertj.core.api.Assertions.*;

class CircularBufferActions {

	static class Model {
		int capacity;
		CircularBuffer_BugFixed<Object> buffer;
		List<Object> contents = new ArrayList<>();

		void initialize(int capacity) {
			this.capacity = capacity;
			this.buffer = new CircularBuffer_BugFixed<>(capacity);
			this.contents.clear();
		}

		@Override
		public String toString() {
			return buffer.toString();
		}

		public boolean bufferExists() {
			return buffer != null;
		}
	}

	static Arbitrary<Action<Model>> actions() {
		return Arbitraries.oneOf(create(), put(), get(), size());
	}

	private static Arbitrary<Action<Model>> create() {
		return Arbitraries.integers().between(0, 100).map(NewAction::new);
	}

	private static Arbitrary<Action<Model>> put() {
		return Arbitraries.integers().map(Object::toString).map(PutAction::new);
	}

	private static Arbitrary<Action<Model>> get() {
		return Arbitraries.constant(new GetAction());
	}

	private static Arbitrary<Action<Model>> size() {
		return Arbitraries.constant(new SizeAction());
	}

	private static class NewAction implements Action<Model> {

		private final int capacity;

		private NewAction(int capacity) {
			this.capacity = capacity;
		}

		@Override
		public boolean precondition(Model model) {
			return model.buffer == null;
		}

		@Override
		public Model run(Model model) {
			model.initialize(capacity);
			assertThat(model.bufferExists()).isTrue();
			return model;
		}

		@Override
		public String toString() {
			return String.format("new(%s)", capacity);
		}

	}

	private static class PutAction implements Action<Model> {

		private final Object element;

		private PutAction(Object element) {
			this.element = element;
		}

		@Override
		public boolean precondition(Model model) {
			return model.bufferExists() && model.contents.size() < model.capacity;
		}

		@Override
		public Model run(Model model) {
			model.contents.add(element);
			model.buffer.put(element);
			return model;
		}

		@Override
		public String toString() {
			return String.format("put(%s)", element);
		}

	}

	private static class GetAction implements Action<Model> {

		@Override
		public boolean precondition(Model model) {
			return model.bufferExists() && !model.contents.isEmpty();
		}

		@Override
		public Model run(Model model) {
			Object element = model.buffer.get();
			Object head = model.contents.remove(0);
			assertThat(element).isEqualTo(head);
			return model;
		}

		@Override
		public String toString() {
			return "get()";
		}
	}

	private static class SizeAction implements Action<Model> {

		@Override
		public boolean precondition(Model model) {
			return model.bufferExists();
		}

		@Override
		public Model run(Model model) {
			int size = model.buffer.size();
			int expectedSize = model.contents.size();
			assertThat(size).isLessThanOrEqualTo(model.capacity);
			assertThat(size)
					.as("size should be %s but was %s", expectedSize, size)
					.isEqualTo(expectedSize);

			return model;
		}

		@Override
		public String toString() {
			return "size()";
		}
	}
}
