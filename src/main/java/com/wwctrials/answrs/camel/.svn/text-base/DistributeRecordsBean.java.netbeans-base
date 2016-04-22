package com.wwctrials.answrs.camel;


public class DistributeRecordsBean {

	//    @Consume(uri = "activemq:input.FILE")
	//    @RecipientList
	public String[] route(String body) {
		return new String[] { "activemq:input.IMPORTDATA" };
	}
}
