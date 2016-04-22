/**
 * File: MyEdcDataImportBean.java Author: jpr
 * 
 * @version $Id: MyEdcDataImportBean.java 7436 2012-12-05 15:13:50Z ayg $
 */
package com.wwctrials.answrs.camel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.camel.Exchange;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * MyEdcDataImportBean The data import class for myEDC
 * 
 * @author jpr
 * @author andyg@cakesolutions.net
 */
public class MyEdcDataImportBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** brief The configuration file */
	private static String CONFIG_FILE_XML = "src/data/myEdc/config.xml";
	/** The validation schema */
	private static String CONFIG_FILE_XSD = "src/data/myEdc/config.xsd";
	/** The buffer size for reading data from the web service */
	private static final int BUFFER_SIZE = 512;

	/** The ActiveMQ producer */
	@Produce(uri = "activemq:input.MY_EDC")
	private ProducerTemplate producer;

	/** Storage for login information */
	private Login loginInfo;
	/** Storage for study data information */
	private List<StudyData> studyDataList;
    
   private enum Frequency {
		HOURLY, DAILY, DISABLED;
	}

    private static final String UPLOAD_ENABLED="true";
    
    private String XmlContents;
    private String XmlFileName;
    
	/**
	 * Timer function for daily updates.
	 */
	public void onDaily() {
		this.logger.info("myEDC Data Import getting all DAILY study data...");
		this.run(Frequency.DAILY);
	}

	/**
	 * Timer function for hourly updates.
	 */
	public void onHourly() {
		this.logger.info("myEDC Data Import getting all HOURLY study data...");
		this.run(Frequency.HOURLY);
	}

    
    /**
     * This Method receive the Message from the file endpoint and if the study is enable to upload
     * call the eCOS web service to upload data inside the study
     * @param exchange
     * @throws UnsupportedEncodingException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public void upload(Exchange exchange) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException{
    
    boolean bret = true;
	 
    //Read the values from the config.xml
    this.loadConfigData();   
    
    File fXmlFile = new File(exchange.getIn().getBody(File.class).toString());
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
	doc.getDocumentElement().normalize();
    
    //get the sudy id from the studyId attribute of the first Item
    NodeList listOfItem = doc.getElementsByTagName("item");
    Element firstItem = (Element)listOfItem.item(0);
    String studyId=firstItem.getAttribute("studyid");
    
    if (this.studyUploadEnabled(studyId)){
        
        this.setupCookies();
        if (this.login(this.loginInfo.url, studyId, this.loginInfo.username, this.loginInfo.password)) {
            this.XmlFileName = exchange.getIn().getHeader("CamelFileName", String.class);
            this.XmlContents = exchange.getIn().getBody(String.class);
            //We need to encode xml contents action Push is the web service call
            //https://eclinicalos.com/edc_studyservices.jsp?action=importfile
            //together with a filecontents=.. <the XML file contents>, and filename = <uniquefile name.xml>
            String actionPush = this.loginInfo.url + "?action=importfile&filecontents=" + URLEncoder.encode(this.XmlContents, "UTF-8");
            actionPush += "&filename=" + this.XmlFileName;
            try {
                String ret = this.readText(actionPush);
                if (ret.contains("ERROR")) {
                this.logger.error("myEDC Upload data Error to study: {}", studyId);
                bret = false;
                }
                else { 
                    this.logger.info("Successful Upload - File Uploaded:"+this.XmlFileName);
                }
            } catch (Exception e) {
                this.logger.error("myEDC Upload data exception: {} ", e.toString());
                bret = false;
            }
            this.logout(this.loginInfo.url);

        } else {
            this.logger.error("Login Issues please contact the administrator");
        }
    }
    //The study is not enabled for upload. The message will be ditched off
    else {
        this.logger.info("Incoming Message for the StudyId==>"+studyId+" which is not enabled to upload data");
        
    } 
        
   }
      
    
    
	/**
	 * Get the data
	 * 
	 * @param theFreq The frequency at which to get data
	 */
	public void run(Frequency frequency) {
		if (this.isConfigValid()) {
			if (this.loadConfigData()) {
				if (!this.getAllData(frequency)) {
					// todo
					// send email
				}
			} else {
				// todo
				// send email
			}
		} else {
			// todo
			// send email
		}
	}

	/**
	 * Determines whether the configuration file is valid
	 * 
	 * @return True if valid, otherwise false.
	 */
	public boolean isConfigValid() {
		boolean ret = false;
		try {
			// validate against schema
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new File(CONFIG_FILE_XSD));

			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			saxFactory.setSchema(schema);
			SAXParser parser = saxFactory.newSAXParser();
			parser.parse(CONFIG_FILE_XML, new DefaultHandler() {
				@Override
				public void error(SAXParseException e) throws SAXException {
					throw e;
				}
			});

			ret = true;
		} catch (FileNotFoundException e) {
			this.logger.error("myEDC Data Import {} not found", CONFIG_FILE_XML);
		} catch (SAXParseException e) {
			this.logger.error("myEDC Data Import {} is NOT valid: {} ", CONFIG_FILE_XML, e.getLocalizedMessage());
		} catch (Exception e) {
			this.logger.error("myEDC Data Import unable to validate {} : {}", CONFIG_FILE_XML, e.getLocalizedMessage());
		}

		return ret;

	}

	/**
	 * Load the configuration data into suitable containers
	 * 
	 * @return True if loaded OK, otherwise false.
	 */
	public boolean loadConfigData() {
		boolean ret = true;
		// create containers for config
		this.loginInfo = new Login();
		this.studyDataList = new ArrayList<StudyData>();

		try {
			File fXmlFile = new File(CONFIG_FILE_XML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			// get the login credentials
			NodeList nLoginList = doc.getElementsByTagName("login");
			for (int temp = 0; temp < nLoginList.getLength(); temp++) {
				Node nNode = nLoginList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					this.loginInfo.url = this.getTagValue("url", eElement);
					this.loginInfo.username = this.getTagValue("username", eElement);
					this.loginInfo.password = this.getTagValue("password", eElement);
				}
			}

			// get a list of studies
			NodeList nStudyList = doc.getElementsByTagName("study");
			for (int temp = 0; temp < nStudyList.getLength(); temp++) {
				Node nNode = nStudyList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					StudyData studyData = new StudyData();
					studyData.studyId = this.getTagValue("studyid", eElement);
                    studyData.studyupload =this.getTagValue("studyupload", eElement);

					// get a list of data tables
					NodeList nDataList = eElement.getElementsByTagName("data");
					for (int temp2 = 0; temp2 < nDataList.getLength(); temp2++) {
						Node nDataNode = nDataList.item(temp2);
						if (nDataNode.getNodeType() == Node.ELEMENT_NODE) {
							DataSourceContainer dataSource = new DataSourceContainer();
							Element eDataElement = (Element) nDataNode;
							dataSource.source = this.getTagValue("source", eDataElement);
							dataSource.frequency = this.getTagValue("frequency", eDataElement);
							
							// optional parameters
							dataSource.param1 = this.getTagValue("param1", eDataElement);
							dataSource.param2 = this.getTagValue("param2", eDataElement);
							dataSource.param3 = this.getTagValue("param3", eDataElement);
							
							dataSource.transformer = this.getTagValue("transformer", eDataElement);
							studyData.dataSource.add(dataSource);
						}
					}
					this.studyDataList.add(studyData);
				}
			}
		} catch (Exception e) {
			this.logger.error("myEDC Data Import unable to load configuration: {}", e.getLocalizedMessage());
			ret = false;
		}
		return ret;
	}
    
    /**
     * Check if the study is enabled for the upload==> studyupload=true
     * @par1 studyid which we check the property
     * @ret true if the study id is enabled false otherwise
     * 
     */
    private boolean studyUploadEnabled(String studyid){
    
        boolean ret=false;
        for (StudyData sd : this.studyDataList) {
            if (sd.studyId.equals(studyid)){
                if (this.UPLOAD_ENABLED.equals(sd.studyupload)) 
                    return true;
            }
        }   
        return ret;
    }
    
	/**
	 * Get all the data from the myEDC web service
	 * 
	 * @return True if OK, otherwise false.
	 */
	public boolean getAllData(Frequency frequency) {
		boolean ret = true;

		this.setupCookies();

		// loop for all studies
		for (StudyData sd : this.studyDataList) {
			// login using stored credentials
			if (this.login(this.loginInfo.url, sd.studyId, this.loginInfo.username, this.loginInfo.password)) {
				// loop for all data tables
				for (DataSourceContainer ds : sd.dataSource) {
					// filter for correct frequency
					if (Frequency.valueOf(ds.frequency.toUpperCase()) == frequency) {
						try {
							this.logger.info("myEDC Data Import getting: study [{}] data source [{}] freq [{}]", new Object[] { sd.studyId,
									ds.source, ds.frequency });

							// get the data
							String xmlMsg = this.getData(this.loginInfo.url, ds.source, ds.param1, ds.param2, ds.param3);

							// send to ActiveMQ
                            
							String preProcessedXmlMsg = MyEdcPreProcessorHelper.preProcessMessage(xmlMsg, sd.studyId, ds.transformer);
							this.producer.sendBody(preProcessedXmlMsg);

							//Taxi.getInstance().run(xmlString);
						} catch (Exception e) {
							this.logger.error("myEDC Data Import unable to get data: {} ", e.getLocalizedMessage());
							ret = false;
						}

					}
				}
			} else {
				ret = false;
			}
			this.logout(this.loginInfo.url);
		}

		return ret;
	}

	/**
	 * Get the XML value
	 * 
	 * @param sTag The tag name
	 * @param eElement The element
	 * @return The string value
	 */
	private String getTagValue(String sTag, Element eElement) {
		
		String value = "";
		if (eElement.getElementsByTagName(sTag).getLength() > 0){
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
			
			Node nValue = nlList.item(0);
			value = nValue.getNodeValue();
		}
				
		return value;
	}

	/**
	 * Set up session-wide cookie manager
	 */
	public void setupCookies() {
		CookieHandler.setDefault(new CookieManager());
	}

	/**
	 * Log into myEDC web service using stored credentials
	 * 
	 * @param url The URL for the myEDC web service
	 * @param studyId The myEDC study ID
	 * @param username Username
	 * @param password Password
	 * @return True if login OK, otherwise false.
	 */
	public boolean login(String url, String studyId, String username, String password) {
		boolean bret = true;
		try {
			String ret = this.readText(url + "?action=login&studyid=" + studyId + "&login=" + username + "&password=" + password);
			if (ret.contains("ERROR")) {
				this.logger.error("myEDC Data Import cannot login to study: {}", studyId);
				bret = false;
			}
		} catch (Exception e) {
			this.logger.error("myEDC Data Import login error: {} ", e.toString());
			bret = false;
		}
		return bret;
	}

	/**
	 * Get data from myEDC web service
	 * 
	 * @param url The URL for the myEDC web service
	 * @param dataSource The myEDC data table name
	 * @return The myEDC data
	 */
	public String getData(String url, String dataSource, String param1, String param2, String param3) {
		String ret = "";
		try {
			ret = this.readText(url + "?action=infoattempt&type=" + dataSource + "&param1=" + param1 + "&param2=" + param2 + "&param3=" + param3);
			this.logger.info(url + "?action=infoattempt&type=" + dataSource + "&param1=" + param1 + "&param2=" + param2 + "&param3=" + param3);
		} catch (Exception e) {
			this.logger.error("myEDC Data Import cannot get data: {}", e.toString());
		}
		return ret;
	}

	/**
	 * Log out of the myEDC web service
	 * 
	 * @param url The URL for the myEDC web service
	 * @return The logout string
	 */
	public String logout(String url) {
		String ret = "";
		try {
			ret = this.readText(url + "?action=logout");
		} catch (Exception e) {
			this.logger.error("myEDC Data Import logout error: {} ", e.toString());
		}
		return ret;
	}

	/**
	 * Get the text data from myEDC web service input stream
	 * 
	 * @param urlStr The URL for the myEDC web service
	 * @return The myEDC XML text
	 */
	public String readText(String urlStr) {
		String ret = "";
		byte[] data = new byte[0];
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead;

		try {
			InputStream is = (new URL(urlStr)).openStream();

			// keep going till you get all the data
			while ((bytesRead = is.read(buffer)) > 0) {
				// construct large enough array for all the data we now have
				byte[] newData = new byte[data.length + bytesRead];
				// copy data previously read
				System.arraycopy(data, 0, newData, 0, data.length);
				// append data newly read
				System.arraycopy(buffer, 0, newData, data.length, bytesRead);
				// discard the old array in favour of the new one
				data = newData;
			}

			is.close();
			ret = new String(data, "UTF-8");
			ret = ret.trim();
		} catch (Exception e) {
			this.logger.error("myEDC Data Import cannot read text {} ", e.toString());
		}
		return ret;
	}
}

/**
 * DataSource A container class for data table parameters
 * 
 * @author jpr
 */
class DataSourceContainer {
	public String source;
	public String param1;
	public String param2;
	public String param3;
	public String frequency;
	public String transformer;
}

/**
 * Login A container class login credentials
 * 
 * @author jpr
 */
class Login {
	public String url;
	public String username;
	public String password;
}

/**
 * StudyData A container class for study data
 * 
 * @author jpr
 */
class StudyData {
	/** The study ID */
	public String  studyId;
    public String  studyupload; 
	/** A list of data source details */
	public List<DataSourceContainer> dataSource;

	/**
	 * Constructor
	 */
	StudyData() {
		// initialise list
		this.dataSource = new ArrayList<DataSourceContainer>();
	}
}
