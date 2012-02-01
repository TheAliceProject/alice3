package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddKeyPressedListener;
import org.lgna.story.Scene;
import org.lgna.story.event.KeyListener;

public class KeyboardEventListenerMenu extends CascadeMenuModel< org.lgna.project.ast.MethodInvocation > {	

	public static final JavaMethod ADD_KEY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addKeyPressedListener", KeyListener.class, AddKeyPressedListener.Detail[].class );

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
	protected List<CascadeBlankChild> updateBlankChildren(
			List<CascadeBlankChild> rv, BlankNode<MethodInvocation> blankNode) {
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_KEY_LISTENER_METHOD ) );
		return rv;
	}

}