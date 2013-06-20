package org.alice.ide.declarationseditor.events;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddEndCollisionListener;
import org.lgna.story.AddEndOcclusionListener;
import org.lgna.story.AddEnterProximityEventListener;
import org.lgna.story.AddEnterViewListener;
import org.lgna.story.AddExitProximityEventListener;
import org.lgna.story.AddExitViewListener;
import org.lgna.story.AddPositionOrientationChangeListener;
import org.lgna.story.AddStartCollisionListener;
import org.lgna.story.AddStartOcclusionListener;
import org.lgna.story.SModel;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;

public class TransformationEventListenerMenu extends EventListenerMenuModel {
	public static final JavaMethod ADD_TRANSFORMATION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addPointOfViewChangeListener",
			PointOfViewChangeListener.class, SThing[].class, AddPositionOrientationChangeListener.Detail[].class );
	public static final JavaMethod ADD_START_COLLISION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addCollisionStartListener",
			CollisionStartListener.class, SThing[].class, SThing[].class, AddStartCollisionListener.Detail[].class );
	public static final JavaMethod ADD_END_COLLISION_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addCollisionEndListener",
			CollisionEndListener.class, SThing[].class, SThing[].class, AddEndCollisionListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addProximityEnterListener",
			ProximityEnterListener.class, SThing[].class, SThing[].class, Double.class, AddEnterProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addProximityExitListener",
			ProximityExitListener.class, SThing[].class, SThing[].class, Double.class, AddExitProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addViewEnterListener",
			ViewEnterListener.class, SModel[].class, AddEnterViewListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addViewExitListener",
			ViewExitListener.class, SModel[].class, AddExitViewListener.Detail[].class );
	public static final JavaMethod ADD_START_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addOcclusionStartListener",
			OcclusionStartListener.class, SModel[].class, SModel[].class, AddStartOcclusionListener.Detail[].class );
	public static final JavaMethod ADD_END_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance(
			SScene.class,
			"addOcclusionEndListener",
			OcclusionEndListener.class, SModel[].class, SModel[].class, AddEndOcclusionListener.Detail[].class );

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
	protected void updateBlankChildren( java.util.List<CascadeBlankChild> blankChildren, BlankNode<MethodInvocation> blankNode ) {
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_START_COLLISION_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_END_COLLISION_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_ENTER_PROXIMITY_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_EXIT_PROXIMITY_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_ENTER_VIEW_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_EXIT_VIEW_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_START_OCCLUSION_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_END_OCCLUSION_EVENT_LISTENER_METHOD ) );
		blankChildren.add( AddEventListenerMethodInvocationFillIn.getInstance( ADD_TRANSFORMATION_LISTENER_METHOD ) );
	}

}
