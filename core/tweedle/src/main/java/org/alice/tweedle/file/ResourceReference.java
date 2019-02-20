package org.alice.tweedle.file;

import org.lgna.common.Resource;

abstract public class ResourceReference {

	public String name;
	public String format;
	public String file;
	//Unitialized provenance is used to add additional info
	public Manifest.Provenance provenance;
	// Held in a variable so it is serialized
	final public String type;

	public ResourceReference() {
		type = getContentType();
	}

	ResourceReference( String name, String fileName, String format ) {
		this.name = name;
		this.file = fileName;
		this.format = format;
		type = getContentType();
	}

	ResourceReference( Resource resource ) {
		name = resource.getName();
		file = resource.getOriginalFileName();
		format = resource.getContentType();
		type = getContentType();
	}

	abstract public String getContentType();

}
