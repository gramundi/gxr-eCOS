import org.junit.Test;

public class Utf8Test {

	@Test
	public void convertToUtf8() throws Exception {
		byte ptext[] = "Mara��n".getBytes();
		String value = new String(ptext, "UTF-8");
		System.out.println(value);
	}
}
