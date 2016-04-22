package com.wwctrials.answrs.jaxb;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.stream.StreamResult;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wwctrials.answrs.jaxb.Ety;
import com.wwctrials.answrs.jaxb.SdsNodesData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/test-jaxb.xml" })
public class SdsNodesDataTest {

	@Autowired
	private Jaxb2Marshaller jaxb2Marshaller;

	@Test
	public void testMarshall() {

		SdsNodesData sdsNodesData = new SdsNodesData("ABC Trail", "ABC DB");
		sdsNodesData.etyList = new ArrayList<Ety>();
		sdsNodesData.etyList.add(new Ety("1", "2", "title", "Mr"));

		StringWriter sw = new StringWriter();
		this.jaxb2Marshaller.marshal(sdsNodesData, new StreamResult(sw));
		System.out.println("\n\n" + sw + "\n\n");

		assertThat(sw.toString(), notNullValue());
	}

	@Test
	@Ignore
	public void testUnmarshall() {

	}
}
