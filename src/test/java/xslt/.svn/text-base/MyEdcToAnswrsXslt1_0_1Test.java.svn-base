package xslt;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.tigris.juxy.JuxyTestCase;
import org.w3c.dom.Node;

public class MyEdcToAnswrsXslt1_0_1Test extends JuxyTestCase {

	@Test
	public void testSingleRecordTransformation() throws Exception {
		// set the transformation stylesheet
		this.newContext("/com/wwctrials/answrs/camel/myedcToAnswrs_1_0_1.xsl");

		// load input document
		//		<sitenum>001</sitenum>
		//		<siteid>15489</siteid>
		//		<sitename>Test 1</sitename>
		this.context().setDocument(new ClassPathResource("/xml/805/myedc_singlesite.xml").getFile());

		// transform
		Node result = this.applyTemplates();

		// assert
		this.print(result);
		this.xpathAssert("/answrs/datasource/schema-version", "myEDC").eval(result);
		this.xpathAssert("/answrs/country/parent/id", "15489").eval(result);
		this.xpathAssert("/answrs/country/parent/parentDb/form/id", "data_sitelist").eval(result);
		this.xpathAssert("/answrs/country/parent/parentDb/form/iterator/id", "15489").eval(result);
	}

	@Test
	public void testMultipleRecordsTransformation() throws Exception {
		// set the transformation stylesheet
		this.newContext("/com/wwctrials/answrs/camel/myedcToAnswrs_1_0_1.xsl");

		// load input document
		this.context().setDocument(new ClassPathResource("/xml/805/myedc_multiplesites.xml").getFile());

		// transform
		Node result = this.applyTemplates();

		// assert
		this.print(result);

		// assert that all three records have correct IDs
		this.xpathAssert("string-join(/answrs/country/parent/id, ',')", "7269,7289,7290").eval(result);
		this.xpathAssert("string-join(/answrs/country/parent/parentDb/form/iterator/id, ',')", "7269,7289,7290").eval(result);
	}
}
