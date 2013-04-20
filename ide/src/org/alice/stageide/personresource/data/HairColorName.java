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
package org.alice.stageide.personresource.data;

/**
 * @author Dennis Cosgrove
 */
public enum HairColorName {
	BLACK( new java.awt.Color( 0, 0, 0 ) ),
	BROWN( new java.awt.Color( 162, 42, 42 ) ),
	BLOND( new java.awt.Color( 255, 255, 0 ) ),
	RED( new java.awt.Color( 255, 0, 0 ) ),
	GREY( new java.awt.Color( 127, 127, 127 ) ),

	NULL( (javax.swing.Icon)null ),
	BARE( new java.awt.Color( 0, 0, 0 ) ),

	BELLA( new java.awt.Color( 0, 0, 0 ) ),
	BLEND( new java.awt.Color( 0, 0, 0 ) ),
	BLEND_BLOND( new java.awt.Color( 0, 0, 0 ) ),
	BLOND_STREAK( new java.awt.Color( 0, 0, 0 ) ),
	BLOOD_RED( new java.awt.Color( 0, 0, 0 ) ),
	BLUE( new java.awt.Color( 0, 0, 0 ) ),
	FLAME( new java.awt.Color( 0, 0, 0 ) ),
	FREAKED( new java.awt.Color( 0, 0, 0 ) ),
	FROSTY_BLOND( new java.awt.Color( 0, 0, 0 ) ),
	FROSTY_PURPLE( new java.awt.Color( 0, 0, 0 ) ),
	GREEN( new java.awt.Color( 0, 0, 0 ) ),
	KINKY( new java.awt.Color( 0, 0, 0 ) ),
	PINK( new java.awt.Color( 0, 0, 0 ) ),
	PINK_ON_BLACK( new java.awt.Color( 0, 0, 0 ) ),
	PLATINUM( new java.awt.Color( 0, 0, 0 ) ),
	PURPLE( new java.awt.Color( 0, 0, 0 ) ),
	RAINBOW( new java.awt.Color( 0, 0, 0 ) ),
	RED_STREAK( new java.awt.Color( 0, 0, 0 ) ),
	STRAWBERRY( new java.awt.Color( 0, 0, 0 ) ),
	STREAKED( new java.awt.Color( 0, 0, 0 ) ),
	VIOLET( new java.awt.Color( 0, 0, 0 ) );

	private final javax.swing.Icon icon;

	private HairColorName( javax.swing.Icon icon ) {
		this.icon = icon;
	}

	private HairColorName( java.awt.Paint paint ) {
		this( new org.alice.ide.swing.icons.PaintIcon( paint ) );
	}

	public javax.swing.Icon getIcon() {
		return this.icon;
	}
}
