package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.ast.EventListenerMethodUtilities;

public class TimeEventListenerMenu extends EventListenerMenuModel {
	private static class SingletonHolder {
		private static TimeEventListenerMenu instance = new TimeEventListenerMenu();
	}

	public static TimeEventListenerMenu getInstance() {
		return SingletonHolder.instance;
	}

	private TimeEventListenerMenu() {
		super( java.util.UUID.fromString( "19e3e7d5-a2da-4f85-8897-7ef34b268af9" ) );
	}

	@Override
	protected void updateBlankChildren( java.util.List<CascadeBlankChild> blankChildren, BlankNode<MethodInvocation> blankNode ) {
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_SCENE_ACTIVATION_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_TIMER_EVENT_LISTENER_METHOD ) );
	}

}
