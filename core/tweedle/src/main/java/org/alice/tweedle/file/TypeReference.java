package org.alice.tweedle.file;

import java.util.Collections;

public class TypeReference extends ResourceReference {
	private static final String TYPE = "type";

	public TypeReference( String id, String fileName, String format ) {
		this.id = id;
		this.files = Collections.singletonList( fileName );
		this.format = format;
	}

	@Override public String getContentType() {
		return TYPE;
	}
}
