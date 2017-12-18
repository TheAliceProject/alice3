package com.dddviewr.collada.effects;

import java.io.PrintStream;
import java.util.Arrays;

import com.dddviewr.collada.Base;

public class EffectAttribute extends Base {
	protected String name;
	protected float[] data;
	protected Texture texture;

	public EffectAttribute(String name) {
		this.name = name;
	}

	public float[] getData() {
		return this.data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this.name);
		if (this.data != null) {
			out.println(prefix + " data: " + Arrays.toString(this.data));
		}
		if (this.texture != null)
			this.texture.dump(out, indent + 1);
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		this.data = new float[values.length];
		for (String s : values)
			this.data[(index++)] = Float.parseFloat(s);
	}
}