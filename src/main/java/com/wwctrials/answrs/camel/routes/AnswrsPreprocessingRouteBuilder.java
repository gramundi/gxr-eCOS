package com.wwctrials.answrs.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;

public class AnswrsPreprocessingRouteBuilder extends SpringRouteBuilder {

	@Override
	public void configure() throws Exception {
		this.errorHandler(this.deadLetterChannel("activemq:failure.ANSWRS"));

		// @formatter:off
		
		this.from("activemqc:input.ANSWRS_PREPROCESS?concurrentConsumers=5")
    		.split()
    		.method("answrsXmlFormSplitter")
    		.parallelProcessing()
    		.streaming()
    		//.aggregationStrategyRef("messageCountAggregationStrategy")
    		.to("activemq:input.ANSWRS")
    		.end()
    			.log(LoggingLevel.INFO, "Split complete: message in ${property.CamelSplitSize} parts");
		
		// @formatter:on
	}
}
