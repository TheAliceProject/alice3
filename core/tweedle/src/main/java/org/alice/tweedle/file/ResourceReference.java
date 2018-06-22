package org.alice.tweedle.file;

import org.lgna.common.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract public class ResourceReference {

	public String id;
	public String format;
	public String file;
	//Unitialized provenance is used to add additional info
	public Manifest.Provenance provenance;
	// Held in a variable so it is serialized
	final public String type;

	ResourceReference() {
		type = getContentType();
	}

	ResourceReference( String id, String fileName, String format ) {
		this.id = id;
		this.file = fileName;
		this.format = format;
		type = getContentType();
	}

	ResourceReference( Resource resource ) {
		id = resource.getName();
		file = resource.getOriginalFileName();
		format = resource.getContentType();
		type = getContentType();
	}

	abstract public String getContentType();

}
