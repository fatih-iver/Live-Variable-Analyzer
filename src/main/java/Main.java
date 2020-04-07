import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {

     public static void main(String[] args) throws IOException {

        System.out.println("Live Variable Analyzer");

         ClassReader classReader = new ClassReader("TestData");
         ClassNode classNode = new ClassNode();
         classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

         List<MethodNode> methodNodeList = classNode.methods;

         for (MethodNode methodNode : methodNodeList) {
             System.out.println();
             System.out.println(methodNode.name);
             InsnList insnList = methodNode.instructions;
             for (AbstractInsnNode abstractInsnNode : insnList) {
                 System.out.println(abstractInsnNode);
             }
         }


         for (MethodNode methodNode : methodNodeList) {
             System.out.println();
             System.out.println(methodNode.name);
             methodNode.accept(new LVAMethodVisitor(Opcodes.ASM4));
         }


     }


}
