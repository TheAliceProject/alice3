
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
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}asset"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_animations"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_animation_clips"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_cameras"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_controllers"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_geometries"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_effects"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_force_fields"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_images"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_lights"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_materials"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_nodes"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_physics_materials"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_physics_models"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_physics_scenes"/>
 *           &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}library_visual_scenes"/>
 *         &lt;/choice>
 *         &lt;element name="scene" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="instance_physics_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="instance_visual_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" minOccurs="0"/>
 *                   &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.collada.org/2005/11/COLLADASchema}extra" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.collada.org/2005/11/COLLADASchema}VersionType" />
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/>
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
    "libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras",
    "scene",
    "extra"
})
@XmlRootElement(name = "COLLADA", namespace = "http://www.collada.org/2005/11/COLLADASchema")
public class COLLADA {

    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema", required = true)
    protected Asset asset;
    @XmlElements({
        @XmlElement(name = "library_animations", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryAnimations.class),
        @XmlElement(name = "library_animation_clips", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryAnimationClips.class),
        @XmlElement(name = "library_cameras", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryCameras.class),
        @XmlElement(name = "library_controllers", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryControllers.class),
        @XmlElement(name = "library_geometries", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryGeometries.class),
        @XmlElement(name = "library_effects", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryEffects.class),
        @XmlElement(name = "library_force_fields", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryForceFields.class),
        @XmlElement(name = "library_images", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryImages.class),
        @XmlElement(name = "library_lights", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryLights.class),
        @XmlElement(name = "library_materials", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryMaterials.class),
        @XmlElement(name = "library_nodes", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryNodes.class),
        @XmlElement(name = "library_physics_materials", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryPhysicsMaterials.class),
        @XmlElement(name = "library_physics_models", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryPhysicsModels.class),
        @XmlElement(name = "library_physics_scenes", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryPhysicsScenes.class),
        @XmlElement(name = "library_visual_scenes", namespace = "http://www.collada.org/2005/11/COLLADASchema", type = LibraryVisualScenes.class)
    })
    protected List<Object> libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected COLLADA.Scene scene;
    @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
    protected List<Extra> extra;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    protected String base;

    /**
     * 
     * 							The COLLADA element must contain an asset element.
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
     * Gets the value of the libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryAnimations }
     * {@link LibraryAnimationClips }
     * {@link LibraryCameras }
     * {@link LibraryControllers }
     * {@link LibraryGeometries }
     * {@link LibraryEffects }
     * {@link LibraryForceFields }
     * {@link LibraryImages }
     * {@link LibraryLights }
     * {@link LibraryMaterials }
     * {@link LibraryNodes }
     * {@link LibraryPhysicsMaterials }
     * {@link LibraryPhysicsModels }
     * {@link LibraryPhysicsScenes }
     * {@link LibraryVisualScenes }
     * 
     * 
     */
    public List<Object> getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras() {
        if (libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras == null) {
            libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras = new ArrayList<Object>();
        }
        return this.libraryAnimationsOrLibraryAnimationClipsOrLibraryCameras;
    }

    /**
     * Gets the value of the scene property.
     * 
     * @return
     *     possible object is
     *     {@link COLLADA.Scene }
     *     
     */
    public COLLADA.Scene getScene() {
        return scene;
    }

    /**
     * Sets the value of the scene property.
     * 
     * @param value
     *     allowed object is
     *     {@link COLLADA.Scene }
     *     
     */
    public void setScene(COLLADA.Scene value) {
        this.scene = value;
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
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * 
     * 						The xml:base attribute allows you to define the base URI for this COLLADA document. See
     * 						http://www.w3.org/TR/xmlbase/ for more information.
     * 					
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBase() {
        return base;
    }

    /**
     * Sets the value of the base property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBase(String value) {
        this.base = value;
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
     *         &lt;element name="instance_physics_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="instance_visual_scene" type="{http://www.collada.org/2005/11/COLLADASchema}InstanceWithExtra" minOccurs="0"/>
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
        "instancePhysicsScene",
        "instanceVisualScene",
        "extra"
    })
    public static class Scene {

        @XmlElement(name = "instance_physics_scene", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<InstanceWithExtra> instancePhysicsScene;
        @XmlElement(name = "instance_visual_scene", namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected InstanceWithExtra instanceVisualScene;
        @XmlElement(namespace = "http://www.collada.org/2005/11/COLLADASchema")
        protected List<Extra> extra;

        /**
         * Gets the value of the instancePhysicsScene property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the instancePhysicsScene property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInstancePhysicsScene().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link InstanceWithExtra }
         * 
         * 
         */
        public List<InstanceWithExtra> getInstancePhysicsScene() {
            if (instancePhysicsScene == null) {
                instancePhysicsScene = new ArrayList<InstanceWithExtra>();
            }
            return this.instancePhysicsScene;
        }

        /**
         * Gets the value of the instanceVisualScene property.
         * 
         * @return
         *     possible object is
         *     {@link InstanceWithExtra }
         *     
         */
        public InstanceWithExtra getInstanceVisualScene() {
            return instanceVisualScene;
        }

        /**
         * Sets the value of the instanceVisualScene property.
         * 
         * @param value
         *     allowed object is
         *     {@link InstanceWithExtra }
         *     
         */
        public void setInstanceVisualScene(InstanceWithExtra value) {
            this.instanceVisualScene = value;
        }

        /**
         * 
         * 										The extra element may appear any number of times.
         * 									Gets the value of the extra property.
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

    }

}
