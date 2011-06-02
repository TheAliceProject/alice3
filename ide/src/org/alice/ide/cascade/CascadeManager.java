/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeManager {
	private java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > expressionFillerInners = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

	public CascadeManager() {
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.NumberFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.IntegerFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.BooleanFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.StringFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.AudioResourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.ImageResourceFillerInner() );
	}

	private edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = null;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement dropParent = null;
	private int dropIndex = -1;

	public void pushContext( edu.cmu.cs.dennisc.alice.ast.Expression previousExpression, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		this.previousExpression = previousExpression;
		if( blockStatementIndexPair != null ) {
			this.dropParent = blockStatementIndexPair.getBlockStatement();
			this.dropIndex = blockStatementIndexPair.getIndex();
		} else {
			this.dropParent = null;
			this.dropIndex = -1;
		}
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "pushContext", previousExpression, blockStatementIndexPair );
	}
	public void popContext() {
		this.previousExpression = null;
		this.dropParent = null;
		this.dropIndex = -1;
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "popContext" );
	}
	
	protected void addExpressionFillerInner( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner ) {
		this.expressionFillerInners.add( expressionFillerInner );
	}

//	@Deprecated
//	public edu.cmu.cs.dennisc.cascade.FillIn createExpressionsFillIn( final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types, final boolean isArrayLengthDesired ) {
//		edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression[] > rv = new edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
//			@Override
//			protected void addChildren() {
//				int N = types.length;
//				int i = 0;
//				for( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type : types ) {
//					this.addBlank( new org.alice.ide.cascade.ExpressionBlank( type, i == N - 1 && isArrayLengthDesired ) );
//					i++;
//				}
//			}
//			@Override
//			public edu.cmu.cs.dennisc.alice.ast.Expression[] getValue() {
//				edu.cmu.cs.dennisc.alice.ast.Expression[] rv = new edu.cmu.cs.dennisc.alice.ast.Expression[ this.getChildren().size() ];
//				int i = 0;
//				for( edu.cmu.cs.dennisc.cascade.Node child : this.getChildren() ) {
//					rv[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((edu.cmu.cs.dennisc.cascade.Blank)child).getSelectedFillIn().getValue();
//					i++;
//				}
//				return rv;
//			}
//			@Override
//			public edu.cmu.cs.dennisc.alice.ast.Expression[] getTransientValue() {
//				edu.cmu.cs.dennisc.alice.ast.Expression[] rv = new edu.cmu.cs.dennisc.alice.ast.Expression[ this.getChildren().size() ];
//				int i = 0;
//				for( edu.cmu.cs.dennisc.cascade.Node child : this.getChildren() ) {
//					rv[ i ] = (edu.cmu.cs.dennisc.alice.ast.Expression)((edu.cmu.cs.dennisc.cascade.Blank)child).getSelectedFillIn().getTransientValue();
//					i++;
//				}
//				return rv;
//			}
//		};
//		return rv;
//	}
	
	//todo: remove this
	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getActualTypeForDesiredParameterType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		return type;
	}
	protected void addFillInAndPossiblyPartFillIns( org.lgna.croquet.CascadeBlank blank, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type2 ) {
		System.err.println( "todo: addFillInAndPossiblyPartFillIns" );
		//rv.add( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.Expression >( expression ) );
	}

	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > updateAccessibleLocalsForBlockStatementAndIndex( java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > rv, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index ) {
		while( index >= 1 ) {
			index--;
			edu.cmu.cs.dennisc.alice.ast.Statement statementI = blockStatement.statements.get( index );
			if( statementI instanceof edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement ) {
				edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement localDeclarationStatement = (edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement)statementI;
				rv.add( localDeclarationStatement.getLocal() );
			}
		}
		return rv;
	}
	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > updateAccessibleLocals( java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > rv, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		edu.cmu.cs.dennisc.alice.ast.Node parent = statement.getParent();
		if( parent instanceof edu.cmu.cs.dennisc.alice.ast.BooleanExpressionBodyPair ) {
			parent = parent.getParent();
		}
		if( parent instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
			edu.cmu.cs.dennisc.alice.ast.Statement statementParent = (edu.cmu.cs.dennisc.alice.ast.Statement)parent;
			if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
				edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatementParent = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)statementParent;
				int index = blockStatementParent.statements.indexOf( statement );
				this.updateAccessibleLocalsForBlockStatementAndIndex(rv, blockStatementParent, index);
			} else if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.CountLoop ) {
				edu.cmu.cs.dennisc.alice.ast.CountLoop countLoopParent = (edu.cmu.cs.dennisc.alice.ast.CountLoop)statementParent;
				boolean areCountLoopLocalsViewable = org.alice.ide.IDE.getSingleton().isJava();
				if( areCountLoopLocalsViewable ) {
					rv.add( countLoopParent.variable.getValue() );
					rv.add( countLoopParent.constant.getValue() );
				}
			} else if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop forEachLoopParent = (edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop)statementParent;
				rv.add( forEachLoopParent.variable.getValue() );
			} else if( statementParent instanceof edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether eachInTogetherParent = (edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether)statementParent;
				rv.add( eachInTogetherParent.variable.getValue() );
			}
			updateAccessibleLocals( rv, statementParent );
		}
		return rv;
	}

	private Iterable< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > getAccessibleLocals( edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index ) {
		java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updateAccessibleLocalsForBlockStatementAndIndex( rv, blockStatement, index );
		updateAccessibleLocals( rv, blockStatement );
		return rv;
	}
	protected java.util.List< org.lgna.croquet.CascadeItem > addExpressionBonusFillInsForType( java.util.List< org.lgna.croquet.CascadeItem > rv, org.lgna.croquet.steps.CascadeBlankStep<edu.cmu.cs.dennisc.alice.ast.Expression> step, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type ) {
//		edu.cmu.cs.dennisc.alice.ast.AbstractCode codeInFocus = org.alice.ide.IDE.getSingleton().getFocusedCode();
//		if( codeInFocus != null ) {
//
//			//todo: fix
//			type = this.getActualTypeForDesiredParameterType( type );
//
//			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> selectedType = org.alice.ide.IDE.getSingleton().getTypeInScope();
//			//boolean isNecessary = true;
//			if( type.isAssignableFrom( selectedType ) ) {
//				//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
//				this.addFillInAndPossiblyPartFillIns( blank, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), selectedType, type );
//			}
//			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : selectedType.getDeclaredFields() ) {
//				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> fieldType = field.getValueType();
//				if( type.isAssignableFrom( fieldType ) ) {
//					//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
//					edu.cmu.cs.dennisc.alice.ast.Expression fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
//					this.addFillInAndPossiblyPartFillIns( blank, fieldAccess, fieldType, type );
//				}
//				if( fieldType.isArray() ) {
//					edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> fieldComponentType = fieldType.getComponentType();
//					if( type.isAssignableFrom( fieldComponentType ) ) {
//						//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
//						edu.cmu.cs.dennisc.alice.ast.Expression fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
//						//rv.add( new ArrayAccessFillIn( fieldType, fieldAccess ) );
//					}
//					if( type.isAssignableFrom( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) ) {
//						//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
//						edu.cmu.cs.dennisc.alice.ast.Expression fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), field );
//						edu.cmu.cs.dennisc.alice.ast.ArrayLength arrayLength = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( fieldAccess );
//						rv.add( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.ArrayLength >( arrayLength ) );
//					}
//				}
//			}
////			edu.cmu.cs.dennisc.alice.ast.Expression prevExpression = this.getPreviousExpression();
////			if( prevExpression != null ) {
////				edu.cmu.cs.dennisc.alice.ast.Statement statement = prevExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.Statement.class );
//				if( this.dropParent != null && this.dropIndex != -1 ) {
//					for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : codeInFocus.getParameters() ) {
//						if( type.isAssignableFrom( parameter.getValueType() ) ) {
//							//isNecessary = this.addSeparatorIfNecessary( blank, "in scope", isNecessary );
//							this.addFillInAndPossiblyPartFillIns( blank, new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter ), parameter.getValueType(), type );
//						}
//					}
//					for( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local : this.getAccessibleLocals( this.dropParent, this.dropIndex ) ) {
//						if( type.isAssignableFrom( local.valueType.getValue() ) ) {
//							edu.cmu.cs.dennisc.alice.ast.Expression expression;
//							if( local instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
//								edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)local;
//								expression = new edu.cmu.cs.dennisc.alice.ast.VariableAccess( variable );
//							} else if( local instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
//								edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant = (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)local;
//								expression = new edu.cmu.cs.dennisc.alice.ast.ConstantAccess( constant );
//							} else {
//								expression = null;
//							}
//							if( expression != null ) {
//								this.addFillInAndPossiblyPartFillIns( blank, expression, local.valueType.getValue(), type );
//							}
//						}
//					}
////				}
//			}
//		}
		return rv;
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		return this.previousExpression;
	}
	public boolean isPreviousExpressionSet() {
		return this.previousExpression != null;
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression createCopyOfPreviousExpression() {
		if( this.previousExpression != null ) {
			return org.alice.ide.IDE.getSingleton().createCopy( this.previousExpression );
		} else {
			return null;
		}
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getEnumTypeForInterfaceType( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> interfaceType ) {
		return null;
	}
	protected java.util.List< org.lgna.croquet.CascadeItem > addFillInsForObjectType( java.util.List< org.lgna.croquet.CascadeItem > rv, org.lgna.croquet.steps.CascadeBlankStep<edu.cmu.cs.dennisc.alice.ast.Expression> step ) {
		rv.add( org.alice.ide.croquet.models.custom.CustomStringInputDialogOperation.getInstance().getFillIn() );
		rv.add( org.alice.ide.croquet.models.custom.CustomDoubleInputDialogOperation.getInstance().getFillIn() );
		rv.add( org.alice.ide.croquet.models.custom.CustomIntegerInputDialogOperation.getInstance().getFillIn() );
		rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
//		if( blank.isTop() ) {
//			//pass
//		} else {
//			if( previousExpression == null || previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.NullLiteral ) {
//				//pass
//			} else {
		
				rv.add( org.alice.ide.croquet.models.cascade.string.StringConcatinationRightOperandOnlyFillIn.getInstance() );
				rv.add( org.alice.ide.croquet.models.cascade.string.StringConcatinationLeftAndRightOperandsFillIn.getInstance() );
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
//			}
//		}
		return rv;
	}
	protected java.util.List< org.lgna.croquet.CascadeItem > addCustomFillIns( java.util.List< org.lgna.croquet.CascadeItem > rv, org.lgna.croquet.steps.CascadeBlankStep<edu.cmu.cs.dennisc.alice.ast.Expression> step, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type ) {
		return rv;
	}

	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getTypeFor( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Number.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE;
		} else {
			return type;
		}
	}

	protected boolean areEnumConstantsDesired( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> enumType ) {
		return true;
	}
	
	public java.util.List< org.lgna.croquet.CascadeItem > updateChildren( java.util.List< org.lgna.croquet.CascadeItem > rv, org.lgna.croquet.steps.CascadeBlankStep<edu.cmu.cs.dennisc.alice.ast.Expression> step, edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type ) {
		if( type != null ) {
//			org.lgna.croquet.steps.Transaction transaction = step.getParent();
//			org.lgna.croquet.steps.TransactionHistory transactionHistory = transaction.getParent();
//			edu.cmu.cs.dennisc.croquet.ModelContext<?> parent = step.getParent();
//			boolean isRoot = parent instanceof edu.cmu.cs.dennisc.croquet.CascadeRootContext;
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo updateChildren isRoot" );
			boolean isRoot = true;

			if( isRoot && this.isPreviousExpressionSet() ) {
				rv.add( org.alice.ide.croquet.models.cascade.PreviousExpressionItselfFillIn.getInstance( type ) );
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			}
			this.addCustomFillIns( rv, step, type );
			type = getTypeFor( type );
			if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) ) {
				this.addFillInsForObjectType( rv, step );
			} else {
				for( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner : this.expressionFillerInners ) {
					if( expressionFillerInner.isAssignableTo( type ) ) {
						expressionFillerInner.addItems( rv, isRoot, this.previousExpression );
					}
				}
			}

			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> enumType;
			if( type.isInterface() ) {
				enumType = this.getEnumTypeForInterfaceType( type );
			} else {
				if( type.isAssignableTo( Enum.class ) ) {
					enumType = type;
				} else {
					enumType = null;
				}
			}
			if( enumType != null && this.areEnumConstantsDesired( enumType ) ) {
				org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner.getInstance( enumType ).addItems( rv, isRoot, this.previousExpression );
			}

			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			this.addExpressionBonusFillInsForType( rv, step, type );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			if( type.isArray() ) {
				//rv.add( new org.alice.ide.cascade.customfillin.CustomArrayFillIn() );
			}

//			if( blank.isEmpty() ) {
//				rv.add( org.alice.ide.croquet.models.cascade.NoFillInsFoundCancelFillIn.getInstance() );
//			}
		} else {
			//todo:
//			rv.add( org.alice.ide.croquet.models.cascade.TypeUnsetCancelFillIn.getInstance() );
		}
		return rv;
	}
	
//	@Deprecated
//	private void promptUserForExpressions( edu.cmu.cs.dennisc.alice.ast.BlockStatement dropParent, int dropIndex, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types, boolean isArrayLengthDesired, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > taskObserver ) {
//		this.dropParent = dropParent;
//		this.dropIndex = dropIndex;
//		edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression[] > fillIn = createExpressionsFillIn( types, isArrayLengthDesired );
//		java.util.List< edu.cmu.cs.dennisc.cascade.Node > children = fillIn.getChildren();
//		if( children.size() == 1 ) {
//			edu.cmu.cs.dennisc.cascade.Blank blank0 = fillIn.getBlankAt( 0 );
//			edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression > selectedFillIn = (edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.Expression >)blank0.getSelectedFillIn();
//			if( selectedFillIn != null ) {
//				taskObserver.handleCompletion( new edu.cmu.cs.dennisc.alice.ast.Expression[] { selectedFillIn.getValue() } );
//				//note: return
//				return;
//			}
//		}
//		
//		throw new RuntimeException( "todo" );
//		//fillIn.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
//	}
}
