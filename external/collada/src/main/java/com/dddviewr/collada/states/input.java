package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class input extends State {
	protected Input theInput;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theInput = new Input(attrs.getValue("semantic"), attrs
				.getValue("source"));
		String value = attrs.getValue("offset");
		if (value != null)
			this.theInput.setOffset(Integer.parseInt(value));
		value = attrs.getValue("set");
		if (value != null)
			this.theInput.setSet(Integer.parseInt(value));

		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("addInput",
					new Class[] { Input.class });
			method.invoke(parent, new Object[] { this.theInput });
		} catch (Exception e) {
			System.err.println("addInput not found in " + parent.getClass().getName());
			//e.printStackTrace();
		}
	}
}