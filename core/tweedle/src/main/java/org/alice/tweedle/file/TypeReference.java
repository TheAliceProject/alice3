package org.alice.tweedle.file;

public class TypeReference extends ResourceReference {
	public static final String CONTENT_TYPE = "Class";

	public TypeReference() {
		super();
	}

	public TypeReference( String id, String fileName, String format ) {
		super(id, fileName, format);
	}

	@Override public String getContentType() {
		return CONTENT_TYPE;
	}
}
