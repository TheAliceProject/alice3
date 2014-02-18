package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.ast.EventListenerMethodUtilities;

public class KeyboardEventListenerMenu extends EventListenerMenuModel {
	private static class SingletonHolder {
		private static KeyboardEventListenerMenu instance = new KeyboardEventListenerMenu();
	}

	public static KeyboardEventListenerMenu getInstance() {
		return SingletonHolder.instance;
	}

	private KeyboardEventListenerMenu() {
		super( java.util.UUID.fromString( "8ef6f603-896c-43fc-8b2e-06aa1c4e0c60" ) );
	}

	@Override
	protected void updateBlankChildren( java.util.List<CascadeBlankChild> blankChildren, BlankNode<MethodInvocation> blankNode ) {
		for( JavaMethod method : EventListenerMethodUtilities.ALL_KEYBOARD_EVENT_METHODS ) {
			blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( method ) );
		}
	}

}
