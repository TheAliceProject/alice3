/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.declarationseditor.code.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCodeDeclarationView extends org.alice.ide.declarationseditor.components.DeclarationView {
	public AbstractCodeDeclarationView( org.alice.ide.declarationseditor.CodeComposite composite, org.alice.ide.codedrop.CodePanelWithDropReceptor codePanelWithDropReceptor ) {
		super( composite );
		this.codePanelWithDropReceptor = codePanelWithDropReceptor;

		org.lgna.project.ast.AbstractCode code = composite.getDeclaration();

		org.lgna.croquet.views.SwingComponentView<?> controlFlowComponent;
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			controlFlowComponent = org.alice.ide.controlflow.ControlFlowComposite.getInstance( code ).getView();
		} else {
			controlFlowComponent = null;
		}

		org.alice.ide.code.UserFunctionStatusComposite userFunctionStatusComposite = composite.getUserFunctionStatusComposite();
		org.lgna.croquet.views.SwingComponentView<?> pageEndComponent;
		if( userFunctionStatusComposite != null ) {
			if( controlFlowComponent != null ) {
				pageEndComponent = new org.lgna.croquet.views.BorderPanel.Builder()
						.center( userFunctionStatusComposite.getView() )
						.pageEnd( controlFlowComponent )
						.build();
			} else {
				pageEndComponent = userFunctionStatusComposite.getView();
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
					@Override
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

	@Override
	protected void setJavaCodeOnTheSide( boolean value, boolean isFirstTime ) {
		super.setJavaCodeOnTheSide( value, isFirstTime );
		this.codePanelWithDropReceptor.setJavaCodeOnTheSide( value, isFirstTime );
	}

	private final org.alice.ide.codedrop.CodePanelWithDropReceptor codePanelWithDropReceptor;
}
