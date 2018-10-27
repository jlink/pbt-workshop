package pbt.exercise2;

import net.jqwik.api.*;
import net.jqwik.api.Tuple.*;

import static org.assertj.core.api.Assertions.*;

class SubstringProperties {

	//@Property
	@Label("String.substring() never throws exception")
	void stringSubstringWorks(@ForAll Tuple3<String, Integer, Integer> substringParams) {
		String initialString = substringParams.get1();
		int beginIndex = substringParams.get2();
		int endIndex = substringParams.get3();

		assertThatCode(() -> initialString.substring(beginIndex, endIndex)).doesNotThrowAnyException();
	}

}
