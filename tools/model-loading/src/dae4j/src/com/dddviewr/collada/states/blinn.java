package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Blinn;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.EffectMaterial;

public class blinn extends State {
	protected Blinn theBlinn;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theBlinn = new Blinn();
		Effect effect = ((effect) getParent().getParent().getParent())
				.getEffect();
		effect.setEffectMaterial(this.theBlinn);
	}

	public EffectMaterial getMaterial() {
		return this.theBlinn;
	}
}