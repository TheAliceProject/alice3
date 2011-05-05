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

/**
 * @author Dennis Cosgrove
 */
public class HelpOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private static class SingletonHolder {
		private static HelpOperation instance = new HelpOperation();
	}
	public static HelpOperation getInstance() {
		return SingletonHolder.instance;
	}

	private static class HelpPanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
		public HelpPanel() {
			BrowserOperation browserOperation = new BrowserOperation( java.util.UUID.fromString( "5a1b1db2-da93-4c85-bca5-e1796bd07d00" ), "http://help.alice.org/" ); //"http://kenai.com/projects/alice/pages/Help"

			edu.cmu.cs.dennisc.croquet.Hyperlink hyperlink = browserOperation.createHyperlink();
			edu.cmu.cs.dennisc.croquet.Label iconLabel = new edu.cmu.cs.dennisc.croquet.Label( new javax.swing.ImageIcon( HelpPanel.class.getResource( "images/help.png" ) ) );
			edu.cmu.cs.dennisc.croquet.Label textLabel = new edu.cmu.cs.dennisc.croquet.Label( "Help is available on the web:" );
			
			textLabel.scaleFont( 2.0f );
			hyperlink.scaleFont( 2.0f );

			edu.cmu.cs.dennisc.croquet.PageAxisPanel pageAxisPanel = new edu.cmu.cs.dennisc.croquet.PageAxisPanel(
					edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalGlue(),
					textLabel,
					hyperlink,
					edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalGlue()
			);
			pageAxisPanel.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
			this.addComponent( iconLabel, Constraint.LINE_START );
			this.addComponent( pageAxisPanel, Constraint.LINE_END );
		}
	}

	private HelpOperation() {
		super( java.util.UUID.fromString( "b478d150-03c2-4972-843a-a1e64dbd2b58" ) );
		//this.setName( "Help..." );
		//this.setAcceleratorKey( javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F1, 0 ) );
	}

	@Override
	protected void performInternal( org.lgna.croquet.steps.ActionOperationStep step ) {
		edu.cmu.cs.dennisc.croquet.Application.getSingleton().showMessageDialog( new HelpPanel(), "Help", edu.cmu.cs.dennisc.croquet.MessageType.PLAIN );
	}
}
