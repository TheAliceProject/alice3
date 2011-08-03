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
	private final org.alice.ide.cascade.fillerinners.BooleanFillerInner booleanFillerInner = new org.alice.ide.cascade.fillerinners.BooleanFillerInner();
	private final java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > expressionFillerInners = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final org.alice.ide.cascade.fillerinners.AssumingStringConcatenationObjectFillerInner assumingStringConcatenationObjectFillerInner = new org.alice.ide.cascade.fillerinners.AssumingStringConcatenationObjectFillerInner();

	public CascadeManager() {
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.DoubleFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.IntegerFillerInner() );
		this.addExpressionFillerInner( this.booleanFillerInner );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.StringFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.AudioResourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.ImageResourceFillerInner() );
	}

	public void addRelationalTypeToBooleanFillerInner( org.lgna.project.ast.AbstractType< ?, ?, ? > operandType ) {
		this.booleanFillerInner.addRelationalType( operandType );
	}
	public void addRelationalTypeToBooleanFillerInner( Class<?> operandCls ) {
		addRelationalTypeToBooleanFillerInner( org.lgna.project.ast.JavaType.getInstance( operandCls ) );
	}
	private org.lgna.project.ast.Expression previousExpression = null;
	private org.alice.ide.codeeditor.BlockStatementIndexPair contextBlockStatementIndexPair = null;

	public void pushContext( org.lgna.project.ast.Expression previousExpression, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		this.previousExpression = previousExpression;
		this.contextBlockStatementIndexPair = blockStatementIndexPair;
	}
	public void popContext() {
		this.previousExpression = null;
		this.contextBlockStatementIndexPair = null;
	}
	
	protected void addExpressionFillerInner( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner ) {
		this.expressionFillerInners.add( expressionFillerInner );
	}

	//todo: remove this
	@Deprecated
	protected org.lgna.project.ast.AbstractType<?,?,?> getActualTypeForDesiredParameterType( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		return type;
	}

	private java.util.LinkedList< org.lgna.project.ast.UserLocal > updateAccessibleLocalsForBlockStatementAndIndex( java.util.LinkedList< org.lgna.project.ast.UserLocal > rv, org.lgna.project.ast.BlockStatement blockStatement, int index ) {
		while( index >= 1 ) {
			index--;
			org.lgna.project.ast.Statement statementI = blockStatement.statements.get( index );
			if( statementI instanceof org.lgna.project.ast.LocalDeclarationStatement ) {
				org.lgna.project.ast.LocalDeclarationStatement localDeclarationStatement = (org.lgna.project.ast.LocalDeclarationStatement)statementI;
				rv.add( localDeclarationStatement.getLocal() );
			}
		}
		return rv;
	}
	private java.util.LinkedList< org.lgna.project.ast.UserLocal > updateAccessibleLocals( java.util.LinkedList< org.lgna.project.ast.UserLocal > rv, org.lgna.project.ast.Statement statement ) {
		org.lgna.project.ast.Node parent = statement.getParent();
		if( parent instanceof org.lgna.project.ast.BooleanExpressionBodyPair ) {
			parent = parent.getParent();
		}
		if( parent instanceof org.lgna.project.ast.Statement ) {
			org.lgna.project.ast.Statement statementParent = (org.lgna.project.ast.Statement)parent;
			if( statementParent instanceof org.lgna.project.ast.BlockStatement ) {
				org.lgna.project.ast.BlockStatement blockStatementParent = (org.lgna.project.ast.BlockStatement)statementParent;
				int index = blockStatementParent.statements.indexOf( statement );
				this.updateAccessibleLocalsForBlockStatementAndIndex(rv, blockStatementParent, index);
			} else if( statementParent instanceof org.lgna.project.ast.CountLoop ) {
				org.lgna.project.ast.CountLoop countLoopParent = (org.lgna.project.ast.CountLoop)statementParent;
				boolean areCountLoopLocalsViewable = org.alice.ide.IDE.getActiveInstance().isJava();
				if( areCountLoopLocalsViewable ) {
					rv.add( countLoopParent.variable.getValue() );
					rv.add( countLoopParent.constant.getValue() );
				}
			} else if( statementParent instanceof org.lgna.project.ast.AbstractForEachLoop ) {
				org.lgna.project.ast.AbstractForEachLoop forEachLoopParent = (org.lgna.project.ast.AbstractForEachLoop)statementParent;
				rv.add( forEachLoopParent.variable.getValue() );
			} else if( statementParent instanceof org.lgna.project.ast.AbstractEachInTogether ) {
				org.lgna.project.ast.AbstractEachInTogether eachInTogetherParent = (org.lgna.project.ast.AbstractEachInTogether)statementParent;
				rv.add( eachInTogetherParent.variable.getValue() );
			}
			updateAccessibleLocals( rv, statementParent );
		}
		return rv;
	}

	private Iterable< org.lgna.project.ast.UserLocal > getAccessibleLocals( org.lgna.project.ast.BlockStatement blockStatement, int index ) {
		java.util.LinkedList< org.lgna.project.ast.UserLocal > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updateAccessibleLocalsForBlockStatementAndIndex( rv, blockStatement, index );
		updateAccessibleLocals( rv, blockStatement );
		return rv;
	}

	protected org.lgna.croquet.CascadeBlankChild createBlankChildForFillInAndPossiblyPartFillIns( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractType<?,?,?> type, org.lgna.project.ast.AbstractType<?,?,?> type2 ) {
		return new org.alice.ide.croquet.models.cascade.SimpleExpressionFillIn< org.lgna.project.ast.Expression >( expression );
	}
	protected final java.util.List< org.lgna.croquet.CascadeBlankChild > addFillInAndPossiblyPartFillIns(  java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractType<?,?,?> type, org.lgna.project.ast.AbstractType<?,?,?> type2 ) {
		rv.add( this.createBlankChildForFillInAndPossiblyPartFillIns( expression, type, type2 ) );
		return rv;
	}
//	protected java.util.List< org.lgna.croquet.CascadeBlankChild > addFillInAndPossiblyPartFillIns(  java.util.List< org.lgna.croquet.CascadeBlankChild > rv, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type2 ) {
//		rv.add( new org.alice.ide.croquet.models.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.Expression >( expression ) );
//		return rv;
//	}

	protected java.util.List< org.lgna.croquet.CascadeBlankChild > addExpressionBonusFillInsForType( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode, org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		org.lgna.project.ast.AbstractCode codeInFocus = org.alice.ide.IDE.getActiveInstance().getFocusedCode();
		if( codeInFocus != null ) {

			java.util.List< org.alice.ide.croquet.models.cascade.array.ArrayLengthFillIn > arrayLengthFillIns;
			if( type.isAssignableFrom( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ) && blankNode.isTop() ) {
				arrayLengthFillIns = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			} else {
				arrayLengthFillIns = null;
			}
			
			//todo: fix
			type = this.getActualTypeForDesiredParameterType( type );

			org.lgna.project.ast.AbstractType<?,?,?> selectedType = org.alice.ide.IDE.getActiveInstance().getTypeInScope();
			if( type.isAssignableFrom( selectedType ) ) {
				this.addFillInAndPossiblyPartFillIns( rv, new org.lgna.project.ast.ThisExpression(), selectedType, type );
			}
			for( org.lgna.project.ast.AbstractField field : selectedType.getDeclaredFields() ) {
				org.lgna.project.ast.AbstractType<?,?,?> fieldType = field.getValueType();
				if( type.isAssignableFrom( fieldType ) ) {
					org.lgna.project.ast.Expression fieldAccess = new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
					this.addFillInAndPossiblyPartFillIns( rv, fieldAccess, fieldType, type );
				}
				if( fieldType.isArray() ) {
					org.lgna.project.ast.AbstractType<?,?,?> fieldComponentType = fieldType.getComponentType();
					if( type.isAssignableFrom( fieldComponentType ) ) {
						org.lgna.project.ast.Expression fieldAccess = new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
						rv.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( fieldAccess ) );
					}
					if( arrayLengthFillIns != null ) {
						arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.ThisFieldArrayLengthFillIn.getInstance( field ) );
					}
				}
			}
			if( this.contextBlockStatementIndexPair != null ) {
				for( org.lgna.project.ast.AbstractParameter parameter : codeInFocus.getParameters() ) {
					org.lgna.project.ast.AbstractType<?,?,?> parameterType = parameter.getValueType();
					if( type.isAssignableFrom( parameterType ) ) {
						this.addFillInAndPossiblyPartFillIns( rv, new org.lgna.project.ast.ParameterAccess( parameter ), parameter.getValueType(), type );
					}
					if( parameterType.isArray() ) {
						org.lgna.project.ast.AbstractType<?,?,?> parameterArrayComponentType = parameterType.getComponentType();
						if( type.isAssignableFrom( parameterArrayComponentType ) ) {
							rv.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( new org.lgna.project.ast.ParameterAccess( parameter ) ) );
						}
						if( arrayLengthFillIns != null ) {
							arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.ParameterArrayLengthFillIn.getInstance( parameter ) );
						}
					}
				}
				for( org.lgna.project.ast.UserLocal local : this.getAccessibleLocals( this.contextBlockStatementIndexPair.getBlockStatement(), this.contextBlockStatementIndexPair.getIndex() ) ) {
					org.lgna.project.ast.AbstractType<?,?,?> localType = local.getValueType();
					if( type.isAssignableFrom( localType ) ) {
						org.lgna.project.ast.Expression expression;
						if( local instanceof org.lgna.project.ast.UserVariable ) {
							org.lgna.project.ast.UserVariable variable = (org.lgna.project.ast.UserVariable)local;
							expression = new org.lgna.project.ast.VariableAccess( variable );
						} else if( local instanceof org.lgna.project.ast.UserConstant ) {
							org.lgna.project.ast.UserConstant constant = (org.lgna.project.ast.UserConstant)local;
							expression = new org.lgna.project.ast.ConstantAccess( constant );
						} else {
							expression = null;
						}
						if( expression != null ) {
							this.addFillInAndPossiblyPartFillIns( rv, expression, localType, type );
						}
					}
					if( localType.isArray() ) {
						org.lgna.project.ast.AbstractType<?,?,?> localArrayComponentType = localType.getComponentType();
						if( local instanceof org.lgna.project.ast.UserVariable ) {
							org.lgna.project.ast.UserVariable variable = (org.lgna.project.ast.UserVariable)local;
							if( type.isAssignableFrom( localArrayComponentType ) ) {
								rv.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( new org.lgna.project.ast.VariableAccess( variable ) ) );
							}
							if( localType.isArray() ) {
								if( arrayLengthFillIns != null ) {
									arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.VariableArrayLengthFillIn.getInstance( variable ) );
								}
							}
						} else if( local instanceof org.lgna.project.ast.UserConstant ) {
							org.lgna.project.ast.UserConstant constant = (org.lgna.project.ast.UserConstant)local;
							if( type.isAssignableFrom( localArrayComponentType ) ) {
								rv.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( new org.lgna.project.ast.ConstantAccess( constant ) ) );
							}
							if( arrayLengthFillIns != null ) {
								arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.ConstantArrayLengthFillIn.getInstance( constant ) );
							}
						}
					}
				}
			}
			
			if( arrayLengthFillIns != null ) {
				if( arrayLengthFillIns.size() > 0 ) {
					rv.add( org.alice.ide.croquet.models.cascade.array.ArrayLengthSeparator.getInstance() );
					rv.addAll( arrayLengthFillIns );
				}
			}
		}
		return rv;
	}
	public org.lgna.project.ast.Expression getPreviousExpression() {
		return this.previousExpression;
	}
	public boolean isPreviousExpressionSet() {
		return this.previousExpression != null;
	}
	public org.lgna.project.ast.Expression createCopyOfPreviousExpression() {
		if( this.previousExpression != null ) {
			return org.alice.ide.IDE.getActiveInstance().createCopy( this.previousExpression );
		} else {
			return null;
		}
	}
	protected org.lgna.project.ast.AbstractType<?,?,?> getEnumTypeForInterfaceType( org.lgna.project.ast.AbstractType<?,?,?> interfaceType ) {
		return null;
	}
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > addCustomFillIns( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> step, org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		return rv;
	}

	protected org.lgna.project.ast.AbstractType<?,?,?> getTypeFor( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		if( type == org.lgna.project.ast.JavaType.getInstance( Number.class ) ) {
			return org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE;
		} else {
			return type;
		}
	}

	protected boolean areEnumConstantsDesired( org.lgna.project.ast.AbstractType<?,?,?> enumType ) {
		return true;
	}
	
	public java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode, org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		if( type != null ) {
//			org.lgna.croquet.steps.Transaction transaction = step.getParent();
//			org.lgna.croquet.steps.TransactionHistory transactionHistory = transaction.getParent();
//			edu.cmu.cs.dennisc.croquet.ModelContext<?> parent = step.getParent();
//			boolean isRoot = parent instanceof edu.cmu.cs.dennisc.croquet.CascadeRootContext;
			boolean isRoot = blankNode.isTop();
			if( isRoot && this.isPreviousExpressionSet() ) {
				rv.add( org.alice.ide.croquet.models.cascade.PreviousExpressionItselfFillIn.getInstance( type ) );
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			}
			this.addCustomFillIns( rv, blankNode, type );
			type = getTypeFor( type );
			if( type == org.lgna.project.ast.JavaType.getInstance( Object.class ) ) {
				this.assumingStringConcatenationObjectFillerInner.addItems( rv, isRoot, this.previousExpression );
			} else {
				for( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner : this.expressionFillerInners ) {
					if( expressionFillerInner.isAssignableTo( type ) ) {
						expressionFillerInner.addItems( rv, isRoot, this.previousExpression );
					}
				}
			}

			org.lgna.project.ast.AbstractType<?,?,?> enumType;
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
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
				org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner.getInstance( enumType ).addItems( rv, isRoot, this.previousExpression );
			}

			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			this.addExpressionBonusFillInsForType( rv, blankNode, type );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			if( type.isArray() ) {
				rv.add( org.alice.ide.croquet.models.custom.CustomArrayInputDialogOperation.getInstance( type.getComponentType() ).getFillIn() );
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
	public java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode, Class<?> cls ) {
		return this.updateChildren( rv, blankNode, org.lgna.project.ast.JavaType.getInstance( cls ) );
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
