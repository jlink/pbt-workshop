package pbt.exercise3;

import java.util.*;

public class CSVLine implements Iterable<String> {
	private final List<String> values;

	public CSVLine(List<String> values) {
		this.values = values;
	}

	public int size() {
		return values.size();
	}

	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
}
