package org.alice.stageide.apis.story.event;

import org.lgna.project.ast.Lambda;
import org.lgna.project.virtualmachine.LambdaContext;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.event.EndOcclusionListener;

import edu.cmu.cs.dennisc.matt.EndOcclusionEvent;

public class EndOcclusionEventAdapter extends AbstractAdapter implements EndOcclusionListener {

	public EndOcclusionEventAdapter(LambdaContext context, Lambda lambda, UserInstance userInstance) {
		super(context, lambda, userInstance);
	}

	public void theseNoLongerOcclude(EndOcclusionEvent e) {
		invokeEntryPoint( e );
	}
}
