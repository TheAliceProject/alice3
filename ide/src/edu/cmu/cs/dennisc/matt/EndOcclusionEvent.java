package edu.cmu.cs.dennisc.matt;

import java.util.List;

import org.lgna.story.Entity;
import org.lgna.story.event.OcclusionEvent;

public class EndOcclusionEvent extends OcclusionEvent {

	public EndOcclusionEvent(List<Entity> entityPair) {
		super(entityPair);
	}

}
