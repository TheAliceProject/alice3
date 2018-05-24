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
package org.alice.ide.croquet.codecs;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ProjectStack;
import org.lgna.croquet.Application;
import org.lgna.croquet.ItemCodec;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NodeUtilities;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class NodeCodec<T extends Node> implements ItemCodec<T> {
	private static Map<Class<?>, NodeCodec<?>> map = Maps.newHashMap();

	public static synchronized <T extends Node> NodeCodec<T> getInstance( Class<T> cls ) {
		NodeCodec<?> rv = map.get( cls );
		if( rv != null ) {
			//pass
		} else {
			rv = new NodeCodec<T>( cls );
		}
		return (NodeCodec<T>)rv;
	}

	private static Map<UUID, Node> mapIdToNode = Maps.newWeakHashMap();

	public static void addNodeToGlobalMap( Node node ) {
		mapIdToNode.put( node.getId(), node );
	}

	public static void removeNodeFromGlobalMap( Node node ) {
		mapIdToNode.remove( node.getId() );
	}

	private Class<T> valueCls;

	private NodeCodec( Class<T> valueCls ) {
		this.valueCls = valueCls;
	}

	@Override
	public Class<T> getValueClass() {
		return this.valueCls;
	}

	@Override
	public T decodeValue( BinaryDecoder binaryDecoder ) {
		boolean valueIsNotNull = binaryDecoder.decodeBoolean();
		if( valueIsNotNull ) {
			UUID id = binaryDecoder.decodeId();
			if( mapIdToNode.containsKey( id ) ) {
				return (T)mapIdToNode.get( id );
			} else {
				Project project = ProjectStack.peekProject();
				return ProgramTypeUtilities.lookupNode( project, id );
			}
		} else {
			return null;
		}
	}

	@Override
	public void encodeValue( BinaryEncoder binaryEncoder, T value ) {
		boolean valueIsNotNull = value != null;
		binaryEncoder.encode( valueIsNotNull );
		if( valueIsNotNull ) {
			binaryEncoder.encode( value.getId() );
		}
	}

	@Override
	public void appendRepresentation( StringBuilder sb, T value ) {
		NodeUtilities.safeAppendRepr( sb, value, Application.getLocale() );
	}
}
