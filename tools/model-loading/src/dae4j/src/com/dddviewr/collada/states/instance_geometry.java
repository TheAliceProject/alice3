package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.InstanceGeometry;
import com.dddviewr.collada.visualscene.InstanceMaterial;

public class instance_geometry extends State {
	protected InstanceGeometry instanceGeometry;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceGeometry = new InstanceGeometry(attrs.getValue("url"));
		Node node = ((node) getParent()).getNode();
		node.addInstanceGeometry(this.instanceGeometry);
	}

	public InstanceGeometry getInstanceGeometry() {
		return this.instanceGeometry;
	}

	public void addInstanceMaterial(InstanceMaterial instMat) {
		this.instanceGeometry.addInstanceMaterial(instMat);
	}
}