import org.objectweb.asm.*;

public class LVAMethodVisitor extends MethodVisitor {

    private MethodGraph methodGraph;

    public LVAMethodVisitor(int api, MethodGraph methodGraph) {
        super(api);
        this.methodGraph = methodGraph;
    }

    public LVAMethodVisitor(int api, MethodVisitor methodVisitor, MethodGraph methodGraph) {
        super(api, methodVisitor);
        this.methodGraph = methodGraph;
    }

    @Override
    public void visitParameter(String name, int access) {
        //System.out.println("visitParameter");
        super.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        //System.out.println("visitAnnotationDefault");
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        //System.out.println("visitAnnotation");
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        //System.out.println("visitTypeAnnotation");
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        //System.out.println("visitAnnotableParameterCount");
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        //System.out.println("visitParameterAnnotation");
        return super.visitParameterAnnotation(parameter, descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        //System.out.println("visitAttribute");
        super.visitAttribute(attribute);
    }

    @Override
    public void visitCode() {
        //System.out.println("visitCode");
        super.visitCode();
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        //System.out.println("visitFrame");
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        //System.out.println("visitInsn");
        this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(opcode));
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        //System.out.println("visitIntInsn");
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        //System.out.println("visitVarInsn");
        this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(opcode));
        switch (opcode){
            case 21:
                this.methodGraph.getLastLabelNode().getLastInstructionNode().addUnresolvedReference(var);
                break;
            case 54:
                this.methodGraph.getLastLabelNode().getLastInstructionNode().addUnresolvedDefinition(var);
                break;
        }
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        //System.out.println("visitTypeInsn");
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        //System.out.println("visitFieldInsn");
        //this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(opcode));
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        //System.out.println("visitMethodInsn");
        if(opcode <= 255){
            //this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(opcode));
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        //System.out.println("visitInvokeDynamicInsn");
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        //System.out.println("visitJumpInsn");
        this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(opcode));
        this.methodGraph.getLastLabelNode().addNextLabel(label.toString());

        this.methodGraph.getLastLabelNode().getLastInstructionNode().setAsJumpInstruction();

        this.methodGraph.getLastLabelNode().getLastInstructionNode().setJumpTo(label.toString());

        if(opcode == Opcodes.GOTO || opcode == Opcodes.JSR){
            this.methodGraph.getLastLabelNode().setContainsGoTo();
            this.methodGraph.getLastLabelNode().getLastInstructionNode().setAsGoToInstruction();
        } else if(opcode == Opcodes.RET){
            this.methodGraph.getLastLabelNode().setContainsGoTo();
            this.methodGraph.getLastLabelNode().getLastInstructionNode().setAsReturnInstruction();
        }
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        //System.out.println("visitLabel");
        this.methodGraph.addLabelNode(new LabelNode(label.toString()));
        this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(-1));
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        //System.out.println("visitLdcInsn");
        //this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(12));
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        //System.out.println("visitIincInsn");
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        //System.out.println("visitTableSwitchInsn");
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        //System.out.println("visitLookupSwitchInsn");
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        //System.out.println("visitMultiANewArrayInsn");
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        //System.out.println("visitInsnAnnotation");
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        //System.out.println("visitTryCatchBlock");
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        //System.out.println("visitTryCatchAnnotation");
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        //System.out.println("visitLocalVariable");
        this.methodGraph.addResolveInfo(index, name);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        //System.out.println("visitLocalVariableAnnotation");
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        //System.out.println("visitLineNumber");
        this.methodGraph.getLastLabelNode().setLineNumber(line);
        //this.methodGraph.getLastLabelNode().addInstructionNode(new InstructionNode(-1));
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        //System.out.println("visitMaxs");
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override
    public void visitEnd() {
        //System.out.println("visitEnd");
        this.methodGraph.finalizeGraph();
        super.visitEnd();
    }
}
