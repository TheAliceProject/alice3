package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.Scale;

public class scale extends State {
	protected Scale theScale;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theScale = new Scale(attrs.getValue("sid"));
		Node node = ((node) getParent()).getNode();
		node.addXform(this.theScale);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		this.theScale.parse(this.content);
		super.endElement(name);
	}

	public Scale getScale() {
		return this.theScale;
	}
}