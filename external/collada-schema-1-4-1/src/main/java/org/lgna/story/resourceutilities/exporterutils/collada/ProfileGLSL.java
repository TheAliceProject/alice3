
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
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="code" type="{http://www.collada.org/2005/11/COLLADASchema}fx_code_profile"/>
 *           &lt;element name="include" type="{http://www.collada.org/2005/11/COLLADASchema}fx_include_common"/>
 *         &lt;/choice>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
 *           &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_newparam"/>
 *         &lt;/choice>
 *         &lt;element name="technique" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element name="code" type="{http://www.collada.org/2005/11/COLLADASchema}fx_code_profile"/>
 *                     &lt;element name="include" type="{http://www.collada.org/2005/11/COLLADASchema}fx_include_common"/>
 *                   &lt;/choice>
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
 *                     &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_newparam"/>
 *                     &lt;element name="setparam" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_setparam"/>
 *                   &lt;/choice>
 *                   &lt;element name="pass" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="color_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_colortarget_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="depth_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_depthtarget_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="stencil_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_stenciltarget_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="color_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_clearcolor_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="depth_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_cleardepth_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="stencil_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_clearstencil_common" maxOccurs="unbounded" minOccurs="0"/>
 *                             &lt;element name="draw" type="{http://www.collada.org/2005/11/COLLADASchema}fx_draw_common" minOccurs="0"/>
 *                             &lt;choice maxOccurs="unbounded">
 *                               &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gl_pipeline_settings"/>
 *                               &lt;element name="shader">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;sequence>
 *                                         &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
 *                                         &lt;sequence minOccurs="0">
 *                                           &lt;element name="compiler_target">
 *                                             &lt;complexType>
 *                                               &lt;simpleContent>
 *                                                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NMTOKEN">
 *                                                 &lt;/extension>
 *                                               &lt;/simpleContent>
 *                                             &lt;/complexType>
 *                                           &lt;/element>
 *                                           &lt;element name="compiler_options" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                         &lt;/sequence>
 *                                         &lt;element name="name">
 *                                           &lt;complexType>
 *                                             &lt;simpleContent>
 *                                               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NCName">
 *                                                 &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                               &lt;/extension>
 *                                             &lt;/simpleContent>
 *                                           &lt;/complexType>
 *                                         &lt;/element>
 *                                         &lt;element name="bind" maxOccurs="unbounded" minOccurs="0">
 *                                           &lt;complexType>
 *                                             &lt;complexContent>
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                 &lt;choice>
 *                                                   &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}glsl_param_type"/>
 *                                                   &lt;element name="param">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                           &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                 &lt;/choice>
 *                                                 &lt;attribute name="symbol" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                               &lt;/restriction>
 *                                             &lt;/complexContent>
 *                                           &lt;/complexType>
 *                                         &lt;/element>
 *                                       &lt;/sequence>
 *                                       &lt;attribute name="stage" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_pipeline_stage" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
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
    "codeOrInclude",
    "imageOrNewparam",
    "technique",
    "extra"
})
public class ProfileGLSL {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected Asset asset;
    @XmlElements({
        @XmlElement(name = "code", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = FxCodeProfile.class),
        @XmlElement(name = "include", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = FxIncludeCommon.class)
    })
    protected List<Object> codeOrInclude;
    @XmlElements({
        @XmlElement(name = "image", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Image.class),
        @XmlElement(name = "newparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlslNewparam.class)
    })
    protected List<Object> imageOrNewparam;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected List<ProfileGLSL.Technique> technique;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

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
     * Gets the value of the codeOrInclude property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeOrInclude property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeOrInclude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FxCodeProfile }
     * {@link FxIncludeCommon }
     * 
     * 
     */
    public List<Object> getCodeOrInclude() {
        if (codeOrInclude == null) {
            codeOrInclude = new ArrayList<Object>();
        }
        return this.codeOrInclude;
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
     * {@link GlslNewparam }
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
     * {@link ProfileGLSL.Technique }
     * 
     * 
     */
    public List<ProfileGLSL.Technique> getTechnique() {
        if (technique == null) {
            technique = new ArrayList<ProfileGLSL.Technique>();
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
     *         &lt;choice maxOccurs="unbounded" minOccurs="0">
     *           &lt;element name="code" type="{http://www.collada.org/2005/11/COLLADASchema}fx_code_profile"/>
     *           &lt;element name="include" type="{http://www.collada.org/2005/11/COLLADASchema}fx_include_common"/>
     *         &lt;/choice>
     *         &lt;choice maxOccurs="unbounded" minOccurs="0">
     *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}image"/>
     *           &lt;element name="newparam" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_newparam"/>
     *           &lt;element name="setparam" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_setparam"/>
     *         &lt;/choice>
     *         &lt;element name="pass" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="color_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_colortarget_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="depth_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_depthtarget_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="stencil_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_stenciltarget_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="color_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_clearcolor_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="depth_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_cleardepth_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="stencil_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_clearstencil_common" maxOccurs="unbounded" minOccurs="0"/>
     *                   &lt;element name="draw" type="{http://www.collada.org/2005/11/COLLADASchema}fx_draw_common" minOccurs="0"/>
     *                   &lt;choice maxOccurs="unbounded">
     *                     &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gl_pipeline_settings"/>
     *                     &lt;element name="shader">
     *                       &lt;complexType>
     *                         &lt;complexContent>
     *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                             &lt;sequence>
     *                               &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
     *                               &lt;sequence minOccurs="0">
     *                                 &lt;element name="compiler_target">
     *                                   &lt;complexType>
     *                                     &lt;simpleContent>
     *                                       &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NMTOKEN">
     *                                       &lt;/extension>
     *                                     &lt;/simpleContent>
     *                                   &lt;/complexType>
     *                                 &lt;/element>
     *                                 &lt;element name="compiler_options" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                               &lt;/sequence>
     *                               &lt;element name="name">
     *                                 &lt;complexType>
     *                                   &lt;simpleContent>
     *                                     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NCName">
     *                                       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;/extension>
     *                                   &lt;/simpleContent>
     *                                 &lt;/complexType>
     *                               &lt;/element>
     *                               &lt;element name="bind" maxOccurs="unbounded" minOccurs="0">
     *                                 &lt;complexType>
     *                                   &lt;complexContent>
     *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                       &lt;choice>
     *                                         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}glsl_param_type"/>
     *                                         &lt;element name="param">
     *                                           &lt;complexType>
     *                                             &lt;complexContent>
     *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                                 &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                                               &lt;/restriction>
     *                                             &lt;/complexContent>
     *                                           &lt;/complexType>
     *                                         &lt;/element>
     *                                       &lt;/choice>
     *                                       &lt;attribute name="symbol" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;/restriction>
     *                                   &lt;/complexContent>
     *                                 &lt;/complexType>
     *                               &lt;/element>
     *                             &lt;/sequence>
     *                             &lt;attribute name="stage" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_pipeline_stage" />
     *                           &lt;/restriction>
     *                         &lt;/complexContent>
     *                       &lt;/complexType>
     *                     &lt;/element>
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
        "annotate",
        "codeOrInclude",
        "imageOrNewparamOrSetparam",
        "pass",
        "extra"
    })
    public static class Technique {

        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<FxAnnotateCommon> annotate;
        @XmlElements({
            @XmlElement(name = "code", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = FxCodeProfile.class),
            @XmlElement(name = "include", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = FxIncludeCommon.class)
        })
        protected List<Object> codeOrInclude;
        @XmlElements({
            @XmlElement(name = "image", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Image.class),
            @XmlElement(name = "newparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlslNewparam.class),
            @XmlElement(name = "setparam", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = GlslSetparam.class)
        })
        protected List<Object> imageOrNewparamOrSetparam;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
        protected List<ProfileGLSL.Technique.Pass> pass;
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
         * Gets the value of the codeOrInclude property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the codeOrInclude property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCodeOrInclude().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FxCodeProfile }
         * {@link FxIncludeCommon }
         * 
         * 
         */
        public List<Object> getCodeOrInclude() {
            if (codeOrInclude == null) {
                codeOrInclude = new ArrayList<Object>();
            }
            return this.codeOrInclude;
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
         * {@link GlslNewparam }
         * {@link GlslSetparam }
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
         * {@link ProfileGLSL.Technique.Pass }
         * 
         * 
         */
        public List<ProfileGLSL.Technique.Pass> getPass() {
            if (pass == null) {
                pass = new ArrayList<ProfileGLSL.Technique.Pass>();
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
         *         &lt;element name="color_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_colortarget_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="depth_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_depthtarget_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="stencil_target" type="{http://www.collada.org/2005/11/COLLADASchema}fx_stenciltarget_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="color_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_clearcolor_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="depth_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_cleardepth_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="stencil_clear" type="{http://www.collada.org/2005/11/COLLADASchema}fx_clearstencil_common" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="draw" type="{http://www.collada.org/2005/11/COLLADASchema}fx_draw_common" minOccurs="0"/>
         *         &lt;choice maxOccurs="unbounded">
         *           &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}gl_pipeline_settings"/>
         *           &lt;element name="shader">
         *             &lt;complexType>
         *               &lt;complexContent>
         *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                   &lt;sequence>
         *                     &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
         *                     &lt;sequence minOccurs="0">
         *                       &lt;element name="compiler_target">
         *                         &lt;complexType>
         *                           &lt;simpleContent>
         *                             &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NMTOKEN">
         *                             &lt;/extension>
         *                           &lt;/simpleContent>
         *                         &lt;/complexType>
         *                       &lt;/element>
         *                       &lt;element name="compiler_options" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                     &lt;/sequence>
         *                     &lt;element name="name">
         *                       &lt;complexType>
         *                         &lt;simpleContent>
         *                           &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NCName">
         *                             &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;/extension>
         *                         &lt;/simpleContent>
         *                       &lt;/complexType>
         *                     &lt;/element>
         *                     &lt;element name="bind" maxOccurs="unbounded" minOccurs="0">
         *                       &lt;complexType>
         *                         &lt;complexContent>
         *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                             &lt;choice>
         *                               &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}glsl_param_type"/>
         *                               &lt;element name="param">
         *                                 &lt;complexType>
         *                                   &lt;complexContent>
         *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                                     &lt;/restriction>
         *                                   &lt;/complexContent>
         *                                 &lt;/complexType>
         *                               &lt;/element>
         *                             &lt;/choice>
         *                             &lt;attribute name="symbol" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;/restriction>
         *                         &lt;/complexContent>
         *                       &lt;/complexType>
         *                     &lt;/element>
         *                   &lt;/sequence>
         *                   &lt;attribute name="stage" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_pipeline_stage" />
         *                 &lt;/restriction>
         *               &lt;/complexContent>
         *             &lt;/complexType>
         *           &lt;/element>
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
            "alphaFuncOrBlendFuncOrBlendFuncSeparate",
            "extra"
        })
        public static class Pass {

            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxAnnotateCommon> annotate;
            @XmlElement(name = "color_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxColortargetCommon> colorTarget;
            @XmlElement(name = "depth_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxDepthtargetCommon> depthTarget;
            @XmlElement(name = "stencil_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxStenciltargetCommon> stencilTarget;
            @XmlElement(name = "color_clear", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxClearcolorCommon> colorClear;
            @XmlElement(name = "depth_clear", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxCleardepthCommon> depthClear;
            @XmlElement(name = "stencil_clear", namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected List<FxClearstencilCommon> stencilClear;
            @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
            protected String draw;
            @XmlElements({
                @XmlElement(name = "alpha_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.AlphaFunc.class),
                @XmlElement(name = "blend_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendFunc.class),
                @XmlElement(name = "blend_func_separate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendFuncSeparate.class),
                @XmlElement(name = "blend_equation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendEquation.class),
                @XmlElement(name = "blend_equation_separate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendEquationSeparate.class),
                @XmlElement(name = "color_material", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorMaterial.class),
                @XmlElement(name = "cull_face", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.CullFace.class),
                @XmlElement(name = "depth_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthFunc.class),
                @XmlElement(name = "fog_mode", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogMode.class),
                @XmlElement(name = "fog_coord_src", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogCoordSrc.class),
                @XmlElement(name = "front_face", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FrontFace.class),
                @XmlElement(name = "light_model_color_control", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelColorControl.class),
                @XmlElement(name = "logic_op", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LogicOp.class),
                @XmlElement(name = "polygon_mode", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonMode.class),
                @XmlElement(name = "shade_model", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ShadeModel.class),
                @XmlElement(name = "stencil_func", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilFunc.class),
                @XmlElement(name = "stencil_op", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilOp.class),
                @XmlElement(name = "stencil_func_separate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilFuncSeparate.class),
                @XmlElement(name = "stencil_op_separate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilOpSeparate.class),
                @XmlElement(name = "stencil_mask_separate", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilMaskSeparate.class),
                @XmlElement(name = "light_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightEnable.class),
                @XmlElement(name = "light_ambient", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightAmbient.class),
                @XmlElement(name = "light_diffuse", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightDiffuse.class),
                @XmlElement(name = "light_specular", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpecular.class),
                @XmlElement(name = "light_position", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightPosition.class),
                @XmlElement(name = "light_constant_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightConstantAttenuation.class),
                @XmlElement(name = "light_linear_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightLinearAttenuation.class),
                @XmlElement(name = "light_quadratic_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightQuadraticAttenuation.class),
                @XmlElement(name = "light_spot_cutoff", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpotCutoff.class),
                @XmlElement(name = "light_spot_direction", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpotDirection.class),
                @XmlElement(name = "light_spot_exponent", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpotExponent.class),
                @XmlElement(name = "texture1D", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture1D.class),
                @XmlElement(name = "texture2D", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture2D.class),
                @XmlElement(name = "texture3D", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture3D.class),
                @XmlElement(name = "textureCUBE", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureCUBE.class),
                @XmlElement(name = "textureRECT", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureRECT.class),
                @XmlElement(name = "textureDEPTH", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureDEPTH.class),
                @XmlElement(name = "texture1D_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture1DEnable.class),
                @XmlElement(name = "texture2D_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture2DEnable.class),
                @XmlElement(name = "texture3D_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture3DEnable.class),
                @XmlElement(name = "textureCUBE_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureCUBEEnable.class),
                @XmlElement(name = "textureRECT_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureRECTEnable.class),
                @XmlElement(name = "textureDEPTH_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureDEPTHEnable.class),
                @XmlElement(name = "texture_env_color", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureEnvColor.class),
                @XmlElement(name = "texture_env_mode", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureEnvMode.class),
                @XmlElement(name = "clip_plane", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClipPlane.class),
                @XmlElement(name = "clip_plane_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClipPlaneEnable.class),
                @XmlElement(name = "blend_color", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendColor.class),
                @XmlElement(name = "clear_color", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClearColor.class),
                @XmlElement(name = "clear_stencil", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClearStencil.class),
                @XmlElement(name = "clear_depth", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClearDepth.class),
                @XmlElement(name = "color_mask", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorMask.class),
                @XmlElement(name = "depth_bounds", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthBounds.class),
                @XmlElement(name = "depth_mask", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthMask.class),
                @XmlElement(name = "depth_range", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthRange.class),
                @XmlElement(name = "fog_density", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogDensity.class),
                @XmlElement(name = "fog_start", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogStart.class),
                @XmlElement(name = "fog_end", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogEnd.class),
                @XmlElement(name = "fog_color", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogColor.class),
                @XmlElement(name = "light_model_ambient", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelAmbient.class),
                @XmlElement(name = "lighting_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightingEnable.class),
                @XmlElement(name = "line_stipple", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineStipple.class),
                @XmlElement(name = "line_width", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineWidth.class),
                @XmlElement(name = "material_ambient", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialAmbient.class),
                @XmlElement(name = "material_diffuse", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialDiffuse.class),
                @XmlElement(name = "material_emission", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialEmission.class),
                @XmlElement(name = "material_shininess", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialShininess.class),
                @XmlElement(name = "material_specular", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialSpecular.class),
                @XmlElement(name = "model_view_matrix", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ModelViewMatrix.class),
                @XmlElement(name = "point_distance_attenuation", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointDistanceAttenuation.class),
                @XmlElement(name = "point_fade_threshold_size", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointFadeThresholdSize.class),
                @XmlElement(name = "point_size", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSize.class),
                @XmlElement(name = "point_size_min", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSizeMin.class),
                @XmlElement(name = "point_size_max", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSizeMax.class),
                @XmlElement(name = "polygon_offset", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffset.class),
                @XmlElement(name = "projection_matrix", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ProjectionMatrix.class),
                @XmlElement(name = "scissor", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Scissor.class),
                @XmlElement(name = "stencil_mask", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilMask.class),
                @XmlElement(name = "alpha_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.AlphaTestEnable.class),
                @XmlElement(name = "auto_normal_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.AutoNormalEnable.class),
                @XmlElement(name = "blend_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendEnable.class),
                @XmlElement(name = "color_logic_op_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorLogicOpEnable.class),
                @XmlElement(name = "color_material_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorMaterialEnable.class),
                @XmlElement(name = "cull_face_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.CullFaceEnable.class),
                @XmlElement(name = "depth_bounds_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthBoundsEnable.class),
                @XmlElement(name = "depth_clamp_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthClampEnable.class),
                @XmlElement(name = "depth_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthTestEnable.class),
                @XmlElement(name = "dither_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DitherEnable.class),
                @XmlElement(name = "fog_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogEnable.class),
                @XmlElement(name = "light_model_local_viewer_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelLocalViewerEnable.class),
                @XmlElement(name = "light_model_two_side_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelTwoSideEnable.class),
                @XmlElement(name = "line_smooth_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineSmoothEnable.class),
                @XmlElement(name = "line_stipple_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineStippleEnable.class),
                @XmlElement(name = "logic_op_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LogicOpEnable.class),
                @XmlElement(name = "multisample_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MultisampleEnable.class),
                @XmlElement(name = "normalize_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.NormalizeEnable.class),
                @XmlElement(name = "point_smooth_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSmoothEnable.class),
                @XmlElement(name = "polygon_offset_fill_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffsetFillEnable.class),
                @XmlElement(name = "polygon_offset_line_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffsetLineEnable.class),
                @XmlElement(name = "polygon_offset_point_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffsetPointEnable.class),
                @XmlElement(name = "polygon_smooth_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonSmoothEnable.class),
                @XmlElement(name = "polygon_stipple_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonStippleEnable.class),
                @XmlElement(name = "rescale_normal_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.RescaleNormalEnable.class),
                @XmlElement(name = "sample_alpha_to_coverage_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.SampleAlphaToCoverageEnable.class),
                @XmlElement(name = "sample_alpha_to_one_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.SampleAlphaToOneEnable.class),
                @XmlElement(name = "sample_coverage_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.SampleCoverageEnable.class),
                @XmlElement(name = "scissor_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ScissorTestEnable.class),
                @XmlElement(name = "stencil_test_enable", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilTestEnable.class),
                @XmlElement(name = "gl_hook_abstract", namespace = "http://www.collada.org/2005/11/COLLADASchema"),
                @XmlElement(name = "shader", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = ProfileGLSL.Technique.Pass.Shader.class)
            })
            protected List<Object> alphaFuncOrBlendFuncOrBlendFuncSeparate;
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
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the colorTarget property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getColorTarget().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxColortargetCommon }
             * 
             * 
             */
            public List<FxColortargetCommon> getColorTarget() {
                if (colorTarget == null) {
                    colorTarget = new ArrayList<FxColortargetCommon>();
                }
                return this.colorTarget;
            }

            /**
             * Gets the value of the depthTarget property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the depthTarget property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getDepthTarget().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxDepthtargetCommon }
             * 
             * 
             */
            public List<FxDepthtargetCommon> getDepthTarget() {
                if (depthTarget == null) {
                    depthTarget = new ArrayList<FxDepthtargetCommon>();
                }
                return this.depthTarget;
            }

            /**
             * Gets the value of the stencilTarget property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the stencilTarget property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getStencilTarget().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxStenciltargetCommon }
             * 
             * 
             */
            public List<FxStenciltargetCommon> getStencilTarget() {
                if (stencilTarget == null) {
                    stencilTarget = new ArrayList<FxStenciltargetCommon>();
                }
                return this.stencilTarget;
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
             * {@link FxClearcolorCommon }
             * 
             * 
             */
            public List<FxClearcolorCommon> getColorClear() {
                if (colorClear == null) {
                    colorClear = new ArrayList<FxClearcolorCommon>();
                }
                return this.colorClear;
            }

            /**
             * Gets the value of the depthClear property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the depthClear property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getDepthClear().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxCleardepthCommon }
             * 
             * 
             */
            public List<FxCleardepthCommon> getDepthClear() {
                if (depthClear == null) {
                    depthClear = new ArrayList<FxCleardepthCommon>();
                }
                return this.depthClear;
            }

            /**
             * Gets the value of the stencilClear property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the stencilClear property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getStencilClear().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link FxClearstencilCommon }
             * 
             * 
             */
            public List<FxClearstencilCommon> getStencilClear() {
                if (stencilClear == null) {
                    stencilClear = new ArrayList<FxClearstencilCommon>();
                }
                return this.stencilClear;
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
             * Gets the value of the alphaFuncOrBlendFuncOrBlendFuncSeparate property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the alphaFuncOrBlendFuncOrBlendFuncSeparate property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAlphaFuncOrBlendFuncOrBlendFuncSeparate().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.AlphaFunc }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendFunc }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendFuncSeparate }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendEquation }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendEquationSeparate }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorMaterial }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.CullFace }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthFunc }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogMode }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogCoordSrc }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FrontFace }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelColorControl }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LogicOp }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonMode }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ShadeModel }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilFunc }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilOp }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilFuncSeparate }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilOpSeparate }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilMaskSeparate }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightAmbient }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightDiffuse }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpecular }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightPosition }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightConstantAttenuation }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightLinearAttenuation }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightQuadraticAttenuation }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpotCutoff }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpotDirection }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightSpotExponent }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture1D }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture2D }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture3D }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureCUBE }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureRECT }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureDEPTH }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture1DEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture2DEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Texture3DEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureCUBEEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureRECTEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureDEPTHEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureEnvColor }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.TextureEnvMode }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClipPlane }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClipPlaneEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendColor }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClearColor }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClearStencil }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ClearDepth }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorMask }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthBounds }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthMask }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthRange }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogDensity }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogStart }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogEnd }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogColor }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelAmbient }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightingEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineStipple }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineWidth }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialAmbient }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialDiffuse }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialEmission }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialShininess }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MaterialSpecular }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ModelViewMatrix }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointDistanceAttenuation }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointFadeThresholdSize }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSize }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSizeMin }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSizeMax }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffset }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ProjectionMatrix }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.Scissor }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilMask }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.AlphaTestEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.AutoNormalEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.BlendEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorLogicOpEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ColorMaterialEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.CullFaceEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthBoundsEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthClampEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DepthTestEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.DitherEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.FogEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelLocalViewerEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LightModelTwoSideEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineSmoothEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LineStippleEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.LogicOpEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.MultisampleEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.NormalizeEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PointSmoothEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffsetFillEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffsetLineEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonOffsetPointEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonSmoothEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.PolygonStippleEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.RescaleNormalEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.SampleAlphaToCoverageEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.SampleAlphaToOneEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.SampleCoverageEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.ScissorTestEnable }
             * {@link org.lgna.story.resourceutilities.exporterutils.collada.ProfileCG.Technique.Pass.StencilTestEnable }
             * {@link Object }
             * {@link ProfileGLSL.Technique.Pass.Shader }
             * 
             * 
             */
            public List<Object> getAlphaFuncOrBlendFuncOrBlendFuncSeparate() {
                if (alphaFuncOrBlendFuncOrBlendFuncSeparate == null) {
                    alphaFuncOrBlendFuncOrBlendFuncSeparate = new ArrayList<Object>();
                }
                return this.alphaFuncOrBlendFuncOrBlendFuncSeparate;
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
             *         &lt;element name="annotate" type="{http://www.collada.org/2005/11/COLLADASchema}fx_annotate_common" maxOccurs="unbounded" minOccurs="0"/>
             *         &lt;sequence minOccurs="0">
             *           &lt;element name="compiler_target">
             *             &lt;complexType>
             *               &lt;simpleContent>
             *                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NMTOKEN">
             *                 &lt;/extension>
             *               &lt;/simpleContent>
             *             &lt;/complexType>
             *           &lt;/element>
             *           &lt;element name="compiler_options" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
             *         &lt;/sequence>
             *         &lt;element name="name">
             *           &lt;complexType>
             *             &lt;simpleContent>
             *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NCName">
             *                 &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/extension>
             *             &lt;/simpleContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="bind" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;choice>
             *                   &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}glsl_param_type"/>
             *                   &lt;element name="param">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/choice>
             *                 &lt;attribute name="symbol" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attribute name="stage" type="{http://www.collada.org/2005/11/COLLADASchema}glsl_pipeline_stage" />
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
                "compilerTarget",
                "compilerOptions",
                "name",
                "bind"
            })
            public static class Shader {

                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                protected List<FxAnnotateCommon> annotate;
                @XmlElement(name = "compiler_target", namespace = "http://www.collada.org/2005/11/COLLADASchema")
                protected ProfileGLSL.Technique.Pass.Shader.CompilerTarget compilerTarget;
                @XmlElement(name = "compiler_options", namespace = "http://www.collada.org/2005/11/COLLADASchema")
                protected String compilerOptions;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
                protected ProfileGLSL.Technique.Pass.Shader.Name name;
                @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                protected List<ProfileGLSL.Technique.Pass.Shader.Bind> bind;
                @XmlAttribute(name = "stage")
                protected GlslPipelineStage stage;

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
                 * Gets the value of the compilerTarget property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLSL.Technique.Pass.Shader.CompilerTarget }
                 *     
                 */
                public ProfileGLSL.Technique.Pass.Shader.CompilerTarget getCompilerTarget() {
                    return compilerTarget;
                }

                /**
                 * Sets the value of the compilerTarget property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLSL.Technique.Pass.Shader.CompilerTarget }
                 *     
                 */
                public void setCompilerTarget(ProfileGLSL.Technique.Pass.Shader.CompilerTarget value) {
                    this.compilerTarget = value;
                }

                /**
                 * Gets the value of the compilerOptions property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCompilerOptions() {
                    return compilerOptions;
                }

                /**
                 * Sets the value of the compilerOptions property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCompilerOptions(String value) {
                    this.compilerOptions = value;
                }

                /**
                 * Gets the value of the name property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link ProfileGLSL.Technique.Pass.Shader.Name }
                 *     
                 */
                public ProfileGLSL.Technique.Pass.Shader.Name getName() {
                    return name;
                }

                /**
                 * Sets the value of the name property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link ProfileGLSL.Technique.Pass.Shader.Name }
                 *     
                 */
                public void setName(ProfileGLSL.Technique.Pass.Shader.Name value) {
                    this.name = value;
                }

                /**
                 * Gets the value of the bind property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the bind property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getBind().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ProfileGLSL.Technique.Pass.Shader.Bind }
                 * 
                 * 
                 */
                public List<ProfileGLSL.Technique.Pass.Shader.Bind> getBind() {
                    if (bind == null) {
                        bind = new ArrayList<ProfileGLSL.Technique.Pass.Shader.Bind>();
                    }
                    return this.bind;
                }

                /**
                 * Gets the value of the stage property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link GlslPipelineStage }
                 *     
                 */
                public GlslPipelineStage getStage() {
                    return stage;
                }

                /**
                 * Sets the value of the stage property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link GlslPipelineStage }
                 *     
                 */
                public void setStage(GlslPipelineStage value) {
                    this.stage = value;
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
                 *         &lt;group ref="{http://www.collada.org/2005/11/COLLADASchema}glsl_param_type"/>
                 *         &lt;element name="param">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/choice>
                 *       &lt;attribute name="symbol" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
                    "_float",
                    "float2",
                    "float3",
                    "float4",
                    "float2X2",
                    "float3X3",
                    "float4X4",
                    "_int",
                    "int2",
                    "int3",
                    "int4",
                    "surface",
                    "sampler1D",
                    "sampler2D",
                    "sampler3D",
                    "samplerCUBE",
                    "samplerRECT",
                    "samplerDEPTH",
                    "_enum",
                    "param"
                })
                public static class Bind {

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
                    @XmlElement(name = "float", namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected Float _float;
                    @XmlList
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
                    protected List<Float> float2;
                    @XmlList
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
                    protected List<Float> float3;
                    @XmlList
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
                    protected List<Float> float4;
                    @XmlList
                    @XmlElement(name = "float2x2", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
                    protected List<Float> float2X2;
                    @XmlList
                    @XmlElement(name = "float3x3", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
                    protected List<Float> float3X3;
                    @XmlList
                    @XmlElement(name = "float4x4", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Float.class)
                    protected List<Float> float4X4;
                    @XmlElement(name = "int", namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected Integer _int;
                    @XmlList
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
                    protected List<Integer> int2;
                    @XmlList
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
                    protected List<Integer> int3;
                    @XmlList
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", type = Integer.class)
                    protected List<Integer> int4;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlslSurfaceType surface;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlSampler1D sampler1D;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlSampler2D sampler2D;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlSampler3D sampler3D;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlSamplerCUBE samplerCUBE;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlSamplerRECT samplerRECT;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected GlSamplerDEPTH samplerDEPTH;
                    @XmlElement(name = "enum", namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected String _enum;
                    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
                    protected ProfileGLSL.Technique.Pass.Shader.Bind.Param param;
                    @XmlAttribute(name = "symbol", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String symbol;

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
                     * Gets the value of the float property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Float }
                     *     
                     */
                    public Float getFloat() {
                        return _float;
                    }

                    /**
                     * Sets the value of the float property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Float }
                     *     
                     */
                    public void setFloat(Float value) {
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
                     * {@link Float }
                     * 
                     * 
                     */
                    public List<Float> getFloat2() {
                        if (float2 == null) {
                            float2 = new ArrayList<Float>();
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
                     * {@link Float }
                     * 
                     * 
                     */
                    public List<Float> getFloat3() {
                        if (float3 == null) {
                            float3 = new ArrayList<Float>();
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
                     * {@link Float }
                     * 
                     * 
                     */
                    public List<Float> getFloat4() {
                        if (float4 == null) {
                            float4 = new ArrayList<Float>();
                        }
                        return this.float4;
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
                     * {@link Float }
                     * 
                     * 
                     */
                    public List<Float> getFloat2X2() {
                        if (float2X2 == null) {
                            float2X2 = new ArrayList<Float>();
                        }
                        return this.float2X2;
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
                     * {@link Float }
                     * 
                     * 
                     */
                    public List<Float> getFloat3X3() {
                        if (float3X3 == null) {
                            float3X3 = new ArrayList<Float>();
                        }
                        return this.float3X3;
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
                     * {@link Float }
                     * 
                     * 
                     */
                    public List<Float> getFloat4X4() {
                        if (float4X4 == null) {
                            float4X4 = new ArrayList<Float>();
                        }
                        return this.float4X4;
                    }

                    /**
                     * Gets the value of the int property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Integer }
                     *     
                     */
                    public Integer getInt() {
                        return _int;
                    }

                    /**
                     * Sets the value of the int property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Integer }
                     *     
                     */
                    public void setInt(Integer value) {
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
                     * {@link Integer }
                     * 
                     * 
                     */
                    public List<Integer> getInt2() {
                        if (int2 == null) {
                            int2 = new ArrayList<Integer>();
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
                     * {@link Integer }
                     * 
                     * 
                     */
                    public List<Integer> getInt3() {
                        if (int3 == null) {
                            int3 = new ArrayList<Integer>();
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
                     * {@link Integer }
                     * 
                     * 
                     */
                    public List<Integer> getInt4() {
                        if (int4 == null) {
                            int4 = new ArrayList<Integer>();
                        }
                        return this.int4;
                    }

                    /**
                     * Gets the value of the surface property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlslSurfaceType }
                     *     
                     */
                    public GlslSurfaceType getSurface() {
                        return surface;
                    }

                    /**
                     * Sets the value of the surface property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlslSurfaceType }
                     *     
                     */
                    public void setSurface(GlslSurfaceType value) {
                        this.surface = value;
                    }

                    /**
                     * Gets the value of the sampler1D property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlSampler1D }
                     *     
                     */
                    public GlSampler1D getSampler1D() {
                        return sampler1D;
                    }

                    /**
                     * Sets the value of the sampler1D property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlSampler1D }
                     *     
                     */
                    public void setSampler1D(GlSampler1D value) {
                        this.sampler1D = value;
                    }

                    /**
                     * Gets the value of the sampler2D property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlSampler2D }
                     *     
                     */
                    public GlSampler2D getSampler2D() {
                        return sampler2D;
                    }

                    /**
                     * Sets the value of the sampler2D property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlSampler2D }
                     *     
                     */
                    public void setSampler2D(GlSampler2D value) {
                        this.sampler2D = value;
                    }

                    /**
                     * Gets the value of the sampler3D property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlSampler3D }
                     *     
                     */
                    public GlSampler3D getSampler3D() {
                        return sampler3D;
                    }

                    /**
                     * Sets the value of the sampler3D property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlSampler3D }
                     *     
                     */
                    public void setSampler3D(GlSampler3D value) {
                        this.sampler3D = value;
                    }

                    /**
                     * Gets the value of the samplerCUBE property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlSamplerCUBE }
                     *     
                     */
                    public GlSamplerCUBE getSamplerCUBE() {
                        return samplerCUBE;
                    }

                    /**
                     * Sets the value of the samplerCUBE property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlSamplerCUBE }
                     *     
                     */
                    public void setSamplerCUBE(GlSamplerCUBE value) {
                        this.samplerCUBE = value;
                    }

                    /**
                     * Gets the value of the samplerRECT property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlSamplerRECT }
                     *     
                     */
                    public GlSamplerRECT getSamplerRECT() {
                        return samplerRECT;
                    }

                    /**
                     * Sets the value of the samplerRECT property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlSamplerRECT }
                     *     
                     */
                    public void setSamplerRECT(GlSamplerRECT value) {
                        this.samplerRECT = value;
                    }

                    /**
                     * Gets the value of the samplerDEPTH property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link GlSamplerDEPTH }
                     *     
                     */
                    public GlSamplerDEPTH getSamplerDEPTH() {
                        return samplerDEPTH;
                    }

                    /**
                     * Sets the value of the samplerDEPTH property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link GlSamplerDEPTH }
                     *     
                     */
                    public void setSamplerDEPTH(GlSamplerDEPTH value) {
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
                     * Gets the value of the param property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link ProfileGLSL.Technique.Pass.Shader.Bind.Param }
                     *     
                     */
                    public ProfileGLSL.Technique.Pass.Shader.Bind.Param getParam() {
                        return param;
                    }

                    /**
                     * Sets the value of the param property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link ProfileGLSL.Technique.Pass.Shader.Bind.Param }
                     *     
                     */
                    public void setParam(ProfileGLSL.Technique.Pass.Shader.Bind.Param value) {
                        this.param = value;
                    }

                    /**
                     * Gets the value of the symbol property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSymbol() {
                        return symbol;
                    }

                    /**
                     * Sets the value of the symbol property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSymbol(String value) {
                        this.symbol = value;
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
                     *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "")
                    public static class Param {

                        @XmlAttribute(name = "ref", required = true)
                        protected String ref;

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


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NMTOKEN">
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
                public static class CompilerTarget {

                    @XmlValue
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NMTOKEN")
                    protected String value;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;simpleContent>
                 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>NCName">
                 *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}NCName" />
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
                public static class Name {

                    @XmlValue
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String value;
                    @XmlAttribute(name = "source")
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String source;

                    /**
                     * Gets the value of the value property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getValue() {
                        return value;
                    }

                    /**
                     * Sets the value of the value property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setValue(String value) {
                        this.value = value;
                    }

                    /**
                     * Gets the value of the source property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getSource() {
                        return source;
                    }

                    /**
                     * Sets the value of the source property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setSource(String value) {
                        this.source = value;
                    }

                }

            }

        }

    }

}
