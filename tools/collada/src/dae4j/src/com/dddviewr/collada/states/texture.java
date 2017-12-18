package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.effects.EffectAttribute;
import com.dddviewr.collada.effects.Texture;

public class texture extends State {
	protected Texture theTexture;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theTexture = new Texture(attrs.getValue("texture"), attrs
				.getValue("texcoord"));

		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("getAttrib",
					new Class[0]);
			EffectAttribute attrib = (EffectAttribute) method.invoke(parent,
					new Object[0]);
			attrib.setTexture(this.theTexture);
		} catch (Exception localException) {
		}
	}

	public Texture getTexture() {
		return this.theTexture;
	}
}