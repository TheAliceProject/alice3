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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionCreatorPane extends org.alice.ide.common.ExpressionLikeSubstance {
	@Override
	public void setActive( boolean isActive ) {
		super.setActive( isActive );
		if( isActive ) {
			getIDE().showStencilOver( this, getExpressionType() );
		} else {
			getIDE().hideStencil();
		}
	}
	
	@Override
	protected boolean isAlphaDesiredWhenOverDropReceptor() {
		return true;
	}
	protected abstract java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > getBlankExpressionTypes( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv );
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
//	@Override
	public final void createExpression( final zoot.event.DragAndDropEvent e, final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, final edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		final java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > types = getBlankExpressionTypes( new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractType >() );
		final edu.cmu.cs.dennisc.alice.ast.AbstractType thisType = this.getExpressionType();
		edu.cmu.cs.dennisc.alice.ast.AbstractType propertyType = expressionProperty.getExpressionType();
		
		final boolean[] accessible = { false, false };
		if( propertyType.isAssignableFrom( thisType ) ) {
			//pass
		} else {
			if( thisType.isArray() ) {
				if( propertyType.isAssignableFrom( thisType.getComponentType() ) ) {
					types.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE );
					accessible[ 0 ] = true;
				}
				for( edu.cmu.cs.dennisc.alice.ast.AbstractType integerType : edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_TYPES ) {
					if( propertyType == integerType ) {
						accessible[ 1 ] = true;
					}
				}
			}
		}
		if( types.size() > 0 ) {
			class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Expression[], Void > {
				@Override
				protected edu.cmu.cs.dennisc.alice.ast.Expression[] doInBackground() throws java.lang.Exception {
					edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > expressionsTaskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
						@Override
						public void run() {
							getIDE().promptUserForExpressions( edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( types, edu.cmu.cs.dennisc.alice.ast.AbstractType.class ), accessible[ 1 ], e.getEndingMouseEvent(), this );
						}
					};
					return expressionsTaskObserver.getResult();
				}
				@Override
				protected void done() {
					edu.cmu.cs.dennisc.alice.ast.Expression[] expressions = null;
					try {
						expressions = this.get();
					} catch( InterruptedException ie ) {
						throw new RuntimeException( ie );
					} catch( java.util.concurrent.ExecutionException ee ) {
						throw new RuntimeException( ee );
					} finally {
						if( expressions != null ) {
							edu.cmu.cs.dennisc.alice.ast.Expression expression = createExpression( expressions );
							if( accessible[ 0 ] ) {
								edu.cmu.cs.dennisc.alice.ast.Expression lastExpression = expressions[ expressions.length-1 ];
								if( accessible[ 1 ] && lastExpression == null ) {
									expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
								} else {
									expression = new edu.cmu.cs.dennisc.alice.ast.ArrayAccess( thisType, expression, lastExpression );
								}
							} else if( accessible[ 1 ] ) {
								expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
							}
							taskObserver.handleCompletion( expression );
						} else {
							taskObserver.handleCancelation();
						}
					}
				}
			}
			Worker worker = new Worker();
			worker.execute();
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					edu.cmu.cs.dennisc.alice.ast.Expression expression = createExpression();
					if( accessible[ 1 ] ) {
						expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
					}
					taskObserver.handleCompletion( expression );
				}
			} );
		}
	}
	@Override
	protected boolean isKnurlDesired() {
		return true;
	}
}
