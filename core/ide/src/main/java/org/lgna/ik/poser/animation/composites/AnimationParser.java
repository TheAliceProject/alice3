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
package org.lgna.ik.poser.animation.composites;

import java.util.ArrayList;

import org.lgna.ik.poser.PoseAstUtilities;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.KeyFrameStyles;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.AnimationStyle;
import org.lgna.story.Pose;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Crawlable;
import edu.cmu.cs.dennisc.pattern.Crawler;

/**
 * @author Matt May
 */
public class AnimationParser implements Crawler {

	org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();
	double currentTime = 0;
	ArrayList<KeyFrameData> dataList = Lists.newArrayList();
	ArrayList<AnimationStyle> styleList = Lists.newArrayList();

	public static ArrayList<KeyFrameData> initializeAndParse( UserMethod animation ) {
		AnimationParser parser = new AnimationParser();
		animation.crawl( parser, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
		return parser.getKeyFrames();
	}

	private ArrayList<KeyFrameData> getKeyFrames() {
		for( int i = 0; i != dataList.size(); ++i ) {
			AnimationStyle second = AnimationStyle.BEGIN_AND_END_GENTLY;
			if( i < ( dataList.size() - 1 ) ) {
				second = styleList.get( i + 1 );
			}
			dataList.get( 0 ).setStyle( KeyFrameStyles.getKeyFrameStyleFromTwoAnimationStyles(
					styleList.get( i ), second ) );
		}
		return dataList;
	}

	@Override
	public void visit( Crawlable crawlable ) {
		if( crawlable instanceof MethodInvocation ) {

			MethodInvocation methodInv = (MethodInvocation)crawlable;
			if( PoseAstUtilities.isStrikePoseMethod( methodInv.method.getValue() ) ) {
				Pose pose = null;
				double duration = 0;
				AnimationStyle style = null;
				ArrayList<Expression> list = Lists.newArrayList();
				list.add( methodInv.requiredArguments.get( 0 ).expression.getValue() );
				for( JavaKeyedArgument kArg : methodInv.keyedArguments ) {
					Expression value = ( (MethodInvocation)kArg.expression.getValue() ).requiredArguments.get( 0 ).expression.getValue();
					if( value instanceof DoubleLiteral ) {
						duration = ( (DoubleLiteral)value ).value.getValue();
					} else {
						list.add( kArg.expression.getValue() );
					}
				}
				Object[] argArr = vm.ENTRY_POINT_evaluate( null, list.toArray( new Expression[ 0 ] ) );
				for( Object o : argArr ) {
					if( o instanceof Pose ) {
						pose = (Pose)o;
					} else if( o instanceof AnimationStyle ) {
						style = (AnimationStyle)o;
					} else {
						System.out.println( "asfd: " + o.getClass() );
					}
				}
				//				assert duration > 0;
				assert pose != null;
				assert style != null;
				dataList.add( new KeyFrameData( currentTime + duration, pose ) );
				styleList.add( style );
			}
		}
	}
}
