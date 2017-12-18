package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.Vcount;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.controller.VertexWeights;

public class vertex_weights extends State {
	protected VertexWeights vertexWeights;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.vertexWeights = new VertexWeights(Integer.parseInt(attrs
				.getValue("count")));
		Skin skin = ((skin) getParent()).getSkin();
		skin.setVertexWeights(this.vertexWeights);
	}

	public void addInput(Input inp) {
		this.vertexWeights.addInput(inp);
	}

	public void setVcount(Vcount vc) {
		this.vertexWeights.setVcount(vc);
	}

	public void parseData(StringBuilder str) {
		this.vertexWeights.parseData(str);
	}
}