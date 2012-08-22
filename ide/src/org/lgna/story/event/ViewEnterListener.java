package org.lgna.story.event;

public interface ViewEnterListener<A extends org.lgna.story.SModel> {

	public void viewEntered( ComesIntoViewEvent<A> e );

}
