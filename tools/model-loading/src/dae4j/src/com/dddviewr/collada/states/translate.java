package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.Translate;

public class translate extends State {
	protected Translate theTranslate;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theTranslate = new Translate(attrs.getValue("sid"));
		Node node = ((node) getParent()).getNode();
		node.addXform(this.theTranslate);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		this.theTranslate.parse(this.content);
		super.endElement(name);
	}

	public Translate getTranslate() {
		return this.theTranslate;
	}
}