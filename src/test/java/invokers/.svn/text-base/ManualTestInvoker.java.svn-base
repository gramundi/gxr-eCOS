package invokers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.wwctrials.answrs.jaxb.OdsNodesData;
import com.wwctrials.answrs.jaxb.SdsNodesData;

/**
 * Dependency on local file?!
 * 
 * @author jpr
 * 
 */
class ManualTestInvoker {
	public static void main(String[] args) {
		try {
			String body = readFileAsString("c:\\jpr\\svn\\CSP\\Projects\\ANSWRS\\source\\module\\camel\\trunk\\answrsArtifactId\\src\\data\\test2.csv");
			//System.out.println(body);
			sendToQueue(body);
		} catch (Exception e) {
			System.out.println("kak!");
		}
	}

	private static String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static void sendToQueue(String body) throws JAXBException {
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
				if ((databaseType.trim()).compareTo("site") == 0) {
					System.out.println("site !");
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
					System.out.println("case/drug !");
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
				System.out.println(xmlStr.toString());

			}
		} catch (Exception e) {
			String strError = "ERROR processing import file (bad format?):\n" + body;
			System.out.println(strError);

		}

	}
}