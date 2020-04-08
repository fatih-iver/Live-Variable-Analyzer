import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodGraph {

    private String methodName;

    private List<LabelNode> labelNodeList;

    private Map<Integer, String> resolveInfoMap;

    private Map<String, Integer> labelToLineNumberMap;

    public MethodGraph(String methodName) {
        this.methodName = methodName;
        this.labelNodeList = new ArrayList<>();
        this.resolveInfoMap = new HashMap<>();
        this.labelToLineNumberMap = new HashMap<>();
    }

    public void addLabelNode(LabelNode labelNode){
        this.labelNodeList.add(labelNode);
    }

    public LabelNode getLastLabelNode(){
        return this.labelNodeList.get(this.labelNodeList.size() -1);
    }

    public List<LabelNode> getLabelNodeList(){
        return labelNodeList;
    }

    private void removeUnusedLabelNode(){
        this.labelNodeList.remove(this.labelNodeList.size() -1);
    }

    private void findLabelLineNumbers(){
        for(int i = 0; i < this.labelNodeList.size(); i++){
            if(this.labelNodeList.get(i).lineNumberExists()){
                continue;
            }
            int j = i - 1;
            while (!labelNodeList.get(j).lineNumberExists()){ j--; }
            int missingLineNumber = this.labelNodeList.get(j).getLineNumber();
            this.labelNodeList.get(i).setLineNumber(missingLineNumber);
        }
    }

    private void findNextLabels(){
        for(int i = 0; i < this.labelNodeList.size(); i++){
            if(!this.labelNodeList.get(i).containsGoTo()){
                if(i != this.labelNodeList.size() - 1){
                    this.labelNodeList.get(i).addNextLabel(this.labelNodeList.get(i + 1).getLabel());
                }
            }

        }
    }

    private void makeResolution(){
        for(LabelNode labelNode: this.labelNodeList){
            labelNode.resolve(this.resolveInfoMap);
        }
    }

    public void finalizeGraph(){
        removeUnusedLabelNode();
        findLabelLineNumbers();
        findNextLabels();
        makeResolution();
        constructLabelToLineNumberMap();
    }

    public void addResolveInfo(int index, String variable){
        resolveInfoMap.put(index, variable);
    }

    private void constructLabelToLineNumberMap(){
        for(LabelNode labelNode: this.labelNodeList){
            labelToLineNumberMap.put(labelNode.getLabel(), labelNode.getLineNumber());
        }
    }

    public String getMethodName(){
        return this.methodName;
    }

    @Override
    public String toString() {
        return "MethodGraph{" +
                "methodName='" + methodName + '\'' +
                ", labelNodeList=" + labelNodeList +
                ", resolveInfoMap=" + resolveInfoMap +
                ", labelToLineNumberMap=" + labelToLineNumberMap +
                '}';
    }
}
