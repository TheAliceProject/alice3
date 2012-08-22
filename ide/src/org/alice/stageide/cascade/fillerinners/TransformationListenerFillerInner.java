package org.alice.stageide.cascade.fillerinners;

public class TransformationListenerFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public TransformationListenerFillerInner() {
		super( org.lgna.story.event.PointOfViewChangeListener.class );
	}

	@Override
	public void appendItems( java.util.List<org.lgna.croquet.CascadeBlankChild> items, org.lgna.project.annotations.ValueDetails<?> details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		items.add( org.alice.stageide.croquet.models.cascade.adapters.TransformationEventAdapterFillIn.getInstance() );
	}
}
