/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 * This should also be a member as in reflection api.
 * put all this stuff in subclasses
 * @author zoran
 */
public class Member { 
    private String name;
    private int modifier;
    private ClassDiagramComponent declaringClass;

    public Member(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
    
    public ClassDiagramComponent getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(ClassDiagramComponent declaringClass) {
        this.declaringClass = declaringClass;
    }
    
    
    
}
