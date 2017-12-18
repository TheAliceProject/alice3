package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.geometry.Mesh;
import com.dddviewr.collada.geometry.Vertices;

public class vertices extends State {
	protected Vertices theVertices;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theVertices = new Vertices(attrs.getValue("id"));
		Mesh mesh = ((mesh) getParent()).getMesh();
		mesh.setVertices(this.theVertices);
	}

	public Vertices getVertices() {
		return this.theVertices;
	}

	public void addInput(Input i) {
		this.theVertices.addInput(i);
	}
}