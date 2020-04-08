import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.util.*;

public class Main {

     public static void main(String[] args) throws IOException {

        //System.out.println("Live Variable Analyzer");

         Map<Integer, String> lineNumberToSourceMap = formLineNumberToSourceMap(new File("C:\\Users\\Fatih\\IdeaProjects\\Live Variable Analyzer\\src\\main\\java\\TestData.java"));

         ClassReader classReader = new ClassReader("TestData");
         ClassNode classNode = new ClassNode();
         classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

         List<MethodNode> methodNodeList = classNode.methods;
         /*
         for (MethodNode methodNode : methodNodeList) {
             if(methodNode.name.equals("<init>")){
                 continue;
             }
             System.out.println();
             System.out.println(methodNode.name);
             InsnList insnList = methodNode.instructions;
             for (AbstractInsnNode abstractInsnNode : insnList) {
                 System.out.println(abstractInsnNode);
             }

         }

          */


         for (MethodNode methodNode : methodNodeList) {
             if (methodNode.name.equals("<init>")){
                 continue;
             }
             //System.out.println();
             //System.out.println(methodNode.name);
             MethodGraph methodGraph = new MethodGraph(methodNode.name);
             methodNode.accept(new LVAMethodVisitor(Opcodes.ASM4, methodGraph));
             //System.out.println(methodGraph.toString());

             /*
             for(LabelNode labelNode: methodGraph.getLabelNodeList()){

                 System.out.println();
                 System.out.println(labelNode.getLabel() + " " +labelNode.getLineNumber());
                 System.out.println("Definitions:");
                 System.out.println(labelNode.getDefinitions());
                 System.out.println("References");
                 System.out.println(labelNode.getReferences());
                 System.out.println();
             }

              */

             LiveVariableAnalyzer liveVariableAnalyzer = new LiveVariableAnalyzer(methodGraph, lineNumberToSourceMap);
             liveVariableAnalyzer.initialize().analyze();

             //LiveVariableAnalyzer liveVariableAnalyzer2 = new LiveVariableAnalyzer(methodGraph, lineNumberToSourceMap);
             //liveVariableAnalyzer2.initialize().analyze2();

         }


         List<Integer> lineNumbers = new ArrayList<>(lineNumberToSourceMap.keySet());
         Collections.sort(lineNumbers);

         FileWriter fileWriter = new FileWriter("TestData.java");;
         try {
             for(int lineNumber: lineNumbers){
                 //System.out.println(lineNumberToSourceMap.get(lineNumber));
                 fileWriter.write("\n");
                 fileWriter.write(lineNumberToSourceMap.get(lineNumber));
             }
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if(fileWriter != null){
                 fileWriter.close();
             }
         }
         /*
         //System.out.println("Dead Code Eliminated Class");
         for(int lineNumber: lineNumbers){
             System.out.println(lineNumberToSourceMap.get(lineNumber));
         }

             */



     }

    private static Map<Integer, String> formLineNumberToSourceMap(File sourceFile) {

        Map<Integer, String> lineNumberToSource = new HashMap<>();

        try {
            FileReader fileReader = new FileReader(sourceFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                i += 1;
                lineNumberToSource.put(i, line);
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineNumberToSource;
    }


}
