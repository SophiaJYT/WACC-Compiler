package backEnd;

import backEnd.instructions.*;
import frontEnd.Identifier;
import frontEnd.Type;

import java.util.*;

import static backEnd.RegisterType.*;
import static backEnd.instructions.BranchType.*;
import static backEnd.instructions.DataProcessingType.*;
import static backEnd.instructions.SingleDataTransferType.*;
import static backEnd.instructions.StackType.*;
import static frontEnd.AllTypes.*;

public class ExtraMethodGenerator {

    private Deque<Instruction> extraMethods = new ArrayDeque<>();
    private List<Label> methodLabels = new ArrayList<>();
    private Label runtimeError = new Label("p_throw_runtime_error");

    private Register r0 = new Register(R0);
    private Register r1 = new Register(R1);
    private Register r2 = new Register(R2);
    private Register r3 = new Register(R3);
    private Register sp = new Register(SP);
    private Register pc = new Register(PC);
    private Register lr = new Register(LR);

    private Data data;

    public ExtraMethodGenerator(Data data) {
        this.data = data;
    }

    public void generatePrintLn(Label printLn, Label newLineLabel) {
        // Check if println label exists
        if (methodLabels.contains(printLn)) {
            return;
        }
        extraMethods.add(printLn);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, newLineLabel));
        extraMethods.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        extraMethods.add(new BranchInstruction(BL, new Label("puts")));
        extraMethods.add(new DataProcessingInstruction<>(MOV, r0, 0));
        extraMethods.add(new BranchInstruction(BL, new Label("fflush")));
        extraMethods.add(new StackInstruction(POP, pc));
        methodLabels.add(printLn);
    }

    public void generatePrint(Type type, Label printLabel) {
        // Check if the print label already exists
        if (methodLabels.contains(printLabel)) {
            return;
        }

        extraMethods.add(printLabel);
        extraMethods.add(new StackInstruction(PUSH, lr));

        if (type.equalsType(INT)) {
            Label formatSpecifier = data.getFormatSpecifier(INT);
            extraMethods.add(new DataProcessingInstruction<>(MOV, r1, r0));
            extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
        }

        if (type.equalsType(STRING)) {
            Label formatSpecifier = data.getFormatSpecifier(STRING);
            extraMethods.add(new SingleDataTransferInstruction<>(LDR, r1, r0));
            extraMethods.add(new DataProcessingInstruction<>(ADD, r2, r0, 4));
            extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
        }

        if (type.equalsType(BOOL)) {
            extraMethods.add(new DataProcessingInstruction<>(CMP, r0, 0));
            Label trueLabel = data.getMessageLocation(new Identifier(BOOL, "true\\0"));
            extraMethods.add(new SingleDataTransferInstruction<>(LDRNE, r0, trueLabel));
            Label falseLabel = data.getMessageLocation(new Identifier(BOOL, "false\\0"));
            extraMethods.add(new SingleDataTransferInstruction<>(LDREQ, r0, falseLabel));
        }

        if (type.equalsType(NULL)) {
            extraMethods.add(new DataProcessingInstruction<>(MOV, r1, r0));
            Label refLabel = data.getFormatSpecifier(type);
            extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, refLabel));
        }

        extraMethods.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        extraMethods.add(new BranchInstruction(BL, new Label("printf")));
        extraMethods.add(new DataProcessingInstruction<>(MOV, r0, 0));
        extraMethods.add(new BranchInstruction(BL, new Label("fflush")));
        extraMethods.add(new StackInstruction(POP, pc));

        methodLabels.add(printLabel);
    }

    public void generateRead(Label readLabel, Label formatSpecifier) {
        if (methodLabels.contains(readLabel)) {
            return;
        }
        extraMethods.add(readLabel);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new DataProcessingInstruction<>(MOV, r1, r0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
        extraMethods.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        extraMethods.add(new BranchInstruction(BL, new Label("scanf")));
        extraMethods.add(new StackInstruction(POP, pc));

        methodLabels.add(readLabel);
    }

    public Deque<Instruction> getExtraMethods() {
        return extraMethods;
    }

    public void freePair(Label freeLabel, Label freeErrorMsg) {
        if (methodLabels.contains(freeLabel)) {
            return;
        }
        extraMethods.add(freeLabel);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new DataProcessingInstruction<>(CMP, r0, 0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDREQ, r0, freeErrorMsg));
        extraMethods.add(new BranchInstruction(BEQ, runtimeError));
        extraMethods.add(new StackInstruction(PUSH, r0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, r0));
        extraMethods.add(new BranchInstruction(BL, new Label("free")));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, sp));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0,
                new ShiftRegister(R0, 4, null)));
        extraMethods.add(new BranchInstruction(BL, new Label("free")));
        extraMethods.add(new StackInstruction(POP, r0));
        extraMethods.add(new BranchInstruction(BL, new Label("free")));
        extraMethods.add(new StackInstruction(POP, pc));

        generateRuntimeError(runtimeError);

        methodLabels.add(freeLabel);
    }

    public void checkNullPointer(Label checkNullPointer, Label nullDeferenceMsg) {
        if (methodLabels.contains(checkNullPointer)) {
            return;
        }

        extraMethods.add(checkNullPointer);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new DataProcessingInstruction<>(CMP, r0, 0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDREQ, r0, nullDeferenceMsg));
        extraMethods.add(new BranchInstruction(BLEQ, runtimeError));
        extraMethods.add(new StackInstruction(POP, pc));

        generateRuntimeError(runtimeError);

        methodLabels.add(checkNullPointer);
    }

    public void throwOverflow(Label overflow, Label overflowMsg) {
        if (methodLabels.contains(overflow)) {
            return;
        }
        extraMethods.add(overflow);
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, overflowMsg));
        extraMethods.add(new BranchInstruction(BL, runtimeError));

        generateRuntimeError(runtimeError);

        methodLabels.add(overflow);
    }

    public void checkArrayBounds(Label arrayBounds, Label negIndex, Label largeIndex) {
        if (methodLabels.contains(arrayBounds)) {
            return;
        }
        extraMethods.add(arrayBounds);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new DataProcessingInstruction<>(CMP, r0, 0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDRLT, r0, negIndex));
        extraMethods.add(new BranchInstruction(BLLT, runtimeError));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r1, r1));
        extraMethods.add(new DataProcessingInstruction<>(CMP, r0, r1));
        extraMethods.add(new SingleDataTransferInstruction<>(LDRCS, r0, largeIndex));
        extraMethods.add(new BranchInstruction(BLCS, runtimeError));
        extraMethods.add(new StackInstruction(POP, pc));

        generateRuntimeError(runtimeError);

        methodLabels.add(arrayBounds);
    }

    public void checkDivByZero(Label divByZero, Label divByZeroMsg) {
        if (methodLabels.contains(divByZero)) {
            return;
        }
        extraMethods.add(divByZero);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new DataProcessingInstruction<>(CMP, r1, 0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDREQ, r0, divByZeroMsg));
        extraMethods.add(new BranchInstruction(BLEQ, runtimeError));
        extraMethods.add(new StackInstruction(POP, pc));

        generateRuntimeError(runtimeError);

        methodLabels.add(divByZero);
    }

    private void generateRuntimeError(Label runtimeErrorLabel) {
        if (methodLabels.contains(runtimeErrorLabel)) {
            return;
        }
        extraMethods.add(runtimeErrorLabel);
        extraMethods.add(new BranchInstruction(BL, new Label("p_print_string")));
        extraMethods.add(new DataProcessingInstruction<>(MOV, r0, -1));
        extraMethods.add(new BranchInstruction(BL, new Label("exit")));

        generatePrint(STRING, new Label("p_print_string"));

        methodLabels.add(runtimeErrorLabel);
    }

}
