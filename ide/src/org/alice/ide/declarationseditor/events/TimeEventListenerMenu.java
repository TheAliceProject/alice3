package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddSceneActivationListener;
import org.lgna.story.AddTimeListener;
import org.lgna.story.Scene;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeListener;

public class TimeEventListenerMenu extends CascadeMenuModel<org.lgna.project.ast.MethodInvocation> {

	public static final JavaMethod ADD_SCENE_ACTIVATION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addSceneActivationListener", SceneActivationListener.class, AddSceneActivationListener.Detail[].class );
	public static final JavaMethod ADD_TIMER_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addTimeListener", TimeListener.class, AddTimeListener.Detail[].class );

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
	protected List<CascadeBlankChild> updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<MethodInvocation> blankNode ) {
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_SCENE_ACTIVATION_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_TIMER_EVENT_LISTENER_METHOD ) );
		return rv;
	}

}
