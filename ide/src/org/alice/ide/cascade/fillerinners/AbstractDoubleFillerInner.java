package org.alice.ide.cascade.fillerinners;

public abstract class AbstractDoubleFillerInner extends AbstractNumberFillerInner {
	private final double[] literals;
	public AbstractDoubleFillerInner( org.lgna.project.ast.AbstractType<?,?,?> type, double[] literals ) {
		super( type );
		this.literals = literals;
	}
	public AbstractDoubleFillerInner( Class<?> cls, double[] literals ) {
		this( org.lgna.project.ast.JavaType.getInstance( cls ), literals );
	}
	
	protected abstract org.lgna.croquet.CascadeItem getCustomItem();
	@Override
	public final java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		super.addItems( rv, isTop, prevExpression );
		for( double d : this.literals ) {
			rv.add( org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn.getInstance( d ) );
		}
		if( isTop && prevExpression != null ) {
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.number.RandomCascadeMenu.getInstance() );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.number.MathCascadeMenu.getInstance() );
		}
		rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		rv.add( this.getCustomItem() );
		return rv;
	}
}
