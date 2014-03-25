package application;

import javax.xml.bind.DatatypeConverter;

public class Converter {
	
	public static byte[] HexToByte (String Hex){
		return DatatypeConverter.parseHexBinary(Hex);
	}
	
	public static String ByteToHex (byte[] Bin){
		return DatatypeConverter.printHexBinary(Bin);
		
	}

}
