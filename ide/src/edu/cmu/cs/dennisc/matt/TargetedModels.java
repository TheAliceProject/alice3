package edu.cmu.cs.dennisc.matt;

import org.lgna.story.Model;

public class TargetedModels implements AddMouseButtonListener.Detail {
	
	private final Model[] models;

	public TargetedModels(Model[] models){
		this.models = models;
	}

	public static Model[] getCollection(TargetedModels detail) {
		return detail.models;
	}

	public static Model[] getEmptyCollection() {
		return new Model[0];
	}

	
}
