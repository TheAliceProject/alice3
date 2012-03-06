package org.lgna.story.event;

public abstract class CollisionEvent<A extends org.lgna.story.MovableTurnable, B extends org.lgna.story.MovableTurnable> extends AbstractEvent {
	private final org.lgna.story.MovableTurnable[] movables;
	private A[] firstArray;
	private B[] secondArray;

	public CollisionEvent( A[] firstArray, B[] secondArray ) {
		org.lgna.story.MovableTurnable[] movables = new org.lgna.story.MovableTurnable[ firstArray.length + secondArray.length ];
		this.firstArray = firstArray;
		this.secondArray = secondArray;
		for( int i = 0; i != movables.length; ++i ) {
			if( i < firstArray.length ) {
				movables[ i ] = firstArray[ i ];
			} else {
				movables[ i ] = secondArray[ i - firstArray.length ];
			}
		}
		this.movables = movables;
	}
	public A[] getCollidingFromGroupA() {
		return this.firstArray;
	}
	public B[] getCollidingFromGroupB() {
		return this.secondArray;
	}
	//keeping around for now for migration purposes
	//	public CollisionEvent( org.lgna.story.MovableTurnable... movables ) {
	//		this.movables = movables;
	//	}
	public org.lgna.story.Model[] getModels() {
		org.lgna.story.Model[] rv = new org.lgna.story.Model[ movables.length ];//EPIC HACK for demo please fix
		for( int i = 0; i != movables.length; ++i ) {
			if( movables[ i ] instanceof org.lgna.story.Model ) {
				org.lgna.story.Model model = (org.lgna.story.Model)movables[ i ];
				rv[ i ] = model;
			}
		}
		return rv;
	}
}
