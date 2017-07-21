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

/**
 * @author Dennis Cosgrove
 */
public class PropertyOfNodeCodec<T extends edu.cmu.cs.dennisc.property.InstanceProperty<?>> implements org.lgna.croquet.ItemCodec<T> {
	private static java.util.Map<Class<?>, PropertyOfNodeCodec<?>> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized <T extends edu.cmu.cs.dennisc.property.InstanceProperty<?>> PropertyOfNodeCodec<T> getInstance( Class<T> cls ) {
		PropertyOfNodeCodec<?> rv = map.get( cls );
		if( rv != null ) {
			//pass
		} else {
			rv = new PropertyOfNodeCodec<T>( cls );
		}
		return (PropertyOfNodeCodec<T>)rv;
	}

	private Class<T> valueCls;

	private PropertyOfNodeCodec( Class<T> valueCls ) {
		this.valueCls = valueCls;
	}

	@Override
	public Class<T> getValueClass() {
		return this.valueCls;
	}

	@Override
	public T decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		boolean valueIsNotNull = binaryDecoder.decodeBoolean();
		if( valueIsNotNull ) {
			org.alice.ide.croquet.codecs.NodeCodec<org.lgna.project.ast.Node> nodeCodec = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Node.class );
			org.lgna.project.ast.Node node = nodeCodec.decodeValue( binaryDecoder );
			String name = binaryDecoder.decodeString();
			if( node != null ) {
				return (T)node.getPropertyNamed( name );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, T value ) {
		boolean valueIsNotNull = value != null;
		binaryEncoder.encode( valueIsNotNull );
		if( valueIsNotNull ) {
			org.alice.ide.croquet.codecs.NodeCodec<org.lgna.project.ast.Node> nodeCodec = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Node.class );
			org.lgna.project.ast.Node node = (org.lgna.project.ast.Node)value.getOwner();
			nodeCodec.encodeValue( binaryEncoder, node );
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "investigate value.getName() or node.getName()" );
			binaryEncoder.encode( node != null ? value.getName() : null );
		}
	}

	@Override
	public void appendRepresentation( StringBuilder sb, T value ) {
		//todo
		sb.append( value );
	}
}
