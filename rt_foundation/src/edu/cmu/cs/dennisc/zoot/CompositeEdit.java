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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public class CompositeEdit implements Edit {
	private Edit[] edits;
	private boolean isDoToBeIgnored;
	private String presentation;
	public CompositeEdit( Edit[] edits, boolean isDoToBeIgnored, String presentation ) {
		this.edits = edits;
		this.isDoToBeIgnored = isDoToBeIgnored;
		this.presentation = presentation;
	}
	public void doOrRedo( boolean isDo ) {
		if( isDo && this.isDoToBeIgnored ) {
			//pass
		} else {
			for( Edit edit : this.edits ) {
				edit.doOrRedo( isDo );
			}
		}
	}
	public void undo() {
		final int N = this.edits.length;
		for( int i=0; i<N; i++ ) {
			this.edits[ N-1-i ].undo();
		}
	}
	public boolean canRedo() {
		for( Edit edit : this.edits ) {
			if( edit.canRedo() ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}
	public boolean canUndo() {
		for( Edit edit : this.edits ) {
			if( edit.canUndo() ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}
	protected StringBuffer updatePresentation( StringBuffer rv, java.util.Locale locale ) {
		return rv;
	}
	public final String getPresentation( java.util.Locale locale ) {
		StringBuffer sb = new StringBuffer();
		sb.append( this.presentation );
		if( sb.length() == 0 ) {
			Class<?> cls = this.getClass();
			sb.append( edu.cmu.cs.dennisc.lang.ClassUtilities.getTrimmedClassName( cls ) );
		}
		return sb.toString();
	}
	public String getRedoPresentation( java.util.Locale locale ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "Redo:" );
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
	public String getUndoPresentation( java.util.Locale locale ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "Undo:" );
		this.updatePresentation( sb, locale );
		return sb.toString();
	}
}
