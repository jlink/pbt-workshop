package pbt.exercise3;

import java.util.*;

public class CSVLine implements Iterable<String> {
	private final List<String> fields;

	public CSVLine(List<String> fields) {
		this.fields = fields;
	}

	public int size() {
		return fields.size();
	}

	@Override
	public Iterator<String> iterator() {
		return fields.iterator();
	}

	public List<String> fields() {
		return Collections.unmodifiableList(fields);
	}
}
