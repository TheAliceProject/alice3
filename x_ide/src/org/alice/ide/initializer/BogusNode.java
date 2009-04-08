/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.initializer;

/**
 * @author Dennis Cosgrove
 */
public class BogusNode extends edu.cmu.cs.dennisc.alice.ast.Node {
	public edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType > componentType = new edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType >( this );
	public edu.cmu.cs.dennisc.property.BooleanProperty isArray = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );
	public edu.cmu.cs.dennisc.alice.ast.ExpressionProperty componentExpression = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
		@Override
		public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
			return BogusNode.this.componentType.getValue();
		}
	};
	public edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions = new edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty( this );

	public BogusNode( edu.cmu.cs.dennisc.alice.ast.AbstractType type, boolean isArrayIfTypeIsNull ) {
		this.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				BogusNode.this.handleChanged( e );
			}
		} );
		this.isArray.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				BogusNode.this.handleChanged( e );
			}
		} );
		edu.cmu.cs.dennisc.alice.ast.AbstractType componentType;
		boolean isArray;
		if( type != null ) {
			if( type.isArray() ) {
				componentType = type.getComponentType();
				isArray = true;
			} else {
				componentType = type;
				isArray = false;
			}
		} else {
			componentType = null;
			isArray = isArrayIfTypeIsNull;
		}
		this.componentType.setValue( componentType );
		this.isArray.setValue( isArray );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression getNextExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression previousExpression ) {
		//todo: check to see if acceptable
		edu.cmu.cs.dennisc.alice.ast.Expression rv;
		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.BooleanLiteral( false );
		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 );
		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) {
			rv = new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 0 );
		} else {
			rv = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
		}
		return rv;
	}
	private void handleChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.componentType.getValue();
		if( type != null ) {
			this.componentExpression.setValue( getNextExpression( type, this.componentExpression.getValue() ) );
			final int N = this.arrayExpressions.size();
			for( int i=0; i<N; i++ ) {
				this.arrayExpressions.set( i, this.arrayExpressions.get( i ) );
			}
		}
	}
//	
//	public void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType ) {
//		this.expressionType = expressionType;
//		edu.cmu.cs.dennisc.alice.ast.Expression expression;
//		if( expressionType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) {
//			expression = new edu.cmu.cs.dennisc.alice.ast.BooleanLiteral( false );
//		} else if( expressionType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) {
//			expression = new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( 0.0 );
//		} else if( expressionType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) {
//			expression = new edu.cmu.cs.dennisc.alice.ast.IntegerLiteral( 0 );
//		} else {
//			expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
//		}
//		this.expression.setValue( expression );
//	}
//	public edu.cmu.cs.dennisc.alice.ast.Expression createCopyOfExpressionValue() {
//		edu.cmu.cs.dennisc.alice.ast.Expression src = this.expression.getValue();
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: create copy" );
//		return src;
//		//		if( src != null ) {
//		//			return (edu.cmu.cs.dennisc.alice.ast.Expression)org.alice.ide.IDE.getSingleton().createCopy( src );
//		//		} else {
//		//			return null;
//		//		}
//	}
}
