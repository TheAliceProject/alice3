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

package edu.cmu.cs.dennisc.issue;

/**
 * @author Dennis Cosgrove
 */
public abstract class UploadWorker extends org.jdesktop.swingworker.SwingWorker< Boolean, String > {
	private ProgressPane progressPane; 
	public UploadWorker( ProgressPane progressPane ) {
		this.progressPane = progressPane;
	}
	@Override
	protected void process( java.util.List< String > chunks ) {
		this.progressPane.handleProcess( chunks );
	}
	private void process( String... chunks ) {
		this.process( edu.cmu.cs.dennisc.util.CollectionUtilities.createArrayList( chunks ) );
	}

	protected abstract void uploadToJIRA() throws Exception;
	protected abstract void sendMail( boolean isTransportLayerSecurityDesired, Integer portOverride ) throws Exception;

	@Override
	protected Boolean doInBackground() throws Exception {
		this.process( "attempting to submit bug report...\n" );
		try {
			this.process( "* uploading directly to database... " );
			this.uploadToJIRA();
			this.process( "SUCCEEDED.\n" );
		} catch( Exception e0 ) {
			this.process( "FAILED.\n" );
			this.process( "* sending mail (on smtp port)... " );
			try {
				this.sendMail( false, null );
				this.process( "SUCCEEDED.\n" );
			} catch( Exception e1 ) {
				this.process( "FAILED.\n" );
				this.process( "* sending secure mail (on secure smtp port)... " );
				try {
					this.sendMail( true, null );
					this.process( "SUCCEEDED.\n" );
				} catch( Exception e2 ) {
					this.process( "FAILED.\n" );
					this.process( "* sending secure mail (on http port)... " );
					try {
						this.sendMail( true, 80 );
						this.process( "SUCCEEDED.\n" );
					} catch( Exception e3 ) {
						this.process( "FAILED.\n" );
						return false;
					}
				}
			}
		}
		return true;
	}
	@Override
	protected void done() {
		try {
			this.progressPane.handleDone( this.get() );
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
}
