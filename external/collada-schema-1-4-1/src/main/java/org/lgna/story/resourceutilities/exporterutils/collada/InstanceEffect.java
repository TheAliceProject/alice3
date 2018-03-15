
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="technique_hint" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="platform" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                 &lt;attribute name="profile" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                 &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="setparam" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}fx_basic_type_common"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="url" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "techniqueHint",
    "setparam",
    "extra"
})
@XmlRootElement(name = "instance_effect", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class InstanceEffect {

    @XmlElement(name = "technique_hint", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceEffect.TechniqueHint> techniqueHint;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<InstanceEffect.Setparam> setparam;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "url", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String url;
    @XmlAttribute(name = "sid")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * Gets the value of the techniqueHint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the techniqueHint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTechniqueHint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceEffect.TechniqueHint }
     * 
     * 
     */
    public List<InstanceEffect.TechniqueHint> getTechniqueHint() {
        if (techniqueHint == null) {
            techniqueHint = new ArrayList<InstanceEffect.TechniqueHint>();
        }
        return this.techniqueHint;
    }

    /**
     * Gets the value of the setparam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the setparam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSetparam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstanceEffect.Setparam }
     * 
     * 
     */
    public List<InstanceEffect.Setparam> getSetparam() {
        if (setparam == null) {
            setparam = new ArrayList<InstanceEffect.Setparam>();
        }
        return this.setparam;
    }

    /**
     * 
     * 							The extra element may appear any number of times.
     * 						Gets the value of the extra property.
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
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
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

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}fx_basic_type_common"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bool",
        "bool2",
        "bool3",
        "bool4",
        "_int",
        "int2",
        "int3",
        "int4",
        "_float",
        "float2",
        "float3",
        "float4",
        "float1X1",
        "float1X2",
        "float1X3",
        "float1X4",
        "float2X1",
        "float2X2",
        "float2X3",
        "float2X4",
        "float3X1",
        "float3X2",
        "float3X3",
        "float3X4",
        "float4X1",
        "float4X2",
        "float4X3",
        "float4X4",
        "surface",
        "sampler1D",
        "sampler2D",
        "sampler3D",
        "samplerCUBE",
        "samplerRECT",
        "samplerDEPTH",
        "_enum"
    })
    public static class Setparam {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Boolean bool;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
        protected List<Boolean> bool2;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
        protected List<Boolean> bool3;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Boolean.class)
        protected List<Boolean> bool4;
        @XmlElement(name = "int", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Long _int;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Long.class)
        protected List<Long> int2;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Long.class)
        protected List<Long> int3;
        @XmlList
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Long.class)
        protected List<Long> int4;
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
        @XmlElement(name = "float1x1", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Double float1X1;
        @XmlList
        @XmlElement(name = "float1x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float1X2;
        @XmlList
        @XmlElement(name = "float1x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float1X3;
        @XmlList
        @XmlElement(name = "float1x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float1X4;
        @XmlList
        @XmlElement(name = "float2x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float2X1;
        @XmlList
        @XmlElement(name = "float2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float2X2;
        @XmlList
        @XmlElement(name = "float2x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float2X3;
        @XmlList
        @XmlElement(name = "float2x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float2X4;
        @XmlList
        @XmlElement(name = "float3x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float3X1;
        @XmlList
        @XmlElement(name = "float3x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float3X2;
        @XmlList
        @XmlElement(name = "float3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float3X3;
        @XmlList
        @XmlElement(name = "float3x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float3X4;
        @XmlList
        @XmlElement(name = "float4x1", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float4X1;
        @XmlList
        @XmlElement(name = "float4x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float4X2;
        @XmlList
        @XmlElement(name = "float4x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float4X3;
        @XmlList
        @XmlElement(name = "float4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
        protected List<Double> float4X4;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSurfaceCommon surface;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSampler1DCommon sampler1D;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSampler2DCommon sampler2D;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSampler3DCommon sampler3D;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSamplerCUBECommon samplerCUBE;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSamplerRECTCommon samplerRECT;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected FxSamplerDEPTHCommon samplerDEPTH;
        @XmlElement(name = "enum", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected String _enum;
        @XmlAttribute(name = "ref", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String ref;

        /**
         * Gets the value of the bool property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isBool() {
            return bool;
        }

        /**
         * Sets the value of the bool property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setBool(Boolean value) {
            this.bool = value;
        }

        /**
         * Gets the value of the bool2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the bool2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBool2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Boolean }
         * 
         * 
         */
        public List<Boolean> getBool2() {
            if (bool2 == null) {
                bool2 = new ArrayList<Boolean>();
            }
            return this.bool2;
        }

        /**
         * Gets the value of the bool3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the bool3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBool3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Boolean }
         * 
         * 
         */
        public List<Boolean> getBool3() {
            if (bool3 == null) {
                bool3 = new ArrayList<Boolean>();
            }
            return this.bool3;
        }

        /**
         * Gets the value of the bool4 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the bool4 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBool4().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Boolean }
         * 
         * 
         */
        public List<Boolean> getBool4() {
            if (bool4 == null) {
                bool4 = new ArrayList<Boolean>();
            }
            return this.bool4;
        }

        /**
         * Gets the value of the int property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        public Long getInt() {
            return _int;
        }

        /**
         * Sets the value of the int property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        public void setInt(Long value) {
            this._int = value;
        }

        /**
         * Gets the value of the int2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the int2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInt2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Long }
         * 
         * 
         */
        public List<Long> getInt2() {
            if (int2 == null) {
                int2 = new ArrayList<Long>();
            }
            return this.int2;
        }

        /**
         * Gets the value of the int3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the int3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInt3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Long }
         * 
         * 
         */
        public List<Long> getInt3() {
            if (int3 == null) {
                int3 = new ArrayList<Long>();
            }
            return this.int3;
        }

        /**
         * Gets the value of the int4 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the int4 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInt4().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Long }
         * 
         * 
         */
        public List<Long> getInt4() {
            if (int4 == null) {
                int4 = new ArrayList<Long>();
            }
            return this.int4;
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
         * Gets the value of the float1X1 property.
         * 
         * @return
         *     possible object is
         *     {@link Double }
         *     
         */
        public Double getFloat1X1() {
            return float1X1;
        }

        /**
         * Sets the value of the float1X1 property.
         * 
         * @param value
         *     allowed object is
         *     {@link Double }
         *     
         */
        public void setFloat1X1(Double value) {
            this.float1X1 = value;
        }

        /**
         * Gets the value of the float1X2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float1X2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat1X2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat1X2() {
            if (float1X2 == null) {
                float1X2 = new ArrayList<Double>();
            }
            return this.float1X2;
        }

        /**
         * Gets the value of the float1X3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float1X3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat1X3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat1X3() {
            if (float1X3 == null) {
                float1X3 = new ArrayList<Double>();
            }
            return this.float1X3;
        }

        /**
         * Gets the value of the float1X4 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float1X4 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat1X4().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat1X4() {
            if (float1X4 == null) {
                float1X4 = new ArrayList<Double>();
            }
            return this.float1X4;
        }

        /**
         * Gets the value of the float2X1 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float2X1 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat2X1().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat2X1() {
            if (float2X1 == null) {
                float2X1 = new ArrayList<Double>();
            }
            return this.float2X1;
        }

        /**
         * Gets the value of the float2X2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float2X2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat2X2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat2X2() {
            if (float2X2 == null) {
                float2X2 = new ArrayList<Double>();
            }
            return this.float2X2;
        }

        /**
         * Gets the value of the float2X3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float2X3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat2X3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat2X3() {
            if (float2X3 == null) {
                float2X3 = new ArrayList<Double>();
            }
            return this.float2X3;
        }

        /**
         * Gets the value of the float2X4 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float2X4 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat2X4().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat2X4() {
            if (float2X4 == null) {
                float2X4 = new ArrayList<Double>();
            }
            return this.float2X4;
        }

        /**
         * Gets the value of the float3X1 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float3X1 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat3X1().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat3X1() {
            if (float3X1 == null) {
                float3X1 = new ArrayList<Double>();
            }
            return this.float3X1;
        }

        /**
         * Gets the value of the float3X2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float3X2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat3X2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat3X2() {
            if (float3X2 == null) {
                float3X2 = new ArrayList<Double>();
            }
            return this.float3X2;
        }

        /**
         * Gets the value of the float3X3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float3X3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat3X3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat3X3() {
            if (float3X3 == null) {
                float3X3 = new ArrayList<Double>();
            }
            return this.float3X3;
        }

        /**
         * Gets the value of the float3X4 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float3X4 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat3X4().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat3X4() {
            if (float3X4 == null) {
                float3X4 = new ArrayList<Double>();
            }
            return this.float3X4;
        }

        /**
         * Gets the value of the float4X1 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float4X1 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat4X1().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat4X1() {
            if (float4X1 == null) {
                float4X1 = new ArrayList<Double>();
            }
            return this.float4X1;
        }

        /**
         * Gets the value of the float4X2 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float4X2 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat4X2().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat4X2() {
            if (float4X2 == null) {
                float4X2 = new ArrayList<Double>();
            }
            return this.float4X2;
        }

        /**
         * Gets the value of the float4X3 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float4X3 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat4X3().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat4X3() {
            if (float4X3 == null) {
                float4X3 = new ArrayList<Double>();
            }
            return this.float4X3;
        }

        /**
         * Gets the value of the float4X4 property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the float4X4 property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFloat4X4().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Double }
         * 
         * 
         */
        public List<Double> getFloat4X4() {
            if (float4X4 == null) {
                float4X4 = new ArrayList<Double>();
            }
            return this.float4X4;
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
         * Gets the value of the sampler1D property.
         * 
         * @return
         *     possible object is
         *     {@link FxSampler1DCommon }
         *     
         */
        public FxSampler1DCommon getSampler1D() {
            return sampler1D;
        }

        /**
         * Sets the value of the sampler1D property.
         * 
         * @param value
         *     allowed object is
         *     {@link FxSampler1DCommon }
         *     
         */
        public void setSampler1D(FxSampler1DCommon value) {
            this.sampler1D = value;
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
         * Gets the value of the sampler3D property.
         * 
         * @return
         *     possible object is
         *     {@link FxSampler3DCommon }
         *     
         */
        public FxSampler3DCommon getSampler3D() {
            return sampler3D;
        }

        /**
         * Sets the value of the sampler3D property.
         * 
         * @param value
         *     allowed object is
         *     {@link FxSampler3DCommon }
         *     
         */
        public void setSampler3D(FxSampler3DCommon value) {
            this.sampler3D = value;
        }

        /**
         * Gets the value of the samplerCUBE property.
         * 
         * @return
         *     possible object is
         *     {@link FxSamplerCUBECommon }
         *     
         */
        public FxSamplerCUBECommon getSamplerCUBE() {
            return samplerCUBE;
        }

        /**
         * Sets the value of the samplerCUBE property.
         * 
         * @param value
         *     allowed object is
         *     {@link FxSamplerCUBECommon }
         *     
         */
        public void setSamplerCUBE(FxSamplerCUBECommon value) {
            this.samplerCUBE = value;
        }

        /**
         * Gets the value of the samplerRECT property.
         * 
         * @return
         *     possible object is
         *     {@link FxSamplerRECTCommon }
         *     
         */
        public FxSamplerRECTCommon getSamplerRECT() {
            return samplerRECT;
        }

        /**
         * Sets the value of the samplerRECT property.
         * 
         * @param value
         *     allowed object is
         *     {@link FxSamplerRECTCommon }
         *     
         */
        public void setSamplerRECT(FxSamplerRECTCommon value) {
            this.samplerRECT = value;
        }

        /**
         * Gets the value of the samplerDEPTH property.
         * 
         * @return
         *     possible object is
         *     {@link FxSamplerDEPTHCommon }
         *     
         */
        public FxSamplerDEPTHCommon getSamplerDEPTH() {
            return samplerDEPTH;
        }

        /**
         * Sets the value of the samplerDEPTH property.
         * 
         * @param value
         *     allowed object is
         *     {@link FxSamplerDEPTHCommon }
         *     
         */
        public void setSamplerDEPTH(FxSamplerDEPTHCommon value) {
            this.samplerDEPTH = value;
        }

        /**
         * Gets the value of the enum property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEnum() {
            return _enum;
        }

        /**
         * Sets the value of the enum property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEnum(String value) {
            this._enum = value;
        }

        /**
         * Gets the value of the ref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRef() {
            return ref;
        }

        /**
         * Sets the value of the ref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRef(String value) {
            this.ref = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="platform" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *       &lt;attribute name="profile" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class TechniqueHint {

        @XmlAttribute(name = "platform")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String platform;
        @XmlAttribute(name = "profile")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String profile;
        @XmlAttribute(name = "ref", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String ref;

        /**
         * Gets the value of the platform property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPlatform() {
            return platform;
        }

        /**
         * Sets the value of the platform property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPlatform(String value) {
            this.platform = value;
        }

        /**
         * Gets the value of the profile property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProfile() {
            return profile;
        }

        /**
         * Sets the value of the profile property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProfile(String value) {
            this.profile = value;
        }

        /**
         * Gets the value of the ref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRef() {
            return ref;
        }

        /**
         * Sets the value of the ref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRef(String value) {
            this.ref = value;
        }

    }

}
