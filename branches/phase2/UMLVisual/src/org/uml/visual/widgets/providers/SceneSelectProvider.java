/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author stefanpetrovic
 */
public class SceneSelectProvider implements SelectProvider{

    private ClassDiagramScene scene;

    public SceneSelectProvider(ClassDiagramScene scene) {
        this.scene = scene;
    }
    
    
    
    @Override
    public boolean isAimingAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public boolean isSelectionAllowed(Widget widget, Point point, boolean bln) {
        return true;
    }

    @Override
    public void select(Widget widget, Point point, boolean bln) {
        if (widget instanceof ClassDiagramScene) {
            for (Widget w : scene.getMainLayer().getChildren()) {
                ComponentWidgetBase comp = (ComponentWidgetBase) w;
                scene.getContent().remove(comp.getComponent());
                comp.notifyStateChanged(comp.getState(), comp.getState().deriveSelected(false).deriveWidgetAimed(true));
            }
            scene.validate();
        }        
    }
    
}
