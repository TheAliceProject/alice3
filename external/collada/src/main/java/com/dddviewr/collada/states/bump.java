package com.dddviewr.collada.states;

import java.lang.reflect.Method;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.EffectAttribute;


public class bump extends State {
	protected EffectAttribute attrib;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		this.attrib = new EffectAttribute("Bump");

		State effectState = getParent().getParent().getParent().getParent().getParent();
		try {
			Method method = effectState.getClass().getMethod("getEffect",
					new Class[0]);
			Effect ef = (Effect) method.invoke(effectState,
					new Object[0]);
			ef.getEffectMaterial().setBump(attrib);
		} catch (Exception localException) {
		}
	}

	public EffectAttribute getAttrib() {
		return this.attrib;
	}
}
