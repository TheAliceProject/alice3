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
package org.alice.ide.issue;

/**
 * @author Dennis Cosgrove
 */
public class CurrentProjectAttachment implements edu.cmu.cs.dennisc.issue.Attachment {
	private boolean isCreateAttempted = false;
	private boolean isCreateSuccessful = false;
	private byte[] bytes = null;

	private void createBytesIfNecessary() {
		if( this.isCreateAttempted ) {
			//pass
		} else {
			javax.swing.JOptionPane.showMessageDialog( null, "todo" );
//			try {
//				org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
//				java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
//				edu.cmu.cs.dennisc.alice.io.FileUtilities.writeProject( project, baos );
//				baos.flush();
//				this.bytes = baos.toByteArray();
//				this.isCreateSuccessful = true;
//			} catch( Throwable t ) {
//				this.bytes = edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsByteArray( t );
//			}
			this.isCreateAttempted = true;
		}
	}
	public byte[] getBytes() {
		this.createBytesIfNecessary();
		return this.bytes;
	}
	public String getMIMEType() {
		this.createBytesIfNecessary();
		if( this.isCreateSuccessful ) {
			return "application/a3p";
		} else {
			return "text/plain";
		}
	}
	public String getFileName() {
		this.createBytesIfNecessary();
		if( this.isCreateSuccessful ) {
			return "currentProject.a3p";
		} else {
			return "failedToAttachProject.txt";
		}
	}
}
