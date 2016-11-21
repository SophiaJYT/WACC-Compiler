package backEnd.instructions;

import backEnd.Register;

public class SingleDataTransferInstruction<T> implements Instruction {

    SingleDataTransferType type;
    Register destination;
    T address;
    //Address? address : register, =expression (3, msg_3 i think), [reg, #expression] (!)


    public SingleDataTransferInstruction
            (SingleDataTransferType type, Register destination, T address) {

        if(address instanceof Integer || address instanceof Label || address instanceof Register) {
            this.type = type;
            this.destination = destination;
            this.address = address;
        }
    }

    @Override
    public String toString() {
        String strAddress = address.toString();
        if (address instanceof Integer || address instanceof Label) {
            strAddress = "=" + address;
        }
        if (address instanceof Register) {
            strAddress = "[" + address + "]";
        }

        return type + " " + destination + ", " + strAddress;
    }
}

