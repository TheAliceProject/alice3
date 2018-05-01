package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.EffectAttribute;

public class color extends State {
	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("getAttrib",
					new Class[0]);
			EffectAttribute attrib = (EffectAttribute) method.invoke(parent,
					new Object[0]);
			attrib.parse(this.content);
		} catch (Exception localException) {
		}
		super.endElement(name);
	}
}