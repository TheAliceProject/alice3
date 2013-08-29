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
	private static final int DIFFERENT_SIGNATURE_PRE_GAP = 0;
	private static final int DIFFERENT_SIGNATURE_POST_GAP = 0;
	private static final int DIFFERENT_IMPLEMENTATION_PRE_GAP = 0;
	private static final int DIFFERENT_IMPLEMENTATION_POST_GAP = 0;

	private static final int GAP_Y = 8;
	private static final int SPACE = 24;
	private static final String COLUMN_0_CONSTRAINT = org.alice.stageide.gallerybrowser.uri.merge.IsMemberDesiredState.IS_TERSE ? "[grow 0,center]" : "[grow,shrink,33%]";
	private static final String COLUMN_1_CONSTRAINT = COLUMN_0_CONSTRAINT;
	private static final String COLUMN_2_CONSTRAINT = org.alice.stageide.gallerybrowser.uri.merge.IsMemberDesiredState.IS_TERSE ? "[grow,shrink]" : "[grow,shrink,34%]";

	private static final int BRACKET_WIDTH = 16;

	private static org.lgna.croquet.components.AbstractLabel createHeader( org.lgna.croquet.PlainStringValue stringValue ) {
		final edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>[] HEADER_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };
		org.lgna.croquet.components.AbstractLabel header = stringValue.createLabel( HEADER_TEXT_ATTRIBUTES );
		return header;
	}

	private static org.lgna.croquet.components.HorizontalSeparator createSeparator() {
		return new org.lgna.croquet.components.HorizontalSeparator();
	}

	private static <M extends org.lgna.project.ast.Member> org.lgna.croquet.components.HoverPopupView createPopupView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, M member ) {
		return composite.getPopupMemberFor( member ).getHoverPopupElement().createHoverPopupView();
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

	private String createDifferentImplementationToolText( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, M member ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>\"" );
		sb.append( member.getName() );
		sb.append( "\" " );
		sb.append( getTitleText( composite ) );
		if( member instanceof org.lgna.project.ast.UserMethod ) {
			sb.append( " have different implementations." );
		} else {
			sb.append( " have different initializers." );
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
		super( composite, "fill, insets 8 12 4 4, gapy " + GAP_Y, COLUMN_0_CONSTRAINT + 16 + COLUMN_1_CONSTRAINT + SPACE + COLUMN_2_CONSTRAINT + 0 + "[" + BRACKET_WIDTH + "px,grow 0,shrink 0]" );
		//todo
		backgroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( backgroundColor, 1.0, 1.0, 1.1 );
		this.setBackgroundColor( backgroundColor );

		this.addComponent( createHeader( composite.getAddHeader() ) );
		this.addComponent( createHeader( composite.getExistingHeader() ) );
		this.addComponent( createHeader( composite.getResultHeader() ), "span 2, wrap" );
		this.addComponent( createSeparator(), "grow, shrink" );
		this.addComponent( createSeparator(), "grow, shrink" );
		this.addComponent( createSeparator(), "span 2, grow, shrink, wrap" );

		for( org.alice.stageide.gallerybrowser.uri.merge.ImportOnly<M> importOnly : composite.getImportOnlys() ) {
			this.addComponent( importOnly.getIsAddDesiredState().createCheckBox(), "split 2" );
			this.addComponent( createPopupView( composite, importOnly.getImportMember() ) );

			org.lgna.croquet.components.AbstractLabel label = MemberViewUtilities.createAddMemberLabel( importOnly.getImportMember(), importOnly.getIcon() );
			this.addComponent( label, "skip 1, wrap" );

			//todo: removeValueListener somewhere
			importOnly.getIsAddDesiredState().addValueListener( new IsAddDesiredListener( label ) );
		}

		for( final org.alice.stageide.gallerybrowser.uri.merge.DifferentSignature<M> differentSignature : composite.getDifferentSignatures() ) {
			//this.addComponent( new org.lgna.croquet.components.Label(), "height 32px, wrap" );
			this.addSpacerNotTrackedAsRow( DIFFERENT_SIGNATURE_PRE_GAP );

			BracketView rightBracket = new BracketView( false );
			rightBracket.setToolTipText( createDifferentSignatureToolText( composite, differentSignature.getImportMember() ) );
			this.addComponent( differentSignature.getIsAddDesiredState().createCheckBox(), "split 2" );
			this.addComponent( createPopupView( composite, differentSignature.getImportMember() ) );

			org.lgna.croquet.components.Label plusLabel = new org.lgna.croquet.components.Label( differentSignature.getImportIcon() );
			org.lgna.croquet.components.TextField textField = MemberViewUtilities.createTextField( differentSignature.getImportNameState(), differentSignature.getForegroundCustomizer() );
			this.addComponent( plusLabel, "skip 1, split 2" );
			this.addComponent( textField );
			this.addComponent( rightBracket, "grow, spany 2, wrap" );

			org.lgna.croquet.components.CheckBox keepCheckBox = differentSignature.getIsKeepDesiredState().createCheckBox();
			this.addComponent( keepCheckBox, "skip 1, split 2" );
			this.addComponent( createPopupView( composite, differentSignature.getProjectMember() ) );

			this.addComponent( new org.lgna.croquet.components.Label( differentSignature.getProjectIcon() ), "split 2" );
			this.addComponent( MemberViewUtilities.createTextField( differentSignature.getProjectNameState(), differentSignature.getForegroundCustomizer() ), "wrap" );

			//todo: removeValueListener somewhere
			differentSignature.getIsAddDesiredState().addValueListener( new IsAddDesiredListener( plusLabel, textField ) );

			this.addSpacerNotTrackedAsRow( DIFFERENT_SIGNATURE_POST_GAP );
		}

		for( final org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementation<M> differentImplementation : composite.getDifferentImplementations() ) {
			this.addSpacerNotTrackedAsRow( DIFFERENT_IMPLEMENTATION_PRE_GAP );
			BracketView rightBracket = new BracketView( false );
			rightBracket.setToolTipText( createDifferentImplementationToolText( composite, differentImplementation.getImportMember() ) );

			org.lgna.croquet.components.CheckBox addCheckBox = differentImplementation.getIsAddDesiredState().createCheckBox();
			org.lgna.croquet.components.CheckBox keepCheckBox = differentImplementation.getIsKeepDesiredState().createCheckBox();
			this.addComponent( addCheckBox, "split 2" );
			this.addComponent( createPopupView( composite, differentImplementation.getImportMember() ) );
			this.addComponent( new org.lgna.croquet.components.Label( differentImplementation.getImportIcon() ), "skip 1, split 2" );
			this.addComponent( differentImplementation.getImportCardOwnerComposite().getRootComponent(), "grow, shrink" );
			this.addComponent( rightBracket, "grow, spany 2, wrap" );

			this.addComponent( keepCheckBox, "skip 1, split 2" );
			this.addComponent( createPopupView( composite, differentImplementation.getProjectMember() ) );
			this.addComponent( new org.lgna.croquet.components.Label( differentImplementation.getProjectIcon() ), "split 2" );
			this.addComponent( differentImplementation.getProjectCardOwnerComposite().getRootComponent(), "grow, shrink, wrap" );
			this.addSpacerNotTrackedAsRow( DIFFERENT_IMPLEMENTATION_POST_GAP );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.Identical<M> identical : composite.getIdenticals() ) {
			String toolTipText = createIdenticalToolText( composite, identical.getImportMember() );
			org.lgna.croquet.components.CheckBox addCheckBox = identical.getIsAddDesiredState().createCheckBox();
			addCheckBox.setToolTipText( toolTipText );
			org.lgna.croquet.components.CheckBox keepCheckBox = identical.getIsKeepDesiredState().createCheckBox();
			keepCheckBox.setToolTipText( toolTipText );
			this.addComponent( addCheckBox, "split 2" );
			this.addComponent( createPopupView( composite, identical.getImportMember() ) );
			this.addComponent( keepCheckBox, "split 2" );
			this.addComponent( createPopupView( composite, identical.getProjectMember() ) );
			this.addComponent( MemberViewUtilities.createKeepIdenticalMemberLabel( identical.getProjectMember() ), "wrap" );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.ProjectOnly<M> projectOnly : composite.getProjectOnlys() ) {
			org.lgna.croquet.components.CheckBox keepCheckBox = projectOnly.getIsKeepDesiredState().createCheckBox();
			this.addComponent( keepCheckBox, "skip 1, split 2" );
			this.addComponent( createPopupView( composite, projectOnly.getProjectMember() ) );
			this.addComponent( MemberViewUtilities.createKeepUniqueMemberLabel( projectOnly.getProjectMember() ), "wrap" );
		}

		//this.setMinimumPreferredHeight( 160 );
	}

	private void addSpacerNotTrackedAsRow( int height ) {
		if( height > 0 ) {
			this.getAwtComponent().add( new javax.swing.JLabel(), "wrap, height " + height + "px" );
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

				final boolean IS_STRETCH_ACROSS_DESIRED = true;
				int width;
				int x;
				if( IS_STRETCH_ACROSS_DESIRED ) {
					x = 16;
					width = this.getWidth() - x - 4 - ( BRACKET_WIDTH / 2 );
				} else {
					x = minX;
					width = maxX - minX;
				}
				rowIndex = 0;
				for( java.util.List<java.awt.Component> row : rows ) {
					if( rowIndex >= SKIP_ROW_COUNT ) {
						if( ( ( rowIndex - SKIP_ROW_COUNT ) % 2 ) == 0 ) {
							java.awt.Rectangle bounds = getRowBounds( row );
							g.setColor( brighterColor );
							g.fillRect( x, bounds.y - ( GAP_Y / 2 ), width, bounds.height + GAP_Y );
						}
					}
					rowIndex++;
				}

				if( this.getComponentCount() > 2 ) {
					int xSeparator = getComponent( 2 ).getX();
					g.setColor( java.awt.Color.DARK_GRAY );
					g.fillRect( xSeparator - ( SPACE / 2 ) - 2, 0, 4, this.getHeight() );
				}

			}
		}
		return new JMembersPanel();
	}
}
