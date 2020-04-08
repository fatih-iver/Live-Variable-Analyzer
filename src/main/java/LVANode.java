import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LVANode {

    private int lineNumber;

    private List<Integer> successors;
    private List<Integer> predecessors;

    private Set<String> definitions;
    private Set<String> references;


    public LVANode(int lineNumber, Set<String> definitions, Set<String> references){
        this.lineNumber = lineNumber;
        this.definitions = definitions;
        this.references = references;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    }

    public int getLineNumber(){
        return this.lineNumber;
    }

    public void addSuccessor(int index) {
        this.successors.add(index);
    }

    public List<Integer> getSuccessors(){
        return this.successors;
    }

    public void addPredecessor(int index) {
        this.predecessors.add(index);
    }

    public List<Integer> getPredecessors(){
        return this.predecessors;
    }

    public Set<String> getDefinitions(){
        return this.definitions;
    }

    public Set<String> getReferences(){
        return this.references;
    }

    @Override
    public String toString() {
        return "LVANode{" +
                "lineNumber=" + lineNumber +
                ", successors=" + successors +
                ", predecessors=" + predecessors +
                ", definitions=" + definitions +
                ", references=" + references +
                '}';
    }
}
