/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

/**
 *
 * @author NUGS
 */
public class PaletteItem {
    
    String icon;
    String title;
    String category;
    Class dropClass;

    public PaletteItem() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Class getDropClass() {
        return dropClass;
    }

    public void setDropClass(Class dropClass) {
        this.dropClass = dropClass;
    }
    
    
    
}
