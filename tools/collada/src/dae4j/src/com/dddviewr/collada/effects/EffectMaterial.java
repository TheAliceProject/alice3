package com.dddviewr.collada.effects;

import com.dddviewr.collada.Base;

public abstract class EffectMaterial extends Base {
	protected EffectAttribute emission;
	protected EffectAttribute ambient;
	protected EffectAttribute diffuse;
	protected EffectAttribute specular;
	protected EffectAttribute shininess;
	protected EffectAttribute reflective;
	protected EffectAttribute reflectivity;
	protected EffectAttribute transparent;
	protected EffectAttribute transparency;
	
	protected EffectAttribute bump;

	public EffectAttribute getAmbient() {
		return this.ambient;
	}

	public void setAmbient(EffectAttribute ambient) {
		this.ambient = ambient;
	}

	public EffectAttribute getEmission() {
		return this.emission;
	}

	public void setEmission(EffectAttribute emission) {
		this.emission = emission;
	}

	public EffectAttribute getDiffuse() {
		return this.diffuse;
	}

	public void setDiffuse(EffectAttribute diffuse) {
		this.diffuse = diffuse;
	}

	public EffectAttribute getReflective() {
		return this.reflective;
	}

	public void setReflective(EffectAttribute reflective) {
		this.reflective = reflective;
	}

	public EffectAttribute getReflectivity() {
		return this.reflectivity;
	}

	public void setReflectivity(EffectAttribute reflectivity) {
		this.reflectivity = reflectivity;
	}

	public EffectAttribute getShininess() {
		return this.shininess;
	}

	public void setShininess(EffectAttribute shininess) {
		this.shininess = shininess;
	}

	public EffectAttribute getSpecular() {
		return this.specular;
	}

	public void setSpecular(EffectAttribute specular) {
		this.specular = specular;
	}

	public EffectAttribute getTransparency() {
		return this.transparency;
	}

	public void setTransparency(EffectAttribute transparency) {
		this.transparency = transparency;
	}

	public EffectAttribute getTransparent() {
		return this.transparent;
	}

	public void setTransparent(EffectAttribute transparent) {
		this.transparent = transparent;
	}

	public EffectAttribute getBump() {
		return bump;
	}

	public void setBump(EffectAttribute bump) {
		this.bump = bump;
	}

}