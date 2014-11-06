package org.uml.explorer;

import java.awt.Image;
import java.io.IOException;
import javax.swing.Action;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;

/**
 *
 * @author Jelena
 */
public class ClassDiagramComponentNode extends AbstractNode {

//    https://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
//    http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/PropertySupport.html
    private ClassDiagramComponent component;

    public ClassDiagramComponentNode(ClassDiagramComponent component) {
        this(component, new InstanceContent());
    }

    private ClassDiagramComponentNode(ClassDiagramComponent component, InstanceContent content) {
        super(Children.create(new ClassDiagramComponentChildrenFactory(component), true), new AbstractLookup(content));
        content.add(this);

        this.component = component;
        setName(component.getName());
        setDisplayName(component.getName());
    }
    
    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{(SystemAction.get(DeleteAction.class))};
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        component.getParentDiagram().removeComponent(component.getName());
        fireNodeDestroyed();
    }

    @Override
    public Image getIcon(int type) {
        if (component instanceof ClassComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/classIcon.png");
        }
        if (component instanceof InterfaceComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/interfaceIcon.png");
        }
        if (component instanceof EnumComponent) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/enumIcon.png");
        }
        return super.getIcon(type);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }

    public ClassDiagramComponent getComponent() {
        return component;
    }
}


//    @Override
//    protected Sheet createSheet() {
//        Sheet sheet = super.createSheet();
//        Sheet.Set propertiesSet = Sheet.createPropertiesSet();
//        propertiesSet.setName("propertiesSet");
//        propertiesSet.setDisplayName("Properties");
//
//        try {
//            Property<String> nameProp = new PropertySupport.Reflection<>(classDiagramComponent, String.class, "getName", null);
//            nameProp.setName("Name");
//            propertiesSet.put(nameProp);
//
//            if (classDiagramComponent instanceof ClassComponent) {
//                Visibility vis = classDiagramComponent.getVisibility();
//                Property<String> visibilityProp = new PropertySupport.Reflection<>(vis, String.class, "toString", null);
//                visibilityProp.setName("Visibility");
//                propertiesSet.put(visibilityProp);
//            }
//
//            PackageComponent pack = classDiagramComponent.getParentPackage();
//            Property<String> packageProp = new PropertySupport.Reflection<>(pack, String.class, "getName", null);
//            packageProp.setName("Package");
//            propertiesSet.put(packageProp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        sheet.put(propertiesSet);
//        return sheet;
//
////        //STARA METODA KOJA JE POTPUNO FUNKCIONALNA I KOJA U PROPERTIES WINDOW POSTAVLJA FIELDS, METHODS, LITERALS...
////        Sheet sheet = super.createSheet();
////        Sheet.Set propertiesSet = Sheet.createPropertiesSet();
////        Sheet.Set fieldsSet = Sheet.createPropertiesSet();
////        Sheet.Set methodsSet = Sheet.createPropertiesSet();
////        Sheet.Set literalsSet = Sheet.createPropertiesSet();
////
////        propertiesSet.setName("propertiesSet");
////        propertiesSet.setDisplayName("Properties");
////        fieldsSet.setName("fields");
////        fieldsSet.setDisplayName("Fields");
////        methodsSet.setName("methods");
////        methodsSet.setDisplayName("Methods");
////        literalsSet.setName("literals");
////        literalsSet.setDisplayName("Literals");
////
////
////        try {
////
////            Property nameProp = new PropertySupport.Reflection(classDiagramComponent, String.class, "getName", null);
////            nameProp.setName("Name");
////            propertiesSet.put(nameProp);
////
////
////            
////            if (classDiagramComponent instanceof ClassComponent) {
////                ClassComponent component = (ClassComponent) classDiagramComponent;
////
////
////                HashMap<String, Field> fields = component.getFields();
////                for (Map.Entry<String, Field> entry : fields.entrySet()) {
////                    Field field = entry.getValue();
////                    Property fieldProp = new PropertySupport.Reflection(field, String.class, "getName", null);
////                    String fieldPropName = field.getType();
////                    fieldProp.setName(fieldPropName);
////                    fieldsSet.put(fieldProp);
////                }
////
////
////                HashMap<String, Method> methods = component.getMethods();
////                for (Map.Entry<String, Method> entry : methods.entrySet()) {
////                    Method method = entry.getValue();
////                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
////                    String methodPropName = method.getSignatureForLabel();
////                    methodProp.setName(methodPropName);
////                    methodsSet.put(methodProp);
////                }
////            } else if (classDiagramComponent instanceof InterfaceComponent) {
////                InterfaceComponent component = (InterfaceComponent) classDiagramComponent;
////                HashMap<String, Method> methods = component.getMethods();
////                for (Map.Entry<String, Method> entry : methods.entrySet()) {
////                    Method method = entry.getValue();
////                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
////                    String methodPropName = method.getSignatureForLabel();
////                    methodProp.setName(methodPropName);
////                    methodsSet.put(methodProp);
////                }
////            } else if (classDiagramComponent instanceof EnumComponent) {
////                EnumComponent component = (EnumComponent) classDiagramComponent;
////
////                HashMap<String, Field> fields = component.getFields();
////                for (Map.Entry<String, Field> entry : fields.entrySet()) {
////                    Field field = entry.getValue();
////                    Property fieldProp = new PropertySupport.Reflection(field, String.class, "getName", null);
////                    String fieldPropName = field.getType();
////                    fieldProp.setName(fieldPropName);
////                    fieldsSet.put(fieldProp);
////                }
////
////                HashMap<String, Method> methods = component.getMethods();
////                for (Map.Entry<String, Method> entry : methods.entrySet()) {
////                    Method method = entry.getValue();
////                    Property methodProp = new PropertySupport.Reflection(method, String.class, "getName", null);
////                    String methodPropName = method.getSignatureForLabel();
////                    methodProp.setName(methodPropName);
////                    methodsSet.put(methodProp);
////                }
////
////                HashMap<String, Literal> literals = component.getLiterals();
////                for (Map.Entry<String, Literal> entry : literals.entrySet()) {
////                    Literal literal = entry.getValue();
////                    Property literalProp = new PropertySupport.Reflection(literal, String.class, "getName", null);
////                    literalProp.setName("Literal");
////                    literalsSet.put(literalProp);
////                }
////
////            }
////
////            sheet.put(propertiesSet);
////            sheet.put(fieldsSet);
////            sheet.put(methodsSet);
////            sheet.put(literalsSet);
////
////
//////            
//////            Property labelProp = new PropertySupport.Reflection(classDiagramComponent, String.class, "getLabel", null);
//////            labelProp.setName("Label");
//////            set.put(labelProp);
////
////        } catch (Exception ex) {
////            System.out.println(ex.getMessage());
////        }
////
////
////        return sheet;
//    }





//staro
//        try {
//            Node.Property label = new PropertySupport.Reflection(classDiagramComponent, String.class, "getName", null);
//            Node.Property inputSize = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "getInputSize", null);
//            Node.Property outputSize = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "getOutputSize", null);
//            Node.Property size = new PropertySupport.Reflection(classDiagramComponent, Integer.class, "size", null);
//label.setName("Label");
//            inputSize.setName("Input size");
//            outputSize.setName("Output size");
//            size.setName("Number of elements");
//set.put(label);
//            set.put(inputSize);
//            set.put(outputSize);
//            set.put(size);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
