/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package componentOriginating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openide.util.Exceptions;
import org.uml.model.CardinalityEnum;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.HasRelationComponent;
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.IsRelationComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.reveng.CompilationProcessor;
import org.uml.reveng.GeneratedDiagramManager;

/**
 *
 * @author Milan
 */
//CODE GENERATOR DOES NOT CURRENTLY SUPPORT NESTED COLLECTIONS (for example HashMap <String, List<String>>) AND RELATIONS SHALL BE MADE ACCORDING TO IT
//IT DOES NOT ALSO SUPPORT ARRAYS [ ] AND IN PLACES WHERE ARRAYS EXIST, USER WILL BE INFORMED BY COMMENT TO REPLACE CURRENT VARIABLE
public class RelationshipResolver {

    private static boolean primitiveTest(String strtst) {
        if (strtst.equals("byte")
                || strtst.equals("short")
                || strtst.equals("int")
                || strtst.equals("long")
                || strtst.equals("float")
                || strtst.equals("double")
                || strtst.equals("boolean")
                || strtst.equals("char")) {
            return true;
        } else {
            return false;
        }
    }
    private static String fieldNameHas = "";

    public static void relationshipHasCreator(String testEl, String fieldClassPath, String additionalData, String fieldName) {
        GeneratedDiagramManager.getDefault().getHasRelationships().clear();
        fieldNameHas = fieldName;
        if (testEl.contains(",")) {
            String[] arguments = testEl.split(",");
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].contains("<") || arguments[i].endsWith(">")) {
                    sharpBracketResolver(arguments[i], fieldClassPath, CompilationProcessor.hasRelationships);
                } else if (arguments[i].contains("[")) {
                    squareBracketResolver(arguments[i], fieldClassPath, CompilationProcessor.hasRelationships);
                } else {
                    noBracketsReslover(arguments[i], fieldClassPath, additionalData, CompilationProcessor.hasRelationships);
                }
            }
        } else {
            if (testEl.contains("<") || testEl.endsWith(">")) {
                sharpBracketResolver(testEl, fieldClassPath, CompilationProcessor.hasRelationships);
            } else if (testEl.contains("[")) {
                squareBracketResolver(testEl, fieldClassPath, CompilationProcessor.hasRelationships);
            } else {
                noBracketsReslover(testEl, fieldClassPath, additionalData, CompilationProcessor.hasRelationships);
            }
        }
    }
    static String leftSharpBracketOpen = "";
    private static String fieldNameUses = "";

    public static void relationshipUsesCreator(String methodArgs, String methodClassPath, String addData, String paramterName) {
        fieldNameUses = paramterName;
        GeneratedDiagramManager.getDefault().getUsesRelationships().clear();
        if (methodArgs.contains(",")) {
            String[] arguments = methodArgs.split(",");
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].contains("<") || arguments[i].endsWith(">")) {
                    sharpBracketResolver(arguments[i], methodClassPath, CompilationProcessor.useRelationships);
                } else if (arguments[i].contains("[")) {
                    squareBracketResolver(arguments[i], methodClassPath, CompilationProcessor.useRelationships);
                } else {
                    noBracketsReslover(arguments[i], methodClassPath, addData, CompilationProcessor.useRelationships);
                }
            }
        } else {
            if (methodArgs.contains("<") || methodArgs.endsWith(">")) {
                sharpBracketResolver(methodArgs, methodClassPath, CompilationProcessor.useRelationships);
            } else if (methodArgs.contains("[")) {
                squareBracketResolver(methodArgs, methodClassPath, CompilationProcessor.useRelationships);
            } else {
                noBracketsReslover(methodArgs, methodClassPath, addData, CompilationProcessor.useRelationships);
            }
        }
    }

    private static void sharpBracketResolver(String arguments, String methodClassPath, HashMap<String, HashMap<String, Object>> designatedRelation) {
        boolean isHasRelation = false;
        if (designatedRelation == CompilationProcessor.hasRelationships) {
            isHasRelation = true;
        }
        String additionalRelationData = "";
        if (arguments.contains("<") && arguments.endsWith(">")) {
            int indexes = arguments.indexOf(">");
            List<Object> indexesOfBrackets = new ArrayList<Object>();
            while (indexes >= 0) {
                indexesOfBrackets.add(indexes);
                indexes = arguments.indexOf(">", indexes + 1);
            }
            if (arguments.split("<").length > 2 && indexesOfBrackets.size() > 1) {
                String[] args = arguments.split("<");
                String firstLess = args[1] + "<" + args[2];
                if (args.length > 2) {
                    firstLess = args[1];
                    for (int i1 = 2; i1 < args.length; i1++) {
                        firstLess = firstLess + "<" + args[i1];
                    }
                }
                String firstAndBracketLess = firstLess.substring(0, firstLess.length() - 1);
                if (isHasRelation) {
                    relationshipHasCreator(firstAndBracketLess, methodClassPath, arguments.split("<")[0], fieldNameHas);
                } else {
                    relationshipUsesCreator(firstAndBracketLess, methodClassPath, arguments.split("<")[0], fieldNameUses);
                }
            } else if (arguments.split("<").length > 2 && indexesOfBrackets.size() == 1) {
                String[] args = arguments.split("<");
                String firstLess = args[1] + "<" + args[2];
                if (args.length > 2) {
                    firstLess = args[1];
                    for (int i1 = 2; i1 < args.length; i1++) {
                        firstLess = firstLess + "<" + args[i1];
                    }
                }
                if (isHasRelation) {
                    relationshipHasCreator(firstLess, methodClassPath, arguments.split("<")[0], fieldNameHas);
                } else {
                    relationshipUsesCreator(firstLess, methodClassPath, arguments.split("<")[0], fieldNameUses);
                }
            } else {
                if (isHasRelation) {
                    relationshipHasCreator(arguments.split("<")[1].split(">")[0], methodClassPath, arguments.split("<")[0], fieldNameHas);
                } else {
                    relationshipUsesCreator(arguments.split("<")[1].split(">")[0], methodClassPath, arguments.split("<")[0], fieldNameUses);
                }
            }
        } else if (arguments.contains("<") && !arguments.endsWith(">")) {
            leftSharpBracketOpen = arguments.split("<")[0];
            if (isHasRelation) {
                relationshipHasCreator(arguments.split("<")[1], methodClassPath, arguments.split("<")[0], fieldNameHas);
            } else {
                relationshipUsesCreator(arguments.split("<")[1], methodClassPath, arguments.split("<")[0], fieldNameUses);
            }
        } else if (!arguments.contains("<") && arguments.endsWith(">")) {
            try {
                if (primitiveTest(arguments.split(">")[0])) {
                    return;
                }
                Class cl = Class.forName(arguments.split(">")[0]);
            } catch (ClassNotFoundException ex) {
                if (isHasRelation) {
                    relationshipHasCreator(arguments.split(">")[0], methodClassPath, leftSharpBracketOpen, fieldNameHas);
                } else {
                    relationshipUsesCreator(arguments.split(">")[0], methodClassPath, leftSharpBracketOpen, fieldNameUses);
                }
                leftSharpBracketOpen = "";
            }
            return;
        }
        try {
            if (primitiveTest(arguments)) {
                return;
            }
            additionalRelationData = arguments.split("<")[0];
            Class cl = Class.forName(arguments.split("<")[0]);
        } catch (ClassNotFoundException ex) {
            //YET TO BE IMPLEMENTED
            if (isHasRelation) {
                putIntoRelHashMap(designatedRelation, methodClassPath, "COLLECTION", additionalRelationData, fieldNameHas);
            } else {
                putIntoRelHashMap(designatedRelation, methodClassPath, "COLLECTION", additionalRelationData, fieldNameUses);
            }
        }
    }

    private static void squareBracketResolver(String arguments, String methodClassPath, HashMap<String, HashMap<String, Object>> designatedRelation) {
        boolean isHasRelation = false;
        if (designatedRelation == CompilationProcessor.hasRelationships) {
            isHasRelation = true;
        }
        String additionalRelationData = "";
        String splitted = arguments.split("\\[")[0];
        try {
            if (primitiveTest(splitted)) {
                return;
            }
            additionalRelationData = "/*CHANGE TO ARRAY WITH [ ]*/ List";
            Class cl = Class.forName(splitted);
        } catch (ClassNotFoundException ex) {
            if (isHasRelation) {
                putIntoRelHashMap(designatedRelation, methodClassPath, splitted, additionalRelationData, fieldNameHas);
            } else {
                putIntoRelHashMap(designatedRelation, methodClassPath, splitted, additionalRelationData, fieldNameUses);
            }
        }
    }

    private static void noBracketsReslover(String arguments, String methodClassPath, String aditionalData, HashMap<String, HashMap<String, Object>> designatedRelation) {
        boolean isHasRelation = false;
        if (designatedRelation == CompilationProcessor.hasRelationships) {
            isHasRelation = true;
        }
        CardinalityEnum cardinality = CardinalityEnum.Zero2Many;
        if (designatedRelation == CompilationProcessor.hasRelationships) {
            cardinality = CardinalityEnum.One2Many;
        }
        try {
            if (primitiveTest(arguments)) {
                return;
            }
            Class cl = Class.forName(arguments);
        } catch (ClassNotFoundException ex) {
            if (!aditionalData.equals("")) {
                if (isHasRelation) {
                    putIntoRelHashMap(designatedRelation, methodClassPath, arguments, aditionalData, fieldNameHas);
                } else {
                    putIntoRelHashMap(designatedRelation, methodClassPath, arguments, aditionalData, fieldNameUses);
                }
            } else {
                if (isHasRelation) {
                    putIntoRelHashMap(designatedRelation, methodClassPath, arguments, cardinality, fieldNameHas);
                } else {
                    putIntoRelHashMap(designatedRelation, methodClassPath, arguments, cardinality, fieldNameUses);
                }
            }
        }
    }

    public static void putIntoRelHashMap(HashMap<String, HashMap<String, Object>> desHashMap, String relationFrom, String relationTo, Object additionalData, String fieldName) {
        if (desHashMap.containsKey(relationFrom)) {
            if (desHashMap.get(relationFrom).containsKey(relationTo)) {
                String additionalPlusses = plusIterator(desHashMap.get(relationFrom), relationTo, "", additionalData.toString());
                if (additionalPlusses.contains("0")) {
                    return;
                }
                desHashMap.get(relationFrom).put(relationTo + additionalPlusses, new ObjectArray(fieldName, additionalData));
            } else {
                desHashMap.get(relationFrom).put(relationTo, new ObjectArray(fieldName, additionalData));
            }
        } else {
            HashMap<String, Object> newHashMap = new HashMap<String, Object>();
            newHashMap.put(relationTo, new ObjectArray(fieldName, additionalData));
            desHashMap.put(relationFrom, newHashMap);
        }
    }

    public static String plusIterator(HashMap<String, Object> hashMapToBeChecked, String desiredKey, String initPlusString, String addData) {
        if (hashMapToBeChecked.containsKey(desiredKey + initPlusString)) {
            if (!hashMapToBeChecked.get(desiredKey + initPlusString).toString().equals(addData)) {
                initPlusString = initPlusString + "+";
                initPlusString = plusIterator(hashMapToBeChecked, desiredKey, initPlusString, addData);
            } else {
                //If Additional data is the same - the relation is the same and needs not be duplicated
                return initPlusString;// + "0";
            }
        }
        return initPlusString;
    }

    public static void resolveRelationsHasAndUses() {
        populateUseRelation();
        populateHasRelation();
    }

    public static void resolveRelationsIsAndImplements() {
        populateIsRelation();
        populateImplementsRelation();
    }

    private static void populateHasRelation() {
        for (Map.Entry firstLevel : CompilationProcessor.hasRelationships.entrySet()) {
            ClassDiagramComponent source = selectDefinitiveComponent(firstLevel.getKey().toString());
            HashMap<String, Object> midLevel = (HashMap<String, Object>) firstLevel.getValue();
            for (Map.Entry secondLevel : midLevel.entrySet()) {
                HasRelationComponent relation = new HasRelationComponent();
                relation.setSource(source);
                //ClassDiagramComponent targetCandidate = new ClassDiagramComponent();
                ClassDiagramComponent targetCandidate = selectDefinitiveComponent(secondLevel.getKey().toString());
                ObjectArray addDataContainer = (ObjectArray)secondLevel.getValue();
                Object addData = addDataContainer.getObject();
                String name = addDataContainer.getField();
                relation.setName(name);
                GeneratedDiagramManager.getDefault().incrementRelationCounter();
                if (addData instanceof String) {
                    relation.setCollectionType(addData.toString());
                    relation.setCardinalityTarget(CardinalityEnum.Zero2Many);
                    //targetCandidate.setName(targetCandidate.getName()+"/*Please add collection parameters if necessary*/");
                } else {
                    relation.setCardinalityTarget((CardinalityEnum) addData);
                    relation.setCollectionType("");
                }
                if (relation.getSource() == (targetCandidate)) {
                    continue;
                }
                relation.setTarget(targetCandidate);
                relation.setCardinalitySource(CardinalityEnum.Zero2Many);
                if (!denyDuplicates(source, targetCandidate, relation.toString(), relation.getCardinalityTarget())) {
                    if (source == targetCandidate) {
                        continue;
                    }
                    GeneratedDiagramManager.getDefault().getClassDiagram().addRelation(relation);
                }
            }
        }
    }

    private static void populateUseRelation() {
        for (Map.Entry firstLevel : CompilationProcessor.useRelationships.entrySet()) {
            ClassDiagramComponent source = (selectDefinitiveComponent(firstLevel.getKey().toString()));
            HashMap<String, Object> midLevel = (HashMap<String, Object>) firstLevel.getValue();
            for (Map.Entry secondLevel : midLevel.entrySet()) {
                UseRelationComponent relation = new UseRelationComponent();
                relation.setSource(source);
                ClassDiagramComponent target = selectDefinitiveComponent(secondLevel.getKey().toString());
                relation.setTarget(target);
                
                ObjectArray addDataContainer = (ObjectArray)secondLevel.getValue();
                String name = addDataContainer.getField();
                
                relation.setName(name);//GeneratedDiagramManager.getDefault().getRelationCounter() + "");
                GeneratedDiagramManager.getDefault().incrementRelationCounter();
                relation.setCardinalitySource(CardinalityEnum.Zero2Many);
                relation.setCardinalityTarget(CardinalityEnum.Zero2Many);
                if (!denyDuplicates(source, target, relation.toString(), null)) {
                    if (source == target) {
                        continue;
                    }
                    GeneratedDiagramManager.getDefault().getClassDiagram().addRelation(relation);
                }
            }
        }
    }

    private static void populateIsRelation() {
        for (Map.Entry firstLevel : GeneratedDiagramManager.getDefault().getIsRelationships().entrySet()) {
            ClassDiagramComponent source = selectDefinitiveComponent(firstLevel.getKey().toString());
            HashMap<String, Object> midLevel = (HashMap<String, Object>) firstLevel.getValue();
            for (Map.Entry secondLevel : midLevel.entrySet()) {
                IsRelationComponent relation = new IsRelationComponent();
                relation.setSource(source);
                relation.setTarget(selectDefinitiveComponent(secondLevel.getKey().toString()));
                ObjectArray addDataContainer = (ObjectArray)secondLevel.getValue();
                String name = addDataContainer.getField();
                relation.setName(name);
                //relation.setName(GeneratedDiagramManager.getDefault().getRelationCounter() + "");
                GeneratedDiagramManager.getDefault().incrementRelationCounter();
                GeneratedDiagramManager.getDefault().getClassDiagram().addRelation(relation);
            }
        }
    }

    private static void populateImplementsRelation() {
        for (Map.Entry firstLevel : GeneratedDiagramManager.getDefault().getImplementsRelationships().entrySet()) {
            ClassDiagramComponent source = selectDefinitiveComponent(firstLevel.getKey().toString());
            HashMap<String, Object> midLevel = (HashMap<String, Object>) firstLevel.getValue();
            for (Map.Entry secondLevel : midLevel.entrySet()) {
                ImplementsRelationComponent relation = new ImplementsRelationComponent();
                relation.setSource(source);
                relation.setTarget(selectDefinitiveComponent(secondLevel.getKey().toString()));
                ObjectArray addDataContainer = (ObjectArray)secondLevel.getValue();
                String name = addDataContainer.getField();
                relation.setName(name);
                //relation.setName(GeneratedDiagramManager.getDefault().getRelationCounter() + "");
                GeneratedDiagramManager.getDefault().incrementRelationCounter();
                GeneratedDiagramManager.getDefault().getClassDiagram().addRelation(relation);
            }
        }
    }

    public static ClassDiagramComponent selectDefinitiveComponent(String componentName) {
        String[] componentNameArray = componentName.split("\\.");
        String componentClasName = componentNameArray[componentNameArray.length - 1].split("\\+")[0];
        Set<String> keySet = CompilationProcessor.generatedDiagram.getComponents().keySet();
        List<ClassDiagramComponent> foundElementsInDiagramComponent = new ArrayList<ClassDiagramComponent>();
        for (String componentSetName : keySet) {
            if (componentSetName.startsWith(componentClasName)) {
                foundElementsInDiagramComponent.add(CompilationProcessor.generatedDiagram.getComponents().get(componentSetName));
            }
        }
        String requiredPackageName = componentName.toString().split("." + componentClasName)[0];
        for (ClassDiagramComponent component : foundElementsInDiagramComponent) {
            if (component.getParentPackage().getName().equals(requiredPackageName)) {
                return component;
            }
        }
        return new ClassDiagramComponent();
    }

    private static boolean denyDuplicates(ClassDiagramComponent source, ClassDiagramComponent target, String relationType, CardinalityEnum targetCardinaliy) {
        boolean reappears = false;
        HashMap<String, RelationComponent> relations = GeneratedDiagramManager.getDefault().getClassDiagram().getRelations();
        for (Map.Entry<String, RelationComponent> relation : relations.entrySet()) {
            //System.out.println(relation.getValue().getSource().getName() + "-" + relation.getValue().getTarget().getName());
            if (relation.getValue().getSource() == source
                    && relation.getValue().getTarget() == target
                    && relation.getValue().toString().equals(relationType)) {
                //Use is Many2Many by default
                /*if (relationType.equals("Use")) {
                 UseRelationComponent useTest = (UseRelationComponent) relation;
                 if (useTest.getCardinalitySource().equals(targetCardinaliy)) {
                 reappears = true;
                 }
                 } else */
                if (relationType.equals("Has")) {
                    HasRelationComponent hasTest = (HasRelationComponent) relation.getValue();
                    if (hasTest.getCardinalityTarget().equals(targetCardinaliy)) {
                        reappears = true;
                        break;
                    } else {
                        reappears = false;
                    }
                } else {
                    reappears = true;
                    break;
                }
            }
        }
        //System.out.println(reappears);
        return reappears;
    }
}