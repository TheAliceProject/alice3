package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class CollisionListener extends TransformationListener {
	
	LinkedList<Model> groupOne;
	LinkedList<Model> groupTwo;

	public CollisionListener(LinkedList<Model> groupOne, LinkedList<Model> groupTwo) {
		this(combine(groupOne, groupTwo));
		this.groupOne = groupOne;
		this.groupTwo = groupTwo;
	}
	public CollisionListener(LinkedList<Model> objectsToObserve){
		super(objectsToObserve);
		this.groupOne = objectsToObserve;
		this.groupTwo = objectsToObserve;
	}
	public CollisionListener(Model observeForCollision, LinkedList<Model> collidesWith){
		this(combine(observeForCollision, collidesWith));
		LinkedList<Model> group1 = new LinkedList<Model>();
		group1.add(observeForCollision);
		this.groupOne = group1;
		this.groupTwo = collidesWith;
	}
	public CollisionListener(Model observeForCollision, Model withThis){
		this(combine(observeForCollision, withThis));
		LinkedList<Model> group1 = new LinkedList<Model>();
		group1.add(observeForCollision);
		LinkedList<Model> group2 = new LinkedList<Model>();
		group2.add(withThis);
		this.groupOne = group1;
		this.groupTwo = group2;
	}
	
	@Override
	protected void fire(LinkedList<Model> targets) {
		whenTheseCollide(targets);
	}
	
	public abstract void whenTheseCollide(LinkedList<Model> targets);
	
	private static LinkedList<Model> combine(Model observeForCollision,
			Model withThis) {
		LinkedList<Model> list = new LinkedList<Model>();
		list.add(withThis);
		return combine(observeForCollision, list);
	}
	private static LinkedList<Model> combine(Model observeForCollision,
			LinkedList<Model> collidesWith) {
		LinkedList<Model> list = new LinkedList<Model>();
		list.add(observeForCollision);
		return combine(list, collidesWith);
	}
	private static LinkedList<Model> combine(LinkedList<Model> groupOne,
			LinkedList<Model> groupTwo) {
		LinkedList<Model> rv = new LinkedList<Model>();
		rv.addAll(groupOne);
		rv.addAll(groupTwo);
		return rv;
	}
}
