package com.wwctrials.answrs.camel;

import org.apache.camel.Consume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class MyEdcDataImportBeanTrigger {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MyEdcDataImportBean myEdcDataImportBean;

	public MyEdcDataImportBeanTrigger(MyEdcDataImportBean myEdcDataImportBean) {
		this.myEdcDataImportBean = myEdcDataImportBean;
	}

	@Consume(uri = "activemq:trigger.MY_EDC_MIGRATION")
	public void triggerImport(String body) {
		this.logger.info(body);

		if (StringUtils.hasText(body) && body.contains("D")) {
			this.myEdcDataImportBean.onDaily();
			return;
		}

		this.myEdcDataImportBean.onHourly();
	}
}
