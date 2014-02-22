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
package org.alice.ide.declarationseditor.code.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCodeDeclarationView extends org.alice.ide.declarationseditor.components.DeclarationView {
	public AbstractCodeDeclarationView( org.alice.ide.declarationseditor.CodeComposite composite, org.alice.ide.codedrop.CodePanelWithDropReceptor codePanelWithDropReceptor ) {
		super( composite );
		this.codePanelWithDropReceptor = codePanelWithDropReceptor;
		this.javaCodeView = new org.alice.ide.javacode.croquet.views.JavaCodeView( composite.getDeclaration() );
		this.sideBySideScrollPane.setBackgroundColor( this.getBackgroundColor() );
		this.sideBySideScrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 0, 0, 0 ) );

		org.lgna.project.ast.AbstractCode code = composite.getDeclaration();

		org.lgna.croquet.views.SwingComponentView<?> controlFlowComponent;
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			controlFlowComponent = org.alice.ide.controlflow.ControlFlowComposite.getInstance( code ).getView();
		} else {
			controlFlowComponent = null;
		}

		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( method.isFunction() ) {
				this.userFunctionStatusComposite = new org.alice.ide.code.UserFunctionStatusComposite( method );
			} else {
				this.userFunctionStatusComposite = null;
			}
		} else {
			this.userFunctionStatusComposite = null;
		}

		org.lgna.croquet.views.SwingComponentView<?> pageEndComponent;
		if( this.userFunctionStatusComposite != null ) {
			if( controlFlowComponent != null ) {
				pageEndComponent = new org.lgna.croquet.views.BorderPanel.Builder()
						.center( this.userFunctionStatusComposite.getView() )
						.pageEnd( controlFlowComponent )
						.build();
			} else {
				pageEndComponent = this.userFunctionStatusComposite.getView();
			}
		} else {
			if( controlFlowComponent != null ) {
				pageEndComponent = controlFlowComponent;
			} else {
				pageEndComponent = null;
			}
		}

		if( pageEndComponent != null ) {
			this.addPageEndComponent( pageEndComponent );
		}
		this.setBackgroundColor( this.codePanelWithDropReceptor.getBackgroundColor() );
		this.handleAstChangeThatCouldBeOfInterest();
	}

	@Deprecated
	public final org.alice.ide.codedrop.CodePanelWithDropReceptor getCodePanelWithDropReceptor() {
		return this.codePanelWithDropReceptor;
	}

	@Override
	public java.awt.print.Printable getPrintable() {
		return this.getCodePanelWithDropReceptor().getPrintable();
	}

	@Override
	public void addPotentialDropReceptors( java.util.List<org.lgna.croquet.DropReceptor> out, org.alice.ide.croquet.models.IdeDragModel dragModel ) {
		if( dragModel instanceof org.alice.ide.ast.draganddrop.CodeDragModel ) {
			org.alice.ide.ast.draganddrop.CodeDragModel codeDragModel = (org.alice.ide.ast.draganddrop.CodeDragModel)dragModel;
			final org.lgna.project.ast.AbstractType<?, ?, ?> type = codeDragModel.getType();
			if( type == org.lgna.project.ast.JavaType.VOID_TYPE ) {
				//pass
			} else {
				java.util.List<org.alice.ide.codeeditor.ExpressionPropertyDropDownPane> list = org.lgna.croquet.views.HierarchyUtilities.findAllMatches( this, org.alice.ide.codeeditor.ExpressionPropertyDropDownPane.class, new edu.cmu.cs.dennisc.pattern.Criterion<org.alice.ide.codeeditor.ExpressionPropertyDropDownPane>() {
					public boolean accept( org.alice.ide.codeeditor.ExpressionPropertyDropDownPane expressionPropertyDropDownPane ) {
						org.lgna.project.ast.AbstractType<?, ?, ?> expressionType = expressionPropertyDropDownPane.getExpressionProperty().getExpressionType();
						assert expressionType != null : expressionPropertyDropDownPane.getExpressionProperty();
						if( expressionType.isAssignableFrom( type ) ) {
							return true;
							//						} else {
							//							if( type.isArray() ) {
							//								if( expressionType.isAssignableFrom( type.getComponentType() ) ) {
							//									return true;
							//								} else {
							//									for( org.lgna.project.ast.JavaType integerType : org.lgna.project.ast.JavaType.INTEGER_TYPES ) {
							//										if( expressionType == integerType ) {
							//											return true;
							//										}
							//									}
							//								}
							//							}
						}
						return false;
					}
				} );
				for( org.alice.ide.codeeditor.ExpressionPropertyDropDownPane pane : list ) {
					out.add( pane.getDropReceptor() );
				}
			}
			org.alice.ide.codedrop.CodePanelWithDropReceptor codePanelWithDropReceptor = this.getCodePanelWithDropReceptor();
			org.lgna.croquet.DropReceptor dropReceptor = codePanelWithDropReceptor.getDropReceptor();
			if( dropReceptor.isPotentiallyAcceptingOf( codeDragModel ) ) {
				out.add( dropReceptor );
			}
		}
	}

	public void handleAstChangeThatCouldBeOfInterest() {
		org.lgna.project.ast.AbstractCode code = ( (org.alice.ide.declarationseditor.CodeComposite)this.getComposite() ).getDeclaration();
		if( this.userFunctionStatusComposite != null ) {
			org.lgna.croquet.AbstractSeverityStatusComposite.ErrorStatus prevErrorStatus = this.userFunctionStatusComposite.getErrorStatus();

			org.lgna.croquet.AbstractSeverityStatusComposite.ErrorStatus nextErrorStatus;
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( org.lgna.project.ast.StaticAnalysisUtilities.containsUnreachableCode( method ) ) {
				nextErrorStatus = this.userFunctionStatusComposite.getUnreachableCodeError();
			} else {
				if( org.lgna.project.ast.StaticAnalysisUtilities.containsAtLeastOneEnabledReturnStatement( method ) ) {
					if( org.lgna.project.ast.StaticAnalysisUtilities.containsAReturnForEveryPath( method ) ) {
						nextErrorStatus = null;
					} else {
						nextErrorStatus = this.userFunctionStatusComposite.getNotAllPathsEndInReturnStatementError();
					}
				} else {
					nextErrorStatus = this.userFunctionStatusComposite.getNoReturnStatementError();
				}
			}
			if( prevErrorStatus != nextErrorStatus ) {
				this.userFunctionStatusComposite.setErrorStatus( nextErrorStatus );
				this.revalidateAndRepaint();
			}

		}
	}

	@Override
	protected void handleDisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsJavaCodeOnTheSideState.getInstance().addAndInvokeNewSchoolValueListener( this.isJavaCodeOnTheSideListener );
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsJavaCodeOnTheSideState.getInstance().removeNewSchoolValueListener( this.isJavaCodeOnTheSideListener );
	}

	public org.lgna.croquet.views.SideBySideScrollPane getSideBySideScrollPane() {
		return this.sideBySideScrollPane;
	}

	protected abstract org.lgna.croquet.views.AwtComponentView<?> getMainComponent();

	private final org.alice.ide.code.UserFunctionStatusComposite userFunctionStatusComposite;

	protected void setJavaCodeOnTheSide( boolean value, boolean isFirstTime ) {
		org.lgna.croquet.views.AwtComponentView<?> mainComponent = this.getMainComponent();
		if( value ) {
			if( isFirstTime ) {
				//pass
			} else {
				this.removeComponent( mainComponent );
			}
			this.sideBySideScrollPane.setLeadingView( mainComponent );
			this.sideBySideScrollPane.setTrailingView( this.javaCodeView );
			this.addCenterComponent( sideBySideScrollPane );
		} else {
			if( isFirstTime ) {
				//pass
			} else {
				this.removeComponent( this.sideBySideScrollPane );
			}
			this.sideBySideScrollPane.setLeadingView( null );
			this.sideBySideScrollPane.setTrailingView( null );
			this.addCenterComponent( mainComponent );
		}
		this.codePanelWithDropReceptor.setJavaCodeOnTheSide( value, isFirstTime );
	}

	private final org.lgna.croquet.event.ValueListener<Boolean> isJavaCodeOnTheSideListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			synchronized( getTreeLock() ) {
				boolean isFirstTime = getCenterComponent() == null;
				setJavaCodeOnTheSide( e.getNextValue(), isFirstTime );
				revalidateAndRepaint();
			}
		}
	};

	private final org.lgna.croquet.views.SideBySideScrollPane sideBySideScrollPane = new org.lgna.croquet.views.SideBySideScrollPane();
	private final org.alice.ide.codedrop.CodePanelWithDropReceptor codePanelWithDropReceptor;
	private final org.alice.ide.javacode.croquet.views.JavaCodeView javaCodeView;

}
