package com.wwctrials.answrs.camel;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.tigris.juxy.JuxyTestCase;
import org.w3c.dom.Element;

public class MyEdcPreProcessorHelperTest extends JuxyTestCase {

	private static final String TRANSFORMER_VERSION = "1.0.0";
	private static final String STUDY_ID = "ABC123";

	@Test
	public void testPreProcessMyEdcMessage() throws Exception {

		String preProcessedString = MyEdcPreProcessorHelper.preProcessMessage("<a><b/></a>", STUDY_ID, TRANSFORMER_VERSION);
		Element node = this.buildNodeFromString(preProcessedString);

		System.out.println(preProcessedString);

		// assert no tabs and carriage returns
		assertThat(preProcessedString, not(containsString("\t")));
		assertThat(preProcessedString, not(containsString("\r")));
		assertThat(preProcessedString, not(containsString("\n")));

		// ensure data exists
		this.xpathAssert("count(/data)", 1).eval(node);

		this.xpathAssert("/data/@transformer", TRANSFORMER_VERSION).eval(node);

		// studyid has prefix prepended to value
		this.xpathAssert("/data/@studyid", MyEdcPreProcessorHelper.MYEDC_PREFIX + STUDY_ID).eval(node);
	}

	private Element buildNodeFromString(String xml) throws Exception {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes())).getDocumentElement();
	}
}
