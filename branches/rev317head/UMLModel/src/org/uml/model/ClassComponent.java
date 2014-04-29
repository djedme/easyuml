package org.uml.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Random;

/**
 * Implementation of ClassComponent, one of four posible types of ClassDiagramComponents.
 * @author Uros
 * @see ClassDiagramComponent
 * @see EnumComponent
 * @see InterfaceComponent
 * @see PackageComponent
 */
public class ClassComponent extends ClassDiagramComponent {
    
    private HashMap<String,Constructor> constructors;
    private HashMap<String,Field> fields;
    private HashMap<String,Method> methods;
    private boolean isAbstract;
    // private Type type; // we dont know why this is here...

    /**
     * Default constructor.
     */
    public ClassComponent() {
        this.setName("UntitledClass");
        fields= new HashMap<String, Field>();
        methods= new HashMap<String, Method>();
        constructors = new HashMap<String, Constructor>();
        setVisibility(Visibility.PUBLIC);
        isAbstract=false;
        parentPackage = null;
    }

    /**
     * Constructor with parameters which calls it's super constructor.
     * @param name 
     */
    public ClassComponent(String name) {
        super(name);
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    public HashMap<String, Constructor> getConstructors() {
        return constructors;
    }

    public Field getField (String name) {
        return fields.get(name);
    }
    
    /**
     * Adds field to ClassComponent's collection of fields.
     * @param field that will be added to collection.
     */
    public void addField (Field field) {
        field.setDeclaringClass(this);
        Random r = new Random();
        int Low = 0;
        int High = 100;
        int R = r.nextInt(High-Low) + Low;
        fields.put(Integer.toString(R), field);
        addMember(field);
    } 
    
    public void removeField (Field field) {
        fields.values().remove(field);
    }
    
     public Method getMethod(String name) {
        return methods.get(name);
    }
     
    /**
     * Adds method to ClassComponent's collection of methods.
     * @param method that will be added to collection.
     */
    public void addMethod(Method method) {
        method.setDeclaringClass(this);
        Random r = new Random();
        int Low = 0;
        int High = 100;
        int R = r.nextInt(High-Low) + Low;
        methods.put(Integer.toString(R), method);
        addMember(method);
    }
    
    /**
     * Removes method from ClassComponent's collection of methods.
     * @param method of the method which will be removed.
     */
    public void removeMethod(Method method) {
        methods.values().remove(method);
    }

    public Constructor getConstuctor(String name) {
        return constructors.get(name);
    }    
    
    /**
     * Adds Constructor to ClassComponent's collection of constructors.
     * @param constructor which will be added to collection.
     */
    public void addConstructor(Constructor constructor) {
        constructor.setDeclaringClass(this);
        constructors.put(this.getName(), constructor);
        addMember(constructor);
    }
    
    /**
     * Removes Constructor from a ClassComponent's collection of constructors.
     * @param constructor of constructor which will be removed.
     */
    public void removeConstructor(Constructor constructor) {
        methods.values().remove(constructor);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

//    public Type getType() {
//        return type;
//    }
//
//    public void setType(Type type) {
//        this.type = type;
//    }
    
}
