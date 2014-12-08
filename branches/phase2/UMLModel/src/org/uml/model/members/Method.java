package org.uml.model.members;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.uml.model.Visibility;

/**
 *
 * @author Boris
 */
public class Method extends MethodBase {

    public Method(String name) {
        super(name);
        arguments = new HashMap<>();
    }

    public Method(String name, String returnType, HashMap<String, MethodArgument> arguments) {
        super(name, returnType, arguments);
    }

    /**
     * Creates a string that represents Method's signature.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "public static String firstMethod(int theNumber)". THIS IS VERSION FOR
     * INTERFACES
     *
     * @return specially formed Method's String representation
     */
    public String getSignatureForInterfaces() {
        StringBuffer result = new StringBuffer();
        if (visibility != null && !Visibility.PACKAGE.equals(visibility)) {
            result = result.append(getVisibility().toString()).append(" ");
        }
        result.append(getSignatureForLabel());
        result.append(";\n");
        return result.toString();
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }

    public void setAbstract(boolean isAbstract) {
        int oldModifiers = modifiers;
        if (isAbstract) {
            modifiers |= Modifier.ABSTRACT;
        } else {
            modifiers &= ~Modifier.ABSTRACT;
        }
        fire("isAbstract", Modifier.isAbstract(oldModifiers), isAbstract());
    }

    /**
     * Returns true if the field has static modifier, false if it is not static
     *
     * @return true if field is static, false if it isn't
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    /**
     * Set this field's static modifier to true or false
     *
     * @param isStatic - set true if the field is static, false if it isn't
     */
    public void setStatic(boolean isStatic) {
        int oldModifiers = modifiers;
        if (isStatic) {
            modifiers |= Modifier.STATIC;
        } else {
            modifiers &= ~Modifier.STATIC;
        }
        fire("isStatic", Modifier.isStatic(oldModifiers), isStatic());
    }

    /**
     * Returns true if the field has final modifier, false if it is not final
     *
     * @return true if field is final, false if it isn't
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    /**
     * Set this field's final modifier to true or false
     *
     * @param isFinal - set true if the field is final, false if it isn't
     */
    public void setFinal(boolean isFinal) {
        int oldModifiers = modifiers;
        if (isFinal) {
            modifiers |= Modifier.FINAL;
        } else {
            modifiers &= ~Modifier.FINAL;
        }
        fire("isFinal", Modifier.isFinal(oldModifiers), isFinal());
    }

    /**
     * Returns true if the field has synchronized modifier, false if it is not
     * synchronized
     *
     * @return true if field is synchronized, false if it isn't
     */
    public boolean isSynchronized() {
        return Modifier.isSynchronized(modifiers);
    }

    /**
     * Set this field's final synchronized to true or false
     *
     * @param isSynchronized - set true if the field is synchronized, false if
     * it isn't
     */
    public void setSynchronized(boolean isSynchronized) {
        int oldModifiers = modifiers;
        if (isSynchronized) {
            modifiers |= Modifier.SYNCHRONIZED;
        } else {
            modifiers &= ~Modifier.SYNCHRONIZED;
        }
        fire("isSynchronized", Modifier.isSynchronized(oldModifiers), isSynchronized());
    }

}
