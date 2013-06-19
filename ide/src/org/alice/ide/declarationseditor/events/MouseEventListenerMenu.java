package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddMouseClickOnObjectListener;
import org.lgna.story.AddMouseClickOnScreenListener;
import org.lgna.story.SScene;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;

import edu.cmu.cs.dennisc.java.util.Collections;

public class MouseEventListenerMenu extends EventListenerMenuModel {

	public static final JavaMethod ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD = JavaMethod.getInstance( SScene.class, "addMouseClickOnScreenListener", MouseClickOnScreenListener.class, AddMouseClickOnScreenListener.Detail[].class );
	public static final JavaMethod ADD_MOUSE_CLICK_ON_OBJECT_LISTENER_METHOD = JavaMethod.getInstance( SScene.class, "addMouseClickOnObjectListener", MouseClickOnObjectListener.class, AddMouseClickOnObjectListener.Detail[].class );
	public static final JavaMethod ADD_DEFAULT_MODEL_MANIPULATOR = JavaMethod.getInstance( SScene.class, "addDefaultModelManipulation" );
	public static final List<JavaMethod> ALL_MOUSE_CLICK_EVENT_METHODS = Collections.newArrayList( ADD_MOUSE_CLICK_ON_OBJECT_LISTENER_METHOD, ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD, ADD_DEFAULT_MODEL_MANIPULATOR );

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
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_MOUSE_CLICK_ON_OBJECT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_DEFAULT_MODEL_MANIPULATOR ) );
	}

}
