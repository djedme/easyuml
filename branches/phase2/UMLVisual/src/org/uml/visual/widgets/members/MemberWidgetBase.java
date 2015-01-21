package org.uml.visual.widgets.members;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.MemberBase;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends LabelWidget implements PropertyChangeListener {

    protected MemberBase component;
    protected LabelWidget visibilityLabel = new LabelWidget(getScene());
    protected LabelWidget nameLabel = new LabelWidget(getScene());

    /* Visual */
    protected static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(1);
    protected static final Border HOVER_BORDER = BorderFactory.createLineBorder(1, Color.GRAY);
    protected static final Border SELECT_BORDER = BorderFactory.createLineBorder(1, new Color(0x0000A1)); //0x0096FF33

    protected static final Color DEFAULT_COLOR = new Color(0, 0, 0, 1);
    protected static final Color HOVER_COLOR = new Color(0xD4DCFF);
    protected static final Color SELECT_COLOR = new Color(0x00, 0x00, 0xA1, 0x4D);

    protected static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    protected static final Color SELECT_FONT_COLOR = new Color(0xFFFFFF);
    /* End Visual */

    public MemberWidgetBase(ClassDiagramScene scene, MemberBase member) {
        super(scene);
        this.component = member;
        scene.addObject(member, this);
        setOpaque(true);
        setBackground(DEFAULT_COLOR);
        setBorder(DEFAULT_BORDER);

        // To support hovering and selecting (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createWidgetHoverAction());
        getActions().addAction(scene.createSelectAction());
    }

    public final MemberBase getMember() {
        return component;
    }

    public final ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    // used for InplaceEditorAction
    public final LabelWidget getNameLabel() {
        return nameLabel;
    }

    protected void notifyTopComponentModified() {
        getClassDiagramScene().getUmlTopComponent().modify();
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // Reaction to hover, focus and selection goes here
        super.notifyStateChanged(previousState, state);

        // in case it has not yet been initialized return (adding to the scene calls notifyStateChanged, before the full object has been initialized)
        if (getParentWidget() == null) return;

        Widget componentWidget = getParentWidget().getParentWidget();

        boolean focused = state.isFocused();
        boolean hovered = state.isHovered();

        if (focused) {
            setSelectedLook(true);
            setBorder(SELECT_BORDER);
            setBackground(SELECT_COLOR);
            if (hovered) componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
        } else if (hovered) {
            setSelectedLook(false);
            setBorder(HOVER_BORDER);
            setBackground(HOVER_COLOR);
            componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
        } else {
            setSelectedLook(false);
            setBorder(DEFAULT_BORDER);
            setBackground(DEFAULT_COLOR);
            componentWidget.setState(componentWidget.getState().deriveWidgetHovered(false));
        }
    }

    public final void updateVisibilityLabel() {
        switch (component.getVisibility()) {
            case PUBLIC:
                visibilityLabel.setLabel("+");
                break;
            case PRIVATE:
                visibilityLabel.setLabel("-");
                break;
            case PROTECTED:
                visibilityLabel.setLabel("#");
                break;
            case PACKAGE:
                visibilityLabel.setLabel("~");
                break;
        }
    }

    // used for setting the font etc.
    protected void setSelectedLook(boolean isSelected) {
        if (isSelected) {
            visibilityLabel.setForeground(SELECT_FONT_COLOR);
            nameLabel.setForeground(SELECT_FONT_COLOR);
        } else {
            visibilityLabel.setForeground(DEFAULT_FONT_COLOR);
            nameLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }
}
