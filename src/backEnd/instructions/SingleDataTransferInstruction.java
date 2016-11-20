package backEnd.instructions;

import backEnd.Register;

public class SingleDataTransferInstruction implements Instruction {

    SingleDataTransferType type;
    Register destination;
    //Address? address : register, =expression (3, msg_3 i think), [reg, #expression] (!)


    public SingleDataTransferInstruction
            (SingleDataTransferType type, Register destination) {

        this.type = type;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return type + " " + destination + ", " ; //+ addresss
    }
}
