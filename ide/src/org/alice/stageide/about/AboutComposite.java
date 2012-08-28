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

package org.alice.stageide.about;

/**
 * @author Dennis Cosgrove
 */
public final class AboutComposite extends org.lgna.croquet.PlainDialogOperationComposite<org.alice.stageide.about.views.AboutView> {
	private static class SingletonHolder {
		private static AboutComposite instance = new AboutComposite();
	}

	public static AboutComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.HtmlStringValue versionLabel = this.createUnlocalizedHtmlStringValue( "current version: " + org.lgna.project.Version.getCurrentVersionText() );
	private final org.lgna.croquet.HtmlStringValue supportedByLabel;
	private final org.lgna.croquet.HtmlStringValue dedicationLabel = this.createUnlocalizedHtmlStringValue( "Alice 3 is dedicated to Randy." );

	private AboutComposite() {
		super( java.util.UUID.fromString( "c3c2bc1a-697e-4934-b605-1019605ce4ea" ), org.lgna.croquet.Application.INFORMATION_GROUP );
		StringBuilder sb = new StringBuilder();
		sb.append( "<html><strong>Alice 3</strong> is supported by:" );
		sb.append( "<br>" );
		sb.append( "<ul>" );
		for( String sponsor : new String[] { "Sun Foundation", "Oracle", "Electronic Arts Foundation", "The National Science Foundation", "Defense Advanced Research Projects Agency", "Hearst Foundations", "Heinz Endowments", "Google", "Disney and Hyperion" } ) {
			sb.append( "<li><strong>" );
			sb.append( sponsor );
			sb.append( "</strong></li>" );
		}
		sb.append( "</ul>" );
		//sb.append( "<br>" );
		sb.append( "<b>The Sims <sup>TM</sup> 2</b> Art Assets donated by <strong>Electronic Arts</strong>." );
		sb.append( "</html>" );
		this.supportedByLabel = this.createUnlocalizedHtmlStringValue( sb.toString() );
	}

	public org.lgna.croquet.HtmlStringValue getVersionLabel() {
		return this.versionLabel;
	}

	public org.lgna.croquet.HtmlStringValue getSupportedByLabel() {
		return this.supportedByLabel;
	}

	public org.lgna.croquet.HtmlStringValue getDedicationLabel() {
		return this.dedicationLabel;
	}

	@Override
	protected org.alice.stageide.about.views.AboutView createView() {
		return new org.alice.stageide.about.views.AboutView( this );
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		AboutComposite.getInstance().getOperation().fire();
		System.exit( 0 );
	}
}
