package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.EffectAttribute;
import com.dddviewr.collada.effects.EffectMaterial;

public class diffuse extends State {
	protected EffectAttribute attrib;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		this.attrib = new EffectAttribute("Diffuse");

		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("getMaterial",
					new Class[0]);
			EffectMaterial mat = (EffectMaterial) method.invoke(parent,
					new Object[0]);
			mat.setDiffuse(this.attrib);
		} catch (Exception localException) {
		}
	}

	public EffectAttribute getAttrib() {
		return this.attrib;
	}
}