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
package org.alice.ide.x.components;

/**
 * @author Dennis Cosgrove
 */
public class ResourcePropertyView extends org.alice.ide.croquet.components.AbstractPropertyPane<org.lgna.project.ast.ResourceProperty, org.lgna.common.Resource> {
	private static final java.text.NumberFormat DURATION_FORMAT = new java.text.DecimalFormat( "0.00" );
	private final org.lgna.croquet.views.Label label = new org.lgna.croquet.views.Label();
	private org.lgna.common.Resource prevResource;
	private edu.cmu.cs.dennisc.pattern.event.NameListener nameListener;

	public ResourcePropertyView( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.ResourceProperty property ) {
		super( factory, property, javax.swing.BoxLayout.LINE_AXIS );
		this.addComponent( this.label );
		this.refreshLater();
	}

	private edu.cmu.cs.dennisc.pattern.event.NameListener getNameListener() {
		if( this.nameListener != null ) {
			//pass
		} else {
			this.nameListener = new edu.cmu.cs.dennisc.pattern.event.NameListener() {
				@Override
				public void nameChanging( edu.cmu.cs.dennisc.pattern.event.NameEvent nameEvent ) {
				}

				@Override
				public void nameChanged( edu.cmu.cs.dennisc.pattern.event.NameEvent nameEvent ) {
					ResourcePropertyView.this.refreshLater();
				}
			};
		}
		return this.nameListener;
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		//refresh takes care of name listener
	}

	@Override
	protected void handleUndisplayable() {
		if( this.prevResource != null ) {
			this.prevResource.removeNameListener( this.getNameListener() );
		}
		super.handleUndisplayable();
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		if( this.prevResource != null ) {
			this.prevResource.removeNameListener( this.getNameListener() );
		}
		org.lgna.common.Resource nextResource = getProperty().getValue();
		StringBuffer sb = new StringBuffer();
		if( nextResource != null ) {
			sb.append( "<html>" );
			sb.append( nextResource.getName() );
			if( nextResource instanceof org.lgna.common.resources.AudioResource ) {
				org.lgna.common.resources.AudioResource audioResource = (org.lgna.common.resources.AudioResource)nextResource;
				double duration = audioResource.getDuration();
				if( Double.isNaN( duration ) ) {
					//pass
				} else {
					sb.append( "<font color=\"gray\">" );
					sb.append( "<i>" );
					sb.append( " (" + DURATION_FORMAT.format( duration ) + "s) " );
					sb.append( "</i>" );
					sb.append( "</font>" );
				}
			} else if( nextResource instanceof org.lgna.common.resources.ImageResource ) {
				org.lgna.common.resources.ImageResource imageResource = (org.lgna.common.resources.ImageResource)nextResource;
				int width = imageResource.getWidth();
				int height = imageResource.getHeight();
				if( ( width >= 0 ) && ( height >= 0 ) ) {
					sb.append( "<font color=\"gray\">" );
					sb.append( "<i>" );
					sb.append( " (" + width + "x" + height + ") " );
					sb.append( "</i>" );
					sb.append( "</font>" );
				}
			}
			sb.append( "</html>" );
		} else {
			sb.append( org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue().getTextForNull() );
		}
		this.label.setText( sb.toString() );
		if( nextResource != null ) {
			nextResource.addNameListener( this.getNameListener() );
		}
		this.prevResource = nextResource;
	}
}
