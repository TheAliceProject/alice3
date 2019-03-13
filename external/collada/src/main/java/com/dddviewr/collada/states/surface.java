package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Surface;


public class surface extends State {
	protected Surface theSurface;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theSurface = new Surface(attrs.getValue("type"));
		((newparam) getParent()).getNewParam().setSurface(this.theSurface);
	}

	public Surface getSurface() {
		return this.theSurface;
	}

	public void setInitFrom(String initFrom) {
		this.theSurface.setInitFrom(initFrom);
	}
}