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
package org.alice.apis.moveandturn.gallery.animals.dinosaurs;
	
public class Trex extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Trex() {
		super( "Animals/Dinosaurs/trex" );
	}
	public enum Part {
		A_B_C_D_E_F_G_H( "a", "b", "c", "d", "e", "f", "g", "h" ),
		A_B_C_D_E_F_G( "a", "b", "c", "d", "e", "f", "g" ),
		A_B_C_D_E_F( "a", "b", "c", "d", "e", "f" ),
		A_B_C_D_E( "a", "b", "c", "d", "e" ),
		A_B_C_D( "a", "b", "c", "d" ),
		A_B_C( "a", "b", "c" ),
		A_B( "a", "b" ),
		A( "a" ),
		Neck_Head_Topteeth( "neck", "head", "topteeth" ),
		Neck_Head_Jaw_Bottomteeth( "neck", "head", "jaw", "bottomteeth" ),
		Neck_Head_Jaw_T3_T2_T1( "neck", "head", "jaw", "t3", "t2", "t1" ),
		Neck_Head_Jaw_T3_T2( "neck", "head", "jaw", "t3", "t2" ),
		Neck_Head_Jaw_T3( "neck", "head", "jaw", "t3" ),
		Neck_Head_Jaw( "neck", "head", "jaw" ),
		Neck_Head_Leye_Llid( "neck", "head", "leye", "llid" ),
		Neck_Head_Leye( "neck", "head", "leye" ),
		Neck_Head_Reye_Rlid( "neck", "head", "reye", "rlid" ),
		Neck_Head_Reye( "neck", "head", "reye" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" ),
		Rleg_Rshin_Rf_Rsf_Rc1_Rc2_Rc3( "rleg", "rshin", "rf", "rsf", "rc1", "rc2", "rc3" ),
		Rleg_Rshin_Rf_Rsf_Rc1_Rc2( "rleg", "rshin", "rf", "rsf", "rc1", "rc2" ),
		Rleg_Rshin_Rf_Rsf_Rc1( "rleg", "rshin", "rf", "rsf", "rc1" ),
		Rleg_Rshin_Rf_Rsf( "rleg", "rshin", "rf", "rsf" ),
		Rleg_Rshin_Rf( "rleg", "rshin", "rf" ),
		Rleg_Rshin( "rleg", "rshin" ),
		Rleg( "rleg" ),
		Lleg_Lshin_Lf_Lsf_Lc1_Lc2_Lc3( "lleg", "lshin", "lf", "lsf", "lc1", "lc2", "lc3" ),
		Lleg_Lshin_Lf_Lsf_Lc1_Lc2( "lleg", "lshin", "lf", "lsf", "lc1", "lc2" ),
		Lleg_Lshin_Lf_Lsf_Lc1( "lleg", "lshin", "lf", "lsf", "lc1" ),
		Lleg_Lshin_Lf_Lsf( "lleg", "lshin", "lf", "lsf" ),
		Lleg_Lshin_Lf( "lleg", "lshin", "lf" ),
		Lleg_Lshin( "lleg", "lshin" ),
		Lleg( "lleg" ),
		Rarm_Rforearm_Rhand_Rf1_Rf2( "rarm", "rforearm", "rhand", "rf1", "rf2" ),
		Rarm_Rforearm_Rhand_Rf1( "rarm", "rforearm", "rhand", "rf1" ),
		Rarm_Rforearm_Rhand( "rarm", "rforearm", "rhand" ),
		Rarm_Rforearm( "rarm", "rforearm" ),
		Rarm( "rarm" ),
		Larm_Lforearm_Lhand_Lf1_Lf2( "larm", "lforearm", "lhand", "lf1", "lf2" ),
		Larm_Lforearm_Lhand_Lf1( "larm", "lforearm", "lhand", "lf1" ),
		Larm_Lforearm_Lhand( "larm", "lforearm", "lhand" ),
		Larm_Lforearm( "larm", "lforearm" ),
		Larm( "larm" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public org.alice.apis.moveandturn.Model getPart( Part part ) {
		return getDescendant( org.alice.apis.moveandturn.Model.class, part.getPath() );
	}

}
