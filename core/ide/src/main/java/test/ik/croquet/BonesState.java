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

package test.ik.croquet;

import org.lgna.croquet.Application;
import org.lgna.croquet.MutableDataSingleSelectListState;
import org.lgna.ik.core.solver.Bone;
import org.lgna.ik.core.solver.Chain;
import test.ik.croquet.codecs.BoneCodec;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class BonesState extends MutableDataSingleSelectListState<Bone> {
	private static class SingletonHolder {
		private static BonesState instance = new BonesState();
	}

	public static BonesState getInstance() {
		return SingletonHolder.instance;
	}

	private BonesState() {
		super( Application.DOCUMENT_UI_GROUP, UUID.fromString( "32660ce0-bf86-4472-8f12-6aa2bc0a39b5" ), BoneCodec.SINGLETON );
	}

	public void setChain( Chain chain ) {
		if( chain == null ) {
			this.clear();
		} else {
			Bone prevBone = this.getValue();
			Bone[] bones = chain.getBones();
			int selectionIndex = -1;
			if( prevBone != null ) {
				int index = 0;
				for( Bone bone : bones ) {
					if( prevBone.getA() == bone.getA() ) {
						selectionIndex = index;
						break;
					}
					index++;
				}
			}
			this.setListData( selectionIndex, bones );
		}
	}
}
