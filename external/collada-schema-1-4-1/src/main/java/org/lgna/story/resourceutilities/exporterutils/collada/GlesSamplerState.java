
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 				Two-dimensional texture sampler state for profile_GLES. This is a bundle of sampler-specific states that will be referenced by one or more texture_units.
 * 			
 * 
 * <p>Java class for gles_sampler_state complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gles_sampler_state">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wrap_s" type="{http://www.collada.org/2005/11/COLLADASchema}gles_sampler_wrap" minOccurs="0"/>
 *         &lt;element name="wrap_t" type="{http://www.collada.org/2005/11/COLLADASchema}gles_sampler_wrap" minOccurs="0"/>
 *         &lt;element name="minfilter" type="{http://www.collada.org/2005/11/COLLADASchema}fx_sampler_filter_common" minOccurs="0"/>
 *         &lt;element name="magfilter" type="{http://www.collada.org/2005/11/COLLADASchema}fx_sampler_filter_common" minOccurs="0"/>
 *         &lt;element name="mipfilter" type="{http://www.collada.org/2005/11/COLLADASchema}fx_sampler_filter_common" minOccurs="0"/>
 *         &lt;element name="mipmap_maxlevel" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" minOccurs="0"/>
 *         &lt;element name="mipmap_bias" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gles_sampler_state", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "wrapS",
    "wrapT",
    "minfilter",
    "magfilter",
    "mipfilter",
    "mipmapMaxlevel",
    "mipmapBias",
    "extra"
})
public class GlesSamplerState {

    @XmlElement(name = "wrap_s", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "REPEAT")
    @XmlSchemaType(name = "NMTOKEN")
    protected GlesSamplerWrap wrapS;
    @XmlElement(name = "wrap_t", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "REPEAT")
    @XmlSchemaType(name = "NMTOKEN")
    protected GlesSamplerWrap wrapT;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "NONE")
    @XmlSchemaType(name = "NMTOKEN")
    protected FxSamplerFilterCommon minfilter;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "NONE")
    @XmlSchemaType(name = "NMTOKEN")
    protected FxSamplerFilterCommon magfilter;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "NONE")
    @XmlSchemaType(name = "NMTOKEN")
    protected FxSamplerFilterCommon mipfilter;
    @XmlElement(name = "mipmap_maxlevel", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "255")
    @XmlSchemaType(name = "unsignedByte")
    protected Short mipmapMaxlevel;
    @XmlElement(name = "mipmap_bias", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
    protected Float mipmapBias;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "sid")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;

    /**
     * Gets the value of the wrapS property.
     * 
     * @return
     *     possible object is
     *     {@link GlesSamplerWrap }
     *     
     */
    public GlesSamplerWrap getWrapS() {
        return wrapS;
    }

    /**
     * Sets the value of the wrapS property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesSamplerWrap }
     *     
     */
    public void setWrapS(GlesSamplerWrap value) {
        this.wrapS = value;
    }

    /**
     * Gets the value of the wrapT property.
     * 
     * @return
     *     possible object is
     *     {@link GlesSamplerWrap }
     *     
     */
    public GlesSamplerWrap getWrapT() {
        return wrapT;
    }

    /**
     * Sets the value of the wrapT property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlesSamplerWrap }
     *     
     */
    public void setWrapT(GlesSamplerWrap value) {
        this.wrapT = value;
    }

    /**
     * Gets the value of the minfilter property.
     * 
     * @return
     *     possible object is
     *     {@link FxSamplerFilterCommon }
     *     
     */
    public FxSamplerFilterCommon getMinfilter() {
        return minfilter;
    }

    /**
     * Sets the value of the minfilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSamplerFilterCommon }
     *     
     */
    public void setMinfilter(FxSamplerFilterCommon value) {
        this.minfilter = value;
    }

    /**
     * Gets the value of the magfilter property.
     * 
     * @return
     *     possible object is
     *     {@link FxSamplerFilterCommon }
     *     
     */
    public FxSamplerFilterCommon getMagfilter() {
        return magfilter;
    }

    /**
     * Sets the value of the magfilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSamplerFilterCommon }
     *     
     */
    public void setMagfilter(FxSamplerFilterCommon value) {
        this.magfilter = value;
    }

    /**
     * Gets the value of the mipfilter property.
     * 
     * @return
     *     possible object is
     *     {@link FxSamplerFilterCommon }
     *     
     */
    public FxSamplerFilterCommon getMipfilter() {
        return mipfilter;
    }

    /**
     * Sets the value of the mipfilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSamplerFilterCommon }
     *     
     */
    public void setMipfilter(FxSamplerFilterCommon value) {
        this.mipfilter = value;
    }

    /**
     * Gets the value of the mipmapMaxlevel property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getMipmapMaxlevel() {
        return mipmapMaxlevel;
    }

    /**
     * Sets the value of the mipmapMaxlevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setMipmapMaxlevel(Short value) {
        this.mipmapMaxlevel = value;
    }

    /**
     * Gets the value of the mipmapBias property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getMipmapBias() {
        return mipmapBias;
    }

    /**
     * Sets the value of the mipmapBias property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setMipmapBias(Float value) {
        this.mipmapBias = value;
    }

    /**
     * 
     * 						The extra element may appear any number of times.
     * 						OpenGL ES extensions may be used here.
     * 					Gets the value of the extra property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extra property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtra().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extra }
     * 
     * 
     */
    public List<Extra> getExtra() {
        if (extra == null) {
            extra = new ArrayList<Extra>();
        }
        return this.extra;
    }

    /**
     * Gets the value of the sid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSid() {
        return sid;
    }

    /**
     * Sets the value of the sid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSid(String value) {
        this.sid = value;
    }

}
