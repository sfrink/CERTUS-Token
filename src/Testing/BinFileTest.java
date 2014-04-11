package Testing;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import application.BinFile;

public class BinFileTest {

	@Test
	public void test() {
		
		String writeContent = "this is my file content.";
		byte [] writeBinContent = writeContent.getBytes();
		
		String filePath = "f:\\temp\\testerfile";
		
		try {
			BinFile.writeFile(filePath, writeBinContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BinFile tester = new BinFile(filePath);
		
		byte[] readBinContent = null;
		try {
			readBinContent = tester.readFile();
			String readContent = new String(readBinContent);
			assertNotNull(readContent);
			assertEquals(readContent, writeContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
