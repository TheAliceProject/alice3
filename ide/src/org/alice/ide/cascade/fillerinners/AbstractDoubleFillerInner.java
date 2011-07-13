package org.alice.ide.cascade.fillerinners;

public abstract class AbstractDoubleFillerInner extends AbstractNumberFillerInner {
	private final double[] literals;
	public AbstractDoubleFillerInner( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, double[] literals ) {
		super( type );
		this.literals = literals;
	}
	public AbstractDoubleFillerInner( Class<?> cls, double[] literals ) {
		this( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ), literals );
	}
	
	protected abstract org.lgna.croquet.CascadeItem getCustomItem();
	@Override
	public final java.util.List< org.lgna.croquet.CascadeBlankChild > addItems( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, boolean isTop, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression ) {
		super.addItems( rv, isTop, prevExpression );
		for( double d : this.literals ) {
			rv.add( org.alice.ide.croquet.models.cascade.literals.DoubleLiteralFillIn.getInstance( d ) );
		}
		if( isTop && prevExpression != null ) {
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.integer.RandomCascadeMenu.getInstance() );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.number.MathCascadeMenu.getInstance() );
		}
		rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		rv.add( this.getCustomItem() );
		return rv;
	}
}
