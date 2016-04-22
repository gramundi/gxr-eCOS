package xslt;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.tigris.juxy.JuxyTestCase;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class MyEdcToAnswrsXsltTest extends JuxyTestCase {

	@Test
	public void testListTransformation() throws Exception {
		// set the transformation stylesheet
		this.newContext("/com/wwctrials/answrs/camel/myedcToAnswrs_1_0_0.xsl");

		// load input document
		this.context().setDocument(new ClassPathResource("/xml/myedc_example.xml").getFile());

		// transform
		Node result = this.applyTemplates();

		// assert
		this.print(result);
		this.xpathAssert("/answrs/datasource/schema-version", "myEDC").eval(result);
	}

	@Test
	public void testValidateXsd() throws Exception {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new ClassPathResource("/xsd/input.ANSWRS.xsd").getFile());
		Validator validator = schema.newValidator();

		Source xmlFile = new StreamSource(new ClassPathResource("/xml/answrs_example.xml").getFile());
		try {
			validator.validate(xmlFile);
			System.out.println(xmlFile.getSystemId() + " is valid");
			assertTrue(true);
		} catch (SAXException e) {
			System.out.println(xmlFile.getSystemId() + " is NOT valid");
			fail("Reason: " + e.getLocalizedMessage());
		}
	}
}
