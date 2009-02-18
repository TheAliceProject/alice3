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
public abstract class AbstractCaughtExceptionPane extends AbstractIssuePane {
	class ExceptionPane extends javax.swing.JPanel {
		private Thread thread;
		private Throwable throwable;
		protected Thread getThread() {
			return this.thread;
		}
		protected Throwable getThrowable() {
			return this.throwable;
		}
		private void setThreadAndThrowable( final Thread thread, final Throwable throwable ) {
			assert thread != null;
			assert throwable != null;
			this.thread = thread;
			this.throwable = throwable;
			this.removeAll();
			this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
			edu.cmu.cs.dennisc.swing.Hyperlink vcShowStackTrace = new edu.cmu.cs.dennisc.swing.Hyperlink( new javax.swing.AbstractAction( "show complete stack trace..." ) {
				public void actionPerformed( java.awt.event.ActionEvent e ) {
					edu.cmu.cs.dennisc.swing.SwingUtilities.showMessageDialogInScrollableUneditableTextArea( ExceptionPane.this, edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsString( throwable ), "Stack Trace", javax.swing.JOptionPane.INFORMATION_MESSAGE );
				}
			} );

			StringBuffer sb = new StringBuffer();
			sb.append( throwable.getClass().getSimpleName() );
			String message = throwable.getLocalizedMessage();
			if( message != null && message.length() > 0 ) {
				sb.append( "[" );
				sb.append( message );
				sb.append( "]" );
			}
			sb.append( " in " );
			sb.append( thread.getClass().getSimpleName() );
			sb.append( "[" );
			sb.append( thread.getName() );
			sb.append( "]" );

			this.add( new javax.swing.JLabel( sb.toString() ) );
			StackTraceElement[] elements = throwable.getStackTrace();
			if( elements.length > 0 ) {
				StackTraceElement e0 = elements[ 0 ];
				this.add( new javax.swing.JLabel( "class: " + e0.getClassName() ) );
				this.add( new javax.swing.JLabel( "method: " + e0.getMethodName() ) );
				this.add( new javax.swing.JLabel( "in file " + e0.getFileName() + " at line number " + e0.getLineNumber() ) );
			}
			this.add( vcShowStackTrace );
		}
	}
	private ExceptionPane vcExceptionPane;

	@Override
	protected java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > updateCriticalAttachments( java.util.ArrayList< edu.cmu.cs.dennisc.mail.Attachment > rv ) {
		rv = super.updateCriticalAttachments( rv );
		rv.add( new edu.cmu.cs.dennisc.mail.Attachment() {
			public javax.activation.DataSource getDataSource() {
				Throwable throwable = AbstractCaughtExceptionPane.this.vcExceptionPane.getThrowable();
				return new javax.mail.util.ByteArrayDataSource( edu.cmu.cs.dennisc.lang.ThrowableUtilities.getStackTraceAsByteArray( throwable ), "text/plain" );
			}
			public String getFileName() {
				return "stacktrace.txt";
			}
		} );
		return rv;
	}
	public void setThreadAndThrowable( Thread thread, Throwable throwable ) {
		assert this.vcExceptionPane != null;
		this.vcExceptionPane.setThreadAndThrowable( thread, throwable );
		this.revalidate();
	}
	@Override
	protected StringBuffer updateSubject( StringBuffer rv ) {
		Throwable throwable = this.vcExceptionPane.getThrowable();
		if( throwable != null ) {
			rv.append( "exception: " );
			rv.append( throwable.getClass().getName() );
			rv.append( "; " );
			String message = throwable.getMessage();
			if( message != null ) {
				rv.append( "message: " );
				rv.append( throwable.getMessage() );
				rv.append( "; " );
			}
			//todo: handle PyException
//			if( throwable instanceof org.python.core.PyException ) {
//				org.python.core.PyException pyException = new org.python.core.PyException();
//				rv.append( "frame: " );
//				rv.append( pyException.traceback.tb_frame );
//				rv.append( "; line: " );
//				rv.append( pyException.traceback.tb_lineno );
//				rv.append( "; " );
//			} else {
				StackTraceElement[] stackTraceElements = throwable.getStackTrace();
				if( stackTraceElements != null && stackTraceElements.length > 0 && stackTraceElements[ 0 ] != null ) {
					rv.append( "stack[0]: " );
					rv.append( stackTraceElements[ 0 ].toString() );
					rv.append( "; " );
				}
//			}
		}
		super.updateSubject( rv );
		return rv;
	}
	
	@Override
	protected java.util.ArrayList< java.awt.Component[] > addBugPaneRows( java.util.ArrayList< java.awt.Component[] > rv ) {
		rv = super.addBugPaneRows( rv );
		assert this.vcExceptionPane == null;
		this.vcExceptionPane = new ExceptionPane();
		javax.swing.JLabel exceptionLabel = new javax.swing.JLabel( "exception:", javax.swing.SwingConstants.TRAILING );
		exceptionLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.add( new java.awt.Component[] { exceptionLabel, this.vcExceptionPane } ); 
		return rv;
	}
}
