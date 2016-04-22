package com.wwctrials.answrs.camel;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Bean to handle pre-processing of myEdc web service XML
 * 
 * @author andyg@cakesolutions.net
 */
public class MyEdcPreProcessorHelper {

	protected static final String MYEDC_PREFIX = "myedc";

	public static String preProcessMessage(String xmlMsg, String studyId, String transformerVersion) throws Exception {

		xmlMsg = xmlMsg.replaceAll("\r\n", "\n");
		xmlMsg = xmlMsg.replaceAll("\r", "\n");
		xmlMsg = xmlMsg.replaceAll("\t", "");

		// transform data a bit for ANSWRS
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xmlMsg)));

		// rename node
		Node reply = doc.getFirstChild();
		doc.renameNode(reply, reply.getNamespaceURI(), "data");

		// add new attributes
		NamedNodeMap studyAttributes = reply.getAttributes();

		Attr study = doc.createAttribute("studyid");
		study.setValue(MYEDC_PREFIX + studyId);
		studyAttributes.setNamedItem(study);

		Attr trans = doc.createAttribute("transformer");
		trans.setValue(transformerVersion);
		studyAttributes.setNamedItem(trans);

		return flattenDocument(doc);
	}

	private static String flattenDocument(Document doc) throws TransformerConfigurationException, TransformerFactoryConfigurationError,
			TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);

		String xmlString = result.getWriter().toString();

		// json doesn't  line newlines
		xmlString = xmlString.replaceAll("\r\n", "\n");
		xmlString = xmlString.replaceAll("\r", "\n");
		xmlString = xmlString.replaceAll("\n", " ");
		xmlString = xmlString.replaceAll("\t", "");
		return xmlString;
	}

}
