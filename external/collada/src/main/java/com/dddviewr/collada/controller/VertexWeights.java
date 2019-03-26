package com.dddviewr.collada.controller;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.Vcount;

public class VertexWeights extends Base {
	protected int count;
	protected List<Input> inputs = new ArrayList<Input>();
	protected Vcount vcount;
	protected int[] data;

	public VertexWeights(int count) {
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Input> getInputs() {
		return this.inputs;
	}

	public void addInput(Input inp) {
		this.inputs.add(inp);
	}

	public Vcount getVcount() {
		return this.vcount;
	}

	public void setVcount(Vcount vcount) {
		this.vcount = vcount;
	}

	public int[] getData() {
		return this.data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	public void parseData(StringBuilder str) {
		String[] vals = str.toString().split("\\s+");
		this.data = new int[vals.length];
		int pos = 0;
		for (String s : vals)
			if (s.length() != 0)
				this.data[(pos++)] = Integer.parseInt(s);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "VertexWeights (count: " + this.count + ")");
		for (Input inp : this.inputs) {
			inp.dump(out, indent + 1);
		}
		if (this.vcount != null) {
			this.vcount.dump(out, indent + 1);
		}
		if (this.data != null) {
			out.print(prefix);
			for (int i : this.data) {
				out.print(" " + i);
			}
			out.println();
		}
	}
}