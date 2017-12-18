package com.dddviewr.collada.visualscene;

import java.io.PrintStream;

public class Matrix extends BaseXform {
	protected float[] data = new float[16];

	public Matrix(String sid) {
		super(sid);
	}

	public float[] getData() {
		return this.data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Matrix (sid: " + this.sid + ",data:");
		for (float f : this.data) {
			out.print(" " + f);
		}
		out.println(")");
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		for (String s : values) {
			if (index >= 16)
				return;
			if (s.length() == 0)
				continue;
			this.data[(index++)] = Float.parseFloat(s);
		}
	}
}