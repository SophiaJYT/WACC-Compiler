package backEnd.instructions;

import backEnd.Register;
import backEnd.ShiftRegister;

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
        if (address instanceof Integer) {
            strAddress = "=" + address;
        }
        if (address instanceof Label) {
            strAddress = "=" + ((Label) address).getName();
        }
        if (address instanceof ShiftRegister) {
            Character charVal = ((ShiftRegister) address).getExclamation();
            strAddress = "[" + address + "]" + ((charVal == null) ? "" : "" + charVal);
            return type + " " + destination + ", " + strAddress;
        }
        if (address instanceof Register) {
            strAddress = "[" + address + "]";
        }

        return type + " " + destination + ", " + strAddress;
    }
}

