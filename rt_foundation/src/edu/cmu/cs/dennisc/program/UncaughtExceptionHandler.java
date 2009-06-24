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
package edu.cmu.cs.dennisc.program;

/**
 * @author Dennis Cosgrove
 */
class ExceptionPane extends javax.swing.JPanel {
	private javax.swing.JLabel m_threadLabel = new javax.swing.JLabel();
	private javax.swing.JLabel m_throwableLabel = new javax.swing.JLabel();
	private javax.swing.JList m_stackTraceVC = new javax.swing.JList();
	private javax.swing.JCheckBox m_continuePostingExceptionsVC = new javax.swing.JCheckBox( "continue posting exceptions" );
	public ExceptionPane() {
		setLayout( new java.awt.BorderLayout() );
		m_continuePostingExceptionsVC.setSelected( true );
		setLayout( new java.awt.GridBagLayout() );
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.weighty = 0.0;
		add( m_threadLabel, gbc );
		add( m_throwableLabel, gbc );
		gbc.weighty = 1.0;
		add( new javax.swing.JScrollPane( m_stackTraceVC ), gbc );
		gbc.weighty = 0.0;
		add( m_continuePostingExceptionsVC, gbc );
	}
	public boolean isDialogPostDesired() {
		return m_continuePostingExceptionsVC.isSelected();
	}
	public void setThreadAndThrowable( Thread thread, Throwable throwable ) {
		m_threadLabel.setText( "Thread: " + thread.getName() );
		m_throwableLabel.setText( "Exception: " + throwable.getClass().getName() );
		m_stackTraceVC.setListData( throwable.getStackTrace() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	private ExceptionPane m_exceptionPane = new ExceptionPane();
	public void uncaughtException( Thread thread, Throwable throwable ) {
		if( m_exceptionPane.isDialogPostDesired() ) {
			m_exceptionPane.setThreadAndThrowable( thread, throwable );
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					javax.swing.JOptionPane.showMessageDialog( null, m_exceptionPane, "Caught Unhandled Exception", javax.swing.JOptionPane.ERROR_MESSAGE );
				}	
			} );
		}
		throwable.printStackTrace();
	}
}
