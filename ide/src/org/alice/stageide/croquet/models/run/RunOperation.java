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
package org.alice.stageide.croquet.models.run;

class RunIcon implements javax.swing.Icon {
	//private static final java.awt.Color ENABLED_CIRCLE_COLOR = java.awt.Color.GREEN.darker();
	//private static final java.awt.Color DISABLED_CIRCLE_COLOR = java.awt.Color.GRAY;
	
	private static final java.awt.Color ROLLOVER_COLOR = new java.awt.Color( 191, 255, 191 );
	private static final java.awt.Color PRESSED_COLOR = new java.awt.Color( 63, 127, 63 );
	private static final java.awt.Color ENABLED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( ROLLOVER_COLOR, PRESSED_COLOR, 0.5f );
	private static final java.awt.Color DISABLED_COLOR = java.awt.Color.GRAY;
	
	public int getIconHeight() {
		return 24;
	}
	public int getIconWidth() {
		return 24;
	}
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		if( c instanceof javax.swing.AbstractButton ) {
			javax.swing.ButtonModel buttonModel = ((javax.swing.AbstractButton)c).getModel();
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			java.awt.Color prevColor = g2.getColor();
			Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
			try {
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				int w = this.getIconWidth();
				int h = this.getIconHeight();
				int offset = w / 5;
				int x0 = x + offset * 2;
				int x1 = x + w - offset;
	
				int y0 = y + offset;
				int y1 = y + h - offset;
				int yC = (y0 + y1) / 2;
	
				int[] xs = { x0, x1, x0 };
				int[] ys = { y0, yC, y1 };
	
				if( buttonModel.isEnabled() ) {
					if( buttonModel.isPressed() ) {
						g2.setColor( PRESSED_COLOR );
					} else {
						if( buttonModel.isRollover() || buttonModel.isArmed() ) {
							g2.setColor( ROLLOVER_COLOR );
						} else {
							g2.setColor( ENABLED_COLOR );
						}
					}
				} else {
					g2.setColor( DISABLED_COLOR );
				}
				        
				g2.fillPolygon( xs, ys, 3 );
				
				g2.setColor( java.awt.Color.DARK_GRAY );
				g2.drawPolygon( xs, ys, 3 );
			} finally {
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
				g2.setColor( prevColor );
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RunOperation extends edu.cmu.cs.dennisc.croquet.DialogOperation {
	private static class SingletonHolder {
		private static RunOperation instance = new RunOperation();
	}
	public static RunOperation getInstance() {
		return SingletonHolder.instance;
	}
	private RunOperation() {
		super( org.alice.ide.IDE.RUN_GROUP, java.util.UUID.fromString( "985b3795-e1c7-4114-9819-fae4dcfe5676" ) );
		this.setSmallIcon( new RunIcon() );
	}
	private java.awt.Point location = new java.awt.Point( 100, 100 );
	private java.awt.Dimension size = null;
	@Override
	protected java.awt.Point getDesiredDialogLocation( edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
		return this.location;
	}
	@Override
	protected java.awt.Dimension getDesiredDialogSize( edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
		return this.size;
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Container< ? > createContentPane( edu.cmu.cs.dennisc.croquet.DialogOperationContext context, edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
		org.alice.stageide.StageIDE ide = (org.alice.stageide.StageIDE)org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		if( ide.getProject() != null ) {
			ide.ensureProjectCodeUpToDate();
			edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = ide.createVirtualMachineForRuntimeProgram();
			vm.registerAnonymousAdapter( org.alice.apis.moveandturn.event.MouseButtonListener.class, org.alice.stageide.apis.moveandturn.event.MouseButtonAdapter.class );
			vm.registerAnonymousAdapter( org.alice.apis.moveandturn.event.KeyListener.class, org.alice.stageide.apis.moveandturn.event.KeyAdapter.class );
			vm.setEntryPointType( ide.getProgramType() );
			org.alice.stageide.MoveAndTurnRuntimeProgram rtProgram = new org.alice.stageide.MoveAndTurnRuntimeProgram( ide.getSceneType(), vm );

			String[] args = {};
			rtProgram.showInAWTContainer( rv.getAwtComponent(), dialog.getAwtComponent(), args );

			ide.disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM );
		} else {
			ide.showMessageDialog( "Please open a project first." );
		}
		return rv;
	}
	@Override
	protected void releaseContentPane( edu.cmu.cs.dennisc.croquet.DialogOperationContext context, edu.cmu.cs.dennisc.croquet.Dialog dialog, edu.cmu.cs.dennisc.croquet.Container< ? > contentPane ) {
		//todo: investigate		
		this.location = dialog.getLocation();
		this.size = dialog.getSize();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: releaseContentPane" );
	}
	
	@Override
	protected void handleFinally( edu.cmu.cs.dennisc.croquet.DialogOperationContext context, edu.cmu.cs.dennisc.croquet.Dialog dialog, edu.cmu.cs.dennisc.croquet.Container< ? > contentPane ) {
		super.handleFinally( context, dialog, contentPane );
		org.alice.ide.IDE.getSingleton().enableRendering();
	}
}
