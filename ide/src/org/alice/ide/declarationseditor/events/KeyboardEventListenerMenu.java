package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddKeyPressListener;
import org.lgna.story.AddObjectMoverFor;
import org.lgna.story.MovableTurnable;
import org.lgna.story.Scene;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyPressListener;

public class KeyboardEventListenerMenu extends EventListenerMenuModel {	

	public static final JavaMethod ADD_KEY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addKeyPressListener", KeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final JavaMethod MOVE_WITH_ARROWS = JavaMethod.getInstance( Scene.class, "addObjectMoverFor", MovableTurnable.class, AddObjectMoverFor.Detail[].class );
	public static final JavaMethod ADD_ARROW_KEY_PRESS_LISTENER = JavaMethod.getInstance( Scene.class, "addArrowKeyPressListener", ArrowKeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final JavaMethod ADD_NUNBER_KEY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addNumberKeyPressListener", NumberKeyPressListener.class, AddKeyPressListener.Detail[].class );

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
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_KEY_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_ARROW_KEY_PRESS_LISTENER ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_NUNBER_KEY_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( MOVE_WITH_ARROWS ) );
		return rv;
	}

}