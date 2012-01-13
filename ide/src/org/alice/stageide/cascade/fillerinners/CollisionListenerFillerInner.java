package org.alice.stageide.cascade.fillerinners;

public class CollisionListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public CollisionListenerFillerInner() {
		super( org.lgna.story.event.CollisionListener.class );
	}
	@Override
	public java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		rv.add( org.alice.stageide.croquet.models.cascade.adapters.CollisionEventAdapterFillIn.getInstance() );
		return rv;
	}
}
