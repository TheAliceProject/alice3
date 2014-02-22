/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.javacode.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeView extends org.lgna.croquet.views.HtmlView {
	private static final boolean IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED = false;

	public JavaCodeView() {
		this( null );
	}

	public JavaCodeView( org.lgna.project.ast.AbstractDeclaration declaration ) {
		this.setDeclaration( declaration );
	}

	public org.lgna.project.ast.AbstractDeclaration getDeclaration() {
		return this.declaration;
	}

	public void setDeclaration( org.lgna.project.ast.AbstractDeclaration declaration ) {
		this.declaration = declaration;
		this.updateHtml();
	}

	@Override
	protected void handleDisplayable() {
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		this.addKeyListener( this.keyListener );
		if( IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED ) {
			this.addMouseWheelListener( this.mouseWheelListener );
		}
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		if( IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED ) {
			this.removeMouseWheelListener( this.mouseWheelListener );
		}
		this.removeKeyListener( this.keyListener );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.removeProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		super.handleUndisplayable();
	}

	private void updateHtml() {
		org.lgna.project.ast.JavaCodeGenerator javaCodeGenerator = org.lgna.story.ast.JavaCodeUtilities.createJavaCodeGenerator();
		String code;
		if( this.declaration instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)this.declaration;
			code = method.generateJavaCode( javaCodeGenerator );
		} else if( this.declaration instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType type = (org.lgna.project.ast.NamedUserType)this.declaration;
			code = type.generateJavaCode( javaCodeGenerator );
		} else {
			code = null;
		}
		if( code != null ) {
			code = org.lgna.project.code.CodeFormatter.format( code );
			de.java2html.options.JavaSourceStyleTable javaSourceStyleTable = de.java2html.options.JavaSourceStyleTable.getDefault().getClone();

			de.java2html.util.RGB keywordRGB = new de.java2html.util.RGB( 0, 0, 230 );
			de.java2html.util.RGB commentRGB = new de.java2html.util.RGB( 150, 150, 150 );
			de.java2html.util.RGB stringRGB = new de.java2html.util.RGB( 206, 123, 0 );
			de.java2html.util.RGB blackRGB = new de.java2html.util.RGB( 0, 0, 0 );

			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.KEYWORD, new de.java2html.options.JavaSourceStyleEntry( keywordRGB ) );
			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.CODE_TYPE, new de.java2html.options.JavaSourceStyleEntry( keywordRGB ) );
			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.STRING, new de.java2html.options.JavaSourceStyleEntry( stringRGB ) );
			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.CHAR_CONSTANT, new de.java2html.options.JavaSourceStyleEntry( stringRGB ) );
			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.COMMENT_LINE, new de.java2html.options.JavaSourceStyleEntry( commentRGB ) );
			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.COMMENT_BLOCK, new de.java2html.options.JavaSourceStyleEntry( commentRGB ) );
			javaSourceStyleTable.put( de.java2html.javasource.JavaSourceType.NUM_CONSTANT, new de.java2html.options.JavaSourceStyleEntry( blackRGB ) );

			de.java2html.options.JavaSourceConversionOptions javaSourceConversionOptions = de.java2html.options.JavaSourceConversionOptions.getDefault().getClone();
			javaSourceConversionOptions.setStyleTable( javaSourceStyleTable );
			final boolean IS_TAB_SIZE_WORKING = false;
			if( IS_TAB_SIZE_WORKING ) {
				javaSourceConversionOptions.setTabSize( 4 );
			} else {
				code = code.replaceAll( "\t", "    " );
			}
			//javaSourceConversionOptions.setShowLineNumbers( true );
			de.java2html.javasource.JavaSource javaSource = new de.java2html.javasource.JavaSourceParser().parse( code );
			de.java2html.converter.JavaSource2HTMLConverter javaSource2HTMLConverter = new de.java2html.converter.JavaSource2HTMLConverter();
			java.io.StringWriter stringWriter = new java.io.StringWriter();
			stringWriter.write( "<html><head><style type=\"text/css\">code { font-size:" + this.fontSize + "px; }</style></head><body>" );
			try {
				javaSource2HTMLConverter.convert( javaSource, javaSourceConversionOptions, stringWriter );
				stringWriter.write( "</body></html>" );
				stringWriter.flush();
				code = stringWriter.toString();
			} catch( java.io.IOException ioe ) {
				code = "";
			}
		} else {
			code = "";
		}
		this.setText( code );
		//		final javax.swing.JScrollPane jScrollPane = this.getScrollPaneIfItExists().getAwtComponent();
		//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
		//			public void run() {
		//				jScrollPane.getViewport().setLocation( 0, 0 );
		//			}
		//		} );
	}

	private final org.alice.ide.project.events.ProjectChangeOfInterestListener projectChangeOfInterestListener = new org.alice.ide.project.events.ProjectChangeOfInterestListener() {
		public void projectChanged() {
			updateHtml();
		}
	};

	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e );
			if( e.isControlDown() ) {
				fontSize += e.getWheelRotation();
				updateHtml();
			} else {
				java.awt.Component src = e.getComponent();
				java.awt.Container parent = src.getParent();
				parent.dispatchEvent( javax.swing.SwingUtilities.convertMouseEvent( src, e, parent ) );
			}
		}
	};
	private final java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
		public void keyPressed( java.awt.event.KeyEvent e ) {
		}

		public void keyReleased( java.awt.event.KeyEvent e ) {
			int keyCode = e.getKeyCode();
			switch( keyCode ) {
			case java.awt.event.KeyEvent.VK_EQUALS: //plus
				fontSize++;
				updateHtml();
				break;
			case java.awt.event.KeyEvent.VK_MINUS: //minus
				fontSize--;
				updateHtml();
				break;
			}
		}

		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};
	private org.lgna.project.ast.AbstractDeclaration declaration;
	private int fontSize = 14;
}
