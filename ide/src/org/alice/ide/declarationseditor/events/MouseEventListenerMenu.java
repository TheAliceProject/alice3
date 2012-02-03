package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddMouseButtonListener;
import org.lgna.story.Scene;
import org.lgna.story.event.MouseClickListener;
import org.lgna.story.event.MouseClickOnScreenListener;

public class MouseEventListenerMenu extends CascadeMenuModel< org.lgna.project.ast.MethodInvocation > {	

	public static final JavaMethod ADD_MOUSE_BUTTON_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addMouseButtonListener", MouseClickListener.class, AddMouseButtonListener.Detail[].class );
	public static final JavaMethod ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addMouseClickOnScreenListener", MouseClickOnScreenListener.class, AddMouseButtonListener.Detail[].class );
	public static final JavaMethod ADD_MOUSE_CLICK_ON_OBJECT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addMouseButtonListener", MouseClickListener.class, AddMouseButtonListener.Detail[].class );

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
	protected List<CascadeBlankChild> updateBlankChildren(
			List<CascadeBlankChild> rv, BlankNode<MethodInvocation> blankNode) {
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_MOUSE_BUTTON_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_MOUSE_CLICK_ON_SCREEN_LISTENER_METHOD ) );
		return rv;
	}

}
