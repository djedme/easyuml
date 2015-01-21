package org.uml.model.relations;

//import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.Objects;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;

/**
 * Is relation in UML class diagrams. Describes relation used usually
 * when an object <i>extends</i> another one.
 *
 * @author "NUGS"
 * @see RelationBase
 * @see HasRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
//@XStreamAlias("IsRelation")
public class IsRelation extends RelationBase {

    /**
     * Returns the name of relation.
     *
     * @return "Is"
     */
    @Override
    public String toString() {
        return "Is";
    }

    @Override
    public boolean canConnect(ComponentBase source, ComponentBase target) {
        Class<?> sc = source.getClass();
        Class<?> tc = target.getClass();
        if(source == target) return false;
        if(sc == ClassComponent.class && tc == ClassComponent.class) return true;
        if(sc == InterfaceComponent.class && tc == InterfaceComponent.class) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.source);
        hash = 53 * hash + Objects.hashCode(this.target);
        return hash;
    }
}
