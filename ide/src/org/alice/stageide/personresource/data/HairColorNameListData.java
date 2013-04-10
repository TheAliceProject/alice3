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

import java.util.Arrays;
import java.util.Comparator;

import org.lgna.story.resources.sims2.Hair;

/**
 * @author Dennis Cosgrove
 */
public class HairColorNameListData extends org.lgna.croquet.data.RefreshableListData<String> {
	private org.lgna.story.resources.sims2.LifeStage lifeStage;
	private org.lgna.story.resources.sims2.Hair hair;

	public HairColorNameListData() {
		super( org.alice.ide.croquet.codecs.StringCodec.SINGLETON );
	}

	public org.lgna.story.resources.sims2.LifeStage getLifeStage() {
		return this.lifeStage;
	}

	public void setLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( this.lifeStage == lifeStage ) {
			//pass
		} else {
			this.lifeStage = lifeStage;
			this.refresh();
		}
	}

	public org.lgna.story.resources.sims2.Hair getHair() {
		return this.hair;
	}

	public void setHair( org.lgna.story.resources.sims2.Hair hair ) {
		if( this.hair == hair ) {
			//pass
		} else {
			this.hair = hair;
			this.refresh();
		}
	}

	public static final String[] getHairColors( Class<? extends org.lgna.story.resources.sims2.Hair> hairCls ) {
		if( hairCls == null )
		{
			return null;
		}
		org.lgna.story.resources.sims2.Hair[] hairEnums = hairCls.getEnumConstants();
		String[] hairNames = new String[ hairEnums.length ];
		for( int i = 0; i < hairEnums.length; i++ ) {
			hairNames[ i ] = hairEnums[ i ].toString();
		}
		Arrays.sort( hairNames );
		return hairNames;
	}

	public static final String[] getHairColors( org.lgna.story.resources.sims2.Hair hair ) {
		if( hair == null )
		{
			return null;
		}
		return getHairColors( hair.getClass() );
	}

	public static final org.lgna.story.resources.sims2.Hair[] getHairColorEnums( Class<? extends org.lgna.story.resources.sims2.Hair> hairCls ) {
		if( hairCls == null )
		{
			return null;
		}
		org.lgna.story.resources.sims2.Hair[] hairEnums = hairCls.getEnumConstants();
		Arrays.sort( hairEnums, new Comparator<org.lgna.story.resources.sims2.Hair>() {
			public int compare( Hair o1, Hair o2 ) {
				return o1.toString().compareTo( o2.toString() );
			}
		} );
		return hairEnums;
	}

	public static final org.lgna.story.resources.sims2.Hair[] getHairColorEnums( org.lgna.story.resources.sims2.Hair hair ) {
		if( hair == null )
		{
			return null;
		}
		return getHairColorEnums( hair.getClass() );
	}

	@Override
	protected java.util.List<String> createValues() {
		//		if( this.lifeStage != null ) {
		//			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.lifeStage.getHairColors() );
		//		} else {
		//			return java.util.Collections.emptyList();
		//		}
		if( this.hair != null ) {
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( getHairColors( this.hair ) );
		} else {
			return java.util.Collections.emptyList();
		}
	}
}
