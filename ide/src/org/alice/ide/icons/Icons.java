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

package org.alice.ide.icons;

/**
 * @author Dennis Cosgrove
 */
public class Icons {
	private Icons() {
		throw new AssertionError();
	}

	public static final int SMALL_WIDTH = 24;
	public static final int SMALL_HEIGHT = 24;

	public static final javax.swing.Icon EMPTY_HEIGHT_ICON_SMALL = new edu.cmu.cs.dennisc.javax.swing.icons.EmptyIcon( 0, SMALL_HEIGHT );
	public static final javax.swing.Icon BOOKMARK_ICON_LARGE = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( Icons.class.getResource( "images/256x256/bookmark.png" ) );
	public static final javax.swing.Icon BOOKMARK_ICON_SMALL = new edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon( BOOKMARK_ICON_LARGE, SMALL_WIDTH, SMALL_HEIGHT );

	public static final javax.swing.Icon FOLDER_ICON_SMALL = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( Icons.class.getResource( "images/24x24/folder.png" ) );
	public static final javax.swing.Icon FOLDER_BACK_ICON_LARGE = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( Icons.class.getResource( "images/160x120/folderBack.png" ) );
	public static final javax.swing.Icon FOLDER_FRONT_ICON_LARGE = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( Icons.class.getResource( "images/160x120/folderFront.png" ) );

	public static final javax.swing.Icon NEXT_SMALL = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( Icons.class.getResource( "images/24x24/go-next.png" ) );
	public static final javax.swing.Icon PREVIOUS_SMALL = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( Icons.class.getResource( "images/24x24/go-previous.png" ) );
}
