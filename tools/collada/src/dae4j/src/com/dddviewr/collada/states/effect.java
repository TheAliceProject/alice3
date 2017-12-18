package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.LibraryEffects;

public class effect extends State {
	protected Effect theEffect;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		this.theEffect = new Effect(attrs.getValue("id"));
		LibraryEffects lib = ((library_effects) getParent()).getLibrary();
		lib.addEffect(theEffect);
	}

	public Effect getEffect() {
		return theEffect;
	}
}