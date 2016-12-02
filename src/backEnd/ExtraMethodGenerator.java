package backEnd;

import backEnd.instructions.*;
import frontEnd.Identifier;
import frontEnd.Type;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Dictionary;
import java.util.Hashtable;

import static backEnd.RegisterType.*;
import static backEnd.instructions.BranchType.*;
import static backEnd.instructions.DataProcessingType.*;
import static backEnd.instructions.SingleDataTransferType.*;
import static backEnd.instructions.StackType.*;
import static frontEnd.AllTypes.*;

public class ExtraMethodGenerator {

    private Deque<Instruction> extraMethods = new ArrayDeque<>();
    private Dictionary<Type, Label> methodLabels = new Hashtable<>();

    private Register r0 = new Register(R0);
    private Register r1 = new Register(R1);
    private Register r2 = new Register(R2);
    private Register r3 = new Register(R3);
    private Register pc = new Register(PC);
    private Register lr = new Register(LR);

    public void generatePrintLn(Label printLn, Label newLineLabel) {
        // Check if println label exists
        if (methodLabels.get(ANY) != null) {
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
        methodLabels.put(ANY, printLn);
    }

    public Data generatePrint(Type type, Label printLabel, Data data) {
        // Check if the print label already exists
        if (methodLabels.get(type) != null) {
            return data;
        }

        methodLabels.put(type, printLabel);
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

        return data;
    }

    public void generateRead(Label readLabel, Label formatSpecifier) {
        extraMethods.add(readLabel);
        extraMethods.add(new StackInstruction(PUSH, lr));
        extraMethods.add(new DataProcessingInstruction<>(MOV, r1, r0));
        extraMethods.add(new SingleDataTransferInstruction<>(LDR, r0, formatSpecifier));
        extraMethods.add(new DataProcessingInstruction<>(ADD, r0, r0, 4));
        extraMethods.add(new BranchInstruction(BL, new Label("scanf")));
        extraMethods.add(new StackInstruction(POP, pc));
    }

    public Deque<Instruction> getExtraMethods() {
        return extraMethods;
    }

}
