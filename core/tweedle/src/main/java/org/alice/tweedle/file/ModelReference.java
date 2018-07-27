package org.alice.tweedle.file;

public class ModelReference extends ResourceReference {
	private static final String MODEL = "model";

	public ModelReference() {
		super();
	}

	@Override public String getContentType() {
		return MODEL;
	}
}
