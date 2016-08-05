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

package org.alice.ide.croquet.edits.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class ParameterEdit extends org.lgna.croquet.edits.AbstractEdit<org.lgna.croquet.CompletionModel> {
	private final org.lgna.project.ast.UserCode code;
	private final org.lgna.project.ast.UserParameter parameter;
	private transient java.util.Map<org.lgna.project.ast.SimpleArgumentListProperty, org.lgna.project.ast.SimpleArgument> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public ParameterEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.UserCode code, org.lgna.project.ast.UserParameter parameter ) {
		super( completionStep );
		this.code = code;
		this.parameter = parameter;
	}

	public ParameterEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		this.code = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserCode.class ).decodeValue( binaryDecoder );
		this.parameter = org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserParameter.class ).decodeValue( binaryDecoder );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserCode.class ).encodeValue( binaryEncoder, this.code );
		org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserParameter.class ).encodeValue( binaryEncoder, this.parameter );
	}

	public org.lgna.project.ast.UserCode getCode() {
		return this.code;
	}

	public org.lgna.project.ast.UserParameter getParameter() {
		return this.parameter;
	}

	protected final org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserParameter> getParametersProperty() {
		return this.code.getRequiredParamtersProperty();
	}

	protected void addParameter( int index ) {
		org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserParameter> parametersProperty = this.getParametersProperty();
		//todo
		org.lgna.project.ast.UserCode code = (org.lgna.project.ast.UserCode)parametersProperty.getOwner();
		org.lgna.project.ast.AstUtilities.addParameter( this.map, parametersProperty, this.parameter, index, org.alice.ide.IDE.getActiveInstance().getArgumentLists( code ) );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	protected void removeParameter( int index ) {
		org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserParameter> parametersProperty = this.getParametersProperty();
		//todo
		org.lgna.project.ast.UserCode code = (org.lgna.project.ast.UserCode)parametersProperty.getOwner();
		org.lgna.project.ast.AstUtilities.removeParameter( this.map, parametersProperty, this.parameter, index, org.alice.ide.IDE.getActiveInstance().getArgumentLists( code ) );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
	}

	@Override
	public boolean canUndo() {
		return true;
	}

	@Override
	public boolean canRedo() {
		return true;
	}
}
