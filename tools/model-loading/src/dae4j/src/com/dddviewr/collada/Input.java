package com.dddviewr.collada;

import java.io.PrintStream;

public class Input extends Base {
	protected String semantic;
	protected String source;
	protected int offset = -1;

	protected int set = -1;

	public Input(String semantic, String source) {
		this.semantic = semantic;
		this.source = source;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getSemantic() {
		return this.semantic;
	}

	public void setSemantic(String semantic) {
		this.semantic = semantic;
	}

	public int getSet() {
		return this.set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Input (semantic: " + this.semantic + ", source: "
				+ this.source);
		if (this.offset >= 0)
			out.print(", offset: " + this.offset);
		if (this.set >= 0)
			out.print(", set: " + this.set);
		out.println(")");
	}
}