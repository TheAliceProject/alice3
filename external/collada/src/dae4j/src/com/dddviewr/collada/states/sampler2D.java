package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Sampler2D;


public class sampler2D extends State {
	protected Sampler2D theSampler2D;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theSampler2D = new Sampler2D();
		((newparam) getParent()).getNewParam().setSampler2D(this.theSampler2D);
	}

	public void endElement(String name) {
		if (name.equals("source")) {
			this.theSampler2D.setSource(this.content.toString());
			this.content.setLength(0);
		} else if (name.equals("minfilter")) {
			this.theSampler2D.setMinFilter(this.content.toString());
			this.content.setLength(0);
		} else if (name.equals("magfilter")) {
			this.theSampler2D.setMagFilter(this.content.toString());
			this.content.setLength(0);
		} else {
			super.endElement(name);
		}
	}

	public void startElement(String name, Attributes attrs) {
		if (name.equals("source"))
			setContentNeeded(true);
		else if (name.equals("minfilter"))
			setContentNeeded(true);
		else if (name.equals("magfilter"))
			setContentNeeded(true);
		else
			super.startElement(name, attrs);
	}

	public Sampler2D getSampler2D() {
		return this.theSampler2D;
	}
}