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
	BLACK( new java.awt.Color( 0x282625 ) ),
	BROWN( new java.awt.Color( 0x4f2c22 ) ),
	BLOND( new java.awt.Color( 0xf5e29c ) ),
	RED( new java.awt.Color( 0x9e4329 ) ),
	GREY( new java.awt.Color( 0xc5c5c5 ) ),

	NULL( (javax.swing.Icon)null ),
	BARE( (javax.swing.Icon)null ),

	BELLA( (javax.swing.Icon)null ),
	BLOOD_RED( (javax.swing.Icon)null ),
	BLUE( (javax.swing.Icon)null ),
	FREAKED( (javax.swing.Icon)null ),
	FROSTY_BLOND( (javax.swing.Icon)null ),
	FROSTY_PURPLE( (javax.swing.Icon)null ),
	GREEN( new java.awt.Color( 0x8ecd49 ) ),
	KINKY( (javax.swing.Icon)null ),
	PINK( new java.awt.Color( 0xcc809f ) ),
	PLATINUM( (javax.swing.Icon)null ),
	PURPLE( new java.awt.Color( 0xac49a5 ) ),
	STRAWBERRY( new java.awt.Color( 0xbe2376 ) ),
	VIOLET( new java.awt.Color( 0x6f2fba ) ),

	BLEND( (javax.swing.Icon)null ),
	BLEND_BLOND( (javax.swing.Icon)null ),
	BLOND_STREAK( (javax.swing.Icon)null ),
	FLAME( (javax.swing.Icon)null ),
	PINK_ON_BLACK( (javax.swing.Icon)null ),
	RAINBOW( (javax.swing.Icon)null ),
	RED_STREAK( (javax.swing.Icon)null ),
	STREAKED( (javax.swing.Icon)null );

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
