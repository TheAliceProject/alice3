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
package org.alice.ide.choosers;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractChooser<E> implements ValueChooser< E >, edu.cmu.cs.dennisc.croquet.InputPanel.Validator {
	private static final String[] LABEL_TEXTS = { "value:" };
	private edu.cmu.cs.dennisc.croquet.InputPanel< ? > inputPanel;
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		org.alice.ide.IDE ide = this.getIDE();
		if( ide != null ) {
			return ide.getPreviousExpression();
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.croquet.InputPanel< ? > getInputPanel() {
		return this.inputPanel;
	}
	public void setInputPanel( edu.cmu.cs.dennisc.croquet.InputPanel< ? > inputPanel ) {
		this.inputPanel = inputPanel;
		this.inputPanel.addValidator( this );
	}
	
	protected String[] getLabelTexts() {
		return LABEL_TEXTS;
	}
	protected abstract edu.cmu.cs.dennisc.croquet.Component< ? >[] getComponents();
	public java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
		String[] labelTexts = this.getLabelTexts();
		edu.cmu.cs.dennisc.croquet.Component< ? >[] components = this.getComponents();
		final int N = labelTexts.length;
		for( int i=0; i<N; i++ ) {
			rv.add( 
					edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
						edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( labelTexts[ i ] ), 
						new edu.cmu.cs.dennisc.croquet.LineAxisPanel( 
								components[ i ],
								edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue()
						)
					) 
			);
		}
		return rv;
	}
	
}
