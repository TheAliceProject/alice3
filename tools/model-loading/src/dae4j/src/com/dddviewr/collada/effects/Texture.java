package com.dddviewr.collada.effects;

import java.io.PrintStream;

import com.dddviewr.collada.Base;

public class Texture extends Base {
	protected String texture;
	protected String texcoord;

	public Texture(String texture, String texcoord) {
		this.texture = texture;
		this.texcoord = texcoord;
	}

	public String getTexcoord() {
		return this.texcoord;
	}

	public void setTexcoord(String texcoord) {
		this.texcoord = texcoord;
	}

	public String getTexture() {
		return this.texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Texture (texture: " + this.texture
				+ ", texcoord: " + this.texcoord + ")");
	}
}