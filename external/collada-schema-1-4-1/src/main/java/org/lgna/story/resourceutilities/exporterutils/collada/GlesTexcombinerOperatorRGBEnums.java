
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gles_texcombiner_operatorRGB_enums.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gles_texcombiner_operatorRGB_enums">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="REPLACE"/>
 *     &lt;enumeration value="MODULATE"/>
 *     &lt;enumeration value="ADD"/>
 *     &lt;enumeration value="ADD_SIGNED"/>
 *     &lt;enumeration value="INTERPOLATE"/>
 *     &lt;enumeration value="SUBTRACT"/>
 *     &lt;enumeration value="DOT3_RGB"/>
 *     &lt;enumeration value="DOT3_RGBA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gles_texcombiner_operatorRGB_enums", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlesTexcombinerOperatorRGBEnums {

    REPLACE("REPLACE"),
    MODULATE("MODULATE"),
    ADD("ADD"),
    ADD_SIGNED("ADD_SIGNED"),
    INTERPOLATE("INTERPOLATE"),
    SUBTRACT("SUBTRACT"),
    @XmlEnumValue("DOT3_RGB")
    DOT_3_RGB("DOT3_RGB"),
    @XmlEnumValue("DOT3_RGBA")
    DOT_3_RGBA("DOT3_RGBA");
    private final String value;

    GlesTexcombinerOperatorRGBEnums(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GlesTexcombinerOperatorRGBEnums fromValue(String v) {
        for (GlesTexcombinerOperatorRGBEnums c: GlesTexcombinerOperatorRGBEnums.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
