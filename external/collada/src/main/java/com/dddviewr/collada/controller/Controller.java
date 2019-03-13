package com.dddviewr.collada.controller;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Controller extends Base {
	protected String id;
	protected String name;
	protected Skin skin;

	public Controller(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Skin getSkin() {
		return this.skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
		skin.setController(this);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Controller (id: " + this.id + ", name: "
				+ this.name + ")");
		if (this.skin != null)
			this.skin.dump(out, indent + 1);
	}
}