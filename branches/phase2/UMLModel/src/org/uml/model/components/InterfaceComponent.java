package org.uml.model.components;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;

/**
 * Interfaces from UML Class diagrams. Used to represent Interface classes in
 * UML diagrams.
 *
 * @author zoran
 * @see ClassComponent
 * @see EnumComponent
 */
public class InterfaceComponent extends ComponentBase {

    /**
     * Set of method this class contains.
     */
    private LinkedHashSet<Method> methods;

    /**
     * Default constructor. Sets name to default value.
     * <p>
     * Used when deserializing or adding a new component from palette or popup menu.
     */
    public InterfaceComponent() {
        this("UntitledInterface");
    }

    /**
     * Constructor with parameters which calls its super constructor.
     * Sets the name of the interface.
     * Instantiates methods collections.
     *
     * @param name to be set
     */
    // Used when reverse engineering
    public InterfaceComponent(String name) {
        super(name);
        methods = new LinkedHashSet<>();
    }

    /**
     * Returns the collection of methods that this class contains.
     *
     * @return HashSet of methods contained
     */
    public LinkedHashSet<Method> getMethods() {
        return methods;
    }

    /**
     * Adds the given methode to the class.
     * Sets the declaring component of the method to be this class.
     *
     * @param method to add
     */
    public void addMethod(Method method) {
        addComponentToContainter(method);
        method.setDeclaringComponent(this);
        methods.add(method);
    }

    /**
     * Returns true if static modifier bit is set, false if not.
     *
     * @return true if interface is static
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    /**
     * Sets the static modifier to true or false. Fires "isStatic" property change event.
     *
     * @param isStatic true if the interface is static, false if not
     */
    public void setStatic(boolean isStatic) {
        int oldModifiers = modifiers;
        if (isStatic) {
            addModifier(Modifier.STATIC);
        } else {
            removeModifier(Modifier.STATIC);
        }
        pcs.firePropertyChange("isStatic", Modifier.isStatic(oldModifiers), isStatic());
    }

    /**
     * Removes the member from the associated collection.
     *
     * @param member to be removed
     */
    @Override
    public void removeMember(MemberBase member) {
        removeComponentFromContainer(member);
        if (member instanceof Method) methods.remove((Method) member);
//        else throw new RuntimeException("Removing unsupported member: " + member.toString());
    }
}
