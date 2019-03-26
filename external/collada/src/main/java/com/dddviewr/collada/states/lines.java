package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.geometry.Lines;
import com.dddviewr.collada.geometry.Mesh;

public class lines extends State {
	protected Lines theLines;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theLines = new Lines(attrs.getValue("material"), Integer
				.parseInt(attrs.getValue("count")));
		Mesh mesh = ((mesh) getParent()).getMesh();
		mesh.addPrimitives(this.theLines);
	}

	public Lines getTriangles() {
		return this.theLines;
	}

	public void addInput(Input i) {
		this.theLines.addInput(i);
	}

	public void addData(StringBuilder str) {
		this.theLines.addData(str);
	}
}