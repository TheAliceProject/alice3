package org.lgna.story.event;

public interface ViewExitListener<A extends org.lgna.story.SModel> {

	public void leftView( LeavesViewEvent<A> e );

}
