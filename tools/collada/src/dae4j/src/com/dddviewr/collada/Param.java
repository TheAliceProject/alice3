package com.dddviewr.collada;

import java.io.PrintStream;

public class Param extends Base {
	protected String name;
	protected String type;

	public Param(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Param (name: " + this.name + ", type: "
				+ this.type + ")");
	}
}