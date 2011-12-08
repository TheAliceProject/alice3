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

package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class ResourceTab extends org.lgna.croquet.TabComposite< org.lgna.croquet.components.View< ?,? > > {
	private static class SingletonHolder {
		private static ResourceTab instance = new ResourceTab();
	}
	public static ResourceTab getInstance() {
		return SingletonHolder.instance;
	}
	private ResourceTab() {
		super( java.util.UUID.fromString( "811380db-5339-4a2e-84e3-695b502188af" ) );
	}
	
	@Override
	public boolean isCloseable() {
		return false;
	}
	@Override
	protected org.lgna.croquet.components.View< ?, ? > createView() {
		class ResourceView extends org.lgna.croquet.components.BorderPanel {
			public ResourceView() {
				org.lgna.croquet.components.BorderPanel topPanel = new org.lgna.croquet.components.BorderPanel();
				topPanel.addComponent( new org.lgna.croquet.components.TreePathViewController( org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance() ), Constraint.LINE_START );
				org.lgna.croquet.components.TextField filterTextField = FilterStringState.getInstance().createTextField();
				filterTextField.setMinimumPreferredWidth( 320 );
				filterTextField.setMaximumSizeClampedToPreferredSize( true );
				filterTextField.getAwtComponent().setTextForBlankCondition( "search entire gallery" );
				filterTextField.scaleFont( 1.5f );

				topPanel.addComponent( filterTextField, Constraint.LINE_END );

				this.addComponent( topPanel, Constraint.PAGE_START );
				this.addComponent( new GalleryDirectoryViewController(), Constraint.CENTER );

				org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromPersonResourceOperation createTypeFromPersonResourceOperation = org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromPersonResourceOperation.getInstance();
				org.lgna.croquet.components.Button createPersonButton = createTypeFromPersonResourceOperation.createButton();
				createPersonButton.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.CENTER );
				createPersonButton.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.BOTTOM );

				createTypeFromPersonResourceOperation.setSmallIcon(edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon(GalleryBrowser.class.getResource("images/create_person.png")));
				
				this.addComponent( createPersonButton, Constraint.LINE_START );
				
				org.lgna.croquet.components.BorderPanel lineEndPanel = new org.lgna.croquet.components.BorderPanel();
				lineEndPanel.addComponent( org.alice.stageide.croquet.models.declaration.BillboardFieldDeclarationOperation.getInstance().createButton(), Constraint.PAGE_START );
				lineEndPanel.addComponent( org.alice.stageide.croquet.models.declaration.ConeFieldDeclarationOperation.getInstance().createButton(), Constraint.PAGE_END );
				this.addComponent( lineEndPanel, Constraint.LINE_END );
				//todo
				this.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
			}
		}
		return new ResourceView();
	}
	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		//todo
		return false;
	}
}
