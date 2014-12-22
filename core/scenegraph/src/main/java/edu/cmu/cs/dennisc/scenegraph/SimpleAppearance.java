package edu.cmu.cs.dennisc.scenegraph;

public class SimpleAppearance extends Appearance {

	public final edu.cmu.cs.dennisc.color.property.Color4fProperty ambientColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, new edu.cmu.cs.dennisc.color.Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN ), true ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, edu.cmu.cs.dennisc.color.Color4f value ) {
			if( value == null ) {
				value = new edu.cmu.cs.dennisc.color.Color4f( Float.NaN, Float.NaN, Float.NaN, Float.NaN );
			}
			super.setValue( owner, value );
		}
	};

	public final edu.cmu.cs.dennisc.color.property.Color4fProperty diffuseColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.WHITE );
	public final edu.cmu.cs.dennisc.property.FloatProperty opacity = new edu.cmu.cs.dennisc.property.FloatProperty( this, 1.0f );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<FillingStyle> fillingStyle = new edu.cmu.cs.dennisc.property.InstanceProperty<FillingStyle>( this, FillingStyle.SOLID ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, FillingStyle value ) {
			assert value != null : this;
			super.setValue( owner, value );
		}
	};
	public final edu.cmu.cs.dennisc.property.InstanceProperty<ShadingStyle> shadingStyle = new edu.cmu.cs.dennisc.property.InstanceProperty<ShadingStyle>( this, ShadingStyle.SMOOTH ) {
		@Override
		public void setValue( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, ShadingStyle value ) {
			assert value != null : this;
			super.setValue( owner, value );
		}
	};
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty specularHighlightColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public final edu.cmu.cs.dennisc.color.property.Color4fProperty emissiveColor = new edu.cmu.cs.dennisc.color.property.Color4fProperty( this, edu.cmu.cs.dennisc.color.Color4f.BLACK );
	public final edu.cmu.cs.dennisc.property.FloatProperty specularHighlightExponent = new edu.cmu.cs.dennisc.property.FloatProperty( this, 0.0f );
	public final edu.cmu.cs.dennisc.property.BooleanProperty isEthereal = new edu.cmu.cs.dennisc.property.BooleanProperty( this, false );

	@Override
	public void setAmbientColor( edu.cmu.cs.dennisc.color.Color4f ambientColor ) {
		this.ambientColor.setValue( ambientColor );
	}

	@Override
	public void setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f diffuseColor ) {
		this.diffuseColor.setValue( diffuseColor );
	}

	@Override
	public void setOpacity( float opacity ) {
		this.opacity.setValue( opacity );
	}

	@Override
	public void setSpecularHighlightExponent( float specularHighlightExponent ) {
		this.specularHighlightExponent.setValue( specularHighlightExponent );
	}

	@Override
	public void setSpecularHighlightColor( edu.cmu.cs.dennisc.color.Color4f specularHighlightColor ) {
		this.specularHighlightColor.setValue( specularHighlightColor );
	}

	@Override
	public void setEmissiveColor( edu.cmu.cs.dennisc.color.Color4f emissiveColor ) {
		this.emissiveColor.setValue( emissiveColor );
	}

	@Override
	public void setShadingStyle( ShadingStyle shadingStyle ) {
		this.shadingStyle.setValue( shadingStyle );
	}

	@Override
	public void setFillingStyle( FillingStyle fillingStyle ) {
		this.fillingStyle.setValue( fillingStyle );
	}

	@Override
	public void setEthereal( boolean isEthereal ) {
		this.isEthereal.setValue( isEthereal );
	}
}
