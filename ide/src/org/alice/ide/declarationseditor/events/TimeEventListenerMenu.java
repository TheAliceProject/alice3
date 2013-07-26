package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddTimeListener;
import org.lgna.story.SScene;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeListener;

public class TimeEventListenerMenu extends EventListenerMenuModel {

	public static final JavaMethod ADD_SCENE_ACTIVATION_LISTENER_METHOD = JavaMethod.getInstance( SScene.class, "addSceneActivationListener", SceneActivationListener.class );
	public static final JavaMethod ADD_TIMER_EVENT_LISTENER_METHOD = JavaMethod.getInstance( SScene.class, "addTimeListener", TimeListener.class, Number.class, AddTimeListener.Detail[].class );

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
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_SCENE_ACTIVATION_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_TIMER_EVENT_LISTENER_METHOD ) );
	}

}
