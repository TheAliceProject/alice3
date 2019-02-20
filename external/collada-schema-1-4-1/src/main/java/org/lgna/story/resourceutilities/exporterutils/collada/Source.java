
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
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}IDREF_array"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}Name_array"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}bool_array"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}float_array"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}int_array"/>
 *         &lt;/choice>
 *         &lt;element name="technique_common" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}accessor"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}technique" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
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
    "idrefArray",
    "nameArray",
    "boolArray",
    "floatArray",
    "intArray",
    "techniqueCommon",
    "technique"
})
@XmlRootElement(name = "source", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class Source {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElement(name = "IDREF_array", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected IDREFArray idrefArray;
    @XmlElement(name = "Name_array", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected NameArray nameArray;
    @XmlElement(name = "bool_array", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected BoolArray boolArray;
    @XmlElement(name = "float_array", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected FloatArray floatArray;
    @XmlElement(name = "int_array", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected IntArray intArray;
    @XmlElement(name = "technique_common", namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Source.TechniqueCommon techniqueCommon;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Technique> technique;
    @XmlAttribute(name = "id", required = true)
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
     * 							The source element may contain an asset element.
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
     * 
     * 								The source element may contain an IDREF_array.
     * 							
     * 
     * @return
     *     possible object is
     *     {@link IDREFArray }
     *     
     */
    public IDREFArray getIDREFArray() {
        return idrefArray;
    }

    /**
     * Sets the value of the idrefArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDREFArray }
     *     
     */
    public void setIDREFArray(IDREFArray value) {
        this.idrefArray = value;
    }

    /**
     * 
     * 								The source element may contain a Name_array.
     * 							
     * 
     * @return
     *     possible object is
     *     {@link NameArray }
     *     
     */
    public NameArray getNameArray() {
        return nameArray;
    }

    /**
     * Sets the value of the nameArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameArray }
     *     
     */
    public void setNameArray(NameArray value) {
        this.nameArray = value;
    }

    /**
     * 
     * 								The source element may contain a bool_array.
     * 							
     * 
     * @return
     *     possible object is
     *     {@link BoolArray }
     *     
     */
    public BoolArray getBoolArray() {
        return boolArray;
    }

    /**
     * Sets the value of the boolArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoolArray }
     *     
     */
    public void setBoolArray(BoolArray value) {
        this.boolArray = value;
    }

    /**
     * 
     * 								The source element may contain a float_array.
     * 							
     * 
     * @return
     *     possible object is
     *     {@link FloatArray }
     *     
     */
    public FloatArray getFloatArray() {
        return floatArray;
    }

    /**
     * Sets the value of the floatArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link FloatArray }
     *     
     */
    public void setFloatArray(FloatArray value) {
        this.floatArray = value;
    }

    /**
     * 
     * 								The source element may contain an int_array.
     * 							
     * 
     * @return
     *     possible object is
     *     {@link IntArray }
     *     
     */
    public IntArray getIntArray() {
        return intArray;
    }

    /**
     * Sets the value of the intArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntArray }
     *     
     */
    public void setIntArray(IntArray value) {
        this.intArray = value;
    }

    /**
     * Gets the value of the techniqueCommon property.
     * 
     * @return
     *     possible object is
     *     {@link Source.TechniqueCommon }
     *     
     */
    public Source.TechniqueCommon getTechniqueCommon() {
        return techniqueCommon;
    }

    /**
     * Sets the value of the techniqueCommon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Source.TechniqueCommon }
     *     
     */
    public void setTechniqueCommon(Source.TechniqueCommon value) {
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
     *       &lt;sequence>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}accessor"/>
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
        "accessor"
    })
    public static class TechniqueCommon {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected Accessor accessor;

        /**
         * 
         * 										The source's technique_common must have one and only one accessor.
         * 									
         * 
         * @return
         *     possible object is
         *     {@link Accessor }
         *     
         */
        public Accessor getAccessor() {
            return accessor;
        }

        /**
         * Sets the value of the accessor property.
         * 
         * @param value
         *     allowed object is
         *     {@link Accessor }
         *     
         */
        public void setAccessor(Accessor value) {
            this.accessor = value;
        }

    }

}
