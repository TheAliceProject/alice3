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
package org.alice.ide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractGalleryBrowser extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static final int GAP = 4;
	private edu.cmu.cs.dennisc.croquet.TreeSelectionState<?> treeSelectionState;
	private static final javax.swing.ImageIcon folderIcon = new javax.swing.ImageIcon(AbstractGalleryBrowser.class.getResource("images/folder.png"));
	private static final javax.swing.ImageIcon folderIconSmall = new javax.swing.ImageIcon(AbstractGalleryBrowser.class.getResource("images/folder24.png"));
	
	public AbstractGalleryBrowser( javax.swing.tree.TreeNode root ) {
		super(GAP * 2, 0);

		this.treeSelectionState = new edu.cmu.cs.dennisc.croquet.TreeSelectionState( org.alice.ide.IDE.IDE_GROUP, java.util.UUID.fromString( "42798d37-0815-4ca8-9fb6-107d47e4642f" ), root, root ) {
			@Override
			protected java.lang.Object decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
				throw new RuntimeException("todo");
			}
			@Override
			protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.lang.Object value) {
				throw new RuntimeException("todo");
			}
		};
		
		edu.cmu.cs.dennisc.croquet.BorderPanel borderPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		borderPanel.addComponent( this.treeSelectionState.createPathControl( this.createInitializer() ), Constraint.NORTH );
		
		this.addComponent( borderPanel, Constraint.CENTER );
	}

	protected abstract edu.cmu.cs.dennisc.croquet.PathControl.Initializer createInitializer();

	protected String getAdornedTextFor(String name, boolean isDirectory, boolean isRequestedByPath) {
		String rv;
		if (isRequestedByPath) {
			rv = name;
		} else {
			if (isDirectory) {
				rv = "<html><i>package:</i> <b>" + name + "</b></html>";
			} else {
				rv = "<html><i>class:</i> <b>" + name + "</b></html>";
			}
		}
		return rv;
	}

	protected final String getTextFor(javax.swing.tree.TreeNode file, boolean isRequestedByPath) {
		String name;
		if (file instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode) {
			edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode) file;
			name = zipTreeNode.getName();
			if (zipTreeNode.isLeaf()) {
				name = name.substring(0, name.length() - 4);
			}

		} else {
			name = file.toString();
		}
		return this.getAdornedTextFor(name, file.isLeaf() == false, isRequestedByPath);
	}
}
