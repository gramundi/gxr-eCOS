package routes;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration
public class AnswrsRouteTest extends AbstractJUnit4SpringContextTests {

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Test
	@DirtiesContext
	public void testSendMatchingMessage() throws Exception {
		String expectedBody = "<matched/>";

		this.resultEndpoint.expectedBodiesReceived(expectedBody);

		this.template.sendBodyAndHeader(expectedBody, "foo", "bar");

		this.resultEndpoint.assertIsSatisfied();
	}

	@Test
	@DirtiesContext
	public void testSendNotMatchingMessage() throws Exception {
		this.resultEndpoint.expectedMessageCount(0);

		this.template.sendBodyAndHeader("<notMatched/>", "foo", "notMatchedHeaderValue");

		this.resultEndpoint.assertIsSatisfied();
	}
}
