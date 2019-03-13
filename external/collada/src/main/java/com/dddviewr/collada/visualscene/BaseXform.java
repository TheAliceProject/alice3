package com.dddviewr.collada.visualscene;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class BaseXform extends Base {
	protected String sid;

	public BaseXform(String sid) {
		this.sid = sid;
	}

	public void dump(PrintStream out, int indent) {
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
}