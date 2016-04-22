package com.wwctrials.answrs.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.Main;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import com.wwctrials.answrs.camel.AnswrsTaxiUrl;

public class AnswrsTaxiRouteBuilder extends SpringRouteBuilder {

	private final AnswrsTaxiUrl answrsTaxiUrl;

	public AnswrsTaxiRouteBuilder(@Value("${environment.server}") String envServer, @Value("${environment.path}") String envPath,
			@Value("${environment.taxi}") String envTaxi, @Value("${environment.taxi.username}") String envTaxiUsername,
			@Value("${environment.taxi.password}") String envTaxiPassword) {

		this.answrsTaxiUrl = new AnswrsTaxiUrl.Builder().baseUrl(envServer).path(envPath).taxi(envTaxi).build();
	}

	@Override
	public void configure() throws Exception {

		// @formatter:off

		this.errorHandler(this.deadLetterChannel("activemqp:failure.ANSWRS"));
		
		this.onException(Exception.class)
			.log(LoggingLevel.ERROR, "Exception on route: " + this.getClass().getSimpleName());
		        
		this.from("activemqc:input.ANSWRS?concurrentConsumers=10")
			.log(LoggingLevel.DEBUG, "INCOMING ANSWRS TAXI MSG:\n ${body}")
			.log(LoggingLevel.INFO, "REQ: ${exchangeId}")
			.to("bean:utf8FilterBean")
			.wireTap("activemqp:wiretap.ANSWRS")
			.setHeader(Exchange.HTTP_METHOD, this.constant("POST"))
			.setHeader(Exchange.HTTP_CHARACTER_ENCODING, this.constant("UTF-8"))
//			.to(this.answrsTaxiUrl.toString() + "?authMethod=Basic&authUsername=Answrs&authPassword=answrs1")
			.to("bean:taxiBean") // this bean is common and in the parent project!
			.log(LoggingLevel.INFO, "RES: ${exchangeId} Status: ${body}");
			
		// @formatter:on
	}

	/**
	 * Allow this route to be run as an application
	 */
	public static void main(String[] args) throws Exception {
		new Main().run(args);
	}
}
