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

import java.io.File;

/**
 * @author Dennis Cosgrove
 */
public class Config {
	public static class Builder {
		Builder mode( Mode mode ) {
			this.mode = mode;
			return this;
		}

		Builder rootDir( File rootDir ) {
			assert rootDir != null;
			assert rootDir.exists() : rootDir;
			assert rootDir.isDirectory() : rootDir;
			this.rootDir = rootDir;
			return this;
		}

		Builder isPlugin8Desired( boolean isPlugin8Desired ) {
			this.isPlugin8Desired = isPlugin8Desired;
			return this;
		}

		Builder isJavaDocGenerationDesired( boolean isJavaDocGenerationDesired ) {
			this.isJavaDocGenerationDesired = isJavaDocGenerationDesired;
			return this;
		}

		Builder isCleanDesired( boolean isCleanDesired ) {
			this.isCleanDesired = isCleanDesired;
			return this;
		}

		Builder joglVersion( String joglVersion ) {
			this.joglVersion = joglVersion;
			return this;
		}

		Builder aliceModelSourceVersion( String aliceModelSourceVersion ) {
			this.aliceModelSourceVersion = aliceModelSourceVersion;
			return this;
		}

		Builder nebulousModelSourceVersion( String nebulousModelSourceVersion ) {
			this.nebulousModelSourceVersion = nebulousModelSourceVersion;
			return this;
		}

		Builder netBeans8Version( String netBeans8Version ) {
			this.netBeans8Version = netBeans8Version;
			return this;
		}

		public Config build() {
			return new Config( this );
		}

		private Mode mode;
		private File rootDir;
		private boolean isPlugin8Desired = true;
		private boolean isJavaDocGenerationDesired = true;
		private boolean isCleanDesired = true;
		private String joglVersion;
		private String aliceModelSourceVersion;
		private String nebulousModelSourceVersion;
		private String netBeans8Version;
	}

	private Config( Builder builder ) {
		assert builder.mode != null : builder;
		this.mode = builder.mode;

		assert builder.rootDir != null : builder;
		this.rootDir = builder.rootDir;

		this.isPlugin8Desired = builder.isPlugin8Desired;
		this.isJavaDocGenerationDesired = builder.isJavaDocGenerationDesired;
		this.isCleanDesired = builder.isCleanDesired;

		assert builder.joglVersion != null : builder;
		this.joglVersion = builder.joglVersion;

		assert builder.aliceModelSourceVersion != null : builder;
		this.aliceModelSourceVersion = builder.aliceModelSourceVersion;

		assert builder.nebulousModelSourceVersion != null : builder;
		this.nebulousModelSourceVersion = builder.nebulousModelSourceVersion;

		assert builder.netBeans8Version != null : builder;
		this.netBeans8Version = builder.netBeans8Version;

	}

	Mode getMode() {
		return this.mode;
	}

	File getRootDir() {
		return this.rootDir;
	}

	boolean isPlugin8Desired() {
		return this.isPlugin8Desired;
	}

	boolean isJavaDocGenerationDesired() {
		return this.isJavaDocGenerationDesired;
	}

	boolean isCleanDesired() {
		return this.isCleanDesired;
	}

	String getJoglVersion() {
		return this.joglVersion;
	}

	String getAliceModelSourceVersion() {
		return this.aliceModelSourceVersion;
	}

	String getNebulousModelSourceVersion() {
		return this.nebulousModelSourceVersion;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "\n\tmode: " );
		sb.append( this.mode );
		sb.append( "\n\trootDir: " );
		sb.append( this.rootDir );
		sb.append( "\n\tjoglVersion: " );
		sb.append( this.joglVersion );
		sb.append( "\n\taliceModelSourceVersionn: " );
		sb.append( this.aliceModelSourceVersion );
		sb.append( "\n\tnebulousModelSourceVersion: " );
		sb.append( this.nebulousModelSourceVersion );
		sb.append( "\n\tnetBeans8Version: " );
		sb.append( this.netBeans8Version );
		return sb.toString();
	}

	private final Mode mode;
	private final File rootDir;
	private final boolean isPlugin8Desired;
	private final boolean isJavaDocGenerationDesired;
	private final boolean isCleanDesired;
	private final String joglVersion;
	private final String aliceModelSourceVersion;
	private final String nebulousModelSourceVersion;
	private final String netBeans8Version;
}
