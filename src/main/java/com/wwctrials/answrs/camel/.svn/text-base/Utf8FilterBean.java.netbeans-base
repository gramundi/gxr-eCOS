package com.wwctrials.answrs.camel;

import org.apache.camel.Handler;
import org.apache.commons.codec.binary.StringUtils;

public class Utf8FilterBean {

	@Handler
	public String ensureBodyIsUtf8Safe(String body) {
		return StringUtils.newStringUtf8(body.getBytes());
	}
}
