package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class source extends State {
	protected Source theSource;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theSource = new Source(attrs.getValue("id"), attrs
				.getValue("name"));

		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("addSource",
					new Class[] { Source.class });
			method.invoke(parent, new Object[] { this.theSource });
		} catch (Exception e) {
			System.err.println("addSource not found in " + parent.getName());
			//e.printStackTrace();
		}
	}

	public Source getSource() {
		return this.theSource;
	}
}