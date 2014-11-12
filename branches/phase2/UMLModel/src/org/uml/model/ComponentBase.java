package org.uml.model;

import org.uml.model.members.Member;
import org.uml.model.members.Visibility;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.Method;
import org.uml.model.relations.RelationComponent;

/**
 * Base class for all UML class diagram components (classes, interfaces or
 * enums) that can be added to class diagram. Note that relations are not
 * represented with this class as well; they are represented with
 * RelationComponent.
 *
 * @author zoran
 * @see ClassComponent
 * @see RelationComponent
 */
public abstract class ComponentBase implements Serializable {

    private Point position; // this should be removed in future
    private Rectangle bounds;

    private String name;
    private HashMap<String, Member> members; // index of all fields, methods and constructors
    private Visibility visibility;
    private ClassDiagram parentDiagram;
    private PackageComponent parentPackage;

    /**
     * Default constructor. Initializes members of the ComponentBase. Members
     * can be fields, methods, constructors and literals.
     *
     * @param name of component
     * @see Field
     * @see Method
     * @see Constructor
     * @see Literal
     */
    public ComponentBase(String name) {
        this.name = name;
        members = new HashMap<>();
        visibility = Visibility.PUBLIC;
    }

    /**
     * Adds a member of ClassDiagramComponent to the members collection. Similar
     * implementation as in ClassDiagram for addComponent and addRelation.
     *
     * @param member to be added to members collection
     * @see ClassDiagram#addComponent(org.uml.model.ClassDiagramComponent)
     * @see ClassDiagram#addRelation(org.uml.model.ClassDiagramComponent)
     * @see Member
     */
    public void addMember(Member member) {
        // TODO throw or inform?
        if (nameExists(member.getName())) {
            //member.setName(member.getName() + memberCounter);
            //JOptionPane.showMessageDialog(null, "You have entered name that already exists, please enter new one.");
            throw new RuntimeException("Error while entering member name: name already exists.");
        }
        members.put(member.getName(), member);
    }

    /**
     * Removes the member with the given name from this ClassDiagramComponent's
     * collection of components.
     *
     * @param name of component that will be removed
     */
    public void removeMember(String name) {
        members.remove(name);
    }

    /**
     * Checks if a member of ClassDiagramComponent already exists in the
     * collection of components (members).
     *
     * @param name of ClassDiagramComponent that is to be checked
     * @return true if already exists, false if it doesn't
     */
    public boolean nameExists(String name) {
        return members.containsKey(name);
    }

    /**
     * Removes a member from ClassDiagramComponent's collection of members and
     * replaces it with the same member (but with new name).
     *
     * @param member that will be renamed
     * @param oldName old name of that component
     * @see Member
     */
    public void notifyMemberNameChanged(Member member, String oldName) {
        members.remove(oldName);
        addMember(member);
    }

    /**
     * Returns members that this ClassDiagramComponent has
     *
     * @return all members of this ClassDiagramComponent
     */
    public HashMap<String, Member> getMembers() {
        return members;
    }

    /**
     * Returns the name of this ClassDiagramComponent.
     *
     * @return name of this ClassDiagramComponent
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of ClassDiagramComponent.
     *
     * @param name to be set to ClassDiagramComponent
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns parentDiagram of this ClassDiagramComponent. Parent diagram is a
     * ClassDiagram object that contains this ClassDiagramComponent.
     *
     * @return ClassDiagram containing this ClassDiagramComponent
     * @see ClassDiagram
     */
    public ClassDiagram getParentDiagram() {
        return parentDiagram;
    }

    /**
     * Sets parentDiagram of this ClassDiagramComponent. Parent diagram is a
     * ClassDiagram object that contains this ClassDiagramComponent.
     *
     * @param parentDiagram
     * @see ClassDiagram
     */
    public void setParentDiagram(ClassDiagram parentDiagram) {
        this.parentDiagram = parentDiagram;
    }

    /**
     * Returns position that this ClassDiagramComponent has on the scene
     *
     * @return point that represents X and Y coordinates
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets this ClassDiagramComponent's position on scene
     *
     * @param position that the component has
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Returns package that contains this ClassDiagramComponent
     *
     * @return PackageComponent of this ClassDiagramComponent
     * @see PackageComponent
     */
    public PackageComponent getParentPackage() {
        return parentPackage;
    }

    /**
     * Sets the package that contains this ClassDiagramComponent
     *
     * @param parentPackage
     */
    public void setParentPackage(PackageComponent parentPackage) {
        this.parentPackage = parentPackage;
        //   parentPackage.addMember(this);
    }

    /**
     * Returns the visibility modifier of this ClassDiagramComponent
     *
     * @return visibility of this ClassDiagramComponent
     * @see Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility modifier of this ClassDiagramComponent
     *
     * @param visibility to be set
     * @see Visibility
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
    
}
