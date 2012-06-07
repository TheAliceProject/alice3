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
package org.alice.ide.croquet.models.help;

import org.lgna.croquet.DialogComposite;
import org.lgna.croquet.StringState;

/**
 * @author Matt May
 */
public class ReportIssueComposite extends DialogComposite<ReportIssueView> {

	private static final org.lgna.croquet.Group ISSUE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "af49d17b-9299-4a0d-b931-0a18a8abf0dd" ), "ISSUE_GROUP" );
	private StringState visibilityLabel = createStringState( this.createKey( "visibilityLabel" ) );
	private StringState typeLabel = createStringState( this.createKey( "typeLabel" ) );
	private StringState summaryLabel = createStringState( this.createKey( "summaryLabel" ) );
	private StringState summaryBlank = createStringState( this.createKey( "summaryBlank" ) );
	private StringState descriptionLabel = createStringState( this.createKey( "descriptionLabel" ) );
	private StringState descriptionBlank = createStringState( this.createKey( "descriptionBlank" ) );
	private StringState stepsLabel = createStringState( this.createKey( "stepsLabel" ) );
	private StringState stepsBlank= createStringState( this.createKey( "stepsBlank" ) );
	private StringState environmentLabel = createStringState( this.createKey( "environmentLabel" ) );
	private StringState environmentBlank = createStringState( this.createKey( "environmentBlank" ) );
	private StringState attachmentLabel = createStringState( this.createKey( "attachmentLabel" ) );
	private StringState attachmentBlank = createStringState( this.createKey( "attachmentBlank" ) );
	
	public StringState getVisibilityLabel() {
		return this.visibilityLabel;
	}
	public StringState getTypeLabel() {
		return this.typeLabel;
	}
	public StringState getSummaryLabel() {
		return this.summaryLabel;
	}
	public StringState getSummaryBlank() {
		return this.summaryBlank;
	}
	public StringState getDescriptionLabel() {
		return this.descriptionLabel;
	}
	public StringState getDescriptionBlank() {
		return this.descriptionBlank;
	}
	public StringState getStepsLabel() {
		return this.stepsLabel;
	}
	public StringState getStepsBlank() {
		return this.stepsBlank;
	}
	public StringState getEnvironmentLabel() {
		return this.environmentLabel;
	}
	public StringState getEnvironmentBlank() {
		return this.environmentBlank;
	}
	public StringState getAttachmentLabel() {
		return this.attachmentLabel;
	}
	public StringState getAttachmentBlank() {
		return this.attachmentBlank;
	}

	public ReportIssueComposite() {
		super( java.util.UUID.fromString( "31d2104a-c90c-4861-9082-3a0390527a04" ), ISSUE_GROUP );
	}

	@Override
	protected ReportIssueView createView() {
		return new ReportIssueView( this );
	}

	public static void main( String[] args ) {
		
	}
}
