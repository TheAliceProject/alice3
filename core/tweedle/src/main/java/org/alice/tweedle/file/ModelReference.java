package org.alice.tweedle.file;

public class ModelReference extends ResourceReference {
	public static final String CONTENT_TYPE = "model";

	public ModelReference() {
		super();
	}

	@Override public String getContentType() {
		return CONTENT_TYPE;
	}
}
