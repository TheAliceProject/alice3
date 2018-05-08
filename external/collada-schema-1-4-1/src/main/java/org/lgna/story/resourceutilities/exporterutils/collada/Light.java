
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
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
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset" minOccurs="0"/>
 *         &lt;element name="technique_common">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ambient">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="directional">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="point">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
 *                             &lt;element name="constant_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="linear_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="quadratic_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="spot">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
 *                             &lt;element name="constant_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="linear_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="quadratic_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="falloff_angle" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="falloff_exponent" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
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
    "asset",
    "techniqueCommon",
    "technique",
    "extra"
})
@XmlRootElement(name = "light", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class Light {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElement(name = "technique_common", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected Light.TechniqueCommon techniqueCommon;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Technique> technique;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * 
     * 							The light element may contain an asset element.
     * 						
     * 
     * @return
     *     possible object is
     *     {@link Asset }
     *     
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * Sets the value of the asset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Asset }
     *     
     */
    public void setAsset(Asset value) {
        this.asset = value;
    }

    /**
     * Gets the value of the techniqueCommon property.
     * 
     * @return
     *     possible object is
     *     {@link Light.TechniqueCommon }
     *     
     */
    public Light.TechniqueCommon getTechniqueCommon() {
        return techniqueCommon;
    }

    /**
     * Sets the value of the techniqueCommon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Light.TechniqueCommon }
     *     
     */
    public void setTechniqueCommon(Light.TechniqueCommon value) {
        this.techniqueCommon = value;
    }

    /**
     * 
     * 							This element may contain any number of non-common profile techniques.
     * 						Gets the value of the technique property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the technique property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTechnique().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Technique }
     * 
     * 
     */
    public List<Technique> getTechnique() {
        if (technique == null) {
            technique = new ArrayList<Technique>();
        }
        return this.technique;
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
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
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
     *       &lt;choice>
     *         &lt;element name="ambient">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="directional">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="point">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
     *                   &lt;element name="constant_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="linear_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="quadratic_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="spot">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
     *                   &lt;element name="constant_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="linear_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="quadratic_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="falloff_angle" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="falloff_exponent" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "ambient",
        "directional",
        "point",
        "spot"
    })
    public static class TechniqueCommon {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Light.TechniqueCommon.Ambient ambient;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Light.TechniqueCommon.Directional directional;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Light.TechniqueCommon.Point point;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Light.TechniqueCommon.Spot spot;

        /**
         * Gets the value of the ambient property.
         * 
         * @return
         *     possible object is
         *     {@link Light.TechniqueCommon.Ambient }
         *     
         */
        public Light.TechniqueCommon.Ambient getAmbient() {
            return ambient;
        }

        /**
         * Sets the value of the ambient property.
         * 
         * @param value
         *     allowed object is
         *     {@link Light.TechniqueCommon.Ambient }
         *     
         */
        public void setAmbient(Light.TechniqueCommon.Ambient value) {
            this.ambient = value;
        }

        /**
         * Gets the value of the directional property.
         * 
         * @return
         *     possible object is
         *     {@link Light.TechniqueCommon.Directional }
         *     
         */
        public Light.TechniqueCommon.Directional getDirectional() {
            return directional;
        }

        /**
         * Sets the value of the directional property.
         * 
         * @param value
         *     allowed object is
         *     {@link Light.TechniqueCommon.Directional }
         *     
         */
        public void setDirectional(Light.TechniqueCommon.Directional value) {
            this.directional = value;
        }

        /**
         * Gets the value of the point property.
         * 
         * @return
         *     possible object is
         *     {@link Light.TechniqueCommon.Point }
         *     
         */
        public Light.TechniqueCommon.Point getPoint() {
            return point;
        }

        /**
         * Sets the value of the point property.
         * 
         * @param value
         *     allowed object is
         *     {@link Light.TechniqueCommon.Point }
         *     
         */
        public void setPoint(Light.TechniqueCommon.Point value) {
            this.point = value;
        }

        /**
         * Gets the value of the spot property.
         * 
         * @return
         *     possible object is
         *     {@link Light.TechniqueCommon.Spot }
         *     
         */
        public Light.TechniqueCommon.Spot getSpot() {
            return spot;
        }

        /**
         * Sets the value of the spot property.
         * 
         * @param value
         *     allowed object is
         *     {@link Light.TechniqueCommon.Spot }
         *     
         */
        public void setSpot(Light.TechniqueCommon.Spot value) {
            this.spot = value;
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
         *         &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "color"
        })
        public static class Ambient {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
            protected TargetableFloat3 color;

            /**
             * Gets the value of the color property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat3 }
             *     
             */
            public TargetableFloat3 getColor() {
                return color;
            }

            /**
             * Sets the value of the color property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat3 }
             *     
             */
            public void setColor(TargetableFloat3 value) {
                this.color = value;
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
         *       &lt;sequence>
         *         &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "color"
        })
        public static class Directional {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
            protected TargetableFloat3 color;

            /**
             * Gets the value of the color property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat3 }
             *     
             */
            public TargetableFloat3 getColor() {
                return color;
            }

            /**
             * Sets the value of the color property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat3 }
             *     
             */
            public void setColor(TargetableFloat3 value) {
                this.color = value;
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
         *       &lt;sequence>
         *         &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
         *         &lt;element name="constant_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="linear_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="quadratic_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "color",
            "constantAttenuation",
            "linearAttenuation",
            "quadraticAttenuation"
        })
        public static class Point {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
            protected TargetableFloat3 color;
            @XmlElement(name = "constant_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "1.0")
            protected TargetableFloat constantAttenuation;
            @XmlElement(name = "linear_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
            protected TargetableFloat linearAttenuation;
            @XmlElement(name = "quadratic_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
            protected TargetableFloat quadraticAttenuation;

            /**
             * Gets the value of the color property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat3 }
             *     
             */
            public TargetableFloat3 getColor() {
                return color;
            }

            /**
             * Sets the value of the color property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat3 }
             *     
             */
            public void setColor(TargetableFloat3 value) {
                this.color = value;
            }

            /**
             * Gets the value of the constantAttenuation property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getConstantAttenuation() {
                return constantAttenuation;
            }

            /**
             * Sets the value of the constantAttenuation property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setConstantAttenuation(TargetableFloat value) {
                this.constantAttenuation = value;
            }

            /**
             * Gets the value of the linearAttenuation property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getLinearAttenuation() {
                return linearAttenuation;
            }

            /**
             * Sets the value of the linearAttenuation property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setLinearAttenuation(TargetableFloat value) {
                this.linearAttenuation = value;
            }

            /**
             * Gets the value of the quadraticAttenuation property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getQuadraticAttenuation() {
                return quadraticAttenuation;
            }

            /**
             * Sets the value of the quadraticAttenuation property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setQuadraticAttenuation(TargetableFloat value) {
                this.quadraticAttenuation = value;
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
         *       &lt;sequence>
         *         &lt;element name="color" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3"/>
         *         &lt;element name="constant_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="linear_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="quadratic_attenuation" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="falloff_angle" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="falloff_exponent" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "color",
            "constantAttenuation",
            "linearAttenuation",
            "quadraticAttenuation",
            "falloffAngle",
            "falloffExponent"
        })
        public static class Spot {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
            protected TargetableFloat3 color;
            @XmlElement(name = "constant_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "1.0")
            protected TargetableFloat constantAttenuation;
            @XmlElement(name = "linear_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
            protected TargetableFloat linearAttenuation;
            @XmlElement(name = "quadratic_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
            protected TargetableFloat quadraticAttenuation;
            @XmlElement(name = "falloff_angle", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "180.0")
            protected TargetableFloat falloffAngle;
            @XmlElement(name = "falloff_exponent", namespace = "http://www.collada.org/2005/11/COLLADASchema", defaultValue = "0.0")
            protected TargetableFloat falloffExponent;

            /**
             * Gets the value of the color property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat3 }
             *     
             */
            public TargetableFloat3 getColor() {
                return color;
            }

            /**
             * Sets the value of the color property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat3 }
             *     
             */
            public void setColor(TargetableFloat3 value) {
                this.color = value;
            }

            /**
             * Gets the value of the constantAttenuation property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getConstantAttenuation() {
                return constantAttenuation;
            }

            /**
             * Sets the value of the constantAttenuation property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setConstantAttenuation(TargetableFloat value) {
                this.constantAttenuation = value;
            }

            /**
             * Gets the value of the linearAttenuation property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getLinearAttenuation() {
                return linearAttenuation;
            }

            /**
             * Sets the value of the linearAttenuation property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setLinearAttenuation(TargetableFloat value) {
                this.linearAttenuation = value;
            }

            /**
             * Gets the value of the quadraticAttenuation property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getQuadraticAttenuation() {
                return quadraticAttenuation;
            }

            /**
             * Sets the value of the quadraticAttenuation property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setQuadraticAttenuation(TargetableFloat value) {
                this.quadraticAttenuation = value;
            }

            /**
             * Gets the value of the falloffAngle property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getFalloffAngle() {
                return falloffAngle;
            }

            /**
             * Sets the value of the falloffAngle property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setFalloffAngle(TargetableFloat value) {
                this.falloffAngle = value;
            }

            /**
             * Gets the value of the falloffExponent property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getFalloffExponent() {
                return falloffExponent;
            }

            /**
             * Sets the value of the falloffExponent property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setFalloffExponent(TargetableFloat value) {
                this.falloffExponent = value;
            }

        }

    }

}
