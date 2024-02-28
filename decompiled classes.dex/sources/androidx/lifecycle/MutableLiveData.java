package androidx.lifecycle;

public class MutableLiveData<T> extends LiveData<T> {
    public MutableLiveData() {
    }

    public MutableLiveData(T t) {
        super(t);
    }

    public void postValue(T t) {
        super.postValue(t);
    }

    public void setValue(T t) {
        super.setValue(t);
    }
}
