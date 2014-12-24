package org.uml.model.members;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Modifier;
import org.uml.model.IHasSignature;
import org.uml.model.INameable;
import org.uml.model.Visibility;
import org.uml.model.components.ComponentBase;

/**
 * Represents a class member.
 *
 * @author zoran
 * @see Literal
 * @see Field
 * @see Method
 * @see Constructor
 * @see ComponentBase
 *
 */
public abstract class MemberBase implements INameable, IHasSignature {

    protected String name;
    // sta ako je niz? da li treba koristiti Type?
    protected String type;

    protected Visibility visibility;
    /**
     * Modifier is a int value representing access and non-access modifier in
     * Java e.g. public is represented as 0x00000001, static as 0x00000008.
     * 
     * @see java.lang.reflect.Modifier
     */
    protected int modifiers;
    private transient ComponentBase declaringComponent;
    protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /**
     * Default constructor. Only sets the name of a Member.
     *
     * @param name of a member
     */
    protected MemberBase(String name) {
        this.name = name;
    }
    
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String newName) {
        String oldName = getName();
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);
    }

    public String getType() {
        return type;
    }

    public void setType(String newType) {
        String oldType = this.type;
        this.type = newType;
        pcs.firePropertyChange("type", oldType, newType);
    }
    
    /**
     * Adds numerical representation of java Modifier enum's constants into
     * modifiers array.
     * <p>
     * Modifiers array can hold up to four modifier constants. Modifiers are
     * thoroughly explained in Member class.
     *
     * @param modifier to be added to modifiers array
     * @see java.lang.reflect.Modifier
     * @see MemberBase
     */
    public void addModifier(int modifier) //throws Exception 
    {
        if ((this instanceof Field && (Modifier.fieldModifiers() & modifier) != 0)
                || (this instanceof Method && (Modifier.methodModifiers() & modifier) != 0)
                || (this instanceof Constructor && (Modifier.constructorModifiers()& modifier) != 0)) {
            modifiers |= modifier;
        } else {
            //throw new Exception("Modifier " + Modifier.toString(modifier) + " not allowed for " + this.getClass().getSimpleName() + ".");
        }
    }

    /**
     * Removes given modifier integer from modifiers array.
     * <p>
     * Modifiers are thoroughly explained in Member class.
     *
     * @param modifier to be removed
     * @see Modifier
     */
    public void removeModifier(int modifier) {
        modifiers &= ~modifier;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        Visibility oldValue = this.visibility;
        this.visibility = visibility;
        pcs.firePropertyChange("visibility", oldValue, this.visibility);
    }

    public ComponentBase getDeclaringComponent() {
        return declaringComponent;
    }

    public void setDeclaringComponent(ComponentBase declaringComponent) {
        this.declaringComponent = declaringComponent;
    }
    
    @Override
    // signature without modifiers
    public abstract String getSignature();
    public abstract String getLabelText();
    
    public abstract String deriveNewSignatureFromName(String newName);
    public abstract String deriveNewSignatureFromType(String newType);
    
//    @Override
//    public String toString(){
//        return getSignature();
//    }
}
