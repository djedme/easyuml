/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.providers;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.RelationComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;

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
        return ((sourceWidget instanceof ClassWidget)&&(sourceWidget!=null));
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
    return (targetWidget instanceof ClassWidget) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
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
        if ((sourceWidget instanceof ClassWidget) && (targetWidget instanceof ClassWidget)) {
            RelationComponent relation= new RelationComponent();
            ClassDiagramScene scene= (ClassDiagramScene)sourceWidget.getScene();
            ClassWidget source= (ClassWidget) sourceWidget;
            ClassWidget target= (ClassWidget) targetWidget;
            scene.addEdge(relation);
            scene.setEdgeSource(relation,source.getClassComponent());
            scene.setEdgeTarget(relation, target.getClassComponent());
        }
    }
    
}
