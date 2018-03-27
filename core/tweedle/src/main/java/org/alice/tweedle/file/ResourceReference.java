package org.alice.tweedle.file;

import java.util.Collections;
import java.util.List;

abstract public class ResourceReference {

	public String id;
	public String format;
	public List<String> files;
	// Held in a variable so it is serialized
	final public String type;

	ResourceReference() {
		type = getContentType();
	}

	ResourceReference( String id, String fileName, String format ) {
		this.id = id;
		this.files = Collections.singletonList( fileName );
		this.format = format;
		type = getContentType();
	}

	ResourceReference( String id, List<String> fileNames, String format ) {
		this.id = id;
		this.files = fileNames;
		this.format = format;
		type = getContentType();
	}

	abstract public String getContentType();

}
