/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import org.dom4j.Element;

/**
 *
 * @author Stefan
 */
public interface XmlDeserializer {
    
    public void deserialize(Element node);
}
