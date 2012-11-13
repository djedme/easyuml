/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;
import org.uml.model.UmlClassDiagram;
import org.uml.model.UmlClassElement;

/**
 *
 * @author NUGS
 */
public class ClassDiagramContainerWidget extends Widget {

    private UmlClassDiagram umlClassDiagram;

    public ClassDiagramContainerWidget(UmlClassDiagram umlClassDiagram, ClassDiagramScene scene) {
        super(scene);
        this.umlClassDiagram = umlClassDiagram;
        setPreferredSize(new Dimension(400, 400));
        addChild(new LabelWidget(getScene(), "Class Diagram 1"));
        //setBorder(BorderFactory.createLineBorder(4, Color.black)); 
        setBorder(BorderFactory.createResizeBorder(2, Color.black, false));
        getActions().addAction(ActionFactory.createResizeAction());
        getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        // getActions().addAction(ActionFactory.createMoveAction());
        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
                return ConnectorState.ACCEPT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable t) {
                Class<? extends UmlClassElement> droppedClass = (Class<? extends UmlClassElement>) t.getTransferDataFlavors()[2].getRepresentationClass(); // Jako ruzno! Osmisliti kako da izvlacimo iz DataFlavor-a bez gadjanja indeksa!
                try {
                    addChild(new ClassWidget((ClassDiagramScene) getScene(), (UmlClassElement) droppedClass.newInstance()));
                } catch (InstantiationException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }));
    }

    public UmlClassDiagram getUmlClassDiagram() {
        return umlClassDiagram;
    }
}
