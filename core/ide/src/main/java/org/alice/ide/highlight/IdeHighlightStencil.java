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
package org.alice.ide.highlight;

/**
 * @author Dennis Cosgrove
 */
public class IdeHighlightStencil extends HighlightStencil {
	public IdeHighlightStencil( org.lgna.croquet.views.AbstractWindow<?> window, Integer layerId ) {
		super( window, layerId );
	}

	public void showHighlightOverField( final org.lgna.project.ast.UserField field, String noteText ) {
		if( field != null ) {
			this.show( new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
				@Override
				public org.lgna.croquet.views.TrackableShape getResolved() {
					org.alice.ide.declarationseditor.DeclarationComposite<?, ?> declarationComposite = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						org.alice.ide.declarationseditor.components.DeclarationView view = declarationComposite.getView();
						if( view instanceof org.alice.ide.declarationseditor.type.components.TypeDeclarationView ) {
							java.util.List<javax.swing.JPanel> jPanels = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( view.getAwtComponent(), javax.swing.JPanel.class );
							for( javax.swing.JPanel jPanel : jPanels ) {
								org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.AwtComponentView.lookup( jPanel );
								if( component instanceof org.alice.ide.common.FieldDeclarationPane ) {
									org.alice.ide.common.FieldDeclarationPane fieldDeclarationPane = (org.alice.ide.common.FieldDeclarationPane)component;
									org.lgna.project.ast.UserField candidate = fieldDeclarationPane.getField();
									if( candidate == field ) {
										return fieldDeclarationPane;
									}
								}
							}
						}
					}
					return null;
				}
			}, null, noteText );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "field is null", noteText );
		}
	}

	public void showHighlightOverExpression( final org.lgna.project.ast.Expression expression, String noteText ) {
		if( expression != null ) {
			this.show( new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
				@Override
				public org.lgna.croquet.views.TrackableShape getResolved() {
					org.alice.ide.declarationseditor.DeclarationComposite<?, ?> declarationComposite = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						org.alice.ide.declarationseditor.components.DeclarationView view = declarationComposite.getView();

						java.util.List<javax.swing.AbstractButton> jButtons = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( view.getAwtComponent(), javax.swing.AbstractButton.class );
						for( javax.swing.AbstractButton jButton : jButtons ) {
							org.lgna.project.ast.Expression candidate = null;
							org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.AwtComponentView.lookup( jButton );
							if( component instanceof org.alice.ide.codeeditor.ExpressionPropertyDropDownPane ) {
								org.alice.ide.codeeditor.ExpressionPropertyDropDownPane expressionPropertyDropDownPane = (org.alice.ide.codeeditor.ExpressionPropertyDropDownPane)component;
								candidate = expressionPropertyDropDownPane.getExpressionProperty().getValue();
							} else if( component instanceof org.alice.ide.croquet.components.ExpressionDropDown ) {
								org.alice.ide.croquet.components.ExpressionDropDown<org.lgna.project.ast.Expression> expressionDropDown = (org.alice.ide.croquet.components.ExpressionDropDown<org.lgna.project.ast.Expression>)component;
								org.lgna.croquet.CompletionModel completionModel = expressionDropDown.getModel().getCascadeRoot().getCompletionModel();
								if( completionModel instanceof org.lgna.croquet.CustomItemState ) {
									org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> state = (org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression>)completionModel;
									candidate = state.getValue();
								}
							} else if( component instanceof org.alice.ide.x.components.AbstractExpressionView ) {
								org.alice.ide.x.components.AbstractExpressionView expressionView = (org.alice.ide.x.components.AbstractExpressionView)component;
								candidate = expressionView.getExpression();
							}
							if( candidate == expression ) {
								return component;
								//							} else {
								//								edu.cmu.cs.dennisc.java.util.logging.Logger.outln( component );
							}
						}
					}
					return null;
				}
			}, null, noteText );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( noteText );
		}
	}

	public void showHighlightOverStatement( final org.lgna.project.ast.Statement statement, String message ) {
		if( statement != null ) {
			this.show( new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
				@Override
				public org.lgna.croquet.views.TrackableShape getResolved() {
					org.alice.ide.declarationseditor.DeclarationComposite<?, ?> declarationComposite = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						org.alice.ide.declarationseditor.components.DeclarationView view = declarationComposite.getView();
						java.util.List<javax.swing.JPanel> jPanels = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( view.getAwtComponent(), javax.swing.JPanel.class );
						for( javax.swing.JPanel jPanel : jPanels ) {
							org.lgna.project.ast.Statement candidate = null;
							org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.AwtComponentView.lookup( jPanel );
							if( component instanceof org.alice.ide.common.AbstractStatementPane ) {
								org.alice.ide.common.AbstractStatementPane statementPane = (org.alice.ide.common.AbstractStatementPane)component;
								candidate = statementPane.getStatement();
							}
							if( candidate == statement ) {
								return component;
								//							} else {
								//								edu.cmu.cs.dennisc.java.util.logging.Logger.outln( component );
							}
						}
					}
					return null;
				}
			}, null, message );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
		}
	}

	public void showHighlightOverCroquetViewController( final org.lgna.croquet.Model model, String noteText ) {
		if( model != null ) {
			this.show( new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
				@Override
				public org.lgna.croquet.views.TrackableShape getResolved() {
					org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.ComponentManager.getFirstComponent( model );
					if( component != null ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "cannot resolve first component for", model );
					}
					return component;
				}
			}, null, noteText );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( noteText );
		}
	}

	private org.lgna.croquet.views.TrackableShape getRenderWindow() {
		org.alice.ide.perspectives.ProjectPerspective perspective = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getPerspectiveState().getValue();
		if( perspective != null ) {
			return perspective.getRenderWindow();
		} else {
			return null;
		}
	}

	public void showHighlightOverStatementAndRenderWindow( final org.lgna.project.ast.Statement statement ) {
		if( statement != null ) {
			this.show( new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
				@Override
				public org.lgna.croquet.views.TrackableShape getResolved() {
					org.alice.ide.declarationseditor.DeclarationComposite<?, ?> declarationComposite = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						org.alice.ide.declarationseditor.components.DeclarationView view = declarationComposite.getView();
						java.util.List<javax.swing.JPanel> jPanels = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( view.getAwtComponent(), javax.swing.JPanel.class );
						for( javax.swing.JPanel jPanel : jPanels ) {
							org.lgna.project.ast.Statement candidate = null;
							org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.AwtComponentView.lookup( jPanel );
							if( component instanceof org.alice.ide.common.AbstractStatementPane ) {
								org.alice.ide.common.AbstractStatementPane statementPane = (org.alice.ide.common.AbstractStatementPane)component;
								candidate = statementPane.getStatement();
							}
							if( candidate == statement ) {
								return component;
								//							} else {
								//								edu.cmu.cs.dennisc.java.util.logging.Logger.outln( component );
							}
						}
					}
					return null;
				}
			}, new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
				@Override
				public org.lgna.croquet.views.TrackableShape getResolved() {
					return getRenderWindow();
				}
			}, "" );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
		}
	}

	public void showHighlightOverCroquetViewControllerAndRenderWindow( final org.lgna.croquet.Model model ) {
		this.show( new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
			@Override
			public org.lgna.croquet.views.TrackableShape getResolved() {
				org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.ComponentManager.getFirstComponent( model );
				if( component != null ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "cannot resolve first component for", model );
				}
				return component;
			}
		}, new org.lgna.croquet.resolvers.RuntimeResolver<org.lgna.croquet.views.TrackableShape>() {
			@Override
			public org.lgna.croquet.views.TrackableShape getResolved() {
				return getRenderWindow();
			}
		}, "" );
	}
}
