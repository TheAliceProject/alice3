/**
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.alice.ide.common.NodeLikeSubstance;
import org.alice.stageide.croquet.models.gallerybrowser.GalleryClassOperation;
import org.lgna.project.ast.TypeDeclaredInAlice;
import org.lgna.story.resourceutilities.ModelResourceTreeNode;
import org.lgna.story.resourceutilities.ModelResourceUtilities;


/**
 * @author dculyba
 *
 */
public class ClassBasedGalleryDragComponent extends NodeLikeSubstance {
	private static java.util.Map<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice>, ClassBasedGalleryDragComponent> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static ClassBasedGalleryDragComponent getInstance( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> treeNode ) {
		ClassBasedGalleryDragComponent rv = map.get( treeNode );
		if( rv != null ) {
			//pass
		} else {
			rv = new ClassBasedGalleryDragComponent( treeNode );
			map.put( treeNode, rv );
		}
		return rv;
	}
	
	private edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> treeNode;
	private ClassBasedGalleryDragComponent( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> treeNode ) {
		this.treeNode = treeNode;
		org.lgna.croquet.components.Label label = new org.lgna.croquet.components.Label();
		
		TypeDeclaredInAlice classValue = treeNode.getValue();
		if (treeNode instanceof ModelResourceTreeNode)
		{
			Class<?> resourceClass = ((ModelResourceTreeNode)treeNode).getResourceClass();
			BufferedImage thumbnail = ModelResourceUtilities.getThumbnail(resourceClass);
			ImageIcon icon = new ImageIcon(thumbnail);
			label.setIcon( icon );
		}
		
		label.setText( ClassBasedGalleryBrowser.getTextFor( this.treeNode, false ) );
		label.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.BOTTOM );
		label.setHorizontalTextPosition( org.lgna.croquet.components.HorizontalTextPosition.CENTER );
		this.setAlignmentY( java.awt.Component.TOP_ALIGNMENT );
		this.setEnabledBackgroundPaint( new java.awt.Color( 0xf7e4b6 ) );
		this.addComponent( label );
		this.setLeftButtonClickModel( GalleryClassOperation.getInstance( treeNode ) );
		this.setDragModel( new org.alice.ide.croquet.models.GalleryDragModel() );
		this.getAwtComponent().setOpaque( false );
	}
	
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> getTreeNode() {
		return this.treeNode;
	}

	@Override
	protected java.awt.geom.RoundRectangle2D.Float createShape( int x, int y, int width, int height ) {
		return new java.awt.geom.RoundRectangle2D.Float( x, y, width-1, height-1, 8, 8 );
	}
	@Override
	protected void fillBounds(java.awt.Graphics2D g2, int x, int y, int width, int height) {
		g2.fill( this.createShape(x, y, width, height));
	}

	@Override
	protected int getDockInsetLeft() {
		return 0;
	}

	@Override
	protected int getInternalInsetLeft() {
		return 0;
	}

	@Override
	protected int getInsetBottom() {
		return 8;
	}

	@Override
	protected int getInsetRight() {
		return 0;
	}

	@Override
	protected int getInsetTop() {
		return 0;
	}

	@Override
	protected void paintPrologue(java.awt.Graphics2D g2, int x, int y, int width, int height) {
		java.awt.geom.RoundRectangle2D rr = new java.awt.geom.RoundRectangle2D.Float( x+1, y+1, width-3, height-3, 8, 8 );
		g2.fill( rr );
	}

}
