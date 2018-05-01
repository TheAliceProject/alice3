package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.controller.Joints;
import com.dddviewr.collada.controller.Skin;

public class joints extends State {
	protected Joints theJoints;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theJoints = new Joints();
		Skin skin = ((skin) getParent()).getSkin();
		skin.setJoints(this.theJoints);
	}

	public void addInput(Input inp) {
		this.theJoints.addInput(inp);
	}
}