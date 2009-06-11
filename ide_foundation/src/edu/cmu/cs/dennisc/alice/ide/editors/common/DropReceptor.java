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
package edu.cmu.cs.dennisc.alice.ide.editors.common;

/**
 * @author Dennis Cosgrove
 */
public interface DropReceptor {
	public boolean isPotentiallyAcceptingOf( PotentiallyDraggablePane source );
	public java.awt.Component getAWTComponent();
	public void dragStarted( PotentiallyDraggablePane source, java.awt.event.MouseEvent e );
	public void dragEntered( PotentiallyDraggablePane source, java.awt.event.MouseEvent e );
	public void dragUpdated( PotentiallyDraggablePane source, java.awt.event.MouseEvent e );
	
	//todo: Dropped or Exited but not both?
	public void dragDropped( PotentiallyDraggablePane source, java.awt.event.MouseEvent e );
	public void dragExited( PotentiallyDraggablePane source, java.awt.event.MouseEvent e, boolean isDropRecipient );
	
	
	public void dragStopped( PotentiallyDraggablePane source, java.awt.event.MouseEvent e );
}
