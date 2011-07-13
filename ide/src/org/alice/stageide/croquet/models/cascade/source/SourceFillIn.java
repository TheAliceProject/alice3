package org.alice.stageide.croquet.models.cascade.source;

public abstract class SourceFillIn<T extends org.alice.virtualmachine.Resource> extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< edu.cmu.cs.dennisc.alice.ast.InstanceCreation > {
	private final Class<?> sourceCls;
	private final T resource;
	private final Class<T> resourceCls;
	private final edu.cmu.cs.dennisc.alice.ast.InstanceCreation transientValue;
	public SourceFillIn( java.util.UUID id, Class<?> sourceCls, Class<T> resourceCls, T resource ) {
		super( java.util.UUID.fromString( "c5d40d9e-b7a9-45d7-8784-1a0bdfc05b90" ) );
		this.sourceCls = sourceCls;
		this.resourceCls = resourceCls;
		this.resource = resource;
		this.transientValue = this.createValue();
	}
	private final edu.cmu.cs.dennisc.alice.ast.InstanceCreation createValue() {
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( this.sourceCls, this.resourceCls );
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( this.resourceCls, this.resource );
		edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter0 = constructor.getParameters().get( 0 );
		edu.cmu.cs.dennisc.alice.ast.Argument argument0 = new edu.cmu.cs.dennisc.alice.ast.Argument( parameter0, resourceExpression );
		return new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor, argument0 );
	}
	@Override
	public final edu.cmu.cs.dennisc.alice.ast.InstanceCreation createValue( org.lgna.croquet.cascade.ItemNode< ? super edu.cmu.cs.dennisc.alice.ast.InstanceCreation,Void > step ) {
		return this.createValue();
	}
	@Override
	public final edu.cmu.cs.dennisc.alice.ast.InstanceCreation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super edu.cmu.cs.dennisc.alice.ast.InstanceCreation,Void > step ) {
		return this.transientValue;
	}
}
