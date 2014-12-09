package org.uml.visual.widgets.components;

import org.uml.visual.widgets.members.ConstructorWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.members.FieldWidget;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.Visibility;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.popups.ClassPopupMenuProvider;
import org.uml.visual.widgets.providers.ComponentWidgetAcceptProvider;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends ComponentWidgetBase {

    private Widget headerWidget;
    private final Widget fieldsContainer;
    private final Widget methodsContainer;

    public ClassWidget(ClassDiagramScene scene, ClassComponent classComponent) {
        super(scene, classComponent);

        // Header
        headerWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_BORDER_4);
        if (classComponent.isAbstract()) {
            LabelWidget abstractLabel = new LabelWidget(headerWidget.getScene(), "<<abstract>>");
            abstractLabel.setFont(headerWidget.getScene().getDefaultFont().deriveFont(Font.ITALIC));
            abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);
            headerWidget.addChild(abstractLabel);
        }

        nameWidget.setLabel(component.getName());
        headerWidget.addChild(nameWidget);
        addChild(headerWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        // Fields
        fieldsContainer = new Widget(scene);
        fieldsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        fieldsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsContainer.setOpaque(false);
        fieldsContainer.setBorder(EMPTY_BORDER_4);
        LabelWidget fieldName = new LabelWidget(scene);
        fieldsContainer.addChild(fieldName);
        addChild(fieldsContainer);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        // Methods
        methodsContainer = new Widget(scene);
        methodsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        methodsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsContainer.setOpaque(false);
        methodsContainer.setBorder(EMPTY_BORDER_4);
        LabelWidget methodName = new LabelWidget(scene);
        methodsContainer.addChild(methodName);
        addChild(methodsContainer);

        // Actions
        getActions().addAction(ActionFactory.createAcceptAction(new ComponentWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(this)));

        // Fill the widget when loading an existing diagram
        for (Constructor c : classComponent.getConstructors().values()) {
            ConstructorWidget w = new ConstructorWidget(scene, c);
            addMember(methodsContainer, w);
        }

        for (Field fieldComp : classComponent.getFields().values()) {
            FieldWidget w = new FieldWidget(getClassDiagramScene(), fieldComp);
            addMember(fieldsContainer, w);
        }

        for (Method methodComp : classComponent.getMethods().values()) {
            MethodWidget mw = new MethodWidget(getClassDiagramScene(), methodComp);
            addMember(methodsContainer, mw);
        }
    }
    
    public void addFieldWidget() {
        Field field = new Field("untitledField", "Object", Visibility.PRIVATE);
        addField(field);
        FieldWidget fieldWidget = new FieldWidget(getClassDiagramScene(), field);
        addMember(fieldsContainer, fieldWidget);
        getScene().validate();

        WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(fieldWidget));
        ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(fieldWidget.getNameLabel());
        MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
        getScene().getView().addMouseListener(mouseListener);
    }

    public void removeField(FieldWidget field) {
        removeMember(fieldsContainer, field);
    }

    public void addMethodWidget() {
        Method method = new Method("untitledMethod", "void", new HashMap<String, MethodArgument>());
        addMethod(method);
        MethodWidget methodWidget = new MethodWidget(getClassDiagramScene(), method);
        addMember(methodsContainer, methodWidget);
        getScene().validate();

        WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(methodWidget));
        ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(methodWidget.getNameLabel());
        MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
        getScene().getView().addMouseListener(mouseListener);
    }

    public final void addConstructorWidget() {
        Constructor constructor = new Constructor(getName());
        getComponent().addConstructor(constructor);
        ConstructorWidget constructorWidget = new ConstructorWidget(getClassDiagramScene(), constructor);
        addMember(methodsContainer, constructorWidget);
        getScene().validate();
    }

    public void removeMethod(MethodWidget methodWidget) {
        removeMember(methodsContainer, methodWidget);
    }

    @Override
    public ClassComponent getComponent() {
        return (ClassComponent) component;
    }

    @Override
    public void setName(String newName) {
        String oldName = component.getName();
        super.setName(newName);

        for (Widget w : methodsContainer.getChildren()) {
            if (w instanceof ConstructorWidget) {
                ((ConstructorWidget) w).propertyChange(new PropertyChangeEvent(nameWidget, "name", oldName, newName));
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if ("isAbstract".equals(evt.getPropertyName())) {
            boolean isAbstract = (Boolean) evt.getNewValue();
            updateAbstractLabel(isAbstract);
        }
    }

    private void updateAbstractLabel(boolean isAbstract) {
        if (isAbstract) {
            LabelWidget abstractLabel = new LabelWidget(headerWidget.getScene(), "<<abstract>>");
            abstractLabel.setFont(headerWidget.getScene().getDefaultFont().deriveFont(Font.ITALIC));
            abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);
            headerWidget.addChild(0, abstractLabel);
        } else {
            headerWidget.removeChild(headerWidget.getChildren().get(0));
        }
        getScene().validate();
    }

    // TODO REMAKE, COPIED FROM ClassPopupMenuProvider
    private int getCounter(MemberBase member) {
        int brojac = 1;
        String name = member.getName();
        String broj = name.substring(name.length() - 1);
        if (broj.matches("[0-9]")) {
            name = name.substring(0, name.length() - 1);
            member.setName(name);
            brojac = Integer.parseInt(broj) + 1;
        }
        return brojac;
    }

    private void addField(Field f) {
//        try {
            getComponent().addField(f);
//        } catch (RuntimeException ex) {
//            int counter = getCounter(f);
//            f.setName(f.getName() + counter);
//            addField(f);
//        }
    }

    private void addMethod(Method method) {
//        try {
            getComponent().addMethod(method);
//        } catch (RuntimeException ex) {
//            int counter = getCounter(method);
//            method.setName(method.getName() + counter);
//            addMethod(method);
//        }
    }

    @Override
    public void setSignature(String signature) {
        String[] keyWords = signature.split(" ");
        for (String keyWord : keyWords) {
            if (keyWord.equals("public")) {
                component.setVisibility(Visibility.PUBLIC);
            } else if (keyWord.equals("protected")) {
                component.setVisibility(Visibility.PUBLIC);
            } else if (keyWord.equals("private")) {
                component.setVisibility(Visibility.PRIVATE);
            }
            if (keyWord.equals("abstract")) {
                getComponent().setAbstract(true);
            }
        }
    }

    @Override
    public String getSignature() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
