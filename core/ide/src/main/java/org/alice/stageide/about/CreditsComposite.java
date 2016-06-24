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

package org.alice.stageide.about;

/**
 * @author Dennis Cosgrove
 */
public class CreditsComposite extends org.lgna.croquet.LazyOperationUnadornedDialogCoreComposite<org.lgna.croquet.views.Panel> {
	public CreditsComposite() {
		super( java.util.UUID.fromString( "05cb2e31-928a-461a-9695-9e2783b651a4" ) );
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Alice 3 is designed and implemented by <strong>Dennis Cosgrove</strong>, <strong>David Culyba</strong>, and <strong>Matt May</strong>.<p>" );
		sb.append( "It is inspired by many systems that have preceded it, most notably <strong>Caitlin Kelleher</strong>'s dissertation: Storytelling Alice.<p>" );
		sb.append( "Great thanks are owed to the members of Caitlin Kelleher's Looking Glass Research Group, specifically:<br><strong>Kyle Harms</strong> for his work on the Croquet toolkit and <br><strong>Gazihan Alankus</strong> for his work on inverse kinematics.<p>" );
		sb.append( "Many have created custom art assets including <strong>Laura Paoletti</strong> and <strong>John DeRiggi</strong>.<p>" );
		sb.append( "Technical and administrative help provided by <strong>Gabe Yu</strong>, <strong>Cleah Schlueter</strong>, and <strong>5teve Audia</strong>.<p>" );
		sb.append( "Songs should be written about the instructors who bravely adopted Alice 3 in the alpha and beta stages, specifically <strong>Wanda Dann</strong> and <strong>Don Slater</strong>.<p>" );
		sb.append( "A special thank you to <strong>Steve Seabolt</strong> and <strong>JoAnn Covington</strong> for faciliating the donation of The Sims <sup>TM</sup> 2 Art Assets.<p>" );

		org.lgna.croquet.views.HtmlMultiLineLabel creditsLabel = new org.lgna.croquet.views.HtmlMultiLineLabel( sb.toString() );
		org.lgna.croquet.views.Panel rv = new org.lgna.croquet.views.BorderPanel.Builder().center( creditsLabel ).build();
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 16, 16, 16, 16 ) );
		return rv;
	}
}
