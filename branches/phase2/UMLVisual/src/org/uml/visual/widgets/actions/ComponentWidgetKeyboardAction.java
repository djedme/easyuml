package org.uml.visual.widgets.actions;

import java.awt.Point;
import java.awt.event.KeyEvent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.components.ComponentWidgetBase;

/**
 *
 * @author Boris Perović
 */
// TODO not detecting key events
public class ComponentWidgetKeyboardAction extends WidgetAction.Adapter {

    private final MoveProvider provider;

    public ComponentWidgetKeyboardAction() {
        this.provider = ActionFactory.createDefaultMoveProvider();
    }

    @Override
    public State keyTyped(Widget widget, WidgetKeyEvent event) {
        return super.keyTyped(widget, event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public State mouseWheelMoved(Widget widget, WidgetMouseWheelEvent event) {
        return super.mouseWheelMoved(widget, event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WidgetAction.State keyPressed(Widget widget, WidgetAction.WidgetKeyEvent event) {
        Widget focused = widget.getScene().getFocusedWidget();
        if (focused.equals(widget)) {
            Point originalSceneLocation = provider.getOriginalLocation(widget);
            int newY = originalSceneLocation.y;
            int newX = originalSceneLocation.x;
            if (event.getKeyCode() == KeyEvent.VK_UP) {
                newY = newY - 20;
            } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                newY = newY + 20;
            } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                newX = newX + 20;
            } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                newX = newX - 20;
            } else if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                ClassDiagram classDiagram = ((ComponentWidgetBase) widget).getComponent().getParentDiagram();
                classDiagram.removeComponentFromContainer(((ComponentWidgetBase) widget).getComponent());
            }
            provider.movementStarted(widget);
            provider.setNewLocation(widget, new Point(newX, newY));
        }
        return WidgetAction.State.CONSUMED;
    }

    @Override
    public WidgetAction.State keyReleased(Widget widget, WidgetAction.WidgetKeyEvent event) {
        provider.movementFinished(widget);
        return WidgetAction.State.REJECTED;
    }
}
