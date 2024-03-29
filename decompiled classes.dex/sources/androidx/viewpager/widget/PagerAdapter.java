package androidx.viewpager.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerAdapter {
    public static final int POSITION_NONE = -2;
    public static final int POSITION_UNCHANGED = -1;
    private final DataSetObservable mObservable = new DataSetObservable();
    private DataSetObserver mViewPagerObserver;

    @Deprecated
    public void destroyItem(View container, int position, Object object) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        destroyItem((View) container, position, object);
    }

    @Deprecated
    public void finishUpdate(View container) {
    }

    public void finishUpdate(ViewGroup container) {
        finishUpdate((View) container);
    }

    public abstract int getCount();

    public int getItemPosition(Object object) {
        return -1;
    }

    public CharSequence getPageTitle(int position) {
        return null;
    }

    public float getPageWidth(int position) {
        return 1.0f;
    }

    @Deprecated
    public Object instantiateItem(View container, int position) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }

    public Object instantiateItem(ViewGroup container, int position) {
        return instantiateItem((View) container, position);
    }

    public abstract boolean isViewFromObject(View view, Object obj);

    public void notifyDataSetChanged() {
        synchronized (this) {
            DataSetObserver dataSetObserver = this.mViewPagerObserver;
            if (dataSetObserver != null) {
                dataSetObserver.onChanged();
            }
        }
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public Parcelable saveState() {
        return null;
    }

    @Deprecated
    public void setPrimaryItem(View container, int position, Object object) {
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        setPrimaryItem((View) container, position, object);
    }

    /* access modifiers changed from: package-private */
    public void setViewPagerObserver(DataSetObserver observer) {
        synchronized (this) {
            this.mViewPagerObserver = observer;
        }
    }

    @Deprecated
    public void startUpdate(View container) {
    }

    public void startUpdate(ViewGroup container) {
        startUpdate((View) container);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }
}
