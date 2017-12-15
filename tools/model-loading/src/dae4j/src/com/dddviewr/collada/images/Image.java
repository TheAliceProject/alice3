package com.dddviewr.collada.images;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Image extends Base {
	protected String id;
	protected String name;
	protected String initFrom;

	public Image(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getInitFrom() {
		return this.initFrom;
	}

	public void setInitFrom(String initFrom) {
		this.initFrom = initFrom;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Image (id: " + this.id + ", name: " + this.name
				+ ")");
		if (this.initFrom != null)
			out.println(prefix + " InitFrom: " + this.initFrom);
	}
}