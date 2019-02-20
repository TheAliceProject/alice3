package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.EffectMaterial;
import com.dddviewr.collada.effects.Lambert;

public class lambert extends State {
	protected Lambert theLambert;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theLambert = new Lambert();
		Effect effect = ((effect) getParent().getParent().getParent())
				.getEffect();
		effect.setEffectMaterial(theLambert);
	}

	public EffectMaterial getMaterial() {
		return theLambert;
	}
}