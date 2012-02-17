package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
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
import org.lgna.story.Entity;
import org.lgna.story.Scene;
import org.lgna.story.event.EndCollisionListener;
import org.lgna.story.event.EndOcclusionListener;
import org.lgna.story.event.EnterProximityListener;
import org.lgna.story.event.EnterViewListener;
import org.lgna.story.event.ExitProximityListener;
import org.lgna.story.event.ExitViewListener;
import org.lgna.story.event.StartCollisionListener;
import org.lgna.story.event.StartOcclusionListener;
import org.lgna.story.event.TransformationListener;

public class TransformationEventListenerMenu extends CascadeMenuModel< org.lgna.project.ast.MethodInvocation > {
	
	public static final JavaMethod ADD_TRANSFORMATION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addPositionOrientationChangeListener", TransformationListener.class, Entity[].class, AddPositionOrientationChangeListener.Detail[].class );
	public static final JavaMethod ADD_START_COLLISION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addStartCollisionListener", StartCollisionListener.class, Entity[].class, Entity[].class, AddStartCollisionListener.Detail[].class );
	public static final JavaMethod ADD_END_COLLISION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addEndCollisionListener", EndCollisionListener.class, Entity[].class, Entity[].class, AddEndCollisionListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addEnterProximityEventListener", EnterProximityListener.class, Entity[].class, Entity[].class, Double.class, AddEnterProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addExitProximityEventListener", ExitProximityListener.class, Entity[].class, Entity[].class, Double.class, AddExitProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_ENTER_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addEnterViewListener", EnterViewListener.class, Entity[].class, AddEnterViewListener.Detail[].class );
	public static final JavaMethod ADD_EXIT_VIEW_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addExitViewListener", ExitViewListener.class, Entity[].class, AddExitViewListener.Detail[].class );
	public static final JavaMethod ADD_START_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addStartOcclusionListener", StartOcclusionListener.class, Entity[].class, Entity[].class, AddStartOcclusionListener.Detail[].class );
	public static final JavaMethod ADD_END_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addEndOcclusionListener", EndOcclusionListener.class, Entity[].class, Entity[].class, AddEndOcclusionListener.Detail[].class );
	
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
	protected List<CascadeBlankChild> updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<MethodInvocation> blankNode) {
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_TRANSFORMATION_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_START_COLLISION_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_END_COLLISION_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_ENTER_PROXIMITY_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_EXIT_PROXIMITY_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_ENTER_VIEW_EVENT_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_EXIT_VIEW_EVENT_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_START_OCCLUSION_EVENT_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_END_OCCLUSION_EVENT_LISTENER_METHOD ) );
		return rv;
	}

}
