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
	BLACK( 0x282625 ),
	BROWN( 0x4f2c22 ),
	BLOND( 0xebd088 ),
	RED( 0xd36d41 ),
	GREY( 0xd5d5d5 ),

	NULL( null ),
	BARE( null ),

	BELLA( 0xe3d395 ),
	BLOOD_RED( 0xb50034 ),
	BLUE( 0x117998 ),
	GREEN( 0x2b8122 ),
	KINKY( null ),
	PINK( 0xab008b ),
	PLATINUM( 0xe3d395 ),
	PURPLE( 0xac49a5 ),
	STRAWBERRY( 0xbe2376 ),
	VIOLET( 0x6f2fba ),

	BLEND( null ),
	BLEND_BLOND( 0xd7af5b, 0x72292b ),
	BLOND_STREAK( 0xebd088, 0x4f2c22 ),
	FLAME( 0xeaad5c, 0x9d391d ),
	FREAKED( 0xebd088, 0x944e66 ),
	FROSTY_BLOND( 0xc19f52, 0x72512f ),
	FROSTY_PURPLE( 0xa346a1, 0x32163c ),
	PINK_ON_BLACK( 0xa62949, 0x000000 ),
	RAINBOW( 0xcc6999, 0x265661 ),
	RED_STREAK( 0xc13e49, 0x000000 ),
	STREAKED( 0x603879, 0xc13e49 );

	private final javax.swing.Icon icon;

	private HairColorName( javax.swing.Icon icon ) {
		this.icon = icon;
	}

	private HairColorName( int rgb ) {
		this( new edu.cmu.cs.dennisc.javax.swing.icons.PaintIcon( new java.awt.Color( rgb ) ) );
	}

	private HairColorName( int rgbTop, int rgbBottom ) {
		this( new edu.cmu.cs.dennisc.javax.swing.icons.PaintIcon( new java.awt.GradientPaint( 0, 0, new java.awt.Color( rgbTop ), 0, edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon.DEFAULT_SIZE, new java.awt.Color( rgbBottom ) ) ) );
	}

	public javax.swing.Icon getIcon() {
		return this.icon;
	}
}
