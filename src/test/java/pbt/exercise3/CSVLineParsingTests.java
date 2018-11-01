package pbt.exercise3;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.*;

@Label("CSV line parsing")
class CSVLineParsingTests {

	@Example
	void line_without_separator_returns_single_field() {
		CSVLine line = CSVLineParser.parse("value");
		assertThat(line.size()).isEqualTo(1);
		assertThat(line).containsOnly("value");
	}

	@Example
	void line_with_one_separator_returns_two_fields() {
		CSVLine line = CSVLineParser.parse("value1,value2");
		assertThat(line.size()).isEqualTo(2);
		assertThat(line).containsExactly("value1", "value2");
	}

	@Example
	void value_within_quotes_is_returned_without_quotes() {
		CSVLine line = CSVLineParser.parse("\"value\"");
		assertThat(line).containsExactly("value");
	}

	@Example
	void separator_within_quotes_does_not_split_value() {
		CSVLine line = CSVLineParser.parse("\"value1,value2\"");
		assertThat(line).containsExactly("value1,value2");
	}

	@Example
	void doubleQuote_is_added_as_quote_char_to_field() {
		CSVLine line = CSVLineParser.parse("\"before\"\"after\",value2");
		assertThat(line).containsExactly("before\"after", "value2");
	}

	@Example
	void quotingOnlyWorksAtBordersOfField() {
		CSVLine line = CSVLineParser.parse("before\"after,value2");
		assertThat(line).containsExactly("before\"after", "value2");
	}
}
