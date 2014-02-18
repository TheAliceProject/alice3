package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.ast.EventListenerMethodUtilities;

public class TransformationEventListenerMenu extends EventListenerMenuModel {
	private static class SingletonHolder {
		private static TransformationEventListenerMenu instance = new TransformationEventListenerMenu();
	}

	public static TransformationEventListenerMenu getInstance() {
		return SingletonHolder.instance;
	}

	private TransformationEventListenerMenu() {
		super( java.util.UUID.fromString( "67822611-4013-4fce-97b0-3c9e4d6440a1" ) );
	}

	@Override
	protected void updateBlankChildren( java.util.List<CascadeBlankChild> blankChildren, BlankNode<MethodInvocation> blankNode ) {
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_START_COLLISION_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_END_COLLISION_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_ENTER_PROXIMITY_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_EXIT_PROXIMITY_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_ENTER_VIEW_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_EXIT_VIEW_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_START_OCCLUSION_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_END_OCCLUSION_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( EventListenerMethodUtilities.ADD_TRANSFORMATION_LISTENER_METHOD ) );
	}

}
