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
package org.alice.stageide.modelresource;

/**
 * @author Dennis Cosgrove
 */
public class TestModelResource {
	private static final javax.swing.Icon EMPTY_ICON = new edu.cmu.cs.dennisc.javax.swing.icons.EmptyIcon( 0, org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE.height );

	public static void main( String[] args ) {
		ResourceNode root = TreeUtilities.getTreeBasedOnClassHierarchy();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( root );
		org.lgna.croquet.simple.SimpleApplication simpleApplication = new org.lgna.croquet.simple.SimpleApplication();
		simpleApplication.getFrame().setMainComposite( new org.lgna.croquet.SimpleComposite( java.util.UUID.randomUUID() ) {
			@Override
			protected org.lgna.croquet.components.View createView() {
				ResourceNodeTreeSelectionState state = ResourceNodeTreeSelectionState.getInstance();
				//				org.lgna.croquet.components.Tree<ResourceNode> tree = ResourceNodeTreeSelectionState.getInstance().createTree();
				//				tree.setRootVisible( false );
				//				tree.expandAllRows();

				org.lgna.croquet.components.BorderPanel topPanel = new org.lgna.croquet.components.BorderPanel.Builder()
						.lineStart( new org.lgna.croquet.components.TreePathViewController( state, new org.lgna.croquet.components.TreePathViewController.Renderer<org.alice.stageide.modelresource.ResourceNode>() {
							public String getText( org.alice.stageide.modelresource.ResourceNode value ) {
								return value.getResourceKey().getText();
							}

							public javax.swing.Icon getIcon( org.alice.stageide.modelresource.ResourceNode value ) {
								org.lgna.croquet.icon.IconFactory iconFactory = value.getResourceKey().getIconFactory();
								return iconFactory != null ? iconFactory.getIcon( org.alice.ide.Theme.DEFAULT_SMALL_ICON_SIZE ) : EMPTY_ICON;
							}
						} ) )
						//.lineEnd( filterTextField )
						.build();

				org.alice.stageide.modelresource.views.ModelResourceDirectoryView view = new org.alice.stageide.modelresource.views.ModelResourceDirectoryView( state );

				org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( view ) {
					@Override
					protected javax.swing.JScrollPane createAwtComponent() {
						return new edu.cmu.cs.dennisc.javax.swing.components.HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane();
					}
				};
				scrollPane.setHorizontalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy.ALWAYS );
				scrollPane.setBorder( null );
				scrollPane.setBothScrollBarIncrements( 16, 160 );
				return new org.lgna.croquet.components.BorderPanel.Builder()
						.pageStart( topPanel )
						.center( view )
						.build();
			}
		} );

		ResourceNodeTreeSelectionState.getInstance().setValue( root );
		simpleApplication.getFrame().setDefaultCloseOperation( org.lgna.croquet.components.Frame.DefaultCloseOperation.EXIT );
		simpleApplication.getFrame().pack();
		simpleApplication.getFrame().setVisible( true );
	}
}
