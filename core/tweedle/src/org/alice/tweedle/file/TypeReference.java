package org.alice.tweedle.file;

public class TypeReference extends ResourceReference {
	private static final String TYPE = "type";

	@Override public String getContentType() {
		return TYPE;
	}
}
