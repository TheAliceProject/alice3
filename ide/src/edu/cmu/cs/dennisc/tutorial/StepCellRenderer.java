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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class StepCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< Step > {
	private StepsModel stepsModel;
	private java.awt.Color background;
	private java.awt.Color disabledBackground;
	private java.awt.Color disabledForeground;

	public StepCellRenderer( StepsModel stepsModel, java.awt.Color background ) {
		this.stepsModel = stepsModel;
		this.background = background;
		this.disabledBackground = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( this.background, 1.0, 0.5, 1.25 );
		this.disabledForeground = java.awt.Color.LIGHT_GRAY;
	}
	@Override
	protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.tutorial.Step value, int index, boolean isSelected, boolean cellHasFocus) {
		assert list != null;
		if( value != null) {
			StringBuilder sb = new StringBuilder();
			int i;
			if (index >= 0) {
				i = index;
			} else {
				i = stepsModel.getSelectedIndex();
				// if( i >= 0 ) {
				// assert value == stepsModel.getElementAt( i ) :
				// stepsModel.getElementAt( i );
				// }
			}
			sb.append("<html>");
			sb.append("<em>");
			sb.append("Step ");
			sb.append(i + 1);
			sb.append(": ");
			sb.append("</em>");
			sb.append(value);
			sb.append("</html>");
			
			rv.setText(sb.toString());
		}
		
		if( stepsModel.isForwardEnabled() || index <= stepsModel.getSelectedIndex() ) {
			if( isSelected || cellHasFocus ) {
				//pass
			} else {
				rv.setBackground( this.background );
			}
		} else {
			rv.setBackground( this.disabledBackground );
			rv.setForeground( this.disabledForeground );
		}
		return rv;
	}
}
