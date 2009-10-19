package org.alice.ide.preferences.programming;

public class BinaryEncodableAndDecodableConfiguration implements Configuration, edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public java.util.UUID getUUID() {
		return null;
	}
	public boolean isDefaultFieldNameGenerationDesired() {
		return false;
	}
	public boolean isSyntaxNoiseDesired() {
		return false;
	}
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
	}
}
