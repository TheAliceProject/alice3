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

package org.alice.ide.x;

/**
 * @author Dennis Cosgrove
 */
public abstract class I18nFactory {
	protected abstract org.lgna.croquet.components.JComponent< ? > createGetsComponent( boolean isTowardLeadingEdge );
	protected abstract org.lgna.croquet.components.JComponent< ? > createTypeComponent( org.lgna.project.ast.AbstractType< ?,?,? > type );

	protected abstract org.lgna.croquet.components.JComponent< ? > createPropertyComponent( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property, int underscoreCount );
	
	protected org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.GetsChunk getsChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		return this.createGetsComponent( getsChunk.isTowardLeading() );
	}
	protected org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.TextChunk textChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		return new org.lgna.croquet.components.Label( textChunk.getText() );
	}	
	protected org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.PropertyChunk propertyChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		int underscoreCount = propertyChunk.getUnderscoreCount();
		String propertyName = propertyChunk.getPropertyName();
		edu.cmu.cs.dennisc.property.InstanceProperty< ? > property = owner.getInstancePropertyNamed( propertyName );
		return createPropertyComponent( property, underscoreCount );
	}
	protected org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.MethodInvocationChunk methodInvocationChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		String methodName = methodInvocationChunk.getMethodName();
		org.lgna.croquet.components.JComponent< ? > rv;
		if( owner instanceof org.lgna.project.ast.AbstractDeclaration && methodName.equals( "getName" ) ) {
			org.lgna.project.ast.AbstractDeclaration declaration = (org.lgna.project.ast.AbstractDeclaration)owner;
			org.alice.ide.ast.components.DeclarationNameLabel label = new org.alice.ide.ast.components.DeclarationNameLabel( declaration );
			if( declaration instanceof org.lgna.project.ast.AbstractMethod ) {
				org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)declaration;
				if( method.getReturnType() == org.lgna.project.ast.JavaType.VOID_TYPE ) {
					label.scaleFont( 1.1f );
					label.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
				}
			}
			rv = label;
		} else if( owner instanceof org.lgna.project.ast.Argument && methodName.equals( "getParameterNameText" ) ) {
			org.lgna.project.ast.Argument argument = (org.lgna.project.ast.Argument)owner;
			rv = new org.alice.ide.ast.components.DeclarationNameLabel( argument.parameter.getValue() );
		} else if( owner instanceof org.lgna.project.ast.AbstractConstructor && methodName.equals( "getDeclaringType" ) ) {
			org.lgna.project.ast.AbstractConstructor constructor = (org.lgna.project.ast.AbstractConstructor)owner;
			rv = this.createTypeComponent( constructor.getDeclaringType() );
		} else {
			java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( owner.getClass(), methodName );
			Object o = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( owner, mthd );
			String s;
			if( o != null ) {
				if( o instanceof org.lgna.project.ast.AbstractType<?,?,?> ) {
					s = ((org.lgna.project.ast.AbstractType<?,?,?>)o).getName();
				} else {
					s = o.toString();
				}
			} else {
				s = null;
			}
			rv = new org.lgna.croquet.components.Label( s );
		}
		return rv;
	}

	protected org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.Chunk chunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		if( chunk instanceof org.alice.ide.i18n.TextChunk ) {
			return createComponent( (org.alice.ide.i18n.TextChunk)chunk, owner );
		} else if( chunk instanceof org.alice.ide.i18n.PropertyChunk ) {
			return createComponent( (org.alice.ide.i18n.PropertyChunk)chunk, owner );
		} else if( chunk instanceof org.alice.ide.i18n.MethodInvocationChunk ) {
			return createComponent( (org.alice.ide.i18n.MethodInvocationChunk)chunk, owner );
		} else if( chunk instanceof org.alice.ide.i18n.GetsChunk ) {
			return createComponent( (org.alice.ide.i18n.GetsChunk)chunk, owner );
		} else {
			return new org.lgna.croquet.components.Label( "unhandled: " + chunk.toString() );
		}
	}
	protected int getPixelsPerIndent() {
		return 4;
	}
	protected org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.Line line, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		int indentCount = line.getIndentCount();
		org.alice.ide.i18n.Chunk[] chunks = line.getChunks();
		assert chunks.length > 0 : owner;
		if( indentCount > 0 || chunks.length > 1 ) {
			org.lgna.croquet.components.LineAxisPanel rv = new org.lgna.croquet.components.LineAxisPanel();
			if( indentCount > 0 ) {
				rv.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( indentCount * this.getPixelsPerIndent() ) );
			}
			for( org.alice.ide.i18n.Chunk chunk : chunks ) {
				org.lgna.croquet.components.Component< ? > component = createComponent( chunk, owner );
				assert component != null : chunk.toString();
//				rv.setAlignmentY( 0.5f );
				rv.addComponent( component );
			}
			return rv;
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping line" );
			org.lgna.croquet.components.JComponent< ? > rv = createComponent( chunks[ 0 ], owner );
			assert rv != null : chunks[ 0 ].toString();
			return rv;
		}
	}
	public org.lgna.croquet.components.JComponent< ? > createComponent( org.alice.ide.i18n.Page page, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		org.alice.ide.i18n.Line[] lines = page.getLines();
		final int N = lines.length;
		assert N > 0;
		if( N > 1 ) {
			final boolean isLoop = lines[ N-1 ].isLoop();
			org.lgna.croquet.components.PageAxisPanel pagePane = new org.lgna.croquet.components.PageAxisPanel() {
				@Override
				protected javax.swing.JPanel createJPanel() {
					return new DefaultJPanel() {
						@Override
						protected void paintComponent(java.awt.Graphics g) {
							java.awt.Color prev = g.getColor();
							if( isLoop ) {
								int n = this.getComponentCount();
								java.awt.Component cFirst = this.getComponent( 0 );
								java.awt.Component cLast = this.getComponent( n-1 );
								g.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 160 ) );
								int xB = I18nFactory.this.getPixelsPerIndent();
								int xA = xB/2;
								int yTop = cFirst.getY() + cFirst.getHeight();
								int yBottom = cLast.getY() + cLast.getHeight()/2;
								g.drawLine( xA, yTop, xA, yBottom );
								g.drawLine( xA, yBottom, xB, yBottom );

								int xC = cLast.getX() + cLast.getWidth();
								int xD = xC + I18nFactory.this.getPixelsPerIndent();;
								g.drawLine( xC, yBottom, xD, yBottom );
								g.drawLine( xD, yBottom, xD, cLast.getY() );
								
								final int HALF_TRIANGLE_WIDTH = 3;
								edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.NORTH, xA-HALF_TRIANGLE_WIDTH, yTop, HALF_TRIANGLE_WIDTH+1+HALF_TRIANGLE_WIDTH, 10 );
							}
							g.setColor( prev );
							super.paintComponent(g);
						}
					};
				}
			};
			for( org.alice.ide.i18n.Line line : lines ) {
				pagePane.addComponent( createComponent( line, owner ) );
			}
			pagePane.revalidateAndRepaint();
			return pagePane;
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping page" );
			return createComponent( lines[ 0 ], owner );
		}
	}
	public org.lgna.croquet.components.JComponent< ? > createComponent( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		org.lgna.croquet.components.JComponent< ? > rv;
		if( owner != null ) {
			Class< ? > cls = owner.getClass();
			String value = edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "org.alice.ide.formatter.Templates", org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getLocale() );
			org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( value );
			rv = createComponent( page, owner );
		} else {
			rv = new org.lgna.croquet.components.Label( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getTextForNull() );
		}
		return rv;
	}
}
