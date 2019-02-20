package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.IdrefArray;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class IDREF_array extends State {
	protected IdrefArray idrefArray;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.idrefArray = new IdrefArray(attrs.getValue("id"), Integer
				.parseInt(attrs.getValue("count")));
		Source src = ((source) getParent()).getSource();
		src.setIdrefArray(this.idrefArray);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		this.idrefArray.parse(this.content);
		super.endElement(name);
	}

	public IdrefArray getIdrefArray() {
		return this.idrefArray;
	}
}