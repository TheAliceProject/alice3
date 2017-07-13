package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.animation.Animation;
import com.dddviewr.collada.animation.Channel;

public class channel extends State {
	protected Channel theChannel;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theChannel = new Channel(attrs.getValue("source"), attrs
				.getValue("target"));
		Animation anim = ((animation) getParent()).getAnimation();
		anim.setChannel(this.theChannel);
	}
}