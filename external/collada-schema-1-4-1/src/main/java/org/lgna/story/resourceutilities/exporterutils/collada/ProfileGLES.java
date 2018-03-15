
package org.lgna.story.resourceutilities.exporterutils.collada;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlList;
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
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
 *           &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}gles_newparam"/>
 *         &lt;/choice>
 *         &lt;element name="technique" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset" minOccurs="0"/>
 *                   &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
 *                     &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}gles_newparam"/>
 *                     &lt;element name="setparam">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *                               &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_basic_type_common"/>
 *                             &lt;/sequence>
 *                             &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                   &lt;element name="pass" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="color_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
 *                             &lt;element name="depth_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
 *                             &lt;element name="stencil_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
 *                             &lt;element name="color_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_color_common" minOccurs="0"/>
 *                             &lt;element name="depth_clear" type="{http://www.collada.org/2005/11/COLLADASchema}float" minOccurs="0"/>
 *                             &lt;element name="stencil_clear" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
 *                             &lt;element name="draw" type="{http://www.collada.org/2005/11/COLLADASchema}fx_draw_common" minOccurs="0"/>
 *                             &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                               &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_pipeline_settings"/>
 *                             &lt;/choice>
 *                             &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *                 &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="platform" type="{http://www.w3.org/2001/XMLSchema}NCName" default="PC" />
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
    "imageOrNewparam",
    "technique",
    "extra"
})
public class ProfileGLES {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElements({
        @XmlElement(name = "image", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Image.class),
        @XmlElement(name = "newparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlesNewparam.class)
    })
    protected List<Object> imageOrNewparam;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected List<ProfileGLES.Technique> technique;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "platform")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String platform;

    /**
     * Gets the value of the asset property.
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
     * Gets the value of the imageOrNewparam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imageOrNewparam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImageOrNewparam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Image }
     * {@link GlesNewparam }
     * 
     * 
     */
    public List<Object> getImageOrNewparam() {
        if (imageOrNewparam == null) {
            imageOrNewparam = new ArrayList<Object>();
        }
        return this.imageOrNewparam;
    }

    /**
     * Gets the value of the technique property.
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
     * {@link ProfileGLES.Technique }
     * 
     * 
     */
    public List<ProfileGLES.Technique> getTechnique() {
        if (technique == null) {
            technique = new ArrayList<ProfileGLES.Technique>();
        }
        return this.technique;
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
     * Gets the value of the platform property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlatform() {
        if (platform == null) {
            return "PC";
        } else {
            return platform;
        }
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
     *         &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;choice maxOccurs="unbounded" minOccurs="0">
     *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
     *           &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}gles_newparam"/>
     *           &lt;element name="setparam">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
     *                     &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_basic_type_common"/>
     *                   &lt;/sequence>
     *                   &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/choice>
     *         &lt;element name="pass" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="color_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
     *                   &lt;element name="depth_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
     *                   &lt;element name="stencil_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
     *                   &lt;element name="color_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_color_common" minOccurs="0"/>
     *                   &lt;element name="depth_clear" type="{http://www.collada.org/2005/11/COLLADASchema}float" minOccurs="0"/>
     *                   &lt;element name="stencil_clear" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
     *                   &lt;element name="draw" type="{http://www.collada.org/2005/11/COLLADASchema}fx_draw_common" minOccurs="0"/>
     *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
     *                     &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_pipeline_settings"/>
     *                   &lt;/choice>
     *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="sid" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
     *       &lt;attribute name="sid" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
        "annotate",
        "imageOrNewparamOrSetparam",
        "pass",
        "extra"
    })
    public static class Technique {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected Asset asset;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<FxAnnotateCommon> annotate;
        @XmlElements({
            @XmlElement(name = "image", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Image.class),
            @XmlElement(name = "newparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlesNewparam.class),
            @XmlElement(name = "setparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Setparam.class)
        })
        protected List<Object> imageOrNewparamOrSetparam;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected List<ProfileGLES.Technique.Pass> pass;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected String id;
        @XmlAttribute(name = "sid", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String sid;

        /**
         * Gets the value of the asset property.
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
         * Gets the value of the annotate property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the annotate property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAnnotate().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FxAnnotateCommon }
         * 
         * 
         */
        public List<FxAnnotateCommon> getAnnotate() {
            if (annotate == null) {
                annotate = new ArrayList<FxAnnotateCommon>();
            }
            return this.annotate;
        }

        /**
         * Gets the value of the imageOrNewparamOrSetparam property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the imageOrNewparamOrSetparam property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImageOrNewparamOrSetparam().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Image }
         * {@link GlesNewparam }
         * {@link ProfileGLES.Technique.Setparam }
         * 
         * 
         */
        public List<Object> getImageOrNewparamOrSetparam() {
            if (imageOrNewparamOrSetparam == null) {
                imageOrNewparamOrSetparam = new ArrayList<Object>();
            }
            return this.imageOrNewparamOrSetparam;
        }

        /**
         * Gets the value of the pass property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the pass property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPass().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ProfileGLES.Technique.Pass }
         * 
         * 
         */
        public List<ProfileGLES.Technique.Pass> getPass() {
            if (pass == null) {
                pass = new ArrayList<ProfileGLES.Technique.Pass>();
            }
            return this.pass;
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
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="color_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
         *         &lt;element name="depth_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
         *         &lt;element name="stencil_target" type="{http://www.collada.org/2005/11/COLLADASchema}gles_rendertarget_common" minOccurs="0"/>
         *         &lt;element name="color_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_color_common" minOccurs="0"/>
         *         &lt;element name="depth_clear" type="{http://www.collada.org/2005/11/COLLADASchema}float" minOccurs="0"/>
         *         &lt;element name="stencil_clear" type="{http://www.w3.org/2001/XMLSchema}byte" minOccurs="0"/>
         *         &lt;element name="draw" type="{http://www.collada.org/2005/11/COLLADASchema}fx_draw_common" minOccurs="0"/>
         *         &lt;choice maxOccurs="unbounded" minOccurs="0">
         *           &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_pipeline_settings"/>
         *         &lt;/choice>
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
        @XmlType(name = "", propOrder = {
            "annotate",
            "colorTarget",
            "depthTarget",
            "stencilTarget",
            "colorClear",
            "depthClear",
            "stencilClear",
            "draw",
            "alphaFuncOrBlendFuncOrClearColor",
            "extra"
        })
        public static class Pass {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxAnnotateCommon> annotate;
            @XmlElement(name = "color_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String colorTarget;
            @XmlElement(name = "depth_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String depthTarget;
            @XmlElement(name = "stencil_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String stencilTarget;
            @XmlList
            @XmlElement(name = "color_clear", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Double.class)
            protected List<Double> colorClear;
            @XmlElement(name = "depth_clear", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Double depthClear;
            @XmlElement(name = "stencil_clear", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected Byte stencilClear;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected String draw;
            @XmlElements({
                @XmlElement(name = "alpha_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.AlphaFunc.class),
                @XmlElement(name = "blend_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.BlendFunc.class),
                @XmlElement(name = "clear_color", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ClearColor.class),
                @XmlElement(name = "clear_stencil", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ClearStencil.class),
                @XmlElement(name = "clear_depth", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ClearDepth.class),
                @XmlElement(name = "clip_plane", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ClipPlane.class),
                @XmlElement(name = "color_mask", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ColorMask.class),
                @XmlElement(name = "cull_face", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.CullFace.class),
                @XmlElement(name = "depth_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.DepthFunc.class),
                @XmlElement(name = "depth_mask", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.DepthMask.class),
                @XmlElement(name = "depth_range", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.DepthRange.class),
                @XmlElement(name = "fog_color", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FogColor.class),
                @XmlElement(name = "fog_density", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FogDensity.class),
                @XmlElement(name = "fog_mode", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FogMode.class),
                @XmlElement(name = "fog_start", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FogStart.class),
                @XmlElement(name = "fog_end", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FogEnd.class),
                @XmlElement(name = "front_face", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FrontFace.class),
                @XmlElement(name = "texture_pipeline", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.TexturePipeline.class),
                @XmlElement(name = "logic_op", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LogicOp.class),
                @XmlElement(name = "light_ambient", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightAmbient.class),
                @XmlElement(name = "light_diffuse", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightDiffuse.class),
                @XmlElement(name = "light_specular", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightSpecular.class),
                @XmlElement(name = "light_position", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightPosition.class),
                @XmlElement(name = "light_constant_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightConstantAttenuation.class),
                @XmlElement(name = "light_linear_attenutation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightLinearAttenutation.class),
                @XmlElement(name = "light_quadratic_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightQuadraticAttenuation.class),
                @XmlElement(name = "light_spot_cutoff", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightSpotCutoff.class),
                @XmlElement(name = "light_spot_direction", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightSpotDirection.class),
                @XmlElement(name = "light_spot_exponent", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightSpotExponent.class),
                @XmlElement(name = "light_model_ambient", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightModelAmbient.class),
                @XmlElement(name = "line_width", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LineWidth.class),
                @XmlElement(name = "material_ambient", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.MaterialAmbient.class),
                @XmlElement(name = "material_diffuse", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.MaterialDiffuse.class),
                @XmlElement(name = "material_emission", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.MaterialEmission.class),
                @XmlElement(name = "material_shininess", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.MaterialShininess.class),
                @XmlElement(name = "material_specular", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.MaterialSpecular.class),
                @XmlElement(name = "model_view_matrix", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ModelViewMatrix.class),
                @XmlElement(name = "point_distance_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PointDistanceAttenuation.class),
                @XmlElement(name = "point_fade_threshold_size", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PointFadeThresholdSize.class),
                @XmlElement(name = "point_size", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PointSize.class),
                @XmlElement(name = "point_size_min", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PointSizeMin.class),
                @XmlElement(name = "point_size_max", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PointSizeMax.class),
                @XmlElement(name = "polygon_offset", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PolygonOffset.class),
                @XmlElement(name = "projection_matrix", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ProjectionMatrix.class),
                @XmlElement(name = "scissor", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.Scissor.class),
                @XmlElement(name = "shade_model", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ShadeModel.class),
                @XmlElement(name = "stencil_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.StencilFunc.class),
                @XmlElement(name = "stencil_mask", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.StencilMask.class),
                @XmlElement(name = "stencil_op", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.StencilOp.class),
                @XmlElement(name = "alpha_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.AlphaTestEnable.class),
                @XmlElement(name = "blend_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.BlendEnable.class),
                @XmlElement(name = "clip_plane_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ClipPlaneEnable.class),
                @XmlElement(name = "color_logic_op_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ColorLogicOpEnable.class),
                @XmlElement(name = "color_material_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ColorMaterialEnable.class),
                @XmlElement(name = "cull_face_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.CullFaceEnable.class),
                @XmlElement(name = "depth_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.DepthTestEnable.class),
                @XmlElement(name = "dither_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.DitherEnable.class),
                @XmlElement(name = "fog_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.FogEnable.class),
                @XmlElement(name = "texture_pipeline_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.TexturePipelineEnable.class),
                @XmlElement(name = "light_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightEnable.class),
                @XmlElement(name = "lighting_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightingEnable.class),
                @XmlElement(name = "light_model_two_side_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LightModelTwoSideEnable.class),
                @XmlElement(name = "line_smooth_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.LineSmoothEnable.class),
                @XmlElement(name = "multisample_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.MultisampleEnable.class),
                @XmlElement(name = "normalize_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.NormalizeEnable.class),
                @XmlElement(name = "point_smooth_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PointSmoothEnable.class),
                @XmlElement(name = "polygon_offset_fill_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.PolygonOffsetFillEnable.class),
                @XmlElement(name = "rescale_normal_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.RescaleNormalEnable.class),
                @XmlElement(name = "sample_alpha_to_coverage_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.SampleAlphaToCoverageEnable.class),
                @XmlElement(name = "sample_alpha_to_one_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.SampleAlphaToOneEnable.class),
                @XmlElement(name = "sample_coverage_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.SampleCoverageEnable.class),
                @XmlElement(name = "scissor_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.ScissorTestEnable.class),
                @XmlElement(name = "stencil_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLES.Technique.Pass.StencilTestEnable.class)
            })
            protected List<Object> alphaFuncOrBlendFuncOrClearColor;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<Extra> extra;
            @XmlAttribute(name = "sid")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String sid;

            /**
             * Gets the value of the annotate property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the annotate property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAnnotate().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxAnnotateCommon }
             * 
             * 
             */
            public List<FxAnnotateCommon> getAnnotate() {
                if (annotate == null) {
                    annotate = new ArrayList<FxAnnotateCommon>();
                }
                return this.annotate;
            }

            /**
             * Gets the value of the colorTarget property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getColorTarget() {
                return colorTarget;
            }

            /**
             * Sets the value of the colorTarget property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setColorTarget(String value) {
                this.colorTarget = value;
            }

            /**
             * Gets the value of the depthTarget property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDepthTarget() {
                return depthTarget;
            }

            /**
             * Sets the value of the depthTarget property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDepthTarget(String value) {
                this.depthTarget = value;
            }

            /**
             * Gets the value of the stencilTarget property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStencilTarget() {
                return stencilTarget;
            }

            /**
             * Sets the value of the stencilTarget property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStencilTarget(String value) {
                this.stencilTarget = value;
            }

            /**
             * Gets the value of the colorClear property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the colorClear property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getColorClear().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Double }
             * 
             * 
             */
            public List<Double> getColorClear() {
                if (colorClear == null) {
                    colorClear = new ArrayList<Double>();
                }
                return this.colorClear;
            }

            /**
             * Gets the value of the depthClear property.
             * 
             * @return
             *     possible object is
             *     {@link Double }
             *     
             */
            public Double getDepthClear() {
                return depthClear;
            }

            /**
             * Sets the value of the depthClear property.
             * 
             * @param value
             *     allowed object is
             *     {@link Double }
             *     
             */
            public void setDepthClear(Double value) {
                this.depthClear = value;
            }

            /**
             * Gets the value of the stencilClear property.
             * 
             * @return
             *     possible object is
             *     {@link Byte }
             *     
             */
            public Byte getStencilClear() {
                return stencilClear;
            }

            /**
             * Sets the value of the stencilClear property.
             * 
             * @param value
             *     allowed object is
             *     {@link Byte }
             *     
             */
            public void setStencilClear(Byte value) {
                this.stencilClear = value;
            }

            /**
             * Gets the value of the draw property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDraw() {
                return draw;
            }

            /**
             * Sets the value of the draw property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDraw(String value) {
                this.draw = value;
            }

            /**
             * Gets the value of the alphaFuncOrBlendFuncOrClearColor property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the alphaFuncOrBlendFuncOrClearColor property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAlphaFuncOrBlendFuncOrClearColor().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ProfileGLES.Technique.Pass.AlphaFunc }
             * {@link ProfileGLES.Technique.Pass.BlendFunc }
             * {@link ProfileGLES.Technique.Pass.ClearColor }
             * {@link ProfileGLES.Technique.Pass.ClearStencil }
             * {@link ProfileGLES.Technique.Pass.ClearDepth }
             * {@link ProfileGLES.Technique.Pass.ClipPlane }
             * {@link ProfileGLES.Technique.Pass.ColorMask }
             * {@link ProfileGLES.Technique.Pass.CullFace }
             * {@link ProfileGLES.Technique.Pass.DepthFunc }
             * {@link ProfileGLES.Technique.Pass.DepthMask }
             * {@link ProfileGLES.Technique.Pass.DepthRange }
             * {@link ProfileGLES.Technique.Pass.FogColor }
             * {@link ProfileGLES.Technique.Pass.FogDensity }
             * {@link ProfileGLES.Technique.Pass.FogMode }
             * {@link ProfileGLES.Technique.Pass.FogStart }
             * {@link ProfileGLES.Technique.Pass.FogEnd }
             * {@link ProfileGLES.Technique.Pass.FrontFace }
             * {@link ProfileGLES.Technique.Pass.TexturePipeline }
             * {@link ProfileGLES.Technique.Pass.LogicOp }
             * {@link ProfileGLES.Technique.Pass.LightAmbient }
             * {@link ProfileGLES.Technique.Pass.LightDiffuse }
             * {@link ProfileGLES.Technique.Pass.LightSpecular }
             * {@link ProfileGLES.Technique.Pass.LightPosition }
             * {@link ProfileGLES.Technique.Pass.LightConstantAttenuation }
             * {@link ProfileGLES.Technique.Pass.LightLinearAttenutation }
             * {@link ProfileGLES.Technique.Pass.LightQuadraticAttenuation }
             * {@link ProfileGLES.Technique.Pass.LightSpotCutoff }
             * {@link ProfileGLES.Technique.Pass.LightSpotDirection }
             * {@link ProfileGLES.Technique.Pass.LightSpotExponent }
             * {@link ProfileGLES.Technique.Pass.LightModelAmbient }
             * {@link ProfileGLES.Technique.Pass.LineWidth }
             * {@link ProfileGLES.Technique.Pass.MaterialAmbient }
             * {@link ProfileGLES.Technique.Pass.MaterialDiffuse }
             * {@link ProfileGLES.Technique.Pass.MaterialEmission }
             * {@link ProfileGLES.Technique.Pass.MaterialShininess }
             * {@link ProfileGLES.Technique.Pass.MaterialSpecular }
             * {@link ProfileGLES.Technique.Pass.ModelViewMatrix }
             * {@link ProfileGLES.Technique.Pass.PointDistanceAttenuation }
             * {@link ProfileGLES.Technique.Pass.PointFadeThresholdSize }
             * {@link ProfileGLES.Technique.Pass.PointSize }
             * {@link ProfileGLES.Technique.Pass.PointSizeMin }
             * {@link ProfileGLES.Technique.Pass.PointSizeMax }
             * {@link ProfileGLES.Technique.Pass.PolygonOffset }
             * {@link ProfileGLES.Technique.Pass.ProjectionMatrix }
             * {@link ProfileGLES.Technique.Pass.Scissor }
             * {@link ProfileGLES.Technique.Pass.ShadeModel }
             * {@link ProfileGLES.Technique.Pass.StencilFunc }
             * {@link ProfileGLES.Technique.Pass.StencilMask }
             * {@link ProfileGLES.Technique.Pass.StencilOp }
             * {@link ProfileGLES.Technique.Pass.AlphaTestEnable }
             * {@link ProfileGLES.Technique.Pass.BlendEnable }
             * {@link ProfileGLES.Technique.Pass.ClipPlaneEnable }
             * {@link ProfileGLES.Technique.Pass.ColorLogicOpEnable }
             * {@link ProfileGLES.Technique.Pass.ColorMaterialEnable }
             * {@link ProfileGLES.Technique.Pass.CullFaceEnable }
             * {@link ProfileGLES.Technique.Pass.DepthTestEnable }
             * {@link ProfileGLES.Technique.Pass.DitherEnable }
             * {@link ProfileGLES.Technique.Pass.FogEnable }
             * {@link ProfileGLES.Technique.Pass.TexturePipelineEnable }
             * {@link ProfileGLES.Technique.Pass.LightEnable }
             * {@link ProfileGLES.Technique.Pass.LightingEnable }
             * {@link ProfileGLES.Technique.Pass.LightModelTwoSideEnable }
             * {@link ProfileGLES.Technique.Pass.LineSmoothEnable }
             * {@link ProfileGLES.Technique.Pass.MultisampleEnable }
             * {@link ProfileGLES.Technique.Pass.NormalizeEnable }
             * {@link ProfileGLES.Technique.Pass.PointSmoothEnable }
             * {@link ProfileGLES.Technique.Pass.PolygonOffsetFillEnable }
             * {@link ProfileGLES.Technique.Pass.RescaleNormalEnable }
             * {@link ProfileGLES.Technique.Pass.SampleAlphaToCoverageEnable }
             * {@link ProfileGLES.Technique.Pass.SampleAlphaToOneEnable }
             * {@link ProfileGLES.Technique.Pass.SampleCoverageEnable }
             * {@link ProfileGLES.Technique.Pass.ScissorTestEnable }
             * {@link ProfileGLES.Technique.Pass.StencilTestEnable }
             * 
             * 
             */
            public List<Object> getAlphaFuncOrBlendFuncOrClearColor() {
                if (alphaFuncOrBlendFuncOrClearColor == null) {
                    alphaFuncOrBlendFuncOrClearColor = new ArrayList<Object>();
                }
                return this.alphaFuncOrBlendFuncOrClearColor;
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
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="func">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_func_type" default="ALWAYS" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="value">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_alpha_value_type" default="0.0" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
                "func",
                "value"
            })
            public static class AlphaFunc {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.AlphaFunc.Func func;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.AlphaFunc.Value value;

                /**
                 * Gets the value of the func property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.AlphaFunc.Func }
                 *     
                 */
                public ProfileGLES.Technique.Pass.AlphaFunc.Func getFunc() {
                    return func;
                }

                /**
                 * Sets the value of the func property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.AlphaFunc.Func }
                 *     
                 */
                public void setFunc(ProfileGLES.Technique.Pass.AlphaFunc.Func value) {
                    this.func = value;
                }

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.AlphaFunc.Value }
                 *     
                 */
                public ProfileGLES.Technique.Pass.AlphaFunc.Value getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.AlphaFunc.Value }
                 *     
                 */
                public void setValue(ProfileGLES.Technique.Pass.AlphaFunc.Value value) {
                    this.value = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_func_type" default="ALWAYS" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Func {

                    @XmlAttribute(name = "value")
                    protected GlFuncType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlFuncType }
                     *     
                     */
                    public GlFuncType getValue() {
                        if (value == null) {
                            return GlFuncType.ALWAYS;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlFuncType }
                     *     
                     */
                    public void setValue(GlFuncType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_alpha_value_type" default="0.0" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Value {

                    @XmlAttribute(name = "value")
                    protected Float value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Float }
                     *     
                     */
                    public float getValue() {
                        if (value == null) {
                            return  0.0F;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Float }
                     *     
                     */
                    public void setValue(Float value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
                    }

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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class AlphaTestEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class BlendEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *         &lt;element name="src">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_blend_type" default="ONE" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="dest">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_blend_type" default="ZERO" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
                "src",
                "dest"
            })
            public static class BlendFunc {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.BlendFunc.Src src;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.BlendFunc.Dest dest;

                /**
                 * Gets the value of the src property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.BlendFunc.Src }
                 *     
                 */
                public ProfileGLES.Technique.Pass.BlendFunc.Src getSrc() {
                    return src;
                }

                /**
                 * Sets the value of the src property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.BlendFunc.Src }
                 *     
                 */
                public void setSrc(ProfileGLES.Technique.Pass.BlendFunc.Src value) {
                    this.src = value;
                }

                /**
                 * Gets the value of the dest property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.BlendFunc.Dest }
                 *     
                 */
                public ProfileGLES.Technique.Pass.BlendFunc.Dest getDest() {
                    return dest;
                }

                /**
                 * Sets the value of the dest property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.BlendFunc.Dest }
                 *     
                 */
                public void setDest(ProfileGLES.Technique.Pass.BlendFunc.Dest value) {
                    this.dest = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_blend_type" default="ZERO" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Dest {

                    @XmlAttribute(name = "value")
                    protected GlBlendType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlBlendType }
                     *     
                     */
                    public GlBlendType getValue() {
                        if (value == null) {
                            return GlBlendType.ZERO;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlBlendType }
                     *     
                     */
                    public void setValue(GlBlendType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_blend_type" default="ONE" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Src {

                    @XmlAttribute(name = "value")
                    protected GlBlendType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlBlendType }
                     *     
                     */
                    public GlBlendType getValue() {
                        if (value == null) {
                            return GlBlendType.ONE;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlBlendType }
                     *     
                     */
                    public void setValue(GlBlendType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
                    }

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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ClearColor {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ClearDepth {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public Double getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}int" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ClearStencil {

                @XmlAttribute(name = "value")
                protected Long value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Long }
                 *     
                 */
                public Long getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Long }
                 *     
                 */
                public void setValue(Long value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool4" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_CLIP_PLANES_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ClipPlane {

                @XmlAttribute(name = "value")
                protected List<Boolean> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Boolean }
                 * 
                 * 
                 */
                public List<Boolean> getValue() {
                    if (value == null) {
                        value = new ArrayList<Boolean>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_CLIP_PLANES_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ClipPlaneEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index")
                protected Integer index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setIndex(Integer value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ColorLogicOpEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool4" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ColorMask {

                @XmlAttribute(name = "value")
                protected List<Boolean> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Boolean }
                 * 
                 * 
                 */
                public List<Boolean> getValue() {
                    if (value == null) {
                        value = new ArrayList<Boolean>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="true" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ColorMaterialEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return true;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_face_type" default="BACK" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class CullFace {

                @XmlAttribute(name = "value")
                protected GlFaceType value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlFaceType }
                 *     
                 */
                public GlFaceType getValue() {
                    if (value == null) {
                        return GlFaceType.BACK;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlFaceType }
                 *     
                 */
                public void setValue(GlFaceType value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class CullFaceEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_func_type" default="ALWAYS" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class DepthFunc {

                @XmlAttribute(name = "value")
                protected GlFuncType value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlFuncType }
                 *     
                 */
                public GlFuncType getValue() {
                    if (value == null) {
                        return GlFuncType.ALWAYS;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlFuncType }
                 *     
                 */
                public void setValue(GlFuncType value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class DepthMask {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float2" default="0 1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class DepthRange {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class DepthTestEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class DitherEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 0 0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FogColor {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FogDensity {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FogEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FogEnd {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_fog_type" default="EXP" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FogMode {

                @XmlAttribute(name = "value")
                protected GlFogType value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlFogType }
                 *     
                 */
                public GlFogType getValue() {
                    if (value == null) {
                        return GlFogType.EXP;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlFogType }
                 *     
                 */
                public void setValue(GlFogType value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FogStart {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  0.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_front_face_type" default="CCW" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class FrontFace {

                @XmlAttribute(name = "value")
                protected GlFrontFaceType value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlFrontFaceType }
                 *     
                 */
                public GlFrontFaceType getValue() {
                    if (value == null) {
                        return GlFrontFaceType.CCW;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlFrontFaceType }
                 *     
                 */
                public void setValue(GlFrontFaceType value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 0 1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightAmbient {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightConstantAttenuation {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 0 0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightDiffuse {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightLinearAttenutation {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0.2 0.2 0.2 1.0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightModelAmbient {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightModelTwoSideEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 1 0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightPosition {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightQuadraticAttenuation {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 0 0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightSpecular {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="180" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightSpotCutoff {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  180.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float3" default="0 0 -1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightSpotDirection {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="index" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}GLES_MAX_LIGHTS_index" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightSpotExponent {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;
                @XmlAttribute(name = "index", required = true)
                protected int index;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  0.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

                /**
                 * Gets the value of the index property.
                 * 
                 */
                public int getIndex() {
                    return index;
                }

                /**
                 * Sets the value of the index property.
                 * 
                 */
                public void setIndex(int value) {
                    this.index = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LightingEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LineSmoothEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LineWidth {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_logic_op_type" default="COPY" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class LogicOp {

                @XmlAttribute(name = "value")
                protected GlLogicOpType value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlLogicOpType }
                 *     
                 */
                public GlLogicOpType getValue() {
                    if (value == null) {
                        return GlLogicOpType.COPY;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlLogicOpType }
                 *     
                 */
                public void setValue(GlLogicOpType value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0.2 0.2 0.2 1.0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class MaterialAmbient {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0.8 0.8 0.8 1.0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class MaterialDiffuse {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 0 1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class MaterialEmission {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class MaterialShininess {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  0.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4" default="0 0 0 1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class MaterialSpecular {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4x4" default="1 0 0 0 0 1 0 0 0 0 1 0 0 0 0 1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ModelViewMatrix {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class MultisampleEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class NormalizeEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float3" default="1 0 0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PointDistanceAttenuation {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PointFadeThresholdSize {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PointSize {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PointSizeMax {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  1.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float" default="0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PointSizeMin {

                @XmlAttribute(name = "value")
                protected Double value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Double }
                 *     
                 */
                public double getValue() {
                    if (value == null) {
                        return  0.0D;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Double }
                 *     
                 */
                public void setValue(Double value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PointSmoothEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float2" default="0 0" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PolygonOffset {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class PolygonOffsetFillEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}float4x4" default="1 0 0 0 0 1 0 0 0 0 1 0 0 0 0 1" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ProjectionMatrix {

                @XmlAttribute(name = "value")
                protected List<Double> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Double }
                 * 
                 * 
                 */
                public List<Double> getValue() {
                    if (value == null) {
                        value = new ArrayList<Double>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class RescaleNormalEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class SampleAlphaToCoverageEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class SampleAlphaToOneEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class SampleCoverageEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}int4" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Scissor {

                @XmlAttribute(name = "value")
                protected List<Long> value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the value property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getValue().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link Long }
                 * 
                 * 
                 */
                public List<Long> getValue() {
                    if (value == null) {
                        value = new ArrayList<Long>();
                    }
                    return this.value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ScissorTestEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_shade_model_type" default="SMOOTH" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class ShadeModel {

                @XmlAttribute(name = "value")
                protected GlShadeModelType value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlShadeModelType }
                 *     
                 */
                public GlShadeModelType getValue() {
                    if (value == null) {
                        return GlShadeModelType.SMOOTH;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlShadeModelType }
                 *     
                 */
                public void setValue(GlShadeModelType value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *         &lt;element name="func">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_func_type" default="ALWAYS" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="ref">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" default="0" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="mask">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" default="255" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
                "func",
                "ref",
                "mask"
            })
            public static class StencilFunc {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.StencilFunc.Func func;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.StencilFunc.Ref ref;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.StencilFunc.Mask mask;

                /**
                 * Gets the value of the func property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.StencilFunc.Func }
                 *     
                 */
                public ProfileGLES.Technique.Pass.StencilFunc.Func getFunc() {
                    return func;
                }

                /**
                 * Sets the value of the func property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.StencilFunc.Func }
                 *     
                 */
                public void setFunc(ProfileGLES.Technique.Pass.StencilFunc.Func value) {
                    this.func = value;
                }

                /**
                 * Gets the value of the ref property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.StencilFunc.Ref }
                 *     
                 */
                public ProfileGLES.Technique.Pass.StencilFunc.Ref getRef() {
                    return ref;
                }

                /**
                 * Sets the value of the ref property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.StencilFunc.Ref }
                 *     
                 */
                public void setRef(ProfileGLES.Technique.Pass.StencilFunc.Ref value) {
                    this.ref = value;
                }

                /**
                 * Gets the value of the mask property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.StencilFunc.Mask }
                 *     
                 */
                public ProfileGLES.Technique.Pass.StencilFunc.Mask getMask() {
                    return mask;
                }

                /**
                 * Sets the value of the mask property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.StencilFunc.Mask }
                 *     
                 */
                public void setMask(ProfileGLES.Technique.Pass.StencilFunc.Mask value) {
                    this.mask = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gl_func_type" default="ALWAYS" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Func {

                    @XmlAttribute(name = "value")
                    protected GlFuncType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlFuncType }
                     *     
                     */
                    public GlFuncType getValue() {
                        if (value == null) {
                            return GlFuncType.ALWAYS;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlFuncType }
                     *     
                     */
                    public void setValue(GlFuncType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
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
                 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" default="255" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Mask {

                    @XmlAttribute(name = "value")
                    @XmlSchemaType(name = "unsignedByte")
                    protected Short value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Short }
                     *     
                     */
                    public short getValue() {
                        if (value == null) {
                            return ((short) 255);
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Short }
                     *     
                     */
                    public void setValue(Short value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
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
                 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" default="0" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Ref {

                    @XmlAttribute(name = "value")
                    @XmlSchemaType(name = "unsignedByte")
                    protected Short value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Short }
                     *     
                     */
                    public short getValue() {
                        if (value == null) {
                            return ((short) 0);
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Short }
                     *     
                     */
                    public void setValue(Short value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
                    }

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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}int" default="4294967295" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class StencilMask {

                @XmlAttribute(name = "value")
                protected Long value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Long }
                 *     
                 */
                public long getValue() {
                    if (value == null) {
                        return  4294967295L;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Long }
                 *     
                 */
                public void setValue(Long value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *         &lt;element name="fail">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_stencil_op_type" default="KEEP" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="zfail">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_stencil_op_type" default="KEEP" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="zpass">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_stencil_op_type" default="KEEP" />
             *                 &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
                "fail",
                "zfail",
                "zpass"
            })
            public static class StencilOp {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.StencilOp.Fail fail;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.StencilOp.Zfail zfail;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLES.Technique.Pass.StencilOp.Zpass zpass;

                /**
                 * Gets the value of the fail property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.StencilOp.Fail }
                 *     
                 */
                public ProfileGLES.Technique.Pass.StencilOp.Fail getFail() {
                    return fail;
                }

                /**
                 * Sets the value of the fail property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.StencilOp.Fail }
                 *     
                 */
                public void setFail(ProfileGLES.Technique.Pass.StencilOp.Fail value) {
                    this.fail = value;
                }

                /**
                 * Gets the value of the zfail property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.StencilOp.Zfail }
                 *     
                 */
                public ProfileGLES.Technique.Pass.StencilOp.Zfail getZfail() {
                    return zfail;
                }

                /**
                 * Sets the value of the zfail property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.StencilOp.Zfail }
                 *     
                 */
                public void setZfail(ProfileGLES.Technique.Pass.StencilOp.Zfail value) {
                    this.zfail = value;
                }

                /**
                 * Gets the value of the zpass property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLES.Technique.Pass.StencilOp.Zpass }
                 *     
                 */
                public ProfileGLES.Technique.Pass.StencilOp.Zpass getZpass() {
                    return zpass;
                }

                /**
                 * Sets the value of the zpass property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLES.Technique.Pass.StencilOp.Zpass }
                 *     
                 */
                public void setZpass(ProfileGLES.Technique.Pass.StencilOp.Zpass value) {
                    this.zpass = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_stencil_op_type" default="KEEP" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Fail {

                    @XmlAttribute(name = "value")
                    protected GlesStencilOpType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlesStencilOpType }
                     *     
                     */
                    public GlesStencilOpType getValue() {
                        if (value == null) {
                            return GlesStencilOpType.KEEP;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlesStencilOpType }
                     *     
                     */
                    public void setValue(GlesStencilOpType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_stencil_op_type" default="KEEP" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Zfail {

                    @XmlAttribute(name = "value")
                    protected GlesStencilOpType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlesStencilOpType }
                     *     
                     */
                    public GlesStencilOpType getValue() {
                        if (value == null) {
                            return GlesStencilOpType.KEEP;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlesStencilOpType }
                     *     
                     */
                    public void setValue(GlesStencilOpType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
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
                 *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_stencil_op_type" default="KEEP" />
                 *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Zpass {

                    @XmlAttribute(name = "value")
                    protected GlesStencilOpType value;
                    @XmlAttribute(name = "param")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String param;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlesStencilOpType }
                     *     
                     */
                    public GlesStencilOpType getValue() {
                        if (value == null) {
                            return GlesStencilOpType.KEEP;
                        } else {
                            return value;
                        }
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlesStencilOpType }
                     *     
                     */
                    public void setValue(GlesStencilOpType value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setParam(String value) {
                        this.param = value;
                    }

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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class StencilTestEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *         &lt;element name="value" type="{http://www.collada.org/2005/11/COLLADASchema}gles_texture_pipeline" minOccurs="0"/>
             *       &lt;/sequence>
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "value"
            })
            public static class TexturePipeline {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                protected GlesTexturePipeline value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlesTexturePipeline }
                 *     
                 */
                public GlesTexturePipeline getValue() {
                    return value;
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlesTexturePipeline }
                 *     
                 */
                public void setValue(GlesTexturePipeline value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
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
             *       &lt;attribute name="value" type="{http://www.collada.org/2005/11/COLLADASchema}bool" default="false" />
             *       &lt;attribute name="param" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class TexturePipelineEnable {

                @XmlAttribute(name = "value")
                protected Boolean value;
                @XmlAttribute(name = "param")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String param;

                /**
                 * Gets the value of the value property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public boolean isValue() {
                    if (value == null) {
                        return false;
                    } else {
                        return value;
                    }
                }

                /**
                 * Sets the value of the value property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setValue(Boolean value) {
                    this.value = value;
                }

                /**
                 * Gets the value of the param property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getParam() {
                    return param;
                }

                /**
                 * Sets the value of the param property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setParam(String value) {
                    this.param = value;
                }

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
         *         &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gles_basic_type_common"/>
         *       &lt;/sequence>
         *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "annotate",
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
            "texturePipeline",
            "samplerState",
            "textureUnit",
            "_enum"
        })
        public static class Setparam {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxAnnotateCommon> annotate;
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
            @XmlElement(name = "texture_pipeline", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected GlesTexturePipeline texturePipeline;
            @XmlElement(name = "sampler_state", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected GlesSamplerState samplerState;
            @XmlElement(name = "texture_unit", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected GlesTextureUnit textureUnit;
            @XmlElement(name = "enum", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected String _enum;
            @XmlAttribute(name = "ref", required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String ref;

            /**
             * Gets the value of the annotate property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the annotate property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAnnotate().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxAnnotateCommon }
             * 
             * 
             */
            public List<FxAnnotateCommon> getAnnotate() {
                if (annotate == null) {
                    annotate = new ArrayList<FxAnnotateCommon>();
                }
                return this.annotate;
            }

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
             * Gets the value of the texturePipeline property.
             * 
             * @return
             *     possible object is
             *     {@link GlesTexturePipeline }
             *     
             */
            public GlesTexturePipeline getTexturePipeline() {
                return texturePipeline;
            }

            /**
             * Sets the value of the texturePipeline property.
             * 
             * @param value
             *     allowed object is
             *     {@link GlesTexturePipeline }
             *     
             */
            public void setTexturePipeline(GlesTexturePipeline value) {
                this.texturePipeline = value;
            }

            /**
             * Gets the value of the samplerState property.
             * 
             * @return
             *     possible object is
             *     {@link GlesSamplerState }
             *     
             */
            public GlesSamplerState getSamplerState() {
                return samplerState;
            }

            /**
             * Sets the value of the samplerState property.
             * 
             * @param value
             *     allowed object is
             *     {@link GlesSamplerState }
             *     
             */
            public void setSamplerState(GlesSamplerState value) {
                this.samplerState = value;
            }

            /**
             * Gets the value of the textureUnit property.
             * 
             * @return
             *     possible object is
             *     {@link GlesTextureUnit }
             *     
             */
            public GlesTextureUnit getTextureUnit() {
                return textureUnit;
            }

            /**
             * Sets the value of the textureUnit property.
             * 
             * @param value
             *     allowed object is
             *     {@link GlesTextureUnit }
             *     
             */
            public void setTextureUnit(GlesTextureUnit value) {
                this.textureUnit = value;
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

    }

}
