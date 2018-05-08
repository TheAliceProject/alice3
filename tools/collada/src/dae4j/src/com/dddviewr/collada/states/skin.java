package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Skin;

public class skin extends State {
	protected Skin theSkin;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theSkin = new Skin(attrs.getValue("source"));
		Controller ctrl = ((controller) getParent()).getController();
		ctrl.setSkin(this.theSkin);
	}

	public Skin getSkin() {
		return this.theSkin;
	}

	public void addSource(Source src) {
		this.theSkin.addSource(src);
	}
}