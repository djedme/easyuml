/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.filetype.cdgfiletype;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.uml.model.ClassDiagram;


 @MIMEResolver.Registration(
   displayName="#CLASS_DIAGRAM_FILE",
   resource="ClassDiagramResolver.xml"
 )
@Messages({
    "LBL_CDG_LOADER=Files of CDG",
    "CLASS_DIAGRAM_FILE=Class Diagram File"
})
@MIMEResolver.ExtensionRegistration(
    displayName = "#LBL_CDG_LOADER",
mimeType = "text/x-cdg",
extension = {"cdg", "CDG"})
@DataObject.Registration(
    mimeType = "text/x-cdg",
iconBase = "org/uml/filetype/cdgfiletype/classdiagramicon.gif",
displayName = "#LBL_CDG_LOADER",
position = 300)
@ActionReferences({
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
    position = 100,
    separatorAfter = 200),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
    position = 300),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
    position = 400,
    separatorAfter = 500),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
    position = 600),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
    position = 700,
    separatorAfter = 800),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
    position = 900,
    separatorAfter = 1000),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
    position = 1100,
    separatorAfter = 1200),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
    position = 1300),
    @ActionReference(
        path = "Loaders/text/x-cdg/Actions",
    id =
    @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
    position = 1400)
})
public class CDGDataObject extends MultiDataObject {

    ClassDiagramOpenSupport openAction;
    ClassDiagram classDiagram;
    FileObject cdFileObject;
    CookieSet cookies;
    
    public CDGDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-cdg", true);
        cdFileObject = pf;

        classDiagram = readFile(pf);
        
        if (classDiagram == null) {
            classDiagram = new ClassDiagram();
        }

        cookies = getCookieSet();
        cookies.assign(ClassDiagram.class, classDiagram); // put it in lookup
        openAction = new ClassDiagramOpenSupport(getPrimaryEntry());

        cookies.add((Node.Cookie) openAction);
        cookies.add(this);
    }

    @Override
    protected Node createNodeDelegate() {
        // can use DataNode here as well
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
       // DataNode node = new DataNode(this, Children.LEAF, cookies.getLookup());
        //  node.setShortDescription("Name is " + getLookup().lookup(NeuralNetwork.class).toString());
        node.setDisplayName(cdFileObject.getName());

        return node;
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }
    
    @Override
    protected int associateLookup() {
        return 1;
    }
    
    private ClassDiagram readFile(FileObject fileObject) {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(fileObject.getInputStream());
            try {
                ClassDiagram nn = (ClassDiagram) stream.readObject();
                stream.close();

                return nn;
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
                stream.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    @MultiViewElement.Registration(
        displayName = "#LBL_CDG_EDITOR",
    iconBase = "org/uml/filetype/cdgfiletype/classdiagramicon.gif",
    mimeType = "text/x-cdg",
    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
    preferredID = "CDG",
    position = 1000)
    @Messages("LBL_CDG_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
}
