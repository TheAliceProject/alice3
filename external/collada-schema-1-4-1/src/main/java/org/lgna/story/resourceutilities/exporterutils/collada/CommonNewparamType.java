
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for common_newparam_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="common_newparam_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="semantic" type="{http://www.w3.org/2001/XMLSchema}NCName" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="float" type="{http://www.collada.org/2005/11/COLLADASchema}float"/>
 *           &lt;element name="float2" type="{http://www.collada.org/2005/11/COLLADASchema}float2"/>
 *           &lt;element name="float3" type="{http://www.collada.org/2005/11/COLLADASchema}float3"/>
 *           &lt;element name="float4" type="{http://www.collada.org/2005/11/COLLADASchema}float4"/>
 *           &lt;element name="surface" type="{http://www.collada.org/2005/11/COLLADASchema}fx_surface_common"/>
 *           &lt;element name="sampler2D" type="{http://www.collada.org/2005/11/COLLADASchema}fx_sampler2D_common"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "common_newparam_type", namespace = "http://www.collada.org/2005/11/COLLADASchema", propOrder = {
    "semantic",
    "_float",
    "float2",
    "float3",
    "float4",
    "surface",
    "sampler2D"
})
public class CommonNewparamType {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String semantic;
    @XmlElement(name = "float", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Double _float;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
    protected List<Double> float2;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
    protected List<Double> float3;
    @XmlList
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
    protected List<Double> float4;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FxSurfaceCommon surface;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FxSampler2DCommon sampler2D;
    @XmlAttribute(name = "sid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;

    /**
     * Gets the value of the semantic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSemantic() {
        return semantic;
    }

    /**
     * Sets the value of the semantic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSemantic(String value) {
        this.semantic = value;
    }

    /**
     * Gets the value of the float property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFloat() {
        return _float;
    }

    /**
     * Sets the value of the float property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFloat(Double value) {
        this._float = value;
    }

    /**
     * Gets the value of the float2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat2() {
        if (float2 == null) {
            float2 = new ArrayList<Double>();
        }
        return this.float2;
    }

    /**
     * Gets the value of the float3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat3() {
        if (float3 == null) {
            float3 = new ArrayList<Double>();
        }
        return this.float3;
    }

    /**
     * Gets the value of the float4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the float4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFloat4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getFloat4() {
        if (float4 == null) {
            float4 = new ArrayList<Double>();
        }
        return this.float4;
    }

    /**
     * Gets the value of the surface property.
     * 
     * @return
     *     possible object is
     *     {@link FxSurfaceCommon }
     *     
     */
    public FxSurfaceCommon getSurface() {
        return surface;
    }

    /**
     * Sets the value of the surface property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSurfaceCommon }
     *     
     */
    public void setSurface(FxSurfaceCommon value) {
        this.surface = value;
    }

    /**
     * Gets the value of the sampler2D property.
     * 
     * @return
     *     possible object is
     *     {@link FxSampler2DCommon }
     *     
     */
    public FxSampler2DCommon getSampler2D() {
        return sampler2D;
    }

    /**
     * Sets the value of the sampler2D property.
     * 
     * @param value
     *     allowed object is
     *     {@link FxSampler2DCommon }
     *     
     */
    public void setSampler2D(FxSampler2DCommon value) {
        this.sampler2D = value;
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
