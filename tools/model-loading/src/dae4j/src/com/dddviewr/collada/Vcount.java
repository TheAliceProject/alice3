package com.dddviewr.collada;

import java.io.PrintStream;

public class Vcount extends Base {
	protected int[] data;

	public int[] getData() {
		return this.data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		this.data = new int[values.length];
		for (String s : values)
			if (s.length() != 0)
				this.data[(index++)] = Integer.parseInt(s);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Vcount");
		if (this.data != null) {
			out.print(prefix);
			for (int i : this.data) {
				out.print(" " + i);
			}
			out.println();
		}
	}
}