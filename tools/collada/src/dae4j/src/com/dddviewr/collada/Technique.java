package com.dddviewr.collada;

import java.io.PrintStream;

public class Technique extends Base {
	protected String sid;
	protected String profile;

	public Technique(String sid, String profile) {
		this.sid = sid;
		this.profile = profile;
	}

	public String getProfile() {
		return this.profile;
	}

	public String getSid() {
		return this.sid;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Technique (");
		if (this.sid != null) {
			out.print("sid: " + this.sid);
			if (this.profile != null)
				out.print(", ");
		}
		if (this.profile != null)
			out.print("profile: " + this.profile);
		out.println(")");
	}
}