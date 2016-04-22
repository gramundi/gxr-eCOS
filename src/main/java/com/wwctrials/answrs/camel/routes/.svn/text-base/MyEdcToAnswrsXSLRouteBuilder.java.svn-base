package com.wwctrials.answrs.camel.routes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.Main;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;


public class MyEdcToAnswrsXSLRouteBuilder extends SpringRouteBuilder {

	private final String xslFile;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public MyEdcToAnswrsXSLRouteBuilder(@Value("${environment.myedctoanswrs.xsl.version}") String xslVersion) {
		this.xslFile = "com/wwctrials/answrs/camel/myedcToAnswrs" + xslVersion + ".xsl";
		
		this.logger.info("XSL transformer = " + this.xslFile);
	}

	@Override
	public void configure() throws Exception {

		// @formatter:off

		this.errorHandler(this.deadLetterChannel("activemq:failure.MYEDC_EXPORT"));
		
		this.onException(Exception.class)
			.log(LoggingLevel.ERROR, "Exception on route: " + this.getClass().getSimpleName());
		
		this.from("activemqc:input.MYEDC_ANSWRS_XSL?concurrentConsumers=5")
			.log(LoggingLevel.DEBUG, "INCOMING RAVE to ANSWRS MSG:\n ${body}")
			.to("xslt:" + this.xslFile)
			.wireTap("activemq:wiretap.MYEDC_ANSWRS_XSL_RESULT")
			.to("activemq:input.ANSWRS_PREPROCESS");
			
		// @formatter:on
	}

	/**
	 * Allow this route to be run as an application
	 */
	public static void main(String[] args) throws Exception {
		new Main().run(args);
	}
}
