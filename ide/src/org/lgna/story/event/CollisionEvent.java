package org.lgna.story.event;

import java.util.List;

import org.lgna.story.Model;

public class CollisionEvent extends AbstractEvent {
	private final List<Model> models;
	public CollisionEvent( List<Model> models ) {
		this.models = models;
	}
	public List<Model> getModels() {
		return models;
	}
}
