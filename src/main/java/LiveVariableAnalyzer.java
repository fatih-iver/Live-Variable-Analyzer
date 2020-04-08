import java.util.*;

public class LiveVariableAnalyzer {

    private MethodGraph methodGraph;
    private List<LVANode> lvaNodeList;

    private Map<String, Integer> labelToIndexMap;

    private Map<Integer, String> lineNumberToSourceMap;

    public LiveVariableAnalyzer(MethodGraph methodGraph, Map<Integer, String > lineNumberToSourceMap){
        this.methodGraph = methodGraph;
        this.lvaNodeList = new ArrayList<>();
        this.labelToIndexMap = new HashMap<>();
        this.lineNumberToSourceMap = lineNumberToSourceMap;
    }

    private void constructLVANodeList(){
        for(LabelNode labelNode: methodGraph.getLabelNodeList()){
            boolean isLabel = true;
            for(InstructionNode instructionNode: labelNode.getInstructionNodeList()){
                this.lvaNodeList.add(new LVANode(labelNode.getLineNumber(),instructionNode.getDefinitions(), instructionNode.getReferences()));
                if(isLabel){
                    isLabel = false;
                    labelToIndexMap.put(labelNode.getLabel(), lvaNodeList.size() - 1);
                }
            }
        }
    }

    private void calculateJumpIndexes(){
        int i = 0;

        for(LabelNode labelNode: methodGraph.getLabelNodeList()){
            for(InstructionNode instructionNode: labelNode.getInstructionNodeList()){

                if(i == lvaNodeList.size() - 1){
                    break;
                }

                if(instructionNode.isGoToInstruction() || instructionNode.isReturnInstruction()){
                    lvaNodeList.get(i).addSuccessor(this.labelToIndexMap.get(instructionNode.getJumpTo()));
                    i += 1;
                    continue;
                }

                lvaNodeList.get(i).addSuccessor(i+1);

                if(instructionNode.isJumpInstruction()){
                    lvaNodeList.get(i).addSuccessor(this.labelToIndexMap.get(instructionNode.getJumpTo()));
                }

                i += 1;

            }

            if(i == lvaNodeList.size() - 1){
                break;
            }

        }

        for(int j = 0; j < lvaNodeList.size(); j++){
            for(int successor: lvaNodeList.get(j).getSuccessors()){
                lvaNodeList.get(successor).addPredecessor(j);
            }
        }

    }

    private void printInitializationDebugOutput(){
        /*
        int k = 0;

        System.out.println();

        for(LabelNode labelNode: methodGraph.getLabelNodeList()){
            for(InstructionNode instructionNode: labelNode.getInstructionNodeList()){
                System.out.println("" + k + " " +instructionNode.toString());
                k += 1;
            }
        }

         */

        //System.out.println();

        /*
        int j = 0;

        for(LVANode lvaNode: lvaNodeList){
            System.out.println("" + j + " : " +lvaNode.toString());
            j += 1;
        }

         */

    }

    public LiveVariableAnalyzer initialize(){

        constructLVANodeList();
        calculateJumpIndexes();
        printInitializationDebugOutput();

        return this;
    }

    public void analyze(){
        //System.out.println();
        //System.out.println("Analysis...");

        /*

        Set<String> allDefinitions = new HashSet<>();

        for(LVANode lvaNode: lvaNodeList){
            allDefinitions.addAll(lvaNode.getDefinitions());
        }

        Set<String> allReferences = new HashSet<>();

        for(LVANode lvaNode: lvaNodeList){
            allReferences.addAll(lvaNode.getReferences());
        }

        Set<String> notReferencedAtAll = new HashSet<>(allDefinitions);
        notReferencedAtAll.removeAll(allReferences);


        Map<String, Integer> firstTimeDefinitions = new HashMap<>();

        for(LVANode lvaNode : lvaNodeList){
            for(String definition: lvaNode.getDefinitions()){
                if(!firstTimeDefinitions.containsKey(definition)){
                    firstTimeDefinitions.put(definition, lvaNode.getLineNumber());
                }
            }
        }


         */

        Map<Integer, Set<String>> livenessMap = new HashMap<>();

        int j = 0;

        for(LVANode lvaNode: lvaNodeList){
            livenessMap.put(j++, new HashSet<>());
        }

        int n = livenessMap.size();

        boolean livenessMapChanged = true;

        while (livenessMapChanged){
            livenessMapChanged = false;

            for(int i = 0; i < n; i++){

                Set<String> successorsLiveness = new HashSet<>();

                for(int successor: lvaNodeList.get(i).getSuccessors()){
                    successorsLiveness.addAll(livenessMap.get(successor));
                }

                for(String definition: lvaNodeList.get(i).getDefinitions()){
                    if(successorsLiveness.contains(definition)){
                        successorsLiveness.remove(definition);
                    }
                }

                successorsLiveness.addAll(lvaNodeList.get(i).getReferences());

                if(successorsLiveness.size() == 0){
                    continue;
                }



                if(livenessMap.get(i).size() != successorsLiveness.size()){
                    livenessMapChanged = true;
                    livenessMap.put(i, successorsLiveness);
                } else {
                    if(!livenessMap.get(i).containsAll(successorsLiveness)){
                        livenessMapChanged = true;
                        livenessMap.put(i, successorsLiveness);
                    } else if (!successorsLiveness.containsAll(livenessMap.get(i))){
                        livenessMapChanged = true;
                        livenessMap.put(i, successorsLiveness);
                    }
                }
            }

        }

        //System.out.println();
        //System.out.println(livenessMap.toString());

        Set<Integer> deadLineNumbers = new HashSet<>();

        for(int i = 0; i < lvaNodeList.size()-1; i++){
            for(String definition: lvaNodeList.get(i).getDefinitions()){
                if(!livenessMap.get(i+1).contains(definition)){
                    deadLineNumbers.add(lvaNodeList.get(i).getLineNumber());
                }
            }

        }

        //System.out.println();

        /*

        for(int lineNumbers: firstTimeDefinitions.values()){
            if(lineNumberToSourceMap.get(lineNumbers).contains("int")){

                if(deadLineNumbers.contains(lineNumbers)){
                    deadLineNumbers.remove(lineNumbers);
                }

                for(String variable: notReferencedAtAll){
                    if(lineNumberToSourceMap.get(lineNumbers).contains(variable)){
                        deadLineNumbers.add(lineNumbers);
                    }
                }
            }
        }

         */

        List<Integer> lineNumberList = new ArrayList<>(deadLineNumbers);
        Collections.sort(lineNumberList);
        System.out.println(methodGraph.getMethodName() + " " + lineNumberList);

        for(int lineNumber: lineNumberList){
            lineNumberToSourceMap.remove(lineNumber);
        }
    }

    /*

    public void analyze2(){

        int n = lvaNodeList.size();

        Map<Integer, Set<String>> in = new HashMap<>();

        for(int i = 0; i < n; i++){
            in.put(i, new HashSet<>());
        }

        Set<Integer> changedNodes = new HashSet<>();

        for(int i = 0; i < n; i++){
            changedNodes.add(i);
        }

        while (!changedNodes.isEmpty()){

            int index = Collections.min(changedNodes);

            if(changedNodes.remove(index)){

                Set<String> out = new HashSet<>();

                for(int successor: lvaNodeList.get(index).getSuccessors()){
                    out.addAll(in.get(successor));
                }

                for(String definition: lvaNodeList.get(index).getDefinitions()){
                    if(out.contains(definition)){
                        out.remove(definition);
                    }
                }

                for(String reference: lvaNodeList.get(index).getReferences()){
                    if(!out.contains(reference)){
                        out.add(reference);
                    }
                }

                if(in.get(index).size() != out.size() || !in.get(index).containsAll(out)){
                    in.put(index, out);
                    for(int predecessor: lvaNodeList.get(index).getPredecessors()){
                        changedNodes.add(predecessor);
                    }
                }

            }

        }

        System.out.println();
        System.out.println("in:" + in.toString());

        Set<Integer> deadLineNumbers = new HashSet<>();

        for(int i = 0; i < lvaNodeList.size(); i++){
            for(String definition: lvaNodeList.get(i).getDefinitions()){
                if(!in.get(i).contains(definition)){
                    deadLineNumbers.add(lvaNodeList.get(i).getLineNumber());
                }
            }
        }

        System.out.println();
        System.out.println(deadLineNumbers.toString());

        for(int index: in.keySet()){
            for(String definition: lvaNodeList.get(index).getDefinitions()){

            }
        }


    }

     */
}
