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
package org.alice.ide.javacode.croquet;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeFrameComposite extends org.lgna.croquet.FrameComposite<org.lgna.croquet.views.Panel> {
	private static final boolean IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED = false;

	private static class SingletonHolder {
		private static JavaCodeFrameComposite instance = new JavaCodeFrameComposite();
	}

	public static JavaCodeFrameComposite getInstance() {
		return SingletonHolder.instance;
	}

	private JavaCodeFrameComposite() {
		super( java.util.UUID.fromString( "7015e117-22dd-49a1-a194-55d5fe17f821" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP );
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return new org.lgna.croquet.views.ScrollPane();
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.lgna.croquet.views.BorderPanel.Builder()
				//.center( this.stringValue.createImmutableEditorPane( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED ) )
				.center( this.htmlView )
				.build();
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromHeight() {
		return 400;
	}

	@Override
	public void handlePreActivation() {
		org.alice.ide.MetaDeclarationFauxState.getInstance().addAndInvokeValueListener( this.declarationListener );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		this.htmlView.addKeyListener( this.keyListener );
		if( IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED ) {
			this.htmlView.addMouseWheelListener( this.mouseWheelListener );
		}
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		if( IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED ) {
			this.htmlView.removeMouseWheelListener( this.mouseWheelListener );
		}
		this.htmlView.removeKeyListener( this.keyListener );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		org.alice.ide.MetaDeclarationFauxState.getInstance().removeValueListener( this.declarationListener );
	}

	private void updateHtml() {
		boolean isLambdaSupported = true;
		String code;
		org.lgna.project.ast.AbstractDeclaration nextValue = org.alice.ide.MetaDeclarationFauxState.getInstance().getValue();
		if( nextValue instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)nextValue;
			code = method.generateJavaCode( isLambdaSupported );
		} else if( nextValue instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType type = (org.lgna.project.ast.NamedUserType)nextValue;
			code = type.generateJavaCode( isLambdaSupported );
		} else {
			code = null;
		}
		if( code != null ) {
			code = org.lgna.project.code.CodeFormatter.format( code );
			de.java2html.options.JavaSourceConversionOptions javaSourceConversionOptions = de.java2html.options.JavaSourceConversionOptions.getDefault();
			//javaSourceConversionOptions.setShowLineNumbers( true );
			de.java2html.javasource.JavaSource javaSource = new de.java2html.javasource.JavaSourceParser().parse( code );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( javaSource.getClassification().length );
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
		htmlView.setText( code );
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

	private final org.alice.ide.MetaDeclarationFauxState.ValueListener declarationListener = new org.alice.ide.MetaDeclarationFauxState.ValueListener() {
		public void changed( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
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

	private final org.lgna.croquet.views.HtmlView htmlView = new org.lgna.croquet.views.HtmlView();
	private int fontSize = 14;

}
