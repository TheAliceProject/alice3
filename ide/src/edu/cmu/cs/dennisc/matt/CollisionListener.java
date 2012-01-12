package edu.cmu.cs.dennisc.matt;

import java.util.LinkedList;

import org.lgna.story.Model;

public abstract class CollisionListener {
	
//	LinkedList<Model> groupOne;
//	LinkedList<Model> groupTwo;
//
//	
//	public CollisionListener(){}
//	
//	@Override
//	protected void fire(LinkedList<Model> targets) {
//		whenTheseCollide(targets);
//	}
//	
	public abstract void whenTheseCollide(LinkedList<Model> targets);
	
//	private static LinkedList<Model> combine(Model observeForCollision,
//			Model withThis) {
//		LinkedList<Model> list = new LinkedList<Model>();
//		list.add(withThis);
//		return combine(observeForCollision, list);
//	}
//	private static LinkedList<Model> combine(Model observeForCollision,
//			LinkedList<Model> collidesWith) {
//		LinkedList<Model> list = new LinkedList<Model>();
//		list.add(observeForCollision);
//		return combine(list, collidesWith);
//	}
//	private static LinkedList<Model> combine(LinkedList<Model> groupOne,
//			LinkedList<Model> groupTwo) {
//		LinkedList<Model> rv = new LinkedList<Model>();
//		rv.addAll(groupOne);
//		rv.addAll(groupTwo);
//		return rv;
//	}
}
