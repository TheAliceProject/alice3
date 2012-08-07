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
package org.alice.ide.warning;

/**
 * @author Dennis Cosgrove
 */
public class WarningDialogComposite extends org.lgna.croquet.PlainDialogOperationComposite< org.alice.ide.warning.components.WarningView > {
	private static class SingletonHolder {
		private static WarningDialogComposite instance = new WarningDialogComposite();
	}
	public static WarningDialogComposite getInstance() {
		return SingletonHolder.instance;
	}
	private final org.lgna.croquet.PlainStringValue descriptionText;
	private WarningDialogComposite() {
		super( java.util.UUID.fromString( "741c9139-a58d-46d6-ba0e-9a8e51f27980" ), org.lgna.croquet.Application.INFORMATION_GROUP );
		StringBuilder sb = new StringBuilder();
		sb.append( "WARNING: Alice3 is not for the faint of heart.\n\n" );
		sb.append( "Alice3 is currently under development.\n" );
		sb.append( "We are working very hard to make this dialog box obsolete.\n" );
		sb.append( "Thank you for your patience.\n" );
		sb.append( "We welcome your feedback.\n" );
		this.descriptionText = this.createUnlocalizedPlainStringValue( sb.toString() );
	}
	public org.lgna.croquet.PlainStringValue getDescriptionText() {
		return this.descriptionText;
	}
	@Override
	protected org.alice.ide.warning.components.WarningView createView() {
		return new org.alice.ide.warning.components.WarningView( this );
	}
	public static void main( String[] args ) {
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		WarningDialogComposite.getInstance().getOperation().fire();
		System.exit( 0 );
	}
}
