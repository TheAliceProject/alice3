/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package alice.ide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class SceneEditor extends alice.ide.Editor {
	private javax.swing.JSplitPane root = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private zoot.ZTree tree = new zoot.ZTree();
	private javax.swing.JPanel lookingGlass = new javax.swing.JPanel();
	public SceneEditor() {
		this.lookingGlass.setBackground( java.awt.Color.RED );
		this.root.setLeftComponent( this.tree );
		this.root.setRightComponent( this.lookingGlass );
		this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.add( this.root );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 100, 100 );
	}
}
