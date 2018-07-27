package org.alice.tweedle.file;

public class TypeReference extends ResourceReference {
	private static final String CLASS_CONTENT = "Class";

	public TypeReference() {
		super();
	}

	public TypeReference( String id, String fileName, String format ) {
		super(id, fileName, format);
	}

	@Override public String getContentType() {
		return CLASS_CONTENT;
	}
}
