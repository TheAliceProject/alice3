package org.alice.stageide.cascade.fillerinners;

import java.util.List;

import org.alice.ide.cascade.fillerinners.ExpressionFillerInner;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.Expression;

public class ExitProximityEventListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public ExitProximityEventListenerFillerInner() {
		super( org.lgna.story.event.ExitProximityListener.class );
	}
	@Override
	public java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		rv.add( org.alice.stageide.croquet.models.cascade.adapters.ExitProximityEventListenerAdapterFillIn.getInstance() );
		return rv;
	}
}

