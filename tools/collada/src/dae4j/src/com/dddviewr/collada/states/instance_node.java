package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.InstanceNode;

public class instance_node  extends State {
	protected InstanceNode instanceNode;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceNode = new InstanceNode(attrs.getValue("url"));
		Node node = ((node) getParent()).getNode();
		node.setInstanceNode(this.instanceNode);
	}

	public InstanceNode getInstanceNode() {
		return this.instanceNode;
	}
}