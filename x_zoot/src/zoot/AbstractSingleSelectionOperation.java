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
package zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSingleSelectionOperation<E> extends AbstractOperation implements SingleSelectionOperation< E >, javax.swing.event.TreeSelectionListener {
	private javax.swing.SingleSelectionModel singleSelectionModelForConfiguringSwingComponents = new javax.swing.DefaultSingleSelectionModel() {
	};

	public javax.swing.SingleSelectionModel getSingleSelectionModelForConfiguringSwingComponents() {
		return this.singleSelectionModelForConfiguringSwingComponents;
	};

	private static Object getLastPathComponent( javax.swing.tree.TreePath path ) { 
		if( path != null ) {
			return path.getLastPathComponent();
		} else {
			return null;
		}
	}
	public void valueChanged( javax.swing.event.TreeSelectionEvent e ) {
		javax.swing.tree.TreePath prevTreePath = e.getOldLeadSelectionPath();
		javax.swing.tree.TreePath nextTreePath = e.getNewLeadSelectionPath();
		Object prevSelection = getLastPathComponent( prevTreePath );
		Object nextSelection = getLastPathComponent( nextTreePath );
		ZManager.performIfAppropriate( this, e, ZManager.CANCEL_IS_FUTILE, prevSelection, nextSelection );
	}
}
