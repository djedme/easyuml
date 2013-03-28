/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.RelationComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.InterfaceWidget;
import org.uml.visual.widgets.ComponentWidgetBase;

/**
 *
 * @author "NUGS"
 */
public class ClassConnectProvider implements ConnectProvider{
    
    //private ClassDiagramComponent source=null;
    //private ClassDiagramComponent target=null;
    //ClassDiagramScene scene;
    public ClassConnectProvider() {

    }

    @Override
    public boolean isSourceWidget(Widget sourceWidget) {
        return (((sourceWidget instanceof ComponentWidgetBase)&&(sourceWidget!=null)));
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {        
    return (targetWidget instanceof ComponentWidgetBase) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point point) {
        return null;
    }

    @Override
    public void createConnection(Widget sourceWidget, Widget targetWidget) {
       //connect class to class
        RelationComponent relation= new RelationComponent();        
        if((sourceWidget instanceof ComponentWidgetBase)&& (targetWidget instanceof ComponentWidgetBase)) {
            ClassDiagramScene scene= (ClassDiagramScene)sourceWidget.getScene();
            ComponentWidgetBase source= (ComponentWidgetBase) sourceWidget;
            ComponentWidgetBase target= (ComponentWidgetBase) targetWidget;
            scene.addEdge(relation);
            scene.setEdgeSource(relation,source.getComponent());
            scene.setEdgeTarget(relation,target.getComponent());
        }
    }
    
}
