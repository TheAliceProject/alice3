package org.alice.tweedle.file;

import java.util.List;

abstract public class ResourceReference {

	public String id;
	public String format;
	public List<String> files;

	abstract public String getContentType();

}
