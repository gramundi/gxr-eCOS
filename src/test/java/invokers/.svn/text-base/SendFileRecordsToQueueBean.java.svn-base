package invokers;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.camel.ProducerTemplate;

import com.wwctrials.answrs.jaxb.OdsNodesData;
import com.wwctrials.answrs.jaxb.SdsNodesData;

public class SendFileRecordsToQueueBean {
	//	@Produce(uri = "activemq:input.FILE")
	ProducerTemplate producer;

	//  @Consume(uri = "file:src/data")
	//	@Consume(uri = "file:src/data?noop=true")
	public void onFileSendToQueue(String body) throws JAXBException {
		int lineNum = 0;
		// split whole file into lines
		String lines[] = body.split("\n");

		// split first line to get file information 
		// e.g. "rb090008,site,status,,,,,,"
		String fileInfo = lines[lineNum];
		String fileDetails[] = fileInfo.split(",");
		String trialName = fileDetails[0];
		String databaseType = fileDetails[1];
		String formId = fileDetails[2];
		lineNum++;

		// second line contains column headers
		// e.g. "SITE_ID, PI_LAST_NAME, PI_FIRST_NAME,etc,etc,,,,,,,"
		String colHeaders = lines[lineNum];
		lineNum++;

		int rep = 1;

		try {
			for (int pageNo = lineNum; pageNo < lines.length; pageNo++) {
				StringWriter xmlStr = new StringWriter();
				// JAXB can't marshall interfaces !!
				// so do it the long way
				if ((databaseType.trim()).compareTo("site") == 0) {
					SdsNodesData nd = new SdsNodesData(trialName, databaseType);
					nd.populate(colHeaders, lines[pageNo], rep, formId);
					rep++;

					JAXBContext jaxbContext = JAXBContext.newInstance(SdsNodesData.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					//jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
					jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

					jaxbMarshaller.marshal(nd, xmlStr);
				} else {
					OdsNodesData nd = new OdsNodesData(trialName, databaseType);
					nd.populate(colHeaders, lines[pageNo], rep, formId);
					rep++;

					JAXBContext jaxbContext = JAXBContext.newInstance(OdsNodesData.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					//jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
					jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

					jaxbMarshaller.marshal(nd, xmlStr);

				}
				this.producer.sendBody(xmlStr.toString());

			}
		} catch (Exception e) {
			String strError = "ERROR processing import file (bad format?):\n" + body;
			System.out.println(strError);
			this.producer.sendBody(strError);
		}

	}
}
