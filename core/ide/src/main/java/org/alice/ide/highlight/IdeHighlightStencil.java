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
package org.alice.ide.highlight;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.codeeditor.ExpressionPropertyDropDownPane;
import org.alice.ide.common.AbstractStatementPane;
import org.alice.ide.common.FieldDeclarationPane;
import org.alice.ide.croquet.components.ExpressionDropDown;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.alice.ide.declarationseditor.type.components.TypeDeclarationView;
import org.alice.ide.perspectives.ProjectPerspective;
import org.alice.ide.x.components.AbstractExpressionView;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.Model;
import org.lgna.croquet.resolvers.RuntimeResolver;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.ComponentManager;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class IdeHighlightStencil extends HighlightStencil {
	public IdeHighlightStencil( AbstractWindow<?> window, Integer layerId ) {
		super( window, layerId );
	}

	public void showHighlightOverField( final UserField field, String noteText ) {
		if( field != null ) {
			this.show( new RuntimeResolver<TrackableShape>() {
				@Override
				public TrackableShape getResolved() {
					DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						DeclarationView view = declarationComposite.getView();
						if( view instanceof TypeDeclarationView ) {
							List<JPanel> jPanels = ComponentUtilities.findAllMatches( view.getAwtComponent(), JPanel.class );
							for( JPanel jPanel : jPanels ) {
								AwtComponentView<?> component = AwtComponentView.lookup( jPanel );
								if( component instanceof FieldDeclarationPane ) {
									FieldDeclarationPane fieldDeclarationPane = (FieldDeclarationPane)component;
									UserField candidate = fieldDeclarationPane.getField();
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
			Logger.severe( "field is null", noteText );
		}
	}

	public void showHighlightOverExpression( final Expression expression, String noteText ) {
		if( expression != null ) {
			this.show( new RuntimeResolver<TrackableShape>() {
				@Override
				public TrackableShape getResolved() {
					DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						DeclarationView view = declarationComposite.getView();

						List<AbstractButton> jButtons = ComponentUtilities.findAllMatches( view.getAwtComponent(), AbstractButton.class );
						for( AbstractButton jButton : jButtons ) {
							Expression candidate = null;
							AwtComponentView<?> component = AwtComponentView.lookup( jButton );
							if( component instanceof ExpressionPropertyDropDownPane ) {
								ExpressionPropertyDropDownPane expressionPropertyDropDownPane = (ExpressionPropertyDropDownPane)component;
								candidate = expressionPropertyDropDownPane.getExpressionProperty().getValue();
							} else if( component instanceof ExpressionDropDown ) {
								ExpressionDropDown<Expression> expressionDropDown = (ExpressionDropDown<Expression>)component;
								CompletionModel completionModel = expressionDropDown.getModel().getCascadeRoot().getCompletionModel();
								if( completionModel instanceof CustomItemState ) {
									CustomItemState<Expression> state = (CustomItemState<Expression>)completionModel;
									candidate = state.getValue();
								}
							} else if( component instanceof AbstractExpressionView ) {
								AbstractExpressionView expressionView = (AbstractExpressionView)component;
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
			Logger.severe( noteText );
		}
	}

	public void showHighlightOverStatement( final Statement statement, String message ) {
		if( statement != null ) {
			this.show( new RuntimeResolver<TrackableShape>() {
				@Override
				public TrackableShape getResolved() {
					DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						DeclarationView view = declarationComposite.getView();
						List<JPanel> jPanels = ComponentUtilities.findAllMatches( view.getAwtComponent(), JPanel.class );
						for( JPanel jPanel : jPanels ) {
							Statement candidate = null;
							AwtComponentView<?> component = AwtComponentView.lookup( jPanel );
							if( component instanceof AbstractStatementPane ) {
								AbstractStatementPane statementPane = (AbstractStatementPane)component;
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
			Logger.severe();
		}
	}

	public void showHighlightOverCroquetViewController( final Model model, String noteText ) {
		if( model != null ) {
			this.show( new RuntimeResolver<TrackableShape>() {
				@Override
				public TrackableShape getResolved() {
					AwtComponentView<?> component = ComponentManager.getFirstComponent( model );
					if( component != null ) {
						//pass
					} else {
						Logger.errln( "cannot resolve first component for", model );
					}
					return component;
				}
			}, null, noteText );
		} else {
			Logger.severe( noteText );
		}
	}

	private TrackableShape getRenderWindow() {
		ProjectPerspective perspective = IDE.getActiveInstance().getDocumentFrame().getPerspectiveState().getValue();
		if( perspective != null ) {
			return perspective.getRenderWindow();
		} else {
			return null;
		}
	}

	public void showHighlightOverStatementAndRenderWindow( final Statement statement ) {
		if( statement != null ) {
			this.show( new RuntimeResolver<TrackableShape>() {
				@Override
				public TrackableShape getResolved() {
					DeclarationComposite<?, ?> declarationComposite = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getValue();
					if( declarationComposite != null ) {
						DeclarationView view = declarationComposite.getView();
						List<JPanel> jPanels = ComponentUtilities.findAllMatches( view.getAwtComponent(), JPanel.class );
						for( JPanel jPanel : jPanels ) {
							Statement candidate = null;
							AwtComponentView<?> component = AwtComponentView.lookup( jPanel );
							if( component instanceof AbstractStatementPane ) {
								AbstractStatementPane statementPane = (AbstractStatementPane)component;
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
			}, new RuntimeResolver<TrackableShape>() {
				@Override
				public TrackableShape getResolved() {
					return getRenderWindow();
				}
			}, "" );
		} else {
			Logger.severe();
		}
	}

	public void showHighlightOverCroquetViewControllerAndRenderWindow( final Model model ) {
		this.show( new RuntimeResolver<TrackableShape>() {
			@Override
			public TrackableShape getResolved() {
				AwtComponentView<?> component = ComponentManager.getFirstComponent( model );
				if( component != null ) {
					//pass
				} else {
					Logger.errln( "cannot resolve first component for", model );
				}
				return component;
			}
		}, new RuntimeResolver<TrackableShape>() {
			@Override
			public TrackableShape getResolved() {
				return getRenderWindow();
			}
		}, "" );
	}
}
