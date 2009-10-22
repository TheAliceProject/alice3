package org.alice.ide.preferences.programming;

public class BinaryEncodableAndDecodableConfiguration implements Configuration, edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private static final int VERSION = 1;
	private java.util.UUID uuid;
	private boolean isDefaultFieldNameGenerationDesired;
	private boolean isSyntaxNoiseDesired;
	public BinaryEncodableAndDecodableConfiguration() {
		this.uuid = java.util.UUID.randomUUID();
	}
	public java.util.UUID getUUID() {
		return this.uuid;
	}
	public boolean isDefaultFieldNameGenerationDesired() {
		return this.isDefaultFieldNameGenerationDesired;
	}
	public boolean isSyntaxNoiseDesired() {
		return this.isSyntaxNoiseDesired;
	}
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		int version = binaryDecoder.decodeInt();
		if( version == 1 ) {
			long mostSignificantBits =  binaryDecoder.decodeLong();
			long leastSignificantBits =  binaryDecoder.decodeLong();
			this.uuid = new java.util.UUID(mostSignificantBits, leastSignificantBits);
			this.isDefaultFieldNameGenerationDesired = binaryDecoder.decodeBoolean();
			this.isSyntaxNoiseDesired = binaryDecoder.decodeBoolean();
		} else {
			//todo
			assert false;
		}
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		assert this.uuid != null;
		binaryEncoder.encode(VERSION);
		binaryEncoder.encode(this.uuid.getMostSignificantBits());
		binaryEncoder.encode(this.uuid.getLeastSignificantBits());
		binaryEncoder.encode(this.isDefaultFieldNameGenerationDesired);
		binaryEncoder.encode(this.isSyntaxNoiseDesired);
	}
	@Override
	public String toString() {
		return "name";
	}
}
