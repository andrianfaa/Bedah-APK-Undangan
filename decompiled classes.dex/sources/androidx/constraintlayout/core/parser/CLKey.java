package androidx.constraintlayout.core.parser;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.util.ArrayList;

public class CLKey extends CLContainer {
    private static ArrayList<String> sections;

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        sections = arrayList;
        arrayList.add("ConstraintSets");
        sections.add("Variables");
        sections.add("Generate");
        sections.add(TypedValues.TransitionType.NAME);
        sections.add("KeyFrames");
        sections.add(TypedValues.AttributesType.NAME);
        sections.add("KeyPositions");
        sections.add("KeyCycles");
    }

    public CLKey(char[] content) {
        super(content);
    }

    public static CLElement allocate(String name, CLElement value) {
        CLKey cLKey = new CLKey(name.toCharArray());
        cLKey.setStart(0);
        cLKey.setEnd((long) (name.length() - 1));
        cLKey.set(value);
        return cLKey;
    }

    public static CLElement allocate(char[] content) {
        return new CLKey(content);
    }

    public String getName() {
        return content();
    }

    public CLElement getValue() {
        if (this.mElements.size() > 0) {
            return (CLElement) this.mElements.get(0);
        }
        return null;
    }

    public void set(CLElement value) {
        if (this.mElements.size() > 0) {
            this.mElements.set(0, value);
        } else {
            this.mElements.add(value);
        }
    }

    /* access modifiers changed from: protected */
    public String toFormattedJSON(int indent, int forceIndent) {
        StringBuilder sb = new StringBuilder(getDebugName());
        addIndent(sb, indent);
        String content = content();
        if (this.mElements.size() <= 0) {
            return content + ": <> ";
        }
        sb.append(content);
        sb.append(": ");
        if (sections.contains(content)) {
            forceIndent = 3;
        }
        if (forceIndent > 0) {
            sb.append(((CLElement) this.mElements.get(0)).toFormattedJSON(indent, forceIndent - 1));
        } else {
            String json = ((CLElement) this.mElements.get(0)).toJSON();
            if (json.length() + indent < MAX_LINE) {
                sb.append(json);
            } else {
                sb.append(((CLElement) this.mElements.get(0)).toFormattedJSON(indent, forceIndent - 1));
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String toJSON() {
        return this.mElements.size() > 0 ? getDebugName() + content() + ": " + ((CLElement) this.mElements.get(0)).toJSON() : getDebugName() + content() + ": <> ";
    }
}
