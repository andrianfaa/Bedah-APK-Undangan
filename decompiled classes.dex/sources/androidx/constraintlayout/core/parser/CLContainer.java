package androidx.constraintlayout.core.parser;

import java.util.ArrayList;
import java.util.Iterator;

public class CLContainer extends CLElement {
    ArrayList<CLElement> mElements = new ArrayList<>();

    public CLContainer(char[] content) {
        super(content);
    }

    public static CLElement allocate(char[] content) {
        return new CLContainer(content);
    }

    public void add(CLElement element) {
        this.mElements.add(element);
        if (CLParser.DEBUG) {
            System.out.println("added element " + element + " to " + this);
        }
    }

    public CLElement get(int index) throws CLParsingException {
        if (index >= 0 && index < this.mElements.size()) {
            return this.mElements.get(index);
        }
        throw new CLParsingException("no element at index " + index, this);
    }

    public CLElement get(String name) throws CLParsingException {
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLKey cLKey = (CLKey) it.next();
            if (cLKey.content().equals(name)) {
                return cLKey.getValue();
            }
        }
        throw new CLParsingException("no element for key <" + name + ">", this);
    }

    public CLArray getArray(int index) throws CLParsingException {
        CLElement cLElement = get(index);
        if (cLElement instanceof CLArray) {
            return (CLArray) cLElement;
        }
        throw new CLParsingException("no array at index " + index, this);
    }

    public CLArray getArray(String name) throws CLParsingException {
        CLElement cLElement = get(name);
        if (cLElement instanceof CLArray) {
            return (CLArray) cLElement;
        }
        throw new CLParsingException("no array found for key <" + name + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
    }

    public CLArray getArrayOrNull(String name) {
        CLElement orNull = getOrNull(name);
        if (orNull instanceof CLArray) {
            return (CLArray) orNull;
        }
        return null;
    }

    public boolean getBoolean(int index) throws CLParsingException {
        CLElement cLElement = get(index);
        if (cLElement instanceof CLToken) {
            return ((CLToken) cLElement).getBoolean();
        }
        throw new CLParsingException("no boolean at index " + index, this);
    }

    public boolean getBoolean(String name) throws CLParsingException {
        CLElement cLElement = get(name);
        if (cLElement instanceof CLToken) {
            return ((CLToken) cLElement).getBoolean();
        }
        throw new CLParsingException("no boolean found for key <" + name + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
    }

    public float getFloat(int index) throws CLParsingException {
        CLElement cLElement = get(index);
        if (cLElement != null) {
            return cLElement.getFloat();
        }
        throw new CLParsingException("no float at index " + index, this);
    }

    public float getFloat(String name) throws CLParsingException {
        CLElement cLElement = get(name);
        if (cLElement != null) {
            return cLElement.getFloat();
        }
        throw new CLParsingException("no float found for key <" + name + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
    }

    public float getFloatOrNaN(String name) {
        CLElement orNull = getOrNull(name);
        if (orNull instanceof CLNumber) {
            return orNull.getFloat();
        }
        return Float.NaN;
    }

    public int getInt(int index) throws CLParsingException {
        CLElement cLElement = get(index);
        if (cLElement != null) {
            return cLElement.getInt();
        }
        throw new CLParsingException("no int at index " + index, this);
    }

    public int getInt(String name) throws CLParsingException {
        CLElement cLElement = get(name);
        if (cLElement != null) {
            return cLElement.getInt();
        }
        throw new CLParsingException("no int found for key <" + name + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
    }

    public CLObject getObject(int index) throws CLParsingException {
        CLElement cLElement = get(index);
        if (cLElement instanceof CLObject) {
            return (CLObject) cLElement;
        }
        throw new CLParsingException("no object at index " + index, this);
    }

    public CLObject getObject(String name) throws CLParsingException {
        CLElement cLElement = get(name);
        if (cLElement instanceof CLObject) {
            return (CLObject) cLElement;
        }
        throw new CLParsingException("no object found for key <" + name + ">, found [" + cLElement.getStrClass() + "] : " + cLElement, this);
    }

    public CLObject getObjectOrNull(String name) {
        CLElement orNull = getOrNull(name);
        if (orNull instanceof CLObject) {
            return (CLObject) orNull;
        }
        return null;
    }

    public CLElement getOrNull(int index) {
        if (index < 0 || index >= this.mElements.size()) {
            return null;
        }
        return this.mElements.get(index);
    }

    public CLElement getOrNull(String name) {
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLKey cLKey = (CLKey) it.next();
            if (cLKey.content().equals(name)) {
                return cLKey.getValue();
            }
        }
        return null;
    }

    public String getString(int index) throws CLParsingException {
        CLElement cLElement = get(index);
        if (cLElement instanceof CLString) {
            return cLElement.content();
        }
        throw new CLParsingException("no string at index " + index, this);
    }

    public String getString(String name) throws CLParsingException {
        CLElement cLElement = get(name);
        if (cLElement instanceof CLString) {
            return cLElement.content();
        }
        String str = null;
        if (cLElement != null) {
            str = cLElement.getStrClass();
        }
        throw new CLParsingException("no string found for key <" + name + ">, found [" + str + "] : " + cLElement, this);
    }

    public String getStringOrNull(int index) {
        CLElement orNull = getOrNull(index);
        if (orNull instanceof CLString) {
            return orNull.content();
        }
        return null;
    }

    public String getStringOrNull(String name) {
        CLElement orNull = getOrNull(name);
        if (orNull instanceof CLString) {
            return orNull.content();
        }
        return null;
    }

    public boolean has(String name) {
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLElement next = it.next();
            if ((next instanceof CLKey) && ((CLKey) next).content().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> names() {
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLElement next = it.next();
            if (next instanceof CLKey) {
                arrayList.add(((CLKey) next).content());
            }
        }
        return arrayList;
    }

    public void put(String name, CLElement value) {
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLKey cLKey = (CLKey) it.next();
            if (cLKey.content().equals(name)) {
                cLKey.set(value);
                return;
            }
        }
        this.mElements.add((CLKey) CLKey.allocate(name, value));
    }

    public void putNumber(String name, float value) {
        put(name, new CLNumber(value));
    }

    public void remove(String name) {
        ArrayList arrayList = new ArrayList();
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLElement next = it.next();
            if (((CLKey) next).content().equals(name)) {
                arrayList.add(next);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.mElements.remove((CLElement) it2.next());
        }
    }

    public int size() {
        return this.mElements.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<CLElement> it = this.mElements.iterator();
        while (it.hasNext()) {
            CLElement next = it.next();
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(next);
        }
        return super.toString() + " = <" + sb + " >";
    }
}
