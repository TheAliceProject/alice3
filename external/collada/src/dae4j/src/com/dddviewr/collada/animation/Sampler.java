package com.dddviewr.collada.animation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;

public class Sampler extends Base {
	protected String id;
	List<Input> inputs = new ArrayList<Input>();

	public Sampler(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public List<Input> getInputs() {
		return this.inputs;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Sampler (id: " + this.id + ")");
		for (Input inp : this.inputs)
			inp.dump(out, indent + 1);
	}

	public void addInput(Input inp) {
		this.inputs.add(inp);
	}
}