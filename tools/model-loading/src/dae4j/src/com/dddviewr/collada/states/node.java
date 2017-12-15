package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;

public class node extends State {
	protected Node theNode;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theNode = new Node(attrs.getValue("id"), attrs.getValue("name"),
				attrs.getValue("sid"), attrs.getValue("type"));

		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("addNode",
					new Class[] { Node.class });
			method.invoke(parent, new Object[] { this.theNode });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Node getNode() {
		return this.theNode;
	}

	public void addNode(Node n) {
		this.theNode.addNode(n);
	}
}