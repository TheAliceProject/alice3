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
package edu.cmu.cs.dennisc.javax.swing.tooltips;

class ToolTipUI extends javax.swing.plaf.basic.BasicToolTipUI {
	private static ToolTipUI singleton = new ToolTipUI();

	public static javax.swing.plaf.ComponentUI createUI( javax.swing.JComponent c ) {
		return ToolTipUI.singleton;
	}

	@Override
	public java.awt.Dimension getPreferredSize( javax.swing.JComponent c ) {
		JToolTip toolTip = (JToolTip)c;
		java.awt.Component subject = toolTip.getSubject();
		if( subject != null ) {
			edu.cmu.cs.dennisc.java.awt.ComponentUtilities.invalidateTree( subject );
			edu.cmu.cs.dennisc.java.awt.ComponentUtilities.doLayoutTree( subject );
			edu.cmu.cs.dennisc.java.awt.ComponentUtilities.setSizeToPreferredSizeTree( subject );
			return subject.getPreferredSize();
		} else {
			return super.getPreferredSize( c );
		}
	}
	//	@Override
	//	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
	//		ZToolTip toolTip = (ZToolTip)c;
	//		java.awt.Component subject = toolTip.getSubject();
	//		if( subject != null ) {
	//			subject.print( g );
	//		} else {
	//			super.paint( g, c );
	//		}
	//	}
}

/**
 * @author Dennis Cosgrove
 */
public class JToolTip extends javax.swing.JToolTip {
	private java.awt.Component subject;

	public JToolTip( java.awt.Component subject ) {
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.setSubject( subject );
		this.setOpaque( false );
	}

	public java.awt.Component getSubject() {
		return this.subject;
	}

	public void setSubject( java.awt.Component subject ) {
		if( this.subject != null ) {
			this.remove( this.subject );
		}
		this.subject = subject;
		if( this.subject != null ) {
			this.add( this.subject );
		}
	}

	@Override
	public boolean contains( int x, int y ) {
		return false;
	}

	@Override
	public void updateUI() {
		setUI( ToolTipUI.createUI( this ) );
	}
}
