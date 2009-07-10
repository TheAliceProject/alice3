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
package edu.cmu.cs.dennisc.toolkit.issue;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractPostIssuePane extends IssueReportPane {
	class TypeSelectionOperation extends zoot.EnumConstantSelectionOperation< edu.cmu.cs.dennisc.jira.JIRAReport.Type > {
		public TypeSelectionOperation() {
			super( edu.cmu.cs.dennisc.jira.JIRAReport.Type.class );
		}
		public void performSelectionChange( zoot.ItemSelectionContext< edu.cmu.cs.dennisc.jira.JIRAReport.Type > context ) {
			AbstractPostIssuePane.this.refreshRows();
		}
	}

	private TypeSelectionOperation typeSelectionOperation = new TypeSelectionOperation();
	private javax.swing.JLabel labelType = createLabelForMultiLine( "type:" );
	private javax.swing.JComboBox comboType = new zoot.ZComboBox( this.typeSelectionOperation ) {
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		}
	};
	private java.awt.Component[] rowType = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelType, new swing.LineAxisPane( comboType, javax.swing.Box.createHorizontalGlue() ) );
	
	private javax.swing.JLabel labelEnvironment = createLabelForMultiLine( "environment:" );
	private javax.swing.JTextArea textEnvironment = new javax.swing.JTextArea();
	private javax.swing.JScrollPane scrollEnvironment = new javax.swing.JScrollPane( this.textEnvironment ){
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight( super.getPreferredSize(), 80 );
		}
	};
	private java.awt.Component[] rowEnvironment = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( labelEnvironment, scrollEnvironment );


	private javax.swing.JPanel centerPane = new javax.swing.JPanel();

	public AbstractPostIssuePane() {
		this.textEnvironment.setBorder( new TextComponentBorder() );
		StringBuffer sb = new StringBuffer();
		
		String intersticial = "";
		for( String propertyName : this.getSystemPropertiesForEnvironmentField() ) {
			sb.append( intersticial );
			sb.append(propertyName );
			sb.append( ": " );
			sb.append( System.getProperty( propertyName ) );
			intersticial = "\n";
		}
		this.textEnvironment.setText( sb.toString() );
		this.scrollEnvironment.setBorder( javax.swing.BorderFactory.createEmptyBorder() );

		this.centerPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 12, 12, 12, 12 ) );
		
		this.add( this.centerPane, java.awt.BorderLayout.CENTER );

		this.scrollEnvironment.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
	}

	public void setIssueType( edu.cmu.cs.dennisc.jira.JIRAReport.Type issueType ) {
		this.typeSelectionOperation.getComboBoxModel().setSelectedItem( issueType );
	}
	@Override
	protected String getSMTPReplyTo() {
		return null;
	}
	@Override
	protected String getSMTPReplyToPersonal() {
		return null;
	}
	
	@Override
	protected String getEnvironmentText() {
		return this.textEnvironment.getText();
	}
	
	@Override
	protected edu.cmu.cs.dennisc.jira.JIRAReport.Type getJIRAType() {
		return (edu.cmu.cs.dennisc.jira.JIRAReport.Type)this.typeSelectionOperation.getComboBoxModel().getSelectedItem();
	}
	
	protected java.util.ArrayList< java.awt.Component[] > addRows( java.util.ArrayList< java.awt.Component[] > rows ) {
		rows.add( this.rowType );
		rows.add( this.rowSummary );
		rows.add( this.rowDescription );
		if( edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG.equals( this.getJIRAType() ) ) {
			rows.add( this.rowSteps );
		}
		rows.add( this.rowEnvironment );
		return rows;
	}
	protected final void refreshRows() {
		this.centerPane.removeAll();

		java.util.ArrayList< java.awt.Component[] > rows = this.addRows( new java.util.ArrayList< java.awt.Component[] >() );
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( this.centerPane, rows, 8, 4 );
		this.centerPane.revalidate();
	}
	
	@Override
	protected Throwable getThrowable() {
		return null;
	}

//	@Override
//	protected int getPreferredWidth() {
//		return 700;
//	}
	@Override
	protected int getPreferredDescriptionHeight() {
		return 128;
	}
	@Override
	protected int getPreferredStepsHeight() {
		return 128;
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
		String summary = this.getSummaryText();
		if( summary != null && summary.length() > 0 ) {
			return true;
		} else {
			javax.swing.JOptionPane.showMessageDialog( this, "You must fill in the summary field in order to submit." );
			return false;
		}
	}
	
//	@Override
//	protected Issue updateIssue( edu.cmu.cs.dennisc.issue.Issue rv ) {
//		super.updateIssue( rv );
//		rv.setType( this.issueType );
//		return rv;
//	}
}
