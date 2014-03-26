package Testing;

import static org.junit.Assert.*;

import org.junit.Test;

import application.Converter;

public class ConverterTest {

	@Test
	public void test() {
		
		Converter tester = new Converter();
		
		String Hex = "018215E0BFD3AE131433206F74705267417AB56438";
		byte [] Bin = tester.HexToByte(Hex);
		
		assertEquals(Hex, tester.ByteToHex(Bin));
		
	}

}
