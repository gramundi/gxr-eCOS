package com.wwctrials.answrs.camel;

import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyEdcUploadBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Handler
	public String uploadPayloadToMyEdc(String body) {
		this.logger.debug("Sending payload to MyEDC for upload");

		return body;
	}
}
