import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InstructionNode {

    private int opcode;

    private Set<Integer> unresolvedDefinitions;
    private Set<Integer> unresolvedReferences;

    private Set<String> definitions;
    private Set<String> references;

    private boolean isJumpInstruction;
    private boolean isGoToInstruction;
    private boolean isReturnInstruction;

    private String jumpTo;

    public InstructionNode(int opcode){
        this.opcode = opcode;

        this.unresolvedDefinitions = new HashSet<>();
        this.unresolvedReferences = new HashSet<>();

        this.definitions = new HashSet<>();
        this.references = new HashSet<>();

        this.isJumpInstruction = false;
        this.isGoToInstruction = false;
    }

    public Set<String> getDefinitions(){
        return definitions;
    }

    public Set<String> getReferences(){
        return references;
    }

    public void addUnresolvedDefinition(int index){
        unresolvedDefinitions.add(index);
    }

    public void addUnresolvedReference(int index){
        unresolvedReferences.add(index);
    }

    public void resolve(Map<Integer, String> resolveInfoMap){
        resolveDefinitions(resolveInfoMap);
        resolveReferences(resolveInfoMap);
    }

    private void resolveDefinitions(Map<Integer, String> resolveInfoMap){
        for(int definition: unresolvedDefinitions){
            if(resolveInfoMap.containsKey(definition)){
                definitions.add(resolveInfoMap.get(definition));
            }
        }
    }

    private void resolveReferences(Map<Integer, String> resolveInfoMap){
        for(int reference: unresolvedReferences){
            if(resolveInfoMap.containsKey(reference)){
                references.add(resolveInfoMap.get(reference));
            }
        }
    }

    public void setAsJumpInstruction(){
        this.isJumpInstruction = true;
    }

    public void setAsGoToInstruction(){
        this.isGoToInstruction = true;
    }

    public boolean isJumpInstruction(){
        return this.isJumpInstruction;
    }

    public boolean isGoToInstruction(){
        return this.isGoToInstruction;
    }

    @Override
    public String toString() {
        return "InstructionNode{" +
                "opcode=" + opcode +
                ", unresolvedDefinitions=" + unresolvedDefinitions +
                ", unresolvedReferences=" + unresolvedReferences +
                ", definitions=" + definitions +
                ", references=" + references +
                '}';
    }

    public void setJumpTo(String label){
        this.jumpTo = label;
    }

    public String getJumpTo(){
        return this.jumpTo;
    }

    public void setAsReturnInstruction(){
        this.isReturnInstruction = true;
    }

    public boolean isReturnInstruction(){
        return this.isReturnInstruction;
    }
}
