package org.uml.visual.widgets.actions;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.ImageUtilities;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author Uros
 */
public class SceneAcceptProvider implements AcceptProvider {

    private ClassDiagramScene classDiagramScene;

    public SceneAcceptProvider(ClassDiagramScene classDiagramScene) {

        this.classDiagramScene = classDiagramScene;
    }

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        Image dragImage = getImageFromTransferable(t);
        JComponent view = classDiagramScene.getView();
        Graphics2D g2 = (Graphics2D) view.getGraphics();
        Rectangle visRect = view.getVisibleRect();
        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
        g2.drawImage(dragImage,
                AffineTransform.getTranslateInstance(point.getLocation().getX(),
                point.getLocation().getY()),
                null);
        DataFlavor flavor = t.getTransferDataFlavors()[2];
        Class droppedClass = flavor.getRepresentationClass();
        return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
        Class<? extends ClassDiagramComponent> droppedClass = (Class<? extends ClassDiagramComponent>) t.getTransferDataFlavors()[2].getRepresentationClass(); // Jako ruzno! Osmisliti kako da izvlacimo iz DataFlavor-a bez gadjanja indeksa!
        //Class<? extends IconNodeWidget> droppedWidget = (Class<? extends IconNodeWidget>) t.getTransferDataFlavors()[3].getRepresentationClass();
        try {
           //IconNodeWidget newInstance = droppedWidget.getConstructor(ClassDiagramScene.class, droppedClass).newInstance(classDiagramScene, droppedClass.newInstance());          
            Widget w=classDiagramScene.addNode(droppedClass.newInstance());
            w.setPreferredLocation(widget.convertLocalToScene(point));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
        }
        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
    }

    public boolean canAccept(Class droppedClass) {

        return droppedClass.equals(ClassDiagramComponent.class)
                || droppedClass.getSuperclass().equals(ClassDiagramComponent.class);
    }
}
