package org.alice.stageide.croquet.models.cascade.source;

public abstract class SourceFillIn<T extends org.lgna.common.Resource> extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< org.lgna.project.ast.InstanceCreation > {
	private final Class<?> sourceCls;
	private final T resource;
	private final Class<T> resourceCls;
	private final org.lgna.project.ast.InstanceCreation transientValue;
	public SourceFillIn( java.util.UUID id, Class<?> sourceCls, Class<T> resourceCls, T resource ) {
		super( java.util.UUID.fromString( "c5d40d9e-b7a9-45d7-8784-1a0bdfc05b90" ) );
		this.sourceCls = sourceCls;
		this.resourceCls = resourceCls;
		this.resource = resource;
		this.transientValue = this.createValue();
	}
	private final org.lgna.project.ast.InstanceCreation createValue() {
		org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( this.sourceCls, this.resourceCls );
		org.lgna.project.ast.ResourceExpression resourceExpression = new org.lgna.project.ast.ResourceExpression( this.resourceCls, this.resource );
		org.lgna.project.ast.AbstractParameter parameter0 = constructor.getRequiredParameters().get( 0 );
		org.lgna.project.ast.SimpleArgument argument0 = new org.lgna.project.ast.SimpleArgument( parameter0, resourceExpression );
		return new org.lgna.project.ast.InstanceCreation( constructor, argument0 );
	}
	@Override
	public final org.lgna.project.ast.InstanceCreation createValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.InstanceCreation,Void > step ) {
		return this.createValue();
	}
	@Override
	public final org.lgna.project.ast.InstanceCreation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.InstanceCreation,Void > step ) {
		return this.transientValue;
	}
}
