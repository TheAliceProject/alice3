package com.dddviewr.collada.effects;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Sampler2D extends Base {
	protected String source;
	protected String minFilter;
	protected String magFilter;

	public String getMagFilter() {
		return this.magFilter;
	}

	public void setMagFilter(String magFilter) {
		this.magFilter = magFilter;
	}

	public String getMinFilter() {
		return this.minFilter;
	}

	public void setMinFilter(String minFilter) {
		this.minFilter = minFilter;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Sampler2D");

		if (this.source != null) {
			out.println(prefix + " Source: " + this.source);
		}
		if (this.minFilter != null) {
			out.println(prefix + " MinFilter: " + this.minFilter);
		}
		if (this.magFilter != null)
			out.println(prefix + " MagFilter: " + this.magFilter);
	}
}