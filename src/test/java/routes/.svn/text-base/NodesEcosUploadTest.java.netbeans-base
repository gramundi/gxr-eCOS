/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routes;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.concurrent.TimeUnit;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author giovanni.ramundi
 */
public class NodesEcosUploadTest extends CamelSpringTestSupport{
    
  @Override
  protected AbstractXmlApplicationContext createApplicationContext(){
    
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context_test.xml");
  
  }    
  
  
  
  @Test
  public void testSendSubjects() throws Exception {
       
    NotifyBuilder notify = new NotifyBuilder(context)
            .wereSentTo("bean:myEdcDataImportBean?method=upload").whenDone(1).create();
   
    
    //This is powerful because give the opportunity to create the exchange compatible with the endpoint
    Endpoint endpoint = context.getEndpoint("file:datafiles");
 
   
    // create the exchange with 
    Exchange exchange = endpoint.createExchange();
    Message in = exchange.getIn();
    
    in.setBody("<ecos-dataprocessitems><item studyid=\"6092\" type=\"ADD_SUBJECT\">"
            + "<subjectidentifier>PE340010001</subjectidentifier>"
            + "<sitenumber>34001</sitenumber>"
            + "<statusid />"
            + "<statusname />"
            + "</item><item type=\"ADD_OR_UPDATE_PAGE_DATA\" studyid=\"6092\"><subjectid></subjectid>"
            + "<subjectidentifier>PE340010007</subjectidentifier>"
            + "<visitid>1</visitid><visitseq>0</visitseq><pageid>10</pageid><pageseq>0</pageseq>"
            + "<add_data_addpage>false</add_data_addpage>"
            + "<round_or_pad_decimalparts>false</round_or_pad_decimalparts>"
            + "<controlidrepeatingcheck>1</controlidrepeatingcheck>"
            + "<datavalues><control id=\"1\" tablecolumn=\"EN.ENROLDT\"><value>1990-01-10</value>"
            + "<reason>Training data import</reason></control><control id=\"12\" tablecolumn=\"EN.ENROLGR\">"
            + "<value>1</value><reason>Training data import</reason></control>"
            + "<control id=\"3\" tablecolumn=\"EN.ENSCREEN\"><value>P118</value>"
            + "<reason>Training data import</reason>"
            + "</control></datavalues></item></ecos-dataprocessitems>");
    in.setHeader(Exchange.FILE_NAME, "test4.xml");
    //create a producer that can produce the exchange (= send the mail)
    Producer producer = endpoint.createProducer();
    //start the producer
   
    producer.start();
    
    // and let it go (processes the exchange by sending the email)
    producer.process(exchange);
    //This operation wait ten second to check if there is match after this timeout
    //the operation return
    Thread.sleep(1000);
    
    boolean match = notify.matches(5,TimeUnit.SECONDS);
    assertTrue(match);
    
  }
  
  @Test
  public void testUploadSubjects() throws Exception {
       
    NotifyBuilder notify = new NotifyBuilder(context)
            .from("bean:myEdcDataImportBean?method=upload").whenCompleted(0).create();
   
    
    //This is powerful because give the opportunity to create the exchange compatible with the endpoint
    Endpoint endpoint = context.getEndpoint("file:datafiles");
 
   
    // create the exchange with 
    Exchange exchange = endpoint.createExchange();
    Message in = exchange.getIn();
    
    in.setBody("<ecos-dataprocessitems><item studyid=\"6092\" type=\"ADD_SUBJECT\">"
            + "<subjectidentifier>PE340010001</subjectidentifier>"
            + "<sitenumber>34001</sitenumber>"
            + "<statusid />"
            + "<statusname />"
            + "</item><item type=\"ADD_OR_UPDATE_PAGE_DATA\" studyid=\"6092\"><subjectid></subjectid>"
            + "<subjectidentifier>PE340010007</subjectidentifier>"
            + "<visitid>1</visitid><visitseq>0</visitseq><pageid>10</pageid><pageseq>0</pageseq>"
            + "<add_data_addpage>false</add_data_addpage>"
            + "<round_or_pad_decimalparts>false</round_or_pad_decimalparts>"
            + "<controlidrepeatingcheck>1</controlidrepeatingcheck>"
            + "<datavalues><control id=\"1\" tablecolumn=\"EN.ENROLDT\"><value>1990-01-10</value>"
            + "<reason>Training data import</reason></control><control id=\"12\" tablecolumn=\"EN.ENROLGR\">"
            + "<value>1</value><reason>Training data import</reason></control>"
            + "<control id=\"3\" tablecolumn=\"EN.ENSCREEN\"><value>P118</value>"
            + "<reason>Training data import</reason>"
            + "</control></datavalues></item></ecos-dataprocessitems>");
    in.setHeader(Exchange.FILE_NAME, "test4.xml");
    //create a producer that can produce the exchange (= send the mail)
    Producer producer = endpoint.createProducer();
    //start the producer
   
    producer.start();
    
    // and let it go (processes the exchange by sending the email)
    producer.process(exchange);
    //This operation wait ten second to check if there is match after this timeout
    //the operation return
    Thread.sleep(1000);
    
    boolean match = notify.matches(5,TimeUnit.SECONDS);
    assertTrue(match);
    
  }
  
  @Test
  public void testBadFileExtension() throws Exception {
     
   //This is powerful because give the opportunity to create the exchange compatible with the endpoint
    Endpoint endpoint = context.getEndpoint("file:datafiles");
 
    // create the exchange with 
    Exchange exchange = endpoint.createExchange();
    Message in = exchange.getIn();
    
    in.setBody("Bad File Extension");
    in.setHeader(Exchange.FILE_NAME, "testBadExtension.txt");
    //create a producer 
    Producer producer = endpoint.createProducer();
    //start the producer
   
    producer.start();
    
    producer.process(exchange);
    //This operation wait ten second to check if there is match after this timeout
    //the operation return
    Thread.sleep(1000);
    
     
  }
  
  
}