
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gles_texcombiner_source_enums.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gles_texcombiner_source_enums">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="TEXTURE"/>
 *     &lt;enumeration value="CONSTANT"/>
 *     &lt;enumeration value="PRIMARY"/>
 *     &lt;enumeration value="PREVIOUS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gles_texcombiner_source_enums", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlesTexcombinerSourceEnums {

    TEXTURE,
    CONSTANT,
    PRIMARY,
    PREVIOUS;

    public String value() {
        return name();
    }

    public static GlesTexcombinerSourceEnums fromValue(String v) {
        return valueOf(v);
    }

}
