package androidx.work;

public abstract class InputMergerFactory {
    public static InputMergerFactory getDefaultInputMergerFactory() {
        return new InputMergerFactory() {
            public InputMerger createInputMerger(String className) {
                return null;
            }
        };
    }

    public abstract InputMerger createInputMerger(String str);

    public final InputMerger createInputMergerWithDefaultFallback(String className) {
        InputMerger createInputMerger = createInputMerger(className);
        return createInputMerger == null ? InputMerger.fromClassName(className) : createInputMerger;
    }
}
