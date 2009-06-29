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
public abstract class AbstractPostIssuePane extends AbstractIssuePane {
	private edu.cmu.cs.dennisc.issue.Issue.Type issueType;
	public AbstractPostIssuePane( edu.cmu.cs.dennisc.issue.Issue.Type issueType ) {
		this.issueType = issueType;
		javax.swing.JTextArea vcEnvironment = new javax.swing.JTextArea() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight( super.getPreferredSize(), 48 );
			}
		};
		vcEnvironment.setBorder( new TextComponentBorder() );
		StringBuffer sb = new StringBuffer();
		sb.append( "java.version: " );
		sb.append( System.getProperty( "java.version" ) );
		sb.append( "\n" );
		sb.append( "os.name: " );
		sb.append( System.getProperty( "os.name" ) );
		sb.append( "\n" );
		vcEnvironment.setText( sb.toString() );
		javax.swing.JScrollPane scrollEnvironment = new javax.swing.JScrollPane( vcEnvironment );
		scrollEnvironment.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		
		
		java.util.ArrayList< java.awt.Component[] > rows = new java.util.ArrayList< java.awt.Component[] >();
		javax.swing.JLabel typeLabel = new javax.swing.JLabel("issue type:", javax.swing.SwingConstants.TRAILING);
		typeLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rows.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( typeLabel, new javax.swing.JLabel( this.issueType.name() ) ) );
		this.addInsightPaneRows( rows );
		javax.swing.JLabel environmentLabel = new javax.swing.JLabel("environment:", javax.swing.SwingConstants.TRAILING);
		environmentLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rows.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( environmentLabel, scrollEnvironment ) );
		
		//this.addReporterPaneRows( rows );

		
		javax.swing.JPanel centerPane = new javax.swing.JPanel();
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( centerPane, rows, 8, 4 );
		
		javax.swing.JPanel southPane = new javax.swing.JPanel();
		southPane.add( this.getSubmitButton() );

		this.setLayout( new java.awt.BorderLayout() );
		this.add( centerPane, java.awt.BorderLayout.CENTER );
		this.add( southPane, java.awt.BorderLayout.SOUTH );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
	}

	@Override
	protected int getPreferredDescriptionHeight() {
		return 128;
	}
	@Override
	protected int getPreferredStepsHeight() {
		return 128;
	}
	
	@Override
	protected boolean isStepsPaneDesired() {
		return edu.cmu.cs.dennisc.issue.Issue.Type.BUG.equals( this.issueType );
	}
	@Override
	protected boolean isSummaryRequired() {
		return true;
	}

	@Override
	protected boolean isInclusionOfCompleteSystemPropertiesDesired() {
		return false;
	}
	
	@Override
	protected boolean isClearedToSubmit() {
		String summary = this.getSummary();
		if( summary != null && summary.length() > 0 ) {
			return true;
		} else {
			javax.swing.JOptionPane.showMessageDialog( this, "You must fill in the summary field in order to submit." );
			return false;
		}
	}
	
	@Override
	protected Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
		super.updateIssue( rv );
		rv.setType( this.issueType );
		return rv;
	}
}
