package org.alice.stageide.cascade.fillerinners;

public class EndOcclusionEventListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public EndOcclusionEventListenerFillerInner() {
		super( org.lgna.story.event.OcclusionEndListener.class );
	}
	@Override
	public void appendItems( java.util.List< org.lgna.croquet.CascadeBlankChild > items, org.lgna.project.annotations.ValueDetails< ? > details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		items.add( org.alice.stageide.croquet.models.cascade.adapters.EndOcclusionEventListenerFillIn.getInstance() );
	}
}

