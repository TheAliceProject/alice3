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
	private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>[] NO_OP_LABEL_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.REGULAR, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };

	private static java.awt.Dimension ICON_SIZE = new java.awt.Dimension( 22, 22 );
	//public static javax.swing.Icon PLUS_ICON = org.alice.stageide.icons.PlusIconFactory.getInstance().getIcon( ICON_SIZE );
	public static javax.swing.Icon EMPTY_ICON = org.lgna.croquet.icon.EmptyIconFactory.getInstance().getIcon( ICON_SIZE );

	private static org.lgna.croquet.components.AbstractLabel createNoOpLabel( org.lgna.project.ast.Member member, String bonusText ) {
		org.lgna.croquet.components.AbstractLabel rv = new org.lgna.croquet.components.Label( member.getName() + bonusText, NO_OP_LABEL_TEXT_ATTRIBUTES );
		//rv.setIcon( CHECK_ONLY_ICON );
		return rv;
	}

	private static org.lgna.croquet.components.AbstractLabel createHeader( org.lgna.croquet.PlainStringValue stringValue ) {
		final edu.cmu.cs.dennisc.java.awt.font.TextAttribute[] HEADER_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE };
		org.lgna.croquet.components.AbstractLabel header = stringValue.createLabel( HEADER_TEXT_ATTRIBUTES );
		//header.setForegroundColor( java.awt.Color.GRAY );
		return header;
	}

	private static org.lgna.croquet.components.TextField createTextField( org.lgna.croquet.StringState state ) {
		org.lgna.croquet.components.TextField textField = state.createTextField();
		textField.enableSelectAllWhenFocusGained();
		return textField;
	}

	private static org.lgna.croquet.components.HorizontalSeparator createSeparator() {
		org.lgna.croquet.components.HorizontalSeparator rv = new org.lgna.croquet.components.HorizontalSeparator();
		return rv;
	}

	private static <D extends org.lgna.project.ast.Member> org.lgna.croquet.components.PopupView createPopupView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, D> composite, D member ) {
		return composite.getPopupMemberFor( member ).getElement().createPopupView();
	}

	private static org.lgna.croquet.components.AbstractLabel createPlusIconLabel( org.lgna.croquet.BooleanState booleanState ) {
		org.lgna.croquet.components.Label rv = new org.lgna.croquet.components.Label();
		rv.setIcon( new org.alice.stageide.gallerybrowser.uri.merge.views.icons.SelectPlusIcon( ICON_SIZE, booleanState.getSwingModel().getButtonModel() ) );
		return rv;
	}

	private int row = 0;
	private final java.util.List<java.util.List<java.awt.Component>> rows = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

	public AddMembersView( org.alice.stageide.gallerybrowser.uri.merge.AddMembersComposite<?, M> composite, java.awt.Color backgroundColor ) {
		super( composite, "", "0[" + ActionRequiredView.ICON.getIconWidth() + "px,grow 0,shrink 0]0[grow,shrink]32[grow,shrink]8[" + ActionRequiredView.ICON.getIconWidth() + "px,grow 0,shrink 0]" );
		//todo
		backgroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( backgroundColor, 1.0, 1.0, 1.1 );
		this.setBackgroundColor( backgroundColor );

		if( composite.getKeepCount() > 0 ) {
			this.addComponent( createHeader( composite.getAddLabel() ), "skip 1" );
			this.addComponent( createHeader( composite.getKeepLabel() ), "wrap" );
			this.addComponent( createSeparator(), "grow, shrink, skip 1" );
			this.addComponent( createSeparator(), "grow, shrink, wrap" );
		} else {
			if( composite.getAddCount() > 0 ) {
				this.addComponent( createHeader( composite.getAddLabel() ), "skip 1, wrap" );
				this.addComponent( createSeparator(), "grow, shrink, skip 1, wrap" );
			}
		}

		java.util.List<org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyMember<M>> importOnlyMembers = composite.getImportOnlyMembers();
		if( importOnlyMembers.size() > 0 ) {
			for( org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyMember<M> importOnlyMember : composite.getImportOnlyMembers() ) {
				this.addComponent( createPlusIconLabel( importOnlyMember.getIsAddMemberDesiredState() ), "skip 1, split 3" );
				this.addComponent( importOnlyMember.getIsAddMemberDesiredState().createCheckBox() );
				this.addComponent( createPopupView( composite, importOnlyMember.getIsAddMemberDesiredState().getMember() ), "wrap" );
			}
		}

		java.util.List<org.alice.stageide.gallerybrowser.uri.merge.data.DifferentSignatureMembers<M>> differentSignatureMembers = composite.getDifferentSignatureMembers();
		if( differentSignatureMembers.size() > 0 ) {
			for( final org.alice.stageide.gallerybrowser.uri.merge.data.DifferentSignatureMembers<M> differentSignatureMember : differentSignatureMembers ) {
				edu.cmu.cs.dennisc.pattern.Criterion<Void> isActionRequiredCriterion = new edu.cmu.cs.dennisc.pattern.Criterion<Void>() {
					public boolean accept( java.lang.Void e ) {
						return differentSignatureMember.isActionRequired();
					}
				};
				ActionRequiredView leftBracket = new ActionRequiredView( isActionRequiredCriterion, true );
				ActionRequiredView rightBracket = new ActionRequiredView( isActionRequiredCriterion, false );
				String tooltipText = "Cannot have multiple " + composite.getOuterComposite().getIsExpandedState().getTrueText() + " named \"" + differentSignatureMember.getImportNameState().getMember().getName() + "\".";
				leftBracket.setToolTipText( tooltipText );
				rightBracket.setToolTipText( tooltipText );
				this.addComponent( leftBracket, "grow, spany 2" );
				this.addComponent( createPlusIconLabel( differentSignatureMember.getIsAddMemberDesiredState() ), "split 4" );
				this.addComponent( differentSignatureMember.getIsAddMemberDesiredState().createCheckBox() );
				this.addComponent( createTextField( differentSignatureMember.getImportNameState() ), "grow" );
				this.addComponent( createPopupView( composite, differentSignatureMember.getImportNameState().getMember() ) );
				this.addComponent( rightBracket, "grow, spany 2, skip, wrap" );

				this.addComponent( createTextField( differentSignatureMember.getProjectNameState() ), "skip 2, split 2, grow" );
				this.addComponent( createPopupView( composite, differentSignatureMember.getProjectNameState().getMember() ), "wrap" );
			}
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.data.IdenticalMembers<M> identicalMembers : composite.getIdenticalMembers() ) {
			org.lgna.croquet.components.AbstractLabel importLabel = createNoOpLabel( identicalMembers.getImportMember(), " (identical)" );
			importLabel.setIcon( EMPTY_ICON );
			importLabel.setForegroundColor( java.awt.Color.GRAY );
			this.addComponent( importLabel, "gap 28, skip 1, split 2" );
			this.addComponent( createPopupView( composite, identicalMembers.getImportMember() ) );
			this.addComponent( createNoOpLabel( identicalMembers.getProjectMember(), "" ), "split 2" );
			this.addComponent( createPopupView( composite, identicalMembers.getProjectMember() ), "wrap" );
		}

		for( org.alice.stageide.gallerybrowser.uri.merge.data.ProjectOnlyMember<M> projectOnlyMember : composite.getProjectOnlyMembers() ) {
			this.addComponent( createNoOpLabel( projectOnlyMember.getProjectMember(), "" ), "skip 2, split 2" );
			this.addComponent( createPopupView( composite, projectOnlyMember.getProjectMember() ), "wrap" );
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
