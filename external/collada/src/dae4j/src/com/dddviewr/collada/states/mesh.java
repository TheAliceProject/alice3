package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Mesh;

public class mesh extends State {
	protected Mesh theMesh;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theMesh = new Mesh();

		Geometry geo = ((geometry) getParent()).getGeometry();
		geo.setMesh(this.theMesh);
	}

	public Mesh getMesh() {
		return this.theMesh;
	}

	public void addSource(Source src) {
		this.theMesh.addSource(src);
	}
}