
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for glsl_pipeline_stage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="glsl_pipeline_stage">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VERTEXPROGRAM"/>
 *     &lt;enumeration value="FRAGMENTPROGRAM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "glsl_pipeline_stage", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlslPipelineStage {

    VERTEXPROGRAM,
    FRAGMENTPROGRAM;

    public String value() {
        return name();
    }

    public static GlslPipelineStage fromValue(String v) {
        return valueOf(v);
    }

}
