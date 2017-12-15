package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Accessor;
import com.dddviewr.collada.Param;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class param extends State {
	protected Param theParam;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theParam = new Param(attrs.getValue("name"), attrs
				.getValue("type"));
		Accessor accessor = ((accessor) getParent()).getAccessor();
		accessor.addParam(this.theParam);
	}

	public Param getParam() {
		return this.theParam;
	}
}