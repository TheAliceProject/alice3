package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.FloatArray;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class float_array extends State {
	protected FloatArray floatArray;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.floatArray = new FloatArray(attrs.getValue("id"), Integer
				.parseInt(attrs.getValue("count")));
		Source src = ((source) getParent()).getSource();
		src.setFloatArray(floatArray);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		super.endElement(name);
		this.floatArray.parse(content);
	}
}