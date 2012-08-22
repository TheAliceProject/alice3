/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.toolkit.issue;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractPostIssuePane extends IssueReportPane {
	public static final org.lgna.croquet.Group POST_ISSUE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "b010ccc1-b127-494d-9cc0-18c378fe0800" ), "POST_ISSUE_GROUP" );

	class TypeSelectionState extends edu.cmu.cs.dennisc.toolkit.croquet.models.EnumConstantSelectionState<edu.cmu.cs.dennisc.jira.JIRAReport.Type> {
		public TypeSelectionState() {
			super( POST_ISSUE_GROUP, java.util.UUID.fromString( "8998dd3a-4ccc-4f75-8699-5d6d5e468259" ), -1, edu.cmu.cs.dennisc.jira.JIRAReport.Type.class );
		}
	}

	private TypeSelectionState typeSelectionState = new TypeSelectionState();
	private javax.swing.JLabel labelType = createLabelForMultiLine( "type:" );
	private javax.swing.JComboBox comboType = typeSelectionState.getPrepModel().createComboBox().getAwtComponent();
	private java.awt.Component[] rowType = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelType, new edu.cmu.cs.dennisc.javax.swing.components.JLineAxisPane( comboType, javax.swing.Box.createHorizontalGlue() ) );

	private javax.swing.JLabel labelEnvironment = createLabelForMultiLine( "environment:" );
	private javax.swing.JTextArea textEnvironment = new javax.swing.JTextArea();
	private javax.swing.JScrollPane scrollEnvironment = new javax.swing.JScrollPane( this.textEnvironment ) {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), 80 );
		}
	};
	private java.awt.Component[] rowEnvironment = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( labelEnvironment, scrollEnvironment );

	private javax.swing.JPanel centerPane = new javax.swing.JPanel();

	private static class TextComponentBorder extends javax.swing.border.CompoundBorder {
		public TextComponentBorder() {
			super( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ), javax.swing.BorderFactory.createEmptyBorder( 1, 3, 1, 3 ) );
		}
	}

	public AbstractPostIssuePane() {
		this.textEnvironment.setBorder( new TextComponentBorder() );
		StringBuffer sb = new StringBuffer();

		String intersticial = "";
		for( String propertyName : this.getSystemPropertiesForEnvironmentField() ) {
			sb.append( intersticial );
			sb.append( propertyName );
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
		this.typeSelectionState.setSelectedItem( issueType );
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
		return this.typeSelectionState.getSelectedItem();
	}

	protected java.util.ArrayList<java.awt.Component[]> addRows( java.util.ArrayList<java.awt.Component[]> rows ) {
		rows.add( this.rowType );
		rows.add( this.rowSummary );
		rows.add( this.rowDescription );
		if( edu.cmu.cs.dennisc.jira.JIRAReport.Type.BUG.equals( this.getJIRAType() ) ) {
			rows.add( this.rowSteps );
		}
		rows.add( this.rowEnvironment );
		return rows;
	}

	private org.lgna.croquet.ListSelectionState.ValueListener<edu.cmu.cs.dennisc.jira.JIRAReport.Type> typeSelectionListener = new org.lgna.croquet.ListSelectionState.ValueListener<edu.cmu.cs.dennisc.jira.JIRAReport.Type>() {
		public void changing( org.lgna.croquet.State<edu.cmu.cs.dennisc.jira.JIRAReport.Type> state, edu.cmu.cs.dennisc.jira.JIRAReport.Type prevValue, edu.cmu.cs.dennisc.jira.JIRAReport.Type nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<edu.cmu.cs.dennisc.jira.JIRAReport.Type> state, edu.cmu.cs.dennisc.jira.JIRAReport.Type prevValue, edu.cmu.cs.dennisc.jira.JIRAReport.Type nextValue, boolean isAdjusting ) {
			AbstractPostIssuePane.this.refreshRows();
		}
	};

	@Override
	public void addNotify() {
		super.addNotify();
		this.typeSelectionState.addAndInvokeValueListener( this.typeSelectionListener );
	}

	@Override
	public void removeNotify() {
		this.typeSelectionState.removeValueListener( this.typeSelectionListener );
		super.removeNotify();
	}

	protected final void refreshRows() {
		this.centerPane.removeAll();

		java.util.ArrayList<java.awt.Component[]> rows = this.addRows( new java.util.ArrayList<java.awt.Component[]>() );
		edu.cmu.cs.dennisc.javax.swing.SpringUtilities.springItUpANotch( this.centerPane, rows, 8, 4 );
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
		if( ( summary != null ) && ( summary.length() > 0 ) ) {
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
