package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddEndCollisionListener;
import org.lgna.story.AddEnterProximityEventListener;
import org.lgna.story.AddEnterViewListener;
import org.lgna.story.AddExitProximityEventListener;
import org.lgna.story.AddExitViewListener;
import org.lgna.story.AddPositionOrientationChangeListener;
import org.lgna.story.AddStartCollisionListener;
import org.lgna.story.Entity;
import org.lgna.story.Scene;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;

public class TransformationEventListenerMenu extends EventListenerMenuModel {

	public static final JavaMethod ADD_TRANSFORMATION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addPointOfViewChangeListener", PointOfViewChangeListener.class, Entity[].class, AddPositionOrientationChangeListener.Detail[].class );
	public static final JavaMethod ADD_START_COLLISION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addCollisionStartListener", CollisionStartListener.class, Class.class, Class.class, AddStartCollisionListener.Detail[].class );
	public static final JavaMethod ADD_END_COLLISION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addCollisionEndListener", CollisionEndListener.class, Class.class, Class.class, AddEndCollisionListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addProximityEnterListener", ProximityEnterListener.class, Class.class, Class.class, Double.class, AddEnterProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addProximityExitListener", ProximityExitListener.class, Class.class, Class.class, Double.class, AddExitProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addViewEnterListener", ViewEnterListener.class, Class.class, AddEnterViewListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addViewExitListener", ViewExitListener.class, Class.class, AddExitViewListener.Detail[].class );

	//	public static final JavaMethod ADD_START_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addOcclusionStartListener", OcclusionStartListener.class, Model[].class, Model[].class, AddStartOcclusionListener.Detail[].class );
	//	public static final JavaMethod ADD_END_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addOcclusionEndListener", OcclusionEndListener.class, Model[].class, Model[].class, AddEndOcclusionListener.Detail[].class );

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
	protected List<CascadeBlankChild> updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<MethodInvocation> blankNode ) {
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_START_COLLISION_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_END_COLLISION_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_ENTER_PROXIMITY_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_EXIT_PROXIMITY_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_ENTER_VIEW_EVENT_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_EXIT_VIEW_EVENT_LISTENER_METHOD ) );
		//		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_START_OCCLUSION_EVENT_LISTENER_METHOD ) );
		//		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_END_OCCLUSION_EVENT_LISTENER_METHOD ) );
		rv.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_TRANSFORMATION_LISTENER_METHOD ) );
		return rv;
	}

}
