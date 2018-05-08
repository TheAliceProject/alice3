package com.dddviewr.collada.visualscene;

import java.io.PrintStream;

public class Translate extends BaseXform {
	protected float[] data = new float[3];

	public Translate(String sid) {
		super(sid);
	}

	public float[] getData() {
		return this.data;
	}

	public float getX() {
		return this.data[0];
	}

	public float getY() {
		return this.data[1];
	}

	public float getZ() {
		return this.data[2];
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Translate (sid: " + this.sid + ",data:");
		for (float f : this.data) {
			out.print(" " + f);
		}
		out.println(")");
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		for (String s : values) {
			if (index >= 3)
				return;
			this.data[(index++)] = Float.parseFloat(s);
		}
	}
}