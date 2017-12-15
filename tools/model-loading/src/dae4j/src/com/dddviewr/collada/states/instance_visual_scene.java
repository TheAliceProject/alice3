package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.scene.InstanceVisualScene;
import com.dddviewr.collada.scene.Scene;

public class instance_visual_scene extends State {
	protected InstanceVisualScene instanceVisualScene;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceVisualScene = new InstanceVisualScene(attrs
				.getValue("url"));
		Scene scene = ((scene) getParent()).getScene();
		scene.setInstanceVisualScene(this.instanceVisualScene);
	}
}