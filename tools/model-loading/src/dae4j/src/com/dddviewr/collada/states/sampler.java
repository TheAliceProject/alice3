package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.animation.Animation;
import com.dddviewr.collada.animation.Sampler;

public class sampler extends State {
	protected Sampler theSampler;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theSampler = new Sampler(attrs.getValue("id"));
		Animation anim = ((animation) getParent()).getAnimation();
		anim.setSampler(this.theSampler);
	}

	public void addInput(Input inp) {
		this.theSampler.addInput(inp);
	}
}