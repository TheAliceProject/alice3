package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.Matrix;

public class matrix extends State {
	protected Matrix theMatrix;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theMatrix = new Matrix(attrs.getValue("sid"));
		Node node = ((node) getParent()).getNode();
		node.addXform(this.theMatrix);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		this.theMatrix.parse(this.content);
		super.endElement(name);
	}

	public Matrix getMatrix() {
		return this.theMatrix;
	}
}