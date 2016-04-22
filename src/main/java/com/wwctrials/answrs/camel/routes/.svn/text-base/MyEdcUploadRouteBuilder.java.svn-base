package com.wwctrials.answrs.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;

public class MyEdcUploadRouteBuilder extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		this.errorHandler(this.deadLetterChannel("activemq:failure.MY_EDC_UPLOAD"));

		// @formatter:off
		
		this.from("activemq:input.UPLOAD_MY_EDC")
			.log(LoggingLevel.DEBUG, "INCOMING UPLOAD_MY_EDC:\n ${body}")
			.wireTap("activemq:wiretap.UPLOAD_MY_EDC")		
			.to("bean:myEdcUploadBean");
		
		// @formatter:on
	}
}
