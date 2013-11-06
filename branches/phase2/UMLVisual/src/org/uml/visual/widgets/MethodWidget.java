package org.uml.visual.widgets;

import java.util.Random;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.Member;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.MethodPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase {

    Method methodComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget methodNameWidget;

    public MethodWidget(ClassDiagramScene scene, Method method) {
        super(scene);
        this.methodComponent = method;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        methodNameWidget = new LabelWidget(getScene());
        methodNameWidget.setLabel(methodComponent.getName());
        this.addChild(methodNameWidget);
        methodNameWidget.getActions().addAction(nameEditorAction);
        methodNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(this)));


    }

    @Override
    public LabelWidget getNameLabel() {
        return methodNameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = methodComponent.getName();
        if (!methodComponent.getDeclaringClass().nameExists(newName)) {
            methodNameWidget.setLabel(newName);
            methodComponent.setName(newName);
            methodComponent.getDeclaringClass().componentNameChanged(methodComponent, oldName);
        } else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return methodComponent.getName();
    }

    @Override
    public Member getMember() {
        return methodComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        // parse method signature and set method name return value and input parameters
        int openBracketIdx = attributes.indexOf("(");
        if (openBracketIdx != -1) {
            methodComponent.setName(attributes.substring(0, openBracketIdx));
        } else {
            methodComponent.setName(attributes + "()");
        }


        // set return type
        String[] keyWords = attributes.split(":");
        if (keyWords.length > 1) { // this confition is not good
            String type = keyWords[keyWords.length - 1];
            methodComponent.setReturnType(type);
        }

        if (openBracketIdx != -1) {
            // set input parameters
            String parameters = attributes.substring(attributes.indexOf("(") + 1, attributes.indexOf(")"));
            if (parameters.length() > 0) {
                String[] params = parameters.split(",");
                Random r = new Random();
                int Low = 0;
                int High = 100;
                for (String param : params) {
                    String[] typeAndName = param.split(" ");
                    int R = r.nextInt(High - Low) + Low;
                    methodComponent.getArguments().put(Integer.toString(R), new MethodArgument(typeAndName[0], typeAndName[1]));
                }
            }
        }


    }
}
