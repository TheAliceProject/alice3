package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.ast.EventListenerMethodUtilities;

public class MouseEventListenerMenu extends EventListenerMenuModel {
	private static class SingletonHolder {
		private static MouseEventListenerMenu instance = new MouseEventListenerMenu();
	}

	public static MouseEventListenerMenu getInstance() {
		return SingletonHolder.instance;
	}

	private MouseEventListenerMenu() {
		super( java.util.UUID.fromString( "632632b7-75f9-4fdc-85a1-e39852f11b71" ) );
	}

	@Override
	protected void updateBlankChildren( java.util.List<CascadeBlankChild> blankChildren, BlankNode<MethodInvocation> blankNode ) {
		for( JavaMethod method : EventListenerMethodUtilities.ALL_MOUSE_CLICK_EVENT_METHODS ) {
			blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( method ) );
		}
	}

}
