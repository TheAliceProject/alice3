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

/**
 * @author Dennis Cosgrove
 */
public class Config {
	public static class Builder {
		public Builder mode( Mode mode ) {
			this.mode = mode;
			return this;
		}

		public Builder joglVersion( String joglVersion ) {
			this.joglVersion = joglVersion;
			return this;
		}

		public Builder aliceModelSourceVersion( String aliceModelSourceVersion ) {
			this.aliceModelSourceVersion = aliceModelSourceVersion;
			return this;
		}

		public Builder nebulousModelSourceVersion( String nebulousModelSourceVersion ) {
			this.nebulousModelSourceVersion = nebulousModelSourceVersion;
			return this;
		}

		public Builder netBeans6Version( String netBeans6Version ) {
			this.netBeans6Version = netBeans6Version;
			return this;
		}

		public Builder netBeans8Version( String netBeans8Version ) {
			this.netBeans8Version = netBeans8Version;
			return this;
		}

		public Config build() {
			return new Config( this );
		}

		private Mode mode;
		private String joglVersion;
		private String aliceModelSourceVersion;
		private String nebulousModelSourceVersion;
		private String netBeans6Version;
		private String netBeans8Version;
	}

	private Config( Builder builder ) {
		assert builder.mode != null : builder;
		this.mode = builder.mode;

		assert builder.joglVersion != null : builder;
		this.joglVersion = builder.joglVersion;

		assert builder.aliceModelSourceVersion != null : builder;
		this.aliceModelSourceVersion = builder.aliceModelSourceVersion;

		assert builder.nebulousModelSourceVersion != null : builder;
		this.nebulousModelSourceVersion = builder.nebulousModelSourceVersion;

		assert builder.netBeans6Version != null : builder;
		this.netBeans6Version = builder.netBeans6Version;

		assert builder.netBeans8Version != null : builder;
		this.netBeans8Version = builder.netBeans8Version;
	}

	public Mode getMode() {
		return this.mode;
	}

	public String getJoglVersion() {
		return this.joglVersion;
	}

	public String getAliceModelSourceVersion() {
		return this.aliceModelSourceVersion;
	}

	public String getNebulousModelSourceVersion() {
		return this.nebulousModelSourceVersion;
	}

	public String getNetBeans6Version() {
		return this.netBeans6Version;
	}

	public String getNetBeans8Version() {
		return this.netBeans8Version;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "\n\tmode: " );
		sb.append( this.mode );
		sb.append( "\n\tjoglVersion: " );
		sb.append( this.joglVersion );
		sb.append( "\n\taliceModelSourceVersionn: " );
		sb.append( this.aliceModelSourceVersion );
		sb.append( "\n\tnebulousModelSourceVersion: " );
		sb.append( this.nebulousModelSourceVersion );
		sb.append( "\n\tnetBeans6Version: " );
		sb.append( this.netBeans6Version );
		sb.append( "\n\tnetBeans8Version: " );
		sb.append( this.netBeans8Version );
		return sb.toString();
	}

	private final Mode mode;
	private final String joglVersion;
	private final String aliceModelSourceVersion;
	private final String nebulousModelSourceVersion;
	private final String netBeans6Version;
	private final String netBeans8Version;
}
