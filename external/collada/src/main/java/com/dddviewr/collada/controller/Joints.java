package com.dddviewr.collada.controller;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;

public class Joints extends Base {
	protected List<Input> inputs = new ArrayList<Input>();

	public List<Input> getInputs() {
		return this.inputs;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Joints");
		for (Input inp : this.inputs)
			inp.dump(out, indent + 1);
	}

	public void addInput(Input inp) {
		this.inputs.add(inp);
	}
}