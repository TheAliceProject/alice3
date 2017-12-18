package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.Unit;


public class unit extends State {
	protected Unit theUnit;

	@Override
	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theUnit = new Unit(Float.parseFloat(attrs.getValue("meter")), attrs.getValue("name"));
		((COLLADA)getParent().getParent()).getCollada().setUnit(theUnit);
	}
}
