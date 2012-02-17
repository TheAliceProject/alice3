package org.alice.stageide.cascade.fillerinners;

import java.util.List;

import org.alice.ide.cascade.fillerinners.ExpressionFillerInner;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.Expression;

public class EndCollisionListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public EndCollisionListenerFillerInner() {
		super( org.lgna.story.event.EndCollisionListener.class );
	}
	@Override
	public java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		rv.add( org.alice.stageide.croquet.models.cascade.adapters.EndCollisionEventAdapterFillIn.getInstance() );
		return rv;
	}
}
