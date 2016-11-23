package backEnd;

import backEnd.instructions.Directive;
import backEnd.instructions.Instruction;
import backEnd.instructions.Label;
import frontEnd.AllTypes;
import frontEnd.Identifier;
import frontEnd.Type;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Dictionary;
import java.util.Hashtable;

public class Data {

    private Deque<Instruction> messages;
    private Dictionary<String, Label> messageLocations;
    private Dictionary<Type, Label> formatSpecifiers;
    private int messageIndex;
    private boolean hasStringMessage, hasIntMessage;

    public Data() {
        messages = new ArrayDeque<>();
        messageLocations = new Hashtable<>();
        formatSpecifiers = new Hashtable<>();
        messageIndex = 0;
    }

    public Label getFormatSpecifier(Type type) {
        return formatSpecifiers.get(type);
    }

    private Label addMessage(Identifier ident) {
        if (isEmpty()) {
            messages.add(new Directive("data"));
        }

        Type exprType = ident.getType();

        if (exprType.equalsType(AllTypes.INT) && !hasIntMessage) {
            hasIntMessage = true;
            addFormatSpecifiers();
            return null;
        }

        Label msg = new Label("msg_", messageIndex++, false);
        String liter = ident.getVal();

        if (exprType.equalsType(AllTypes.STRING)) {
            int size = liter.length();
            if (liter.equals("\\0")) {
                size--;
            }
            messages.add(msg);
            messages.add(new Directive("word " + size));
            messages.add(new Directive("ascii \"" + liter + "\""));

            if (!hasStringMessage) {
                hasStringMessage = true;
            }
        }

        if (exprType.equalsType(AllTypes.BOOL)) {
            messages.add(msg);
            messages.add(new Directive("word " + (liter.length() - 1)));
            messages.add(new Directive("ascii \"" + liter + "\""));
        }

        messageLocations.put(liter, msg);

        addFormatSpecifiers();

        return msg;
    }

    public Label getMessageLocation(Identifier ident) {
        Label result = messageLocations.get(ident.getVal());
        if (result != null) {
            return result;
        } else {
            return addMessage(ident);
        }
    }

    private void addFormatSpecifier(Type type) {
        Label msg = new Label("msg_", messageIndex++, false);
        messages.add(msg);

        if (type.equalsType(AllTypes.INT)) {
            String intFormat = "%d\\0";
            messages.add(new Directive("word " + (intFormat.length() - 1)));
            messages.add(new Directive("ascii \"" + intFormat + "\""));
            formatSpecifiers.put(AllTypes.INT, msg);
        }

        if (type.equalsType(AllTypes.STRING)) {
            String strFormat = "%.*s\\0";
            messages.add(new Directive("word " + (strFormat.length() - 1)));
            messages.add(new Directive("ascii \"" + strFormat + "\""));
            formatSpecifiers.put(AllTypes.STRING, msg);
        }
    }

    private void addFormatSpecifiers() {
        if (hasStringMessage && getFormatSpecifier(AllTypes.STRING) == null) {
            addFormatSpecifier(AllTypes.STRING);
        }
        if (hasIntMessage && getFormatSpecifier(AllTypes.INT) == null) {
            addFormatSpecifier(AllTypes.INT);
        }
    }

    public Deque<Instruction> getData() {
        return messages;
    }

    public boolean isEmpty() {
        return messages.size() == 0;
    }

}
