package com.dddviewr.collada.effects;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class NewParam extends Base {
	protected String sid;
	protected Surface surface;
	protected Sampler2D sampler2D;

	public NewParam(String sid) {
		this.sid = sid;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Surface getSurface() {
		return this.surface;
	}

	public void setSurface(Surface surface) {
		this.surface = surface;
	}

	public Sampler2D getSampler2D() {
		return this.sampler2D;
	}

	public void setSampler2D(Sampler2D sampler2D) {
		this.sampler2D = sampler2D;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "NewParam (sid: " + this.sid + ")");

		if (this.surface != null) {
			this.surface.dump(out, indent + 1);
		}
		if (this.sampler2D != null)
			this.sampler2D.dump(out, indent + 1);
	}
}