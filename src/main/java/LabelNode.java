import java.util.*;

public class LabelNode {

    private String label;
    private int lineNumber;

    private Set<String> nextLabels;

    private boolean containsGoTo;


    private List<InstructionNode> instructionNodeList;

    private Set<String> definitions;
    private Set<String> references;

    public LabelNode(String label, int lineNumber){
        this.label = label;
        this.lineNumber = lineNumber;
        this.nextLabels = new HashSet<>();
        this.containsGoTo = false;
        this.instructionNodeList = new ArrayList<>();
        this.definitions = new HashSet<>();
        this.references = new HashSet<>();
    }

    public LabelNode(String label){
        this(label, -1);
    }

    public boolean lineNumberExists() {
        return this.lineNumber != -1;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void addInstructionNode(InstructionNode instructionNode){
        instructionNodeList.add(instructionNode);
    }


    public Set<String> getDefinitions(){
        for(InstructionNode instructionNode: instructionNodeList) {
            definitions.addAll(instructionNode.getDefinitions());
        }
        return definitions;
    }

    public Set<String> getReferences(){
        for(InstructionNode instructionNode: instructionNodeList){
            references.addAll(instructionNode.getReferences());
        }
        return references;
    }

    public InstructionNode getLastInstructionNode(){
        return this.instructionNodeList.get(this.instructionNodeList.size() - 1);
    }

    public void addNextLabel(String label){
        this.nextLabels.add(label);
    }

    public void setContainsGoTo(){
        this.containsGoTo = true;
    }

    public boolean containsGoTo(){
        return this.containsGoTo;
    }

    public String getLabel(){
        return this.label;
    }

    public void resolve(Map<Integer, String> resolveInfoMap){
        for(InstructionNode instructionNode: this.instructionNodeList){
            instructionNode.resolve(resolveInfoMap);
        }
    }


    @Override
    public String toString() {
        return "LabelNode{" +
                "label='" + label + '\'' +
                ", lineNumber=" + lineNumber +
                ", nextLabels=" + nextLabels +
                ", containsGoTo=" + containsGoTo +
                ", instructionNodeList=" + instructionNodeList +
                ", definitions=" + definitions +
                ", references=" + references +
                '}';
    }

    public List<InstructionNode> getInstructionNodeList(){
        return this.instructionNodeList;
    }
}
