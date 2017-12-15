package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.scene.Scene;

public class scene extends State {
	protected Scene theScene;

	public Scene getScene() {
		return this.theScene;
	}

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theScene = new Scene();
		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setScene(this.theScene);
	}
}