/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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

package org.alice.netbeans.aliceprojectwizard;

import com.sun.source.tree.ModifiersTree;
import edu.cmu.cs.dennisc.java.util.Sets;

import org.netbeans.api.java.source.TreeMaker;

import org.lgna.project.ast.*;

public class ModifierUtilities {
//	private static final java.util.Map<Integer, javax.lang.model.element.Modifier> mapModifier;
//	static {
//		ModifierUtilities.mapModifier = Collections.newHashMap();
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.PUBLIC, javax.lang.model.element.Modifier.PUBLIC);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.PROTECTED, javax.lang.model.element.Modifier.PROTECTED);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.PRIVATE, javax.lang.model.element.Modifier.PRIVATE);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.ABSTRACT, javax.lang.model.element.Modifier.ABSTRACT);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.STATIC, javax.lang.model.element.Modifier.STATIC);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.FINAL, javax.lang.model.element.Modifier.FINAL);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.TRANSIENT, javax.lang.model.element.Modifier.TRANSIENT);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.VOLATILE, javax.lang.model.element.Modifier.VOLATILE);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.SYNCHRONIZED, javax.lang.model.element.Modifier.SYNCHRONIZED);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.NATIVE, javax.lang.model.element.Modifier.NATIVE);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.SYNCHRONIZED, javax.lang.model.element.Modifier.SYNCHRONIZED);
//		ModifierUtilities.mapModifier.put(java.lang.reflect.Modifier.STRICT, javax.lang.model.element.Modifier.STRICTFP);
//	}

//	public static java.util.Set<javax.lang.model.element.Modifier> createModifierSetForAccess( AccessLevel access, boolean isFinal ) {
//		java.util.Set<javax.lang.model.element.Modifier> newModifiers = Collections.newHashSet();
//		access.addModifiers(newModifiers);
//		if( isFinal ) {
//			newModifiers.add( javax.lang.model.element.Modifier.FINAL );
//		}
//		return newModifiers;
//	}
	public static ModifiersTree generateModifiersTree(TreeMaker treeMaker, java.util.Set<javax.lang.model.element.Modifier> modifiers) {
		return treeMaker.Modifiers(modifiers);
	}

	public static ModifiersTree generateModifiersTree(TreeMaker treeMaker, javax.lang.model.element.Modifier... modifiers) {
		java.util.Set<javax.lang.model.element.Modifier> set = Sets.newHashSet(modifiers);
		return generateModifiersTree(treeMaker, set);
	}
}
