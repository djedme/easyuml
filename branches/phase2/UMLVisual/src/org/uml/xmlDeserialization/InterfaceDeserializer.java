package org.uml.xmlDeserialization;

import java.awt.Point;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.Visibility;
import org.uml.model.members.Method;

/**
 *
 * @author stefanpetrovic
 */
public class InterfaceDeserializer implements XmlDeserializer{

    private InterfaceComponent interfaceComponent;
    
    public InterfaceDeserializer(InterfaceComponent component) {
        interfaceComponent = component;
    }
    
    /**
     * Fills in attributes of interfaceComponent object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the interfaceComponent.
     */
    @Override
    public void deserialize(Element node) {
        String packageName = node.attributeValue("package");
        String interfaceName = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        Point position = new Point((int) Double.parseDouble(node.attributeValue("xPosition")), (int) Double.parseDouble(node.attributeValue("yPosition")));
        if (packageName != null) {
            interfaceComponent.setParentPackage(packageName);
        }
        if (interfaceName != null && !interfaceName.equals("")) {
            interfaceComponent.setName(interfaceName);
        }
        if (visibility != null && !visibility.equals("")) {
            interfaceComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        }
        interfaceComponent.setLocation(position);
        
        Iterator<?> methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && methodIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method(null, null);
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            interfaceComponent.addMethod(method);
        }
        
    }
    
}
