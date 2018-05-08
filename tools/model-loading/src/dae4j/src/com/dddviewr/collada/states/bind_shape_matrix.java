package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.controller.Skin;

public class bind_shape_matrix extends State {
	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		Skin skin = ((skin) getParent()).getSkin();
		skin.parseBindShapeMatrix(this.content);
		super.endElement(name);
	}
}