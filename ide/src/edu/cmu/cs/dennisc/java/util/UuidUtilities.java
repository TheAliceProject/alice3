package edu.cmu.cs.dennisc.java.util;

public class UuidUtilities {
	private UuidUtilities() {
		throw new AssertionError();
	}
	public static java.util.UUID decodeUuid( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		long mostSignificantBits = binaryDecoder.decodeLong();
		long leastSignificantBits = binaryDecoder.decodeLong();
		return new java.util.UUID(mostSignificantBits, leastSignificantBits);
	}
	public static void encodeUuid( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.util.UUID uuid ) {
		long mostSignificantBits = uuid.getMostSignificantBits();
		long leastSignificantBits = uuid.getLeastSignificantBits();
		binaryEncoder.encode( mostSignificantBits );
		binaryEncoder.encode( leastSignificantBits );
	}
	
}
