/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.issue.swing;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class JEnvironmentSubPane extends javax.swing.JPanel {
	private static javax.swing.JLabel createSystemPropertyLabel( String propertyName ) {
		return new javax.swing.JLabel( propertyName + ": " + System.getProperty( propertyName ) );
	}

	private class ShowAllSystemPropertiesAction extends javax.swing.AbstractAction {
		public ShowAllSystemPropertiesAction() {
			super( "show all system properties..." );
		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			java.util.List<edu.cmu.cs.dennisc.java.lang.SystemProperty> propertyList = edu.cmu.cs.dennisc.java.lang.SystemUtilities.getSortedPropertyList();
			StringBuilder sb = new StringBuilder();
			sb.append( "<html>" );
			sb.append( "<body>" );
			for( edu.cmu.cs.dennisc.java.lang.SystemProperty property : propertyList ) {
				sb.append( "<strong> " );
				sb.append( property.getKey() );
				sb.append( ":</strong> " );
				sb.append( property.getValue() );
				sb.append( "<br>" );
			}
			sb.append( "</body>" );
			sb.append( "</html>" );
			javax.swing.JEditorPane editorPane = new javax.swing.JEditorPane();
			editorPane.setEditable( false );
			editorPane.setContentType( "text/html" );
			editorPane.setText( sb.toString() );
			javax.swing.JOptionPane.showMessageDialog( JEnvironmentSubPane.this, new javax.swing.JScrollPane( editorPane ) {
				@Override
				public java.awt.Dimension getPreferredSize() {
					java.awt.Dimension rv = super.getPreferredSize();
					rv.width = Math.min( rv.width, 640 );
					rv.height = Math.min( rv.height, 480 );
					return rv;
				}
			}, "System Properties", javax.swing.JOptionPane.INFORMATION_MESSAGE );
		}
	}

	private edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink vcShowAllSystemProperties = new edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink( new ShowAllSystemPropertiesAction() );

	public JEnvironmentSubPane( java.util.List<String> systemPropertyNames ) {
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		for( String propertyName : systemPropertyNames ) {
			this.add( createSystemPropertyLabel( propertyName ) );
		}
		this.add( this.vcShowAllSystemProperties );
	}
}
