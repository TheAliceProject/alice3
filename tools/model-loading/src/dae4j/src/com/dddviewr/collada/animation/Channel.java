package com.dddviewr.collada.animation;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Channel extends Base {
	protected String source;
	protected String target;

	public Channel(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Channel (source: " + this.source + ", target: "
				+ this.target + ")");
	}
}