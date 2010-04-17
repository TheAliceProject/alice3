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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public class KDragComponent extends KComponent< javax.swing.JComponent > {
	@Override
	protected javax.swing.JComponent createJComponent() {
		edu.cmu.cs.dennisc.zoot.ZDragComponent rv = new edu.cmu.cs.dennisc.zoot.ZDragComponent() {

			@Override
			protected void fillBounds(java.awt.Graphics2D g2, int x, int y, int width, int height) {
				g2.fillRect( x, y, width, height );
			}

			@Override
			protected int getInsetBottom() {
				return 0;
			}

			@Override
			protected int getInsetLeft() {
				return 0;
			}

			@Override
			protected int getInsetRight() {
				return 0;
			}

			@Override
			protected int getInsetTop() {
				return 0;
			}

			@Override
			protected void paintPrologue(java.awt.Graphics2D g2, int x, int y, int width, int height) {
			}
			@Override
			protected void paintEpilogue(java.awt.Graphics2D g2, int x, int y, int width, int height) {
			}
			
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				g2.setColor( new java.awt.Color( 191, 191, 255 ) );
				this.fillBounds(g2, 0, 0, this.getWidth(), this.getHeight());
			}
		};
		rv.setDragAndDropOperation( new edu.cmu.cs.dennisc.zoot.AbstractDragAndDropOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP ) {
			public java.util.List<? extends edu.cmu.cs.dennisc.zoot.DropReceptor> createListOfPotentialDropReceptors(edu.cmu.cs.dennisc.zoot.ZDragComponent dragSource) {
				return java.util.Collections.EMPTY_LIST;
			}
			public void handleDragEnteredDropReceptor(edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext) {
			}
			public void handleDragExitedDropReceptor(edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext) {
			}
			public void handleDragStarted(edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext) {
			}
			public void handleDragStopped(edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext) {
			}
			
		} );
		return rv;
	}
}
