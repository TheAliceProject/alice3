/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.gallerybrowser.uri.merge.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddMembersView<M extends org.lgna.project.ast.Member> extends org.lgna.croquet.components.MigPanel {
	//private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>[] NO_OP_LABEL_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.REGULAR, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };
	private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>[] NO_OP_LABEL_TEXT_ATTRIBUTES = {};

	private static java.awt.Dimension ICON_SIZE = new java.awt.Dimension( 22, 22 );
	public static javax.swing.Icon EMPTY_ICON = org.lgna.croquet.icon.EmptyIconFactory.getInstance().getIcon( ICON_SIZE );

	private static org.lgna.croquet.components.AbstractLabel createNoOpLabel( org.lgna.project.ast.Member member, String bonusText, javax.swing.Icon icon ) {
		org.lgna.croquet.components.AbstractLabel rv = new org.lgna.croquet.components.Label( member.getName() + bonusText, NO_OP_LABEL_TEXT_ATTRIBUTES );
		rv.setIcon( icon );
		return rv;
	}

	private static org.lgna.croquet.components.AbstractLabel createHeader( org.lgna.croquet.PlainStringValue stringValue ) {
		final edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>[] HEADER_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };
		org.lgna.croquet.components.AbstractLabel header = stringValue.createLabel( HEADER_TEXT_ATTRIBUTES );
		return header;
	}

	private static org.lgna.croquet.components.TextField createTextField( org.lgna.croquet.StringState state ) {
		org.lgna.croquet.components.TextField textField = state.createTextField();
		textField.enableSelectAllWhenFocusGained();
		return textField;
	}

	private static org.lgna.croquet.components.HorizontalSeparator createSeparator() {
		return new org.lgna.croquet.components.HorizontalSeparator();
	}

	private static <D extends org.lgna.project.ast.Member> org.lgna.croquet.components.PopupView createPopupView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, D> composite, D member ) {
		return composite.getPopupMemberFor( member ).getElement().createPopupView();
	}

	private static javax.swing.Icon createPlusIcon( org.lgna.croquet.BooleanState booleanState ) {
		return new org.alice.stageide.gallerybrowser.uri.merge.views.icons.SelectPlusIcon( ICON_SIZE, booleanState.getSwingModel().getButtonModel() );
	}

	private static org.lgna.croquet.components.Label createPlusIconLabel( org.lgna.croquet.BooleanState booleanState ) {
		return new org.lgna.croquet.components.Label( createPlusIcon( booleanState ) );
	}

	private int row = 0;
	private final java.util.List<java.util.List<java.awt.Component>> rows = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

	private String getTitleText( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite ) {
		return composite.getOuterComposite().getIsExpandedState().getTrueText();
	}

	private String createDifferentSignatureToolText( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, M member ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>\"" );
		sb.append( member.getName() );
		sb.append( "\" " );
		sb.append( getTitleText( composite ) );
		if( member instanceof org.lgna.project.ast.UserMethod ) {
			sb.append( " have different signatures." );
		} else {
			sb.append( " have different value classes." );
		}
		sb.append( "<p><strong>You must change at least one of their names.</strong></html>" );
		return sb.toString();
	}

	private String createIdenticalToolText( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, M member ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>\"" );
		sb.append( member.getName() );
		sb.append( "\" " );
		sb.append( getTitleText( composite ) );
		sb.append( " " );
		sb.append( " are identical.<p>No action is required.</html>" );
		return sb.toString();
	}

	public AddMembersView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, java.awt.Color backgroundColor ) {
		super( composite, "fill, insets 0", "[grow,shrink,50%]32[grow,shrink,50%]0[" + ActionRequiredView.ICON.getIconWidth() + "px,grow 0,shrink 0]" );
		//todo
		backgroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( backgroundColor, 1.0, 1.0, 1.1 );
		this.setBackgroundColor( backgroundColor );

		this.addComponent( createHeader( composite.getAddHeader() ), "" );
		this.addComponent( createHeader( composite.getResultHeader() ), "wrap" );
		this.addComponent( createSeparator(), "grow, shrink" );
		this.addComponent( createSeparator(), "grow, shrink, wrap" );

		for( org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnly<M> importOnly : composite.getImportOnlys() ) {
			this.addComponent( importOnly.getIsAddDesiredState().createCheckBox(), "" );

			org.lgna.croquet.components.AbstractLabel label = createNoOpLabel( importOnly.getImportMember(), "", createPlusIcon( importOnly.getIsAddDesiredState() ) );
			org.lgna.croquet.components.PopupView popupView = createPopupView( composite, importOnly.getImportMember() );
			this.addComponent( label, "split 2" );
			this.addComponent( popupView, "wrap" );

			//todo: removeValueListener somewhere
			importOnly.getIsAddDesiredState().addValueListener( new IsAddDesiredListener( label, popupView ) );
		}

		for( final org.alice.stageide.gallerybrowser.uri.merge.data.DifferentSignature<M> differentSignature : composite.getDifferentSignatures() ) {
			edu.cmu.cs.dennisc.pattern.Criterion<Void> isActionRequiredCriterion = new edu.cmu.cs.dennisc.pattern.Criterion<Void>() {
				public boolean accept( java.lang.Void e ) {
					return differentSignature.isActionRequired();
				}
			};
			ActionRequiredView rightBracket = new ActionRequiredView( isActionRequiredCriterion, false );
			rightBracket.setToolTipText( createDifferentSignatureToolText( composite, differentSignature.getImportMember() ) );
			this.addComponent( differentSignature.getIsAddDesiredState().createCheckBox() );

			org.lgna.croquet.components.Label plusLabel = createPlusIconLabel( differentSignature.getIsAddDesiredState() );
			org.lgna.croquet.components.TextField textField = createTextField( differentSignature.getImportNameState() );
			org.lgna.croquet.components.PopupView popupView = createPopupView( composite, differentSignature.getImportMember() );
			this.addComponent( plusLabel, "split 3" );
			this.addComponent( textField, "growx" );
			this.addComponent( popupView );
			this.addComponent( rightBracket, "grow, spany 2, wrap" );

			this.addComponent( new org.lgna.croquet.components.Label( EMPTY_ICON ), "skip 1, split 3" );
			this.addComponent( createTextField( differentSignature.getProjectNameState() ), "grow" );
			this.addComponent( createPopupView( composite, differentSignature.getProjectMember() ), "wrap" );

			//todo: removeValueListener somewhere
			differentSignature.getIsAddDesiredState().addValueListener( new IsAddDesiredListener( plusLabel, textField, popupView ) );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.data.DifferentImplementation<M> differentImplementation : composite.getDifferentImplementations() ) {
			org.lgna.croquet.ListSelectionState<org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction> courseOfActionState = differentImplementation.getCourseOfActionState();

			org.lgna.croquet.BooleanState replaceState = courseOfActionState.getItemSelectedState( org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction.REPLACE );
			org.lgna.croquet.components.RadioButton replaceRadioButton = replaceState.createRadioButton();
			this.addComponent( replaceRadioButton );
			org.lgna.croquet.components.AbstractLabel replaceLabel = createNoOpLabel( differentImplementation.getImportMember(), "", createPlusIcon( replaceState ) );
			org.lgna.croquet.components.PopupView replacePopupView = createPopupView( composite, differentImplementation.getImportMember() );
			this.addComponent( replaceLabel, "split 2" );
			this.addComponent( replacePopupView, "wrap" );

			org.lgna.croquet.BooleanState keepState = courseOfActionState.getItemSelectedState( org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction.KEEP );
			org.lgna.croquet.components.RadioButton keepRadioButton = keepState.createRadioButton();
			this.addComponent( keepRadioButton );

			org.lgna.croquet.components.AbstractLabel keepLabel = createNoOpLabel( differentImplementation.getProjectMember(), "", EMPTY_ICON );
			org.lgna.croquet.components.PopupView keepPopupView = createPopupView( composite, differentImplementation.getProjectMember() );
			this.addComponent( keepLabel, "split 2" );
			this.addComponent( keepPopupView, "wrap" );

			org.lgna.croquet.BooleanState renameState = courseOfActionState.getItemSelectedState( org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction.ADD_AND_KEEP_WITH_RENAME );
			org.lgna.croquet.components.RadioButton renameRadioButton = renameState.createRadioButton();
			this.addComponent( renameRadioButton );

			org.lgna.croquet.components.Label renameAddPlusLabel = createPlusIconLabel( renameState );
			org.lgna.croquet.components.TextField renameAddTextField = createTextField( differentImplementation.getImportNameState() );
			org.lgna.croquet.components.PopupView renameAddPopupView = createPopupView( composite, differentImplementation.getImportMember() );
			this.addComponent( renameAddPlusLabel, "split 3" );
			this.addComponent( renameAddTextField, "growx" );
			this.addComponent( renameAddPopupView, "wrap" );

			org.lgna.croquet.components.Label renameKeepPlusLabel = new org.lgna.croquet.components.Label( EMPTY_ICON );
			org.lgna.croquet.components.TextField renameKeepTextField = createTextField( differentImplementation.getProjectNameState() );
			org.lgna.croquet.components.PopupView renameKeepPopupView = createPopupView( composite, differentImplementation.getProjectMember() );
			this.addComponent( renameKeepPlusLabel, "skip 1, split 3" );
			this.addComponent( renameKeepTextField, "grow" );
			this.addComponent( renameKeepPopupView, "wrap" );

			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			buttonGroup.add( replaceRadioButton.getAwtComponent() );
			buttonGroup.add( keepRadioButton.getAwtComponent() );
			buttonGroup.add( renameRadioButton.getAwtComponent() );

			//todo: removeValueListener somewhere
			courseOfActionState.addAndInvokeValueListener( new DifferentImplementationCourseOfActionListener(
					new org.lgna.croquet.components.Component[] { replaceLabel, replacePopupView },
					new org.lgna.croquet.components.Component[] { keepLabel, keepPopupView },
					new org.lgna.croquet.components.Component[] { renameAddPlusLabel, renameAddTextField, renameAddPopupView, renameKeepPlusLabel, renameKeepTextField, renameKeepPopupView }
					) );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.data.Identical<M> identical : composite.getIdenticals() ) {
			org.lgna.croquet.components.CheckBox checkBox = identical.getIsAddDesiredState().createCheckBox();
			checkBox.setToolTipText( createIdenticalToolText( composite, identical.getImportMember() ) );
			this.addComponent( checkBox, "" );
			this.addComponent( createNoOpLabel( identical.getProjectMember(), "", EMPTY_ICON ), "split 2" );
			this.addComponent( createPopupView( composite, identical.getProjectMember() ), "wrap" );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.data.ProjectOnly<M> projectOnly : composite.getProjectOnlys() ) {
			this.addComponent( createNoOpLabel( projectOnly.getProjectMember(), "", EMPTY_ICON ), "skip 1, split 2" );
			this.addComponent( createPopupView( composite, projectOnly.getProjectMember() ), "wrap" );
		}
	}

	private void addToRow( org.lgna.croquet.components.Component<?> component ) {
		while( this.rows.size() <= this.row ) {
			this.rows.add( new java.util.LinkedList<java.awt.Component>() );
		}
		this.rows.get( this.row ).add( component.getAwtComponent() );
	}

	@Override
	public void addComponent( org.lgna.croquet.components.Component<?> component ) {
		super.addComponent( component );
		this.addToRow( component );
	}

	@Override
	public void addComponent( org.lgna.croquet.components.Component<?> component, String constraint ) {
		super.addComponent( component, constraint );
		if( constraint.contains( "spany" ) ) {
			//pass
		} else {
			this.addToRow( component );
		}
		if( constraint.contains( "wrap" ) ) {
			this.row++;
		}
	}

	private static java.awt.Rectangle getRowBounds( java.util.List<java.awt.Component> row ) {
		if( row.size() > 0 ) {
			java.awt.Rectangle rv = null;
			for( java.awt.Component awtComponent : row ) {
				java.awt.Rectangle bounds = awtComponent.getBounds();
				if( rv != null ) {
					rv = rv.union( bounds );
				} else {
					rv = bounds;
				}
			}
			return rv;
		} else {
			return new java.awt.Rectangle( 0, 0, 0, 0 );
		}
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		class JMembersPanel extends DefaultJPanel {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				java.awt.Color brighterColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.getBackground(), 1.0, 1.0, 1.1 );
				final int SKIP_ROW_COUNT = 2;
				int rowIndex = 0;
				int minX = Integer.MAX_VALUE;
				int maxX = Integer.MIN_VALUE;
				for( java.util.List<java.awt.Component> row : rows ) {
					if( rowIndex >= SKIP_ROW_COUNT ) {
						for( java.awt.Component awtComponent : row ) {
							java.awt.Rectangle bounds = awtComponent.getBounds();
							minX = Math.min( minX, bounds.x );
							maxX = Math.max( maxX, bounds.x + bounds.width );
						}
					}
					rowIndex++;
				}
				int width = maxX - minX;
				rowIndex = 0;
				for( java.util.List<java.awt.Component> row : rows ) {
					if( rowIndex >= SKIP_ROW_COUNT ) {
						if( ( ( rowIndex - SKIP_ROW_COUNT ) % 2 ) == 1 ) {
							java.awt.Rectangle bounds = getRowBounds( row );
							g.setColor( brighterColor );
							g.fillRect( minX, bounds.y, width, bounds.height );
						}
					}
					rowIndex++;
				}
			}
		}
		return new JMembersPanel();
	}
}
