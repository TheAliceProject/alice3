package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;


abstract class TransformationListener extends AbstractEventHandler {
	
	private LinkedList<Model> observing;

	public TransformationListener() {
//		super(EventPolicy.IGNORE);
//		this.observing = objectsToObserve;
	}

	public LinkedList<Model> getObserving() {
		return observing;
	}
}
