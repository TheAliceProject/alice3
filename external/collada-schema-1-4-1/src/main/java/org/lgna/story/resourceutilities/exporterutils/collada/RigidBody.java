
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
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
 *         &lt;element name="technique_common">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="dynamic" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
 *                           &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="mass" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                   &lt;element name="mass_frame" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice maxOccurs="unbounded">
 *                             &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
 *                             &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="inertia" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
 *                   &lt;choice minOccurs="0">
 *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_physics_material"/>
 *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}physics_material"/>
 *                   &lt;/choice>
 *                   &lt;element name="shape" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="hollow" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;simpleContent>
 *                                   &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
 *                                     &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                   &lt;/extension>
 *                                 &lt;/simpleContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="mass" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;element name="density" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
 *                             &lt;choice minOccurs="0">
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_physics_material"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}physics_material"/>
 *                             &lt;/choice>
 *                             &lt;choice>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_geometry"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}plane"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}box"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}sphere"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}cylinder"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tapered_cylinder"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}capsule"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tapered_capsule"/>
 *                             &lt;/choice>
 *                             &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
 *                               &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
 *                             &lt;/choice>
 *                             &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
    "techniqueCommon",
    "technique",
    "extra"
})
@XmlRootElement(name = "rigid_body", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class RigidBody {

    @XmlElement(name = "technique_common", namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected RigidBody.TechniqueCommon techniqueCommon;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Technique> technique;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "sid", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String sid;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;

    /**
     * Gets the value of the techniqueCommon property.
     * 
     * @return
     *     possible object is
     *     {@link RigidBody.TechniqueCommon }
     *     
     */
    public RigidBody.TechniqueCommon getTechniqueCommon() {
        return techniqueCommon;
    }

    /**
     * Sets the value of the techniqueCommon property.
     * 
     * @param value
     *     allowed object is
     *     {@link RigidBody.TechniqueCommon }
     *     
     */
    public void setTechniqueCommon(RigidBody.TechniqueCommon value) {
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
     *         &lt;element name="dynamic" minOccurs="0">
     *           &lt;complexType>
     *             &lt;simpleContent>
     *               &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
     *                 &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *               &lt;/extension>
     *             &lt;/simpleContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="mass" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *         &lt;element name="mass_frame" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice maxOccurs="unbounded">
     *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
     *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="inertia" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat3" minOccurs="0"/>
     *         &lt;choice minOccurs="0">
     *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_physics_material"/>
     *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}physics_material"/>
     *         &lt;/choice>
     *         &lt;element name="shape" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="hollow" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;simpleContent>
     *                         &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
     *                           &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                         &lt;/extension>
     *                       &lt;/simpleContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="mass" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;element name="density" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
     *                   &lt;choice minOccurs="0">
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_physics_material"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}physics_material"/>
     *                   &lt;/choice>
     *                   &lt;choice>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_geometry"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}plane"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}box"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}sphere"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}cylinder"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tapered_cylinder"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}capsule"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tapered_capsule"/>
     *                   &lt;/choice>
     *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
     *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
     *                   &lt;/choice>
     *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "dynamic",
        "mass",
        "massFrame",
        "inertia",
        "instancePhysicsMaterial",
        "physicsMaterial",
        "shape"
    })
    public static class TechniqueCommon {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected RigidBody.TechniqueCommon.Dynamic dynamic;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected TargetableFloat mass;
        @XmlElement(name = "mass_frame", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected RigidBody.TechniqueCommon.MassFrame massFrame;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected TargetableFloat3 inertia;
        @XmlElement(name = "instance_physics_material", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected InstanceWithExtra instancePhysicsMaterial;
        @XmlElement(name = "physics_material", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected PhysicsMaterial physicsMaterial;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected List<RigidBody.TechniqueCommon.Shape> shape;

        /**
         * Gets the value of the dynamic property.
         * 
         * @return
         *     possible object is
         *     {@link RigidBody.TechniqueCommon.Dynamic }
         *     
         */
        public RigidBody.TechniqueCommon.Dynamic getDynamic() {
            return dynamic;
        }

        /**
         * Sets the value of the dynamic property.
         * 
         * @param value
         *     allowed object is
         *     {@link RigidBody.TechniqueCommon.Dynamic }
         *     
         */
        public void setDynamic(RigidBody.TechniqueCommon.Dynamic value) {
            this.dynamic = value;
        }

        /**
         * Gets the value of the mass property.
         * 
         * @return
         *     possible object is
         *     {@link TargetableFloat }
         *     
         */
        public TargetableFloat getMass() {
            return mass;
        }

        /**
         * Sets the value of the mass property.
         * 
         * @param value
         *     allowed object is
         *     {@link TargetableFloat }
         *     
         */
        public void setMass(TargetableFloat value) {
            this.mass = value;
        }

        /**
         * Gets the value of the massFrame property.
         * 
         * @return
         *     possible object is
         *     {@link RigidBody.TechniqueCommon.MassFrame }
         *     
         */
        public RigidBody.TechniqueCommon.MassFrame getMassFrame() {
            return massFrame;
        }

        /**
         * Sets the value of the massFrame property.
         * 
         * @param value
         *     allowed object is
         *     {@link RigidBody.TechniqueCommon.MassFrame }
         *     
         */
        public void setMassFrame(RigidBody.TechniqueCommon.MassFrame value) {
            this.massFrame = value;
        }

        /**
         * Gets the value of the inertia property.
         * 
         * @return
         *     possible object is
         *     {@link TargetableFloat3 }
         *     
         */
        public TargetableFloat3 getInertia() {
            return inertia;
        }

        /**
         * Sets the value of the inertia property.
         * 
         * @param value
         *     allowed object is
         *     {@link TargetableFloat3 }
         *     
         */
        public void setInertia(TargetableFloat3 value) {
            this.inertia = value;
        }

        /**
         * 
         * 											References a physics_material for the rigid_body.
         * 										
         * 
         * @return
         *     possible object is
         *     {@link InstanceWithExtra }
         *     
         */
        public InstanceWithExtra getInstancePhysicsMaterial() {
            return instancePhysicsMaterial;
        }

        /**
         * Sets the value of the instancePhysicsMaterial property.
         * 
         * @param value
         *     allowed object is
         *     {@link InstanceWithExtra }
         *     
         */
        public void setInstancePhysicsMaterial(InstanceWithExtra value) {
            this.instancePhysicsMaterial = value;
        }

        /**
         * 
         * 											Defines a physics_material for the rigid_body.
         * 										
         * 
         * @return
         *     possible object is
         *     {@link PhysicsMaterial }
         *     
         */
        public PhysicsMaterial getPhysicsMaterial() {
            return physicsMaterial;
        }

        /**
         * Sets the value of the physicsMaterial property.
         * 
         * @param value
         *     allowed object is
         *     {@link PhysicsMaterial }
         *     
         */
        public void setPhysicsMaterial(PhysicsMaterial value) {
            this.physicsMaterial = value;
        }

        /**
         * Gets the value of the shape property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the shape property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getShape().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RigidBody.TechniqueCommon.Shape }
         * 
         * 
         */
        public List<RigidBody.TechniqueCommon.Shape> getShape() {
            if (shape == null) {
                shape = new ArrayList<RigidBody.TechniqueCommon.Shape>();
            }
            return this.shape;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;simpleContent>
         *     &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
         *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *     &lt;/extension>
         *   &lt;/simpleContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
        public static class Dynamic {

            @XmlValue
            protected boolean value;
            @XmlAttribute(name = "sid")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String sid;

            /**
             * Gets the value of the value property.
             * 
             */
            public boolean isValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             */
            public void setValue(boolean value) {
                this.value = value;
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


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice maxOccurs="unbounded">
         *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
         *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
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
            "translateOrRotate"
        })
        public static class MassFrame {

            @XmlElements({
                @XmlElement(name = "translate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = TargetableFloat3 .class),
                @XmlElement(name = "rotate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Rotate.class)
            })
            protected List<Object> translateOrRotate;

            /**
             * Gets the value of the translateOrRotate property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the translateOrRotate property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTranslateOrRotate().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TargetableFloat3 }
             * {@link Rotate }
             * 
             * 
             */
            public List<Object> getTranslateOrRotate() {
                if (translateOrRotate == null) {
                    translateOrRotate = new ArrayList<Object>();
                }
                return this.translateOrRotate;
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
         *         &lt;element name="hollow" minOccurs="0">
         *           &lt;complexType>
         *             &lt;simpleContent>
         *               &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
         *                 &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *               &lt;/extension>
         *             &lt;/simpleContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="mass" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;element name="density" type="{http://www.collada.org/2005/11/COLLADASchema}TargetableFloat" minOccurs="0"/>
         *         &lt;choice minOccurs="0">
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_physics_material"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}physics_material"/>
         *         &lt;/choice>
         *         &lt;choice>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}instance_geometry"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}plane"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}box"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}sphere"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}cylinder"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tapered_cylinder"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}capsule"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}tapered_capsule"/>
         *         &lt;/choice>
         *         &lt;choice maxOccurs="unbounded" minOccurs="0">
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}translate"/>
         *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}rotate"/>
         *         &lt;/choice>
         *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
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
            "hollow",
            "mass",
            "density",
            "instancePhysicsMaterial",
            "physicsMaterial",
            "instanceGeometry",
            "plane",
            "box",
            "sphere",
            "cylinder",
            "taperedCylinder",
            "capsule",
            "taperedCapsule",
            "translateOrRotate",
            "extra"
        })
        public static class Shape {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected RigidBody.TechniqueCommon.Shape.Hollow hollow;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected TargetableFloat mass;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected TargetableFloat density;
            @XmlElement(name = "instance_physics_material", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected InstanceWithExtra instancePhysicsMaterial;
            @XmlElement(name = "physics_material", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected PhysicsMaterial physicsMaterial;
            @XmlElement(name = "instance_geometry", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected InstanceGeometry instanceGeometry;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Plane plane;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Box box;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Sphere sphere;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Cylinder cylinder;
            @XmlElement(name = "tapered_cylinder", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected TaperedCylinder taperedCylinder;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Capsule capsule;
            @XmlElement(name = "tapered_capsule", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected TaperedCapsule taperedCapsule;
            @XmlElements({
                @XmlElement(name = "translate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = TargetableFloat3 .class),
                @XmlElement(name = "rotate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Rotate.class)
            })
            protected List<Object> translateOrRotate;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<Extra> extra;

            /**
             * Gets the value of the hollow property.
             * 
             * @return
             *     possible object is
             *     {@link RigidBody.TechniqueCommon.Shape.Hollow }
             *     
             */
            public RigidBody.TechniqueCommon.Shape.Hollow getHollow() {
                return hollow;
            }

            /**
             * Sets the value of the hollow property.
             * 
             * @param value
             *     allowed object is
             *     {@link RigidBody.TechniqueCommon.Shape.Hollow }
             *     
             */
            public void setHollow(RigidBody.TechniqueCommon.Shape.Hollow value) {
                this.hollow = value;
            }

            /**
             * Gets the value of the mass property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getMass() {
                return mass;
            }

            /**
             * Sets the value of the mass property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setMass(TargetableFloat value) {
                this.mass = value;
            }

            /**
             * Gets the value of the density property.
             * 
             * @return
             *     possible object is
             *     {@link TargetableFloat }
             *     
             */
            public TargetableFloat getDensity() {
                return density;
            }

            /**
             * Sets the value of the density property.
             * 
             * @param value
             *     allowed object is
             *     {@link TargetableFloat }
             *     
             */
            public void setDensity(TargetableFloat value) {
                this.density = value;
            }

            /**
             * 
             * 														References a physics_material for the shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link InstanceWithExtra }
             *     
             */
            public InstanceWithExtra getInstancePhysicsMaterial() {
                return instancePhysicsMaterial;
            }

            /**
             * Sets the value of the instancePhysicsMaterial property.
             * 
             * @param value
             *     allowed object is
             *     {@link InstanceWithExtra }
             *     
             */
            public void setInstancePhysicsMaterial(InstanceWithExtra value) {
                this.instancePhysicsMaterial = value;
            }

            /**
             * 
             * 														Defines a physics_material for the shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link PhysicsMaterial }
             *     
             */
            public PhysicsMaterial getPhysicsMaterial() {
                return physicsMaterial;
            }

            /**
             * Sets the value of the physicsMaterial property.
             * 
             * @param value
             *     allowed object is
             *     {@link PhysicsMaterial }
             *     
             */
            public void setPhysicsMaterial(PhysicsMaterial value) {
                this.physicsMaterial = value;
            }

            /**
             * 
             * 														Instances a geometry to use to define this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link InstanceGeometry }
             *     
             */
            public InstanceGeometry getInstanceGeometry() {
                return instanceGeometry;
            }

            /**
             * Sets the value of the instanceGeometry property.
             * 
             * @param value
             *     allowed object is
             *     {@link InstanceGeometry }
             *     
             */
            public void setInstanceGeometry(InstanceGeometry value) {
                this.instanceGeometry = value;
            }

            /**
             * 
             * 														Defines a plane to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link Plane }
             *     
             */
            public Plane getPlane() {
                return plane;
            }

            /**
             * Sets the value of the plane property.
             * 
             * @param value
             *     allowed object is
             *     {@link Plane }
             *     
             */
            public void setPlane(Plane value) {
                this.plane = value;
            }

            /**
             * 
             * 														Defines a box to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link Box }
             *     
             */
            public Box getBox() {
                return box;
            }

            /**
             * Sets the value of the box property.
             * 
             * @param value
             *     allowed object is
             *     {@link Box }
             *     
             */
            public void setBox(Box value) {
                this.box = value;
            }

            /**
             * 
             * 														Defines a sphere to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link Sphere }
             *     
             */
            public Sphere getSphere() {
                return sphere;
            }

            /**
             * Sets the value of the sphere property.
             * 
             * @param value
             *     allowed object is
             *     {@link Sphere }
             *     
             */
            public void setSphere(Sphere value) {
                this.sphere = value;
            }

            /**
             * 
             * 														Defines a cyliner to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link Cylinder }
             *     
             */
            public Cylinder getCylinder() {
                return cylinder;
            }

            /**
             * Sets the value of the cylinder property.
             * 
             * @param value
             *     allowed object is
             *     {@link Cylinder }
             *     
             */
            public void setCylinder(Cylinder value) {
                this.cylinder = value;
            }

            /**
             * 
             * 														Defines a tapered_cylinder to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link TaperedCylinder }
             *     
             */
            public TaperedCylinder getTaperedCylinder() {
                return taperedCylinder;
            }

            /**
             * Sets the value of the taperedCylinder property.
             * 
             * @param value
             *     allowed object is
             *     {@link TaperedCylinder }
             *     
             */
            public void setTaperedCylinder(TaperedCylinder value) {
                this.taperedCylinder = value;
            }

            /**
             * 
             * 														Defines a capsule to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link Capsule }
             *     
             */
            public Capsule getCapsule() {
                return capsule;
            }

            /**
             * Sets the value of the capsule property.
             * 
             * @param value
             *     allowed object is
             *     {@link Capsule }
             *     
             */
            public void setCapsule(Capsule value) {
                this.capsule = value;
            }

            /**
             * 
             * 														Defines a tapered_capsule to use for this shape.
             * 													
             * 
             * @return
             *     possible object is
             *     {@link TaperedCapsule }
             *     
             */
            public TaperedCapsule getTaperedCapsule() {
                return taperedCapsule;
            }

            /**
             * Sets the value of the taperedCapsule property.
             * 
             * @param value
             *     allowed object is
             *     {@link TaperedCapsule }
             *     
             */
            public void setTaperedCapsule(TaperedCapsule value) {
                this.taperedCapsule = value;
            }

            /**
             * Gets the value of the translateOrRotate property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the translateOrRotate property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTranslateOrRotate().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TargetableFloat3 }
             * {@link Rotate }
             * 
             * 
             */
            public List<Object> getTranslateOrRotate() {
                if (translateOrRotate == null) {
                    translateOrRotate = new ArrayList<Object>();
                }
                return this.translateOrRotate;
            }

            /**
             * 
             * 													The extra element may appear any number of times.
             * 												Gets the value of the extra property.
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;simpleContent>
             *     &lt;extension base="&lt;http://www.collada.org/2005/11/COLLADASchema>bool">
             *       &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/extension>
             *   &lt;/simpleContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class Hollow {

                @XmlValue
                protected boolean value;
                @XmlAttribute(name = "sid")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String sid;

                /**
                 * Gets the value of the value property.
                 * 
                 */
                public boolean isValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 */
                public void setValue(boolean value) {
                    this.value = value;
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

        }

    }

}
