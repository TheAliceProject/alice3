package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.BaseXform;
import com.dddviewr.collada.visualscene.Rotate;

public class rotate extends State {
	protected Rotate theRotate;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theRotate = new Rotate(attrs.getValue("sid"));
		Node node = ((node) getParent()).getNode();
		node.addXform(this.theRotate);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		this.theRotate.parse(this.content);
		super.endElement(name);
	}

	public BaseXform getRotate() {
		return this.theRotate;
	}
}