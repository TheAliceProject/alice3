
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 * 				The fx_surface_common type is used to declare a resource that can be used both as the source for texture samples and as the target of a rendering pass.
 * 			
 * 
 * <p>Java class for fx_surface_common complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fx_surface_common">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}fx_surface_init_common" minOccurs="0"/>
 *         &lt;element name="format" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="format_hint" type="{http://www.collada.org/2005/11/COLLADASchema}fx_surface_format_hint_common" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="size" type="{http://www.collada.org/2005/11/COLLADASchema}int3"/>
 *           &lt;element name="viewport_ratio" type="{http://www.collada.org/2005/11/COLLADASchema}float2"/>
 *         &lt;/choice>
 *         &lt;element name="mip_levels" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;element name="mipmap_generate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}fx_surface_type_enum" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fx_surface_common", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "initAsNull",
    "initAsTarget",
    "initCube",
    "initVolume",
    "initPlanar",
    "initFrom",
    "format",
    "formatHint",
    "size",
    "viewportRatio",
    "mipLevels",
    "mipmapGenerate",
    "extra"
})
@XmlSeeAlso({
    GlslSurfaceType.class,
    CgSurfaceType.class
})
public class FxSurfaceCommon {

    @XmlElement(name = "init_as_null", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Object initAsNull;
    @XmlElement(name = "init_as_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Object initAsTarget;
    @XmlElement(name = "init_cube", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FxSurfaceInitCubeCommon initCube;
    @XmlElement(name = "init_volume", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FxSurfaceInitVolumeCommon initVolume;
    @XmlElement(name = "init_planar", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FxSurfaceInitPlanarCommon initPlanar;
    @XmlElement(name = "init_from", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<FxSurfaceInitFromCommon> initFrom;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String format;
    @XmlElement(name = "format_hint", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FxSurfaceFormatHintCommon formatHint;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Long.class, defaultValue = "0 0 0")
    protected List<Long> size;
    @XmlList
    @XmlElement(name = "viewport_ratio", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class, defaultValue = "1 1")
    protected List<Double> viewportRatio;
    @XmlElement(name = "mip_levels", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0")
    @XmlSchemaType(name = "unsignedInt")
    protected Long mipLevels;
    @XmlElement(name = "mipmap_generate", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Boolean mipmapGenerate;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "type", required = true)
    protected String type;

    /**
     * Gets the value of the initAsNull property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInitAsNull() {
        return initAsNull;
    }

    /**
     * Sets the value of the initAsNull property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInitAsNull(Object value) {
        this.initAsNull = value;
    }

    /**
     * Gets the value of the initAsTarget property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInitAsTarget() {
        return initAsTarget;
    }

    /**
     * Sets the value of the initAsTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInitAsTarget(Object value) {
        this.initAsTarget = value;
    }

    /**
     * Gets the value of the initCube property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceInitCubeCommon }
     *     
     */
    public FxSurfaceInitCubeCommon getInitCube() {
        return initCube;
    }

    /**
     * Sets the value of the initCube property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceInitCubeCommon }
     *     
     */
    public void setInitCube(FxSurfaceInitCubeCommon value) {
        this.initCube = value;
    }

    /**
     * Gets the value of the initVolume property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceInitVolumeCommon }
     *     
     */
    public FxSurfaceInitVolumeCommon getInitVolume() {
        return initVolume;
    }

    /**
     * Sets the value of the initVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceInitVolumeCommon }
     *     
     */
    public void setInitVolume(FxSurfaceInitVolumeCommon value) {
        this.initVolume = value;
    }

    /**
     * Gets the value of the initPlanar property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceInitPlanarCommon }
     *     
     */
    public FxSurfaceInitPlanarCommon getInitPlanar() {
        return initPlanar;
    }

    /**
     * Sets the value of the initPlanar property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceInitPlanarCommon }
     *     
     */
    public void setInitPlanar(FxSurfaceInitPlanarCommon value) {
        this.initPlanar = value;
    }

    /**
     * Gets the value of the initFrom property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initFrom property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitFrom().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FxSurfaceInitFromCommon }
     * 
     * 
     */
    public List<FxSurfaceInitFromCommon> getInitFrom() {
        if (initFrom == null) {
            initFrom = new ArrayList<FxSurfaceInitFromCommon>();
        }
        return this.initFrom;
    }

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the formatHint property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceFormatHintCommon }
     *     
     */
    public FxSurfaceFormatHintCommon getFormatHint() {
        return formatHint;
    }

    /**
     * Sets the value of the formatHint property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceFormatHintCommon }
     *     
     */
    public void setFormatHint(FxSurfaceFormatHintCommon value) {
        this.formatHint = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the size property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSize().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getSize() {
        if (size == null) {
            size = new ArrayList<Long>();
        }
        return this.size;
    }

    /**
     * Gets the value of the viewportRatio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the viewportRatio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getViewportRatio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getViewportRatio() {
        if (viewportRatio == null) {
            viewportRatio = new ArrayList<Double>();
        }
        return this.viewportRatio;
    }

    /**
     * Gets the value of the mipLevels property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMipLevels() {
        return mipLevels;
    }

    /**
     * Sets the value of the mipLevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMipLevels(Long value) {
        this.mipLevels = value;
    }

    /**
     * Gets the value of the mipmapGenerate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMipmapGenerate() {
        return mipmapGenerate;
    }

    /**
     * Sets the value of the mipmapGenerate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMipmapGenerate(Boolean value) {
        this.mipmapGenerate = value;
    }

    /**
     * Gets the value of the extra property.
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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
