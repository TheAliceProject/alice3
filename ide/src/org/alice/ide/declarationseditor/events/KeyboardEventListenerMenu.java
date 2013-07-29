package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddKeyPressListener;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SScene;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyPressListener;

import edu.cmu.cs.dennisc.java.util.Collections;

public class KeyboardEventListenerMenu extends EventListenerMenuModel {
	public static final JavaMethod ADD_KEY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addKeyPressListener",
			KeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final JavaMethod MOVE_WITH_ARROWS = JavaMethod.getInstance(
			SScene.class,
			"addObjectMoverFor",
			SMovableTurnable.class );
	public static final JavaMethod ADD_ARROW_KEY_PRESS_LISTENER = JavaMethod.getInstance(
			SScene.class,
			"addArrowKeyPressListener",
			ArrowKeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final JavaMethod ADD_NUNBER_KEY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addNumberKeyPressListener",
			NumberKeyPressListener.class, AddKeyPressListener.Detail[].class );
	public static final java.util.List<JavaMethod> ALL_KEYBOARD_EVENT_METHODS = Collections.newArrayList( ADD_KEY_LISTENER_METHOD, ADD_ARROW_KEY_PRESS_LISTENER, ADD_NUNBER_KEY_LISTENER_METHOD, MOVE_WITH_ARROWS );

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
		for( JavaMethod method : ALL_KEYBOARD_EVENT_METHODS ) {
			blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( method ) );
		}
	}

}
