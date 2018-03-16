package org.alice.tweedle.file;

public class ModelReference extends ResourceReference {
	private static final String MODEL = "model";

	@Override public String getContentType() {
		return MODEL;
	}
}
