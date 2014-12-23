package org.uml.visual.widgets;

import com.timboudreau.vl.jung.ObjectSceneAdapter;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.visual.widgets.components.PackageWidget;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.components.PackageComponent;
import org.uml.model.components.EnumComponent;
import org.uml.visual.widgets.anchors.RhombusAnchorShape;
import org.uml.model.relations.UseRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.HasBaseRelation;
import org.uml.visual.widgets.providers.popups.RelationPopupMenuProvider;
import java.awt.BasicStroke;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.*;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.*;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.*;
import org.uml.explorer.ComponentNode;
import org.uml.explorer.MemberNode;
import org.uml.model.*;
import org.uml.model.members.MemberBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.widgets.actions.RelationLabelTextFieldEditorAction;
import org.uml.visual.widgets.anchors.ParallelNodeAnchor;
import org.uml.visual.widgets.providers.*;
import org.uml.visual.widgets.providers.popups.ScenePopupMenuProvider;

/**
 *
 * https://blogs.oracle.com/geertjan/entry/how_to_serialize_visual_library
 * https://platform.netbeans.org/graph/examples.html layout, serijalizacija,
 * save as image
 *
 * @author NUGS
 */
public class ClassDiagramScene extends GraphScene<ComponentBase, RelationBase> implements LookupListener {

    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;
    private ClassDiagram classDiagram;
    private UMLTopComponent umlTopComponent;
    private HashMap<ComponentWidgetBase, Anchor> anchorMap = new HashMap<>();
    private InstanceContent content = new InstanceContent();
    private AbstractLookup aLookup = new AbstractLookup(content);

    private Router selfLinkRouter;

    Lookup.Result<ComponentNode> selectedExplorerComponent;
    Lookup.Result<MemberNode> selectedExplorerMember;

    public ClassDiagramScene(ClassDiagram umlClassDiagram, final UMLTopComponent umlTopComponent) {

        classDiagram = umlClassDiagram;

        classDiagram.addDeleteListener(new IComponentDeleteListener() {
            @Override
            public void componentDeleted(INameable component) {
                removeNodeWithEdges((ComponentBase) component);

                classDiagram.removeRelationsForAComponent(component);

                repaint();
                validate();
            }
        });

        this.umlTopComponent = umlTopComponent;
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        interractionLayer = new LayerWidget(this);
        addChild(interractionLayer);

        // middle-click + drag  Scene.getInputBindings().getPanActionButton()
        getActions().addAction(ActionFactory.createPanAction());

        // ctrl + scroll        Scene.getInputBindings().getZoomActionModifiers()
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));

        // To support selecting background scene (deselecting all widgets)
        getActions().addAction(ActionFactory.createSelectAction(new SceneSelectProvider(this), false));

        // To support drag-and-drop from the palette
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider(this)));

        getActions().addAction(ActionFactory.createPopupMenuAction(new ScenePopupMenuProvider(this)));

        for (ComponentBase comp : umlClassDiagram.getComponents()) {
            Widget w = addNode(comp);
            w.setPreferredLocation(convertLocalToScene(comp.getPosition()));
            w.setPreferredBounds(comp.getBounds());
        }

        for (RelationBase rel : umlClassDiagram.getRelations()) {
            addRelationToScene(rel, rel.getSource(), rel.getTarget());
        }

        addObjectSceneListener(new ObjectSceneAdapter() {
            @Override
            public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
                ExplorerManager em = umlTopComponent.getExplorerManager();

                try {
                    if (previousFocusedObject != null) {
                        content.remove(previousFocusedObject);
                    } else {
                        content.remove(classDiagram);
                    }

                    if (newFocusedObject != null) {
                        // for selection inside the Explorer
                        content.add(newFocusedObject);

                        // for properties
                        if (newFocusedObject instanceof ComponentBase) {
                            for (Node n : em.getRootContext().getChildren().getNodes()) {
                                ComponentNode cn = (ComponentNode) n;
                                if (cn.getComponent() == newFocusedObject) {
                                    em.setSelectedNodes(new Node[]{cn});
                                    break;
                                }
                            }
                        } else if (newFocusedObject instanceof MemberBase) {
                            boolean over = false;
                            for (Node n : em.getRootContext().getChildren().getNodes()) {
                                for (Node cn : n.getChildren().getNodes()) {
                                    MemberNode mn = (MemberNode) cn;
                                    if (mn.getMember()== newFocusedObject) {
                                        em.setSelectedNodes(new Node[]{mn});
                                        over = true;
                                        break;
                                    }
                                }
                                if(over) break;
                            }
                        }
                    } else {
                        em.setSelectedNodes(new Node[]{em.getRootContext()});
                        content.add(classDiagram);
                    }

                } catch (PropertyVetoException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            @Override
            public void objectAdded(ObjectSceneEvent event, Object addedObject) {
                umlTopComponent.modify();
            }

            @Override
            public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
                umlTopComponent.modify();
            }
        }, ObjectSceneEventType.OBJECT_ADDED, ObjectSceneEventType.OBJECT_REMOVED, ObjectSceneEventType.OBJECT_FOCUS_CHANGED);

        selectedExplorerComponent = Utilities.actionsGlobalContext().lookupResult(ComponentNode.class);
        selectedExplorerComponent.addLookupListener(this);
        selectedExplorerMember = Utilities.actionsGlobalContext().lookupResult(MemberNode.class);
        selectedExplorerMember.addLookupListener(this);
    }

    public UMLTopComponent getUmlTopComponent() {
        return umlTopComponent;
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(aLookup, Lookups.singleton(classDiagram));
    }

    public InstanceContent getContent() {
        return content;
    }

    // adding of a component
    @Override
    protected Widget attachNodeWidget(ComponentBase component) {
        ComponentWidgetBase widget = null;

        // need to check, if loading existing diagram...
        if (!classDiagram.getComponents().contains(component)) {
            classDiagram.addComponent(component);
        }

        if (component instanceof ClassComponent) {
            widget = new ClassWidget(this, (ClassComponent) component);
        } else if (component instanceof InterfaceComponent) {
            widget = new InterfaceWidget(this, (InterfaceComponent) component);
        } else if (component instanceof EnumComponent) {
            widget = new EnumWidget(this, (EnumComponent) component);
        } else if (component instanceof PackageComponent) {
            widget = new PackageWidget(this, (PackageComponent) component);
        } else {
            throw new RuntimeException("Unknown component!");
        }

        mainLayer.addChild(widget);

        return widget;
    }

    // adding of a relation
    @Override
    protected Widget attachEdgeWidget(RelationBase e) {
        if (getObjects().contains(e)) {
            System.out.println("Vec postoji!");
            return null;
        }

        LabelWidget name = new LabelWidget(this, e.getName());
        name.setOpaque(true);

        if (!classDiagram.getRelations().contains(e)) { // need to check, if loading existing diagram...
            classDiagram.addRelation(e);
        }

        ConnectionWidget widget = new ConnectionWidget(this);
        if (e instanceof ImplementsRelation) {
            final float dash1[] = {10.0f};
            final BasicStroke dashed
                    = new BasicStroke(1.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
            widget.setStroke(dashed);
            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));
            name.setLabel("<<implements>>");
            //e.setName("<<implements>>");
        } else if (e instanceof IsRelation) {
            widget.setTargetAnchorShape(AnchorShape.TRIANGLE_HOLLOW);
            name.setLabel("is");
            //e.setName("is");
        } else if (e instanceof HasBaseRelation) {
            HasBaseRelation hasRelation = (HasBaseRelation) e;

            if (hasRelation.isComposition())
                widget.setSourceAnchorShape(new RhombusAnchorShape(45, 10, true));
            else
                widget.setSourceAnchorShape(new RhombusAnchorShape(45, 10, false));

            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

            LabelWidget cardinalityTarget = new LabelWidget(this, hasRelation.getCardinalityTarget().toString());
            widget.addChild(cardinalityTarget);
            widget.setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 0.93f);
        } else {
            UseRelation useRelation = (UseRelation) e;

            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

            LabelWidget cardinalitySource = new LabelWidget(this, useRelation.getCardinalitySource().toString());
            widget.addChild(cardinalitySource);
            widget.setConstraint(cardinalitySource, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_SOURCE, 0.07f);

            LabelWidget cardinalityTarget = new LabelWidget(this, useRelation.getCardinalityTarget().toString());
            widget.addChild(cardinalityTarget);
            widget.setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 0.93f);
        }
        widget.addChild(name);
        widget.setConstraint(name, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        name.getActions().addAction(ActionFactory.createInplaceEditorAction(new RelationLabelTextFieldEditorAction(e, classDiagram)));

        widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        widget.setRouter(RouterFactory.createFreeRouter());
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new RelationPopupMenuProvider(widget, e)));

        widget.getActions().addAction(createObjectHoverAction());
        widget.getActions().addAction(createSelectAction());

        widget.setPaintControlPoints(true);
        widget.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        widget.getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
        widget.getActions().addAction(ActionFactory.createMoveControlPointAction(ActionFactory.createFreeMoveControlPointProvider(), ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY));
        //reconnectAction;

        connectionLayer.addChild(widget);
        return widget;
    }

    private Anchor getAnchorForWidget(ComponentWidgetBase widget) {
        Anchor retVal = anchorMap.get(widget);

        if (retVal == null) {
            retVal = new ParallelNodeAnchor(widget);
            anchorMap.put(widget, retVal);
        }

        return retVal;
    }

    @Override
    protected void attachEdgeSourceAnchor(RelationBase edge, ComponentBase oldSourceNode, ComponentBase sourceNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        ComponentWidgetBase nodeWidget = (ComponentWidgetBase) findWidget(sourceNode);
        if (nodeWidget != null) {
            edgeWidget.setSourceAnchor(getAnchorForWidget(nodeWidget));
        }
//        else {
//            edgeWidget.setSourceAnchor(null);
//        }
        if (isSelfLink(edgeWidget))
            setSelfLinkRouter(edgeWidget);
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationBase edge, ComponentBase oldTargetNode, ComponentBase targetNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        ComponentWidgetBase nodeWidget = (ComponentWidgetBase) findWidget(targetNode);
        if (nodeWidget != null) {
            edgeWidget.setTargetAnchor(getAnchorForWidget(nodeWidget));
        }
//        else {
//            edgeWidget.setTargetAnchor(null);
//        }
        if (isSelfLink(edgeWidget))
            setSelfLinkRouter(edgeWidget);
    }

    private boolean isSelfLink(ConnectionWidget connection) {
        Anchor sourceAnchor = connection.getSourceAnchor();
        Anchor targetAnchor = connection.getTargetAnchor();
        if (sourceAnchor != null && targetAnchor != null && sourceAnchor.getRelatedWidget() == targetAnchor.getRelatedWidget())
            return true;
        else
            return false;
    }

    private void setSelfLinkRouter(ConnectionWidget connection) {
        if (selfLinkRouter == null) {
            selfLinkRouter = new SelfLinkRouter();
        }
        connection.setRouter(selfLinkRouter);
    }

    public final void addRelationToScene(RelationBase relation, ComponentBase source, ComponentBase target) {
        if (!getObjects().contains(relation)) {
            addEdge(relation);
            setEdgeSource(relation, source);
            setEdgeTarget(relation, target);
        } else {
            JOptionPane.showMessageDialog(null, "Relation already exists!");
        }
        repaint();
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result source = (Lookup.Result) ev.getSource();
        List instances = (List) source.allInstances();
        if (!instances.isEmpty()) {
            Object instance = instances.get(0);

            if (instance instanceof ComponentNode) {
                ComponentBase component = ((ComponentNode) instance).getComponent();
                if (isNode(component)) {
                    setFocusedObject(component);
                }
            } else if (instance instanceof MemberNode) {
                MemberBase member = ((MemberNode) instance).getMember();
                if (isObject(member)) {
                    setFocusedObject(member);
                }
            } else {
                setFocusedObject(null);
            }
        } else {
//            setFocusedObject(null);
        }

        repaint();
        validate();
    }

}
