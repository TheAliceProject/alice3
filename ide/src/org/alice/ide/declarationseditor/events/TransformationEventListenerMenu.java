package org.alice.ide.declarationseditor.events;

import java.util.List;

import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.cascade.BlankNode;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.story.AddProximityEventListener;
import org.lgna.story.Entity;
import org.lgna.story.Scene;
import org.lgna.story.event.CollisionListener;
import org.lgna.story.event.OcclusionEventListener;
import org.lgna.story.event.ProximityEventListener;
import org.lgna.story.event.TransformationListener;

public class TransformationEventListenerMenu extends CascadeMenuModel< org.lgna.project.ast.MethodInvocation > {
	
	public static final JavaMethod ADD_COLLISION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addCollisionListener", CollisionListener.class, Entity[].class, Entity[].class );
	public static final JavaMethod ADD_PROXIMITY_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addProximityEventListener", ProximityEventListener.class, Entity[].class, Entity[].class, AddProximityEventListener.Detail[].class );
	public static final JavaMethod ADD_TRANSFORMATION_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addTransformationListener", TransformationListener.class, Entity[].class );
	public static final JavaMethod ADD_OCCLUSION_EVENT_LISTENER_METHOD = JavaMethod.getInstance( Scene.class, "addOcclusionEventListener", OcclusionEventListener.class, Entity[].class, Entity[].class );
	
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
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_COLLISION_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_PROXIMITY_LISTENER_METHOD ) );
		rv.add(AddEventListenerMethodInvocationFillIn.getInstance( ADD_OCCLUSION_EVENT_LISTENER_METHOD ) );
		return rv;
	}

}
