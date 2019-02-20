package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.geometry.Mesh;
import com.dddviewr.collada.geometry.Triangles;

public class triangles extends State {
	protected Triangles theTriangles;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theTriangles = new Triangles(attrs.getValue("material"), Integer
				.parseInt(attrs.getValue("count")));
		Mesh mesh = ((mesh) getParent()).getMesh();
		mesh.addPrimitives(this.theTriangles);
	}

	public Triangles getTriangles() {
		return this.theTriangles;
	}

	public void addInput(Input i) {
		this.theTriangles.addInput(i);
	}

	public void addData(StringBuilder str) {
		this.theTriangles.addData(str);
	}
}