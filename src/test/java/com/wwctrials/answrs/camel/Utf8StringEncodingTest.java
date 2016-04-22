package com.wwctrials.answrs.camel;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

public class Utf8StringEncodingTest {
	private static String UTF_8_REPLACEMENT_CHAR = "\uFFFD";

	@Test
	public void convertUtf8() {

		// í is replaced with a UTF-8 safe char
		assertThat(StringUtils.newStringUtf8("The Ucayali-Apurímac river system".getBytes()), is("The Ucayali-Apur" + UTF_8_REPLACEMENT_CHAR
				+ "mac river system"));

		String amazon = "The Amazon River (US play /?æm?z?n/ or UK /?æm?z?n/; "
				+ "Spanish &amp; Portuguese: Amazonas) in South America is the second longest [2][not in citation given] "
				+ "river in the world and by far the largest by waterflow with an average discharge greater "
				+ "than the next seven largest rivers combined (not including Madeira and Rio Negro, which are tributaries of the Amazon). "
				+ "The Amazon, which has the largest drainage basin in the world, about 7,050,000 square kilometres (2,720,000 sq mi), "
				+ "accounts for approximately one-fifth of the world's total river flow.[3][4] -cr- -cr- In its upper stretches, above the "
				+ "confluence of the Rio Negro, the Amazon is called Solimões in Brazil; however, in Peru, Colombia and Ecuador, as well as the "
				+ "rest of the Spanish-speaking world, the river is generally called the Amazon downstream from the confluence of the Marañón "
				+ "and Ucayali rivers in Peru. The Ucayali-Apurímac river system is "
				+ "considered the main source of the Amazon. -cr- -cr- The width of "
				+ "the Amazon varies between 1.6 and 10 kilometres (1.0 and 6.2 mi) "
				+ "at low stage, but expands during the wet season to 48 kilometres "
				+ "(30 mi) or more. The river enters the Atlantic Ocean in a broad estuary about 240 kilometres (150 mi) wide.";

		String amazonUtf8Safe = StringUtils.newStringUtf8(amazon.getBytes());
		System.out.println(amazonUtf8Safe);
		assertThat(amazonUtf8Safe, containsString(UTF_8_REPLACEMENT_CHAR));
		assertThat(amazonUtf8Safe, not(containsString("í")));
	}
}
