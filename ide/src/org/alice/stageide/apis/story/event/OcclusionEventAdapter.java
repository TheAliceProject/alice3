package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionEventListener;

public class OcclusionEventAdapter extends AbstractAdapter implements OcclusionEventListener {

	public OcclusionEventAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void whenTheseOcclude(OcclusionEvent e) {
		invokeEntryPoint( e );
	}
}
