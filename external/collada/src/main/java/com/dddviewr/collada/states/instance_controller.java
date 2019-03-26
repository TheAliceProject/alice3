package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.InstanceController;
import com.dddviewr.collada.visualscene.InstanceMaterial;

public class instance_controller extends State {
	protected InstanceController instanceController;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceController = new InstanceController(attrs.getValue("url"));
		Node node = ((node) getParent()).getNode();
		node.setInstanceController(this.instanceController);
	}

	public InstanceController getInstanceController() {
		return this.instanceController;
	}

	public void addInstanceMaterial(InstanceMaterial instMat) {
		this.instanceController.addInstanceMaterial(instMat);
	}
}