package com.dddviewr.collada.geometry;

import java.io.PrintStream;

import com.dddviewr.collada.Input;


public class Triangles extends Primitives {
	private int pos;

	public Triangles(String material, int count) {
		this.material = material;
		this.count = count;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Triangles (material: " + this.material
				+ ", count: " + this.count + ")");
		for (Input i : this.inputs) {
			i.dump(out, indent + 1);
		}
		if (this.data != null) {
			out.print(prefix);
			for (int d : this.data) {
				out.print(" " + d);
			}
			out.println();
		}
	}

	public void addData(StringBuilder str) {
		if (this.data == null) {
			this.data = new int[this.count * 3 * getStride()];
			this.pos = 0;
		}

		String[] vals = str.toString().split("\\s+");
		for (String s : vals) {
			if (this.pos >= this.data.length)
				return;
			if (s.length() != 0)
				this.data[(this.pos++)] = Integer.parseInt(s);
		}
	}

}