package org.alice.stageide.cascade.fillerinners;

public class EndCollisionListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public EndCollisionListenerFillerInner() {
		super( org.lgna.story.event.CollisionEndListener.class );
	}
	@Override
	public void appendItems( java.util.List< org.lgna.croquet.CascadeBlankChild > items, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		items.add( org.alice.stageide.croquet.models.cascade.adapters.EndCollisionEventAdapterFillIn.getInstance() );
	}
}
