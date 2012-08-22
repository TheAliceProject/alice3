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
public abstract class ExpressionCascadeManager {
	private final org.alice.ide.cascade.fillerinners.BooleanFillerInner booleanFillerInner = new org.alice.ide.cascade.fillerinners.BooleanFillerInner();
	private final java.util.List<org.alice.ide.cascade.fillerinners.ExpressionFillerInner> expressionFillerInners = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final org.alice.ide.cascade.fillerinners.AssumingStringConcatenationObjectFillerInner assumingStringConcatenationObjectFillerInner = new org.alice.ide.cascade.fillerinners.AssumingStringConcatenationObjectFillerInner();

	private final java.util.Stack<ExpressionCascadeContext> contextStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	public ExpressionCascadeManager() {
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.DoubleFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.IntegerFillerInner() );
		this.addExpressionFillerInner( this.booleanFillerInner );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.StringFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.AudioResourceFillerInner() );
		this.addExpressionFillerInner( new org.alice.ide.cascade.fillerinners.ImageResourceFillerInner() );
	}

	public void addRelationalTypeToBooleanFillerInner( org.lgna.project.ast.AbstractType<?, ?, ?> operandType ) {
		this.booleanFillerInner.addRelationalType( operandType );
	}

	public void addRelationalTypeToBooleanFillerInner( Class<?> operandCls ) {
		addRelationalTypeToBooleanFillerInner( org.lgna.project.ast.JavaType.getInstance( operandCls ) );
	}

	protected void addExpressionFillerInner( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner ) {
		this.expressionFillerInners.add( expressionFillerInner );
	}

	private final ExpressionCascadeContext NULL_CONTEXT = new ExpressionCascadeContext() {
		public org.lgna.project.ast.Expression getPreviousExpression() {
			return null;
		}

		public org.alice.ide.ast.draganddrop.BlockStatementIndexPair getBlockStatementIndexPair() {
			return null;
		}
	};

	private ExpressionCascadeContext safePeekContext() {
		if( this.contextStack.size() > 0 ) {
			return this.contextStack.peek();
		} else {
			return NULL_CONTEXT;
		}
	}

	public void pushContext( ExpressionCascadeContext context ) {
		assert context != null;
		if( this.contextStack.size() > 0 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.contextStack );
		}
		this.contextStack.push( context );
	}

	public ExpressionCascadeContext popContext() {
		if( this.contextStack.size() > 0 ) {
			return this.contextStack.pop();
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
			//todo?
			return NULL_CONTEXT;
		}
	}

	public ExpressionCascadeContext popAndCheckContext( ExpressionCascadeContext expectedContext ) {
		ExpressionCascadeContext poppedContext = this.popContext();
		if( poppedContext != expectedContext ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( poppedContext, expectedContext );
		}
		return poppedContext;
	}

	public org.lgna.project.ast.Expression getPreviousExpression() {
		return this.safePeekContext().getPreviousExpression();
	}

	private java.util.LinkedList<org.lgna.project.ast.UserLocal> updateAccessibleLocalsForBlockStatementAndIndex( java.util.LinkedList<org.lgna.project.ast.UserLocal> rv, org.lgna.project.ast.BlockStatement blockStatement, int index ) {
		while( index >= 1 ) {
			index--;
			//todo: investigate
			if( index >= blockStatement.statements.size() ) {
				try {
					throw new IndexOutOfBoundsException( index + " " + blockStatement.statements.size() );
				} catch( IndexOutOfBoundsException ioobe ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( ioobe );
				}
				index = blockStatement.statements.size();
			} else {
				org.lgna.project.ast.Statement statementI = blockStatement.statements.get( index );
				if( statementI instanceof org.lgna.project.ast.LocalDeclarationStatement ) {
					org.lgna.project.ast.LocalDeclarationStatement localDeclarationStatement = (org.lgna.project.ast.LocalDeclarationStatement)statementI;
					rv.add( localDeclarationStatement.local.getValue() );
				}
			}
		}
		return rv;
	}

	private java.util.LinkedList<org.lgna.project.ast.UserLocal> updateAccessibleLocals( java.util.LinkedList<org.lgna.project.ast.UserLocal> rv, org.lgna.project.ast.Statement statement ) {
		org.lgna.project.ast.Node parent = statement.getParent();
		if( parent instanceof org.lgna.project.ast.BooleanExpressionBodyPair ) {
			parent = parent.getParent();
		}
		if( parent instanceof org.lgna.project.ast.Statement ) {
			org.lgna.project.ast.Statement statementParent = (org.lgna.project.ast.Statement)parent;
			if( statementParent instanceof org.lgna.project.ast.BlockStatement ) {
				org.lgna.project.ast.BlockStatement blockStatementParent = (org.lgna.project.ast.BlockStatement)statementParent;
				int index = blockStatementParent.statements.indexOf( statement );
				this.updateAccessibleLocalsForBlockStatementAndIndex( rv, blockStatementParent, index );
			} else if( statementParent instanceof org.lgna.project.ast.CountLoop ) {
				org.lgna.project.ast.CountLoop countLoopParent = (org.lgna.project.ast.CountLoop)statementParent;
				boolean areCountLoopLocalsViewable = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.isJava();
				if( areCountLoopLocalsViewable ) {
					rv.add( countLoopParent.variable.getValue() );
					rv.add( countLoopParent.constant.getValue() );
				}
			} else if( statementParent instanceof org.lgna.project.ast.AbstractForEachLoop ) {
				org.lgna.project.ast.AbstractForEachLoop forEachLoopParent = (org.lgna.project.ast.AbstractForEachLoop)statementParent;
				rv.add( forEachLoopParent.item.getValue() );
			} else if( statementParent instanceof org.lgna.project.ast.AbstractEachInTogether ) {
				org.lgna.project.ast.AbstractEachInTogether eachInTogetherParent = (org.lgna.project.ast.AbstractEachInTogether)statementParent;
				rv.add( eachInTogetherParent.item.getValue() );
			}
			updateAccessibleLocals( rv, statementParent );
		}
		return rv;
	}

	private Iterable<org.lgna.project.ast.UserLocal> getAccessibleLocals( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		java.util.LinkedList<org.lgna.project.ast.UserLocal> rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updateAccessibleLocalsForBlockStatementAndIndex( rv, blockStatementIndexPair.getBlockStatement(), blockStatementIndexPair.getIndex() );
		updateAccessibleLocals( rv, blockStatementIndexPair.getBlockStatement() );
		return rv;
	}

	protected final boolean isApplicableForFillIn( org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType ) {
		return desiredType.isAssignableFrom( expressionType );
	}

	protected abstract boolean isApplicableForPartFillIn( org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType );

	protected final boolean isApplicableForFillInAndPossiblyPartFillIns( org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType ) {
		return this.isApplicableForFillIn( desiredType, expressionType ) || this.isApplicableForPartFillIn( desiredType, expressionType );
	}

	protected abstract org.lgna.croquet.CascadeMenuModel<org.lgna.project.ast.Expression> createPartMenuModel( org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType, boolean isOwnedByCascadeItemMenuCombo );

	private void appendFillInAndPossiblyPartFillIns( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.Expression expression, org.lgna.project.ast.AbstractType<?, ?, ?> expressionType ) {
		org.lgna.croquet.CascadeBlankChild blankChild;
		org.lgna.croquet.CascadeFillIn<org.lgna.project.ast.Expression, ?> expressionFillIn;
		if( this.isApplicableForFillIn( desiredType, expressionType ) ) {
			expressionFillIn = new org.alice.ide.croquet.models.cascade.SimpleExpressionFillIn<org.lgna.project.ast.Expression>( expression );
		} else {
			expressionFillIn = null;
		}
		if( this.isApplicableForPartFillIn( desiredType, expressionType ) ) {
			org.lgna.croquet.CascadeMenuModel<org.lgna.project.ast.Expression> menuModel = this.createPartMenuModel( expression, desiredType, expressionType, expressionFillIn != null );
			if( menuModel != null ) {
				if( expressionFillIn != null ) {
					blankChild = new org.lgna.croquet.CascadeItemMenuCombo( expressionFillIn, menuModel );
				} else {
					blankChild = menuModel;
				}
			} else {
				blankChild = expressionFillIn;
			}
		} else {
			if( expressionFillIn != null ) {
				blankChild = expressionFillIn;
			} else {
				blankChild = null;
			}
		}
		if( blankChild != null ) {
			blankChildren.add( blankChild );
		}
	}

	private void appendFillInAndPossiblyPartFillIns( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.project.ast.AbstractType<?, ?, ?> desiredType, org.lgna.project.ast.Expression expression ) {
		this.appendFillInAndPossiblyPartFillIns( blankChildren, desiredType, expression, expression.getType() );
	}

	private void appendExpressionBonusFillInsForType( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode, org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		org.lgna.project.ast.Expression prevExpression = this.safePeekContext().getPreviousExpression();
		org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair = this.safePeekContext().getBlockStatementIndexPair();
		java.util.List<org.alice.ide.croquet.models.cascade.array.ArrayLengthFillIn> arrayLengthFillIns;
		if( blankNode.isTop() ) {
			if( ( type == org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ) || ( type.isAssignableFrom( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ) && ( prevExpression != null ) ) ) {
				arrayLengthFillIns = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			} else {
				arrayLengthFillIns = null;
			}
		} else {
			arrayLengthFillIns = null;
		}

		org.lgna.project.ast.AbstractType<?, ?, ?> selectedType = org.alice.ide.declarationseditor.TypeState.getInstance().getValue();
		if( this.isApplicableForFillInAndPossiblyPartFillIns( type, selectedType ) ) {
			this.appendFillInAndPossiblyPartFillIns( blankChildren, type, new org.lgna.project.ast.ThisExpression(), selectedType );
		}

		for( org.lgna.project.ast.AbstractField field : selectedType.getDeclaredFields() ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> fieldType = field.getValueType();
			if( this.isApplicableForFillInAndPossiblyPartFillIns( type, fieldType ) ) {
				org.lgna.project.ast.Expression fieldAccess = new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
				this.appendFillInAndPossiblyPartFillIns( blankChildren, type, fieldAccess );
			}
			if( fieldType.isArray() ) {
				org.lgna.project.ast.AbstractType<?, ?, ?> fieldComponentType = fieldType.getComponentType();
				if( this.isApplicableForFillInAndPossiblyPartFillIns( type, fieldComponentType ) ) {
					org.lgna.project.ast.Expression fieldAccess = new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.ThisExpression(), field );
					blankChildren.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( fieldAccess ) );
				}
				if( arrayLengthFillIns != null ) {
					arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.ThisFieldArrayLengthFillIn.getInstance( field ) );
				}
			}
		}

		org.lgna.project.ast.AbstractCode codeInFocus = org.alice.ide.IDE.getActiveInstance().getFocusedCode();
		if( codeInFocus != null ) {
			for( org.lgna.project.ast.AbstractParameter parameter : codeInFocus.getRequiredParameters() ) {
				org.lgna.project.ast.AbstractType<?, ?, ?> parameterType = parameter.getValueType();
				if( this.isApplicableForFillInAndPossiblyPartFillIns( type, parameterType ) ) {
					this.appendFillInAndPossiblyPartFillIns( blankChildren, type, new org.lgna.project.ast.ParameterAccess( parameter ) );
				}
				if( parameterType.isArray() ) {
					org.lgna.project.ast.AbstractType<?, ?, ?> parameterArrayComponentType = parameterType.getComponentType();
					if( this.isApplicableForFillInAndPossiblyPartFillIns( type, parameterArrayComponentType ) ) {
						blankChildren.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( new org.lgna.project.ast.ParameterAccess( parameter ) ) );
					}
					if( arrayLengthFillIns != null ) {
						arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.ParameterArrayLengthFillIn.getInstance( parameter ) );
					}
				}
			}
			if( blockStatementIndexPair != null ) {
				for( org.lgna.project.ast.UserLocal local : this.getAccessibleLocals( blockStatementIndexPair ) ) {
					org.lgna.project.ast.AbstractType<?, ?, ?> localType = local.getValueType();
					if( this.isApplicableForFillInAndPossiblyPartFillIns( type, localType ) ) {
						this.appendFillInAndPossiblyPartFillIns( blankChildren, type, new org.lgna.project.ast.LocalAccess( local ) );
					}
					if( localType.isArray() ) {
						org.lgna.project.ast.AbstractType<?, ?, ?> localArrayComponentType = localType.getComponentType();
						if( this.isApplicableForFillInAndPossiblyPartFillIns( type, localArrayComponentType ) ) {
							blankChildren.add( new org.alice.ide.croquet.models.cascade.array.ArrayAccessFillIn( new org.lgna.project.ast.LocalAccess( local ) ) );
						}
						if( localType.isArray() ) {
							if( arrayLengthFillIns != null ) {
								arrayLengthFillIns.add( org.alice.ide.croquet.models.cascade.array.LocalArrayLengthFillIn.getInstance( local ) );
							}
						}
					}
				}
			}
		}
		if( arrayLengthFillIns != null ) {
			if( arrayLengthFillIns.size() > 0 ) {
				blankChildren.add( org.alice.ide.croquet.models.cascade.array.ArrayLengthSeparator.getInstance() );
				blankChildren.addAll( arrayLengthFillIns );
			}
		}
	}

	protected org.lgna.project.ast.AbstractType<?, ?, ?> getEnumTypeForInterfaceType( org.lgna.project.ast.AbstractType<?, ?, ?> interfaceType ) {
		return null;
	}

	protected java.util.List<org.lgna.croquet.CascadeBlankChild> addCustomFillIns( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> step, org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return rv;
	}

	protected org.lgna.project.ast.AbstractType<?, ?, ?> getTypeFor( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type == org.lgna.project.ast.JavaType.getInstance( Number.class ) ) {
			return org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE;
		} else {
			return type;
		}
	}

	protected boolean areEnumConstantsDesired( org.lgna.project.ast.AbstractType<?, ?, ?> enumType ) {
		return true;
	}

	public void appendItems( java.util.List<org.lgna.croquet.CascadeBlankChild> items, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode, org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.annotations.ValueDetails<?> details ) {
		if( type != null ) {
			boolean isRoot = blankNode.isTop();

			ExpressionCascadeContext context = this.safePeekContext();

			org.lgna.project.ast.Expression prevExpression = context.getPreviousExpression();
			if( isRoot && ( prevExpression != null ) ) {
				if( type.isAssignableFrom( prevExpression.getType() ) ) {
					items.add( org.alice.ide.croquet.models.cascade.PreviousExpressionItselfFillIn.getInstance( type ) );
					items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( prevExpression );
				}
			}
			this.addCustomFillIns( items, blankNode, type );
			type = getTypeFor( type );
			if( type == org.lgna.project.ast.JavaType.getInstance( Object.class ) ) {
				this.assumingStringConcatenationObjectFillerInner.appendItems( items, details, isRoot, prevExpression );
			} else {
				for( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner : this.expressionFillerInners ) {
					if( expressionFillerInner.isAssignableTo( type ) ) {
						expressionFillerInner.appendItems( items, details, isRoot, prevExpression );
						items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
					}
				}
			}

			org.lgna.project.ast.AbstractType<?, ?, ?> enumType;
			if( type.isInterface() ) {
				enumType = this.getEnumTypeForInterfaceType( type );
			} else {
				if( type.isAssignableTo( Enum.class ) ) {
					enumType = type;
				} else {
					enumType = null;
				}
			}
			if( ( enumType != null ) && this.areEnumConstantsDesired( enumType ) ) {
				items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
				org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner.getInstance( enumType ).appendItems( items, details, isRoot, prevExpression );
			}

			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			this.appendExpressionBonusFillInsForType( items, blankNode, type );
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			if( type.isArray() ) {
				items.add( org.alice.ide.croquet.models.custom.CustomArrayInputDialogOperation.getInstance( type.getComponentType() ).getFillIn() );
				//rv.add( org.alice.ide.custom.ArrayCustomExpressionCreatorComposite.getInstance( type ).getValueCreator().getFillIn() );
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "type is null" );
		}
	}
}
