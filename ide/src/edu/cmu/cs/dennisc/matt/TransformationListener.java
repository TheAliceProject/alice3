package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;


abstract class TransformationListener extends AbstractListener {
	
	private LinkedList<Model> observing;

	public TransformationListener(LinkedList<Model> objectsToObserve) {
		this.observing = objectsToObserve;
	}

	public LinkedList<Model> getObserving() {
		return observing;
	}
}
