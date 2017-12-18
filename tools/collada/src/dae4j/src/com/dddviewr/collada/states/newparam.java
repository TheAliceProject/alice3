package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.NewParam;

public class newparam extends State {
	protected NewParam theNewParam;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theNewParam = new NewParam(attrs.getValue("sid"));
		Effect e = ((effect) getParent().getParent()).getEffect();
		e.addNewParam(this.theNewParam);
	}

	public NewParam getNewParam() {
		return this.theNewParam;
	}
}