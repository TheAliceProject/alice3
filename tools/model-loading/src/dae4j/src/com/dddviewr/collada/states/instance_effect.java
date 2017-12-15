package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.materials.InstanceEffect;


public class instance_effect extends State {
	protected InstanceEffect instanceEffect;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceEffect = new InstanceEffect(attrs.getValue("url"));
		((material) getParent()).getMaterial().setInstanceEffect(
				this.instanceEffect);
	}
}