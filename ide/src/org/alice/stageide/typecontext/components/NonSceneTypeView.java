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

package org.alice.stageide.typecontext.components;


class TypeView extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.components.Label typeLabel = new org.lgna.croquet.components.Label();
	private final org.lgna.croquet.components.Label snapshotLabel = new org.lgna.croquet.components.Label();
	private final org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.NamedUserType > typeListener = new org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.NamedUserType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			TypeView.this.handleTypeStateChanged( nextValue );
		}
	};
	public TypeView() {
		this.typeLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
		this.snapshotLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.CENTER );
		this.addComponent( this.typeLabel, Constraint.PAGE_START );
		this.addComponent( this.snapshotLabel, Constraint.CENTER );
	}
	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		this.typeLabel.setIcon( org.alice.ide.common.TypeIcon.getInstance( nextValue ) );
		
		javax.swing.Icon snapshotIcon = null;
		String snapshotText = null;
		if( nextValue != null ) {
			org.lgna.project.ast.AbstractType< ?,?,? > snapshotType = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructorParameter0Type( nextValue );
			
			if( snapshotType != null ) {
				if( snapshotType instanceof org.lgna.project.ast.JavaType ) {
					java.awt.image.BufferedImage thumbnail = org.lgna.story.resourceutilities.ModelResourceUtilities.getThumbnail(((org.lgna.project.ast.JavaType)snapshotType).getClassReflectionProxy().getReification());
					if( thumbnail != null ) {
						snapshotIcon = new javax.swing.ImageIcon(thumbnail);
					}
				}
				//snapshotIcon = org.alice.stageide.gallerybrowser.ResourceManager.getLargeIconForType( snapshotType );
				//snapshotText = snapshotType.toString();
			} else {
				org.lgna.project.ast.JavaField field = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( nextValue.getDeclaredConstructors().get( 0 ) );
				if( field != null ) {
					java.awt.image.BufferedImage thumbnail = org.lgna.story.resourceutilities.ModelResourceUtilities.getThumbnail(field.getValueType().getClassReflectionProxy().getReification());
					snapshotIcon = new javax.swing.ImageIcon(thumbnail);
					//snapshotText = field.toString();
				}
			}
		}
		this.snapshotLabel.setText( snapshotText );
		this.snapshotLabel.setIcon( snapshotIcon );
		this.revalidateAndRepaint();
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component< ? > parent ) {
		super.handleAddedTo( parent );
		org.alice.ide.croquet.models.typeeditor.TypeState.getInstance().addAndInvokeValueObserver( this.typeListener );
	}
	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component< ? > parent ) {
		org.alice.ide.croquet.models.typeeditor.TypeState.getInstance().removeValueObserver( this.typeListener );
		super.handleRemovedFrom( parent );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class NonSceneTypeView extends org.lgna.croquet.components.CornerSpringPanel {
	public NonSceneTypeView( org.alice.stageide.typecontext.NonSceneTypeComposite composite ) {
		super( composite );
		org.lgna.croquet.components.Button button = org.alice.stageide.typecontext.SelectSceneTypeOperation.getInstance().createButton();
		button.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.BOTTOM );
		button.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.CENTER );
		this.setNorthWestComponent( button );
		this.setNorthEastComponent( new TypeView() );
	}
	@Override
	protected void handleHierarchyChanged( java.awt.event.HierarchyEvent e ) {
		super.handleHierarchyChanged( e );
		
		//org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().createThumbnail();
		
		javax.swing.Icon icon = new javax.swing.Icon() {
			public int getIconWidth() {
				return 80;
			}
			public int getIconHeight() {
				return 60;
			}
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				int w = this.getIconWidth();
				int h = this.getIconHeight()/2;
				g.setColor( java.awt.Color.BLUE );
				g.fillRect( x, y, w, h );
				g.setColor( java.awt.Color.GREEN );
				g.fillRect( x, y+h, w, h );
			}
		};
		org.alice.stageide.typecontext.SelectSceneTypeOperation.getInstance().setSmallIcon( icon );
	}
}
