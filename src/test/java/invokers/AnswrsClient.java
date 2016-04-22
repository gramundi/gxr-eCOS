package invokers;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Client that uses the {@link ProducerTemplate} to easily exchange messages with the Server. Requires that the JMS broker and camel routes are
 * running!
 */
public final class AnswrsClient {
	private AnswrsClient() {
		// Helper class
	}

	public static void main(final String[] args) {

		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("/invokers/answrs-client.xml");

			// get the camel template for Spring template style sending of messages (= producer)
			ProducerTemplate camelTemplate = context.getBean("camelTemplate", ProducerTemplate.class);

			ClassPathResource classPathResource = new ClassPathResource("/xml/814/test.xml");
			String payload = IOUtils.toString(classPathResource.getInputStream(), "UTF-8");

			System.out.println("Payload: " + payload);

			Object response = camelTemplate.sendBody("activemq:input.ANSWRS?requestTimeout=60000", ExchangePattern.InOut, payload);

			System.out.println("Result: " + response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
