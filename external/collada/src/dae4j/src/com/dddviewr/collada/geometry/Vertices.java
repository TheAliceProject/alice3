package com.dddviewr.collada.geometry;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Base;
import com.dddviewr.collada.Input;

public class Vertices extends Base {
	protected String id;
	protected List<Input> inputs = new ArrayList<Input>();

	public Vertices(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Vertices (id: " + this.id + ")");
		for (Input i : this.inputs)
			i.dump(out, indent + 1);
	}

	public void addInput(Input i) {
		this.inputs.add(i);
	}

	public List<Input> getInputs() {
		return this.inputs;
	}
}