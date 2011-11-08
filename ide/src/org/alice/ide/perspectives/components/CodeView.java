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

package org.alice.ide.perspectives.components;

/**
 * @author Dennis Cosgrove
 */
public class CodeView extends org.alice.stageide.perspectives.components.IdePerspectiveView< javax.swing.JSplitPane, org.alice.ide.perspectives.CodePerspective > {
	private Integer leftDividerLocation = null;
	private final org.lgna.croquet.components.VerticalSplitPane left = new org.lgna.croquet.components.VerticalSplitPane();
	private final org.alice.ide.typeeditor.TypeEditor typeEditor = org.alice.ide.typeeditor.TypeEditor.getInstance();
	public CodeView() {
		super( org.alice.ide.perspectives.CodePerspective.getInstance() );
		org.alice.ide.perspectives.components.TypeOrCodeCardPanel contextView = new org.alice.ide.perspectives.components.TypeOrCodeCardPanel( 
				org.alice.ide.typehierarchy.TypeHierarchyComposite.getInstance(), 
				org.alice.ide.members.MembersComposite.getInstance() 
		);
		this.left.setBottomComponent( contextView );
		this.left.setTopComponent( new org.alice.stageide.typecontext.components.SceneOrNonSceneCardPanel( 
				org.alice.stageide.typecontext.SceneTypeComposite.getInstance(),
				org.alice.stageide.typecontext.NonSceneTypeComposite.getInstance()
		) );
		javax.swing.JSplitPane jSplitPane = this.getAwtComponent();
		jSplitPane.setLeftComponent( this.left.getAwtComponent() );
		jSplitPane.setRightComponent( this.typeEditor.getAwtComponent() );
		
		jSplitPane.setMinimumSize( new java.awt.Dimension( SPLIT_MINIMUM_SIZE, SPLIT_MINIMUM_SIZE ) );
		jSplitPane.getLeftComponent().setMinimumSize( new java.awt.Dimension( SPLIT_MINIMUM_SIZE, SPLIT_MINIMUM_SIZE ) );
		jSplitPane.getRightComponent().setMinimumSize( new java.awt.Dimension( SPLIT_MINIMUM_SIZE, SPLIT_MINIMUM_SIZE ) );
		
	}
	@Override
	protected void setIgnoreRepaintOnSplitPanes( boolean isIgnoreRepaint ) {
		this.getAwtComponent().setIgnoreRepaint( isIgnoreRepaint );
		this.left.setIgnoreRepaint( isIgnoreRepaint );
	}
	@Override
	protected javax.swing.JSplitPane createAwtComponent() {
		return new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	}
	@Override
	public void handleDeactivated( org.alice.ide.MainComponent mainComponent ) {
		this.leftDividerLocation = this.left.getDividerLocation();
	}
	@Override
	public void handleActivated( org.alice.ide.MainComponent mainComponent ) {
		if( this.leftDividerLocation != null ) {
			//pass
		} else {
			this.leftDividerLocation = 240;
		}
		org.alice.stageide.typecontext.SceneTypeComposite.getInstance().getView().getAwtComponent().add( org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getAwtComponent(), java.awt.BorderLayout.CENTER );
		this.left.setDividerLocation( this.leftDividerLocation );
	}
	
	public org.alice.ide.typeeditor.TypeEditor getTypeEditor() {
		return this.typeEditor;
	}
}
