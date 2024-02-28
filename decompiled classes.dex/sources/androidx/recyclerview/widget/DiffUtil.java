package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mt.Log1F380D;

public class DiffUtil {
    private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>() {
        public int compare(Snake o1, Snake o2) {
            int i = o1.x - o2.x;
            return i == 0 ? o1.y - o2.y : i;
        }
    };

    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int i, int i2);

        public abstract boolean areItemsTheSame(int i, int i2);

        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return null;
        }

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    /* compiled from: 008C */
    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        public static final int NO_POSITION = -1;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;

        DiffResult(Callback callback, List<Snake> list, int[] oldItemStatuses, int[] newItemStatuses, boolean detectMoves) {
            this.mSnakes = list;
            this.mOldItemStatuses = oldItemStatuses;
            this.mNewItemStatuses = newItemStatuses;
            Arrays.fill(oldItemStatuses, 0);
            Arrays.fill(newItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = detectMoves;
            addRootSnake();
            findMatchingItems();
        }

        private void addRootSnake() {
            Snake snake = this.mSnakes.isEmpty() ? null : this.mSnakes.get(0);
            if (snake == null || snake.x != 0 || snake.y != 0) {
                Snake snake2 = new Snake();
                snake2.x = 0;
                snake2.y = 0;
                snake2.removal = false;
                snake2.size = 0;
                snake2.reverse = false;
                this.mSnakes.add(0, snake2);
            }
        }

        private void dispatchAdditions(List<PostponedUpdate> list, ListUpdateCallback updateCallback, int start, int count, int globalIndex) {
            if (!this.mDetectMoves) {
                updateCallback.onInserted(start, count);
                return;
            }
            for (int i = count - 1; i >= 0; i--) {
                int[] iArr = this.mNewItemStatuses;
                int i2 = iArr[globalIndex + i] & 31;
                switch (i2) {
                    case 0:
                        updateCallback.onInserted(start, 1);
                        for (PostponedUpdate postponedUpdate : list) {
                            postponedUpdate.currentPos++;
                        }
                        break;
                    case 4:
                    case 8:
                        int i3 = iArr[globalIndex + i] >> 5;
                        updateCallback.onMoved(removePostponedUpdate(list, i3, true).currentPos, start);
                        if (i2 != 4) {
                            break;
                        } else {
                            updateCallback.onChanged(start, 1, this.mCallback.getChangePayload(i3, globalIndex + i));
                            break;
                        }
                    case 16:
                        list.add(new PostponedUpdate(globalIndex + i, start, false));
                        break;
                    default:
                        StringBuilder append = new StringBuilder().append("unknown flag for pos ").append(globalIndex + i).append(" ");
                        String binaryString = Long.toBinaryString((long) i2);
                        Log1F380D.a((Object) binaryString);
                        throw new IllegalStateException(append.append(binaryString).toString());
                }
            }
        }

        private void findAddition(int x, int y, int snakeIndex) {
            if (this.mOldItemStatuses[x - 1] == 0) {
                findMatchingItem(x, y, snakeIndex, false);
            }
        }

        private boolean findMatchingItem(int x, int y, int snakeIndex, boolean removal) {
            int i;
            int i2;
            int i3;
            if (removal) {
                i3 = y - 1;
                i2 = x;
                i = y - 1;
            } else {
                i3 = x - 1;
                i2 = x - 1;
                i = y;
            }
            for (int i4 = snakeIndex; i4 >= 0; i4--) {
                Snake snake = this.mSnakes.get(i4);
                int i5 = snake.x + snake.size;
                int i6 = snake.y + snake.size;
                int i7 = 8;
                if (removal) {
                    for (int i8 = i2 - 1; i8 >= i5; i8--) {
                        if (this.mCallback.areItemsTheSame(i8, i3)) {
                            if (!this.mCallback.areContentsTheSame(i8, i3)) {
                                i7 = 4;
                            }
                            this.mNewItemStatuses[i3] = (i8 << 5) | 16;
                            this.mOldItemStatuses[i8] = (i3 << 5) | i7;
                            return true;
                        }
                    }
                    continue;
                } else {
                    for (int i9 = i - 1; i9 >= i6; i9--) {
                        if (this.mCallback.areItemsTheSame(i3, i9)) {
                            if (!this.mCallback.areContentsTheSame(i3, i9)) {
                                i7 = 4;
                            }
                            this.mOldItemStatuses[x - 1] = (i9 << 5) | 16;
                            this.mNewItemStatuses[i9] = ((x - 1) << 5) | i7;
                            return true;
                        }
                    }
                    continue;
                }
                i2 = snake.x;
                i = snake.y;
            }
            return false;
        }

        private void findMatchingItems() {
            int i = this.mOldListSize;
            int i2 = this.mNewListSize;
            for (int size = this.mSnakes.size() - 1; size >= 0; size--) {
                Snake snake = this.mSnakes.get(size);
                int i3 = snake.x + snake.size;
                int i4 = snake.y + snake.size;
                if (this.mDetectMoves) {
                    while (i > i3) {
                        findAddition(i, i2, size);
                        i--;
                    }
                    while (i2 > i4) {
                        findRemoval(i, i2, size);
                        i2--;
                    }
                }
                for (int i5 = 0; i5 < snake.size; i5++) {
                    int i6 = snake.x + i5;
                    int i7 = snake.y + i5;
                    int i8 = this.mCallback.areContentsTheSame(i6, i7) ? 1 : 2;
                    this.mOldItemStatuses[i6] = (i7 << 5) | i8;
                    this.mNewItemStatuses[i7] = (i6 << 5) | i8;
                }
                i = snake.x;
                i2 = snake.y;
            }
        }

        private void findRemoval(int x, int y, int snakeIndex) {
            if (this.mNewItemStatuses[y - 1] == 0) {
                findMatchingItem(x, y, snakeIndex, true);
            }
        }

        private static PostponedUpdate removePostponedUpdate(List<PostponedUpdate> list, int pos, boolean removal) {
            for (int size = list.size() - 1; size >= 0; size--) {
                PostponedUpdate postponedUpdate = list.get(size);
                if (postponedUpdate.posInOwnerList == pos && postponedUpdate.removal == removal) {
                    list.remove(size);
                    for (int i = size; i < list.size(); i++) {
                        list.get(i).currentPos += removal ? 1 : -1;
                    }
                    return postponedUpdate;
                }
            }
            return null;
        }

        public int convertNewPositionToOld(int newListPosition) {
            if (newListPosition < 0 || newListPosition >= this.mNewListSize) {
                throw new IndexOutOfBoundsException("Index out of bounds - passed position = " + newListPosition + ", new list size = " + this.mNewListSize);
            }
            int i = this.mNewItemStatuses[newListPosition];
            if ((i & 31) == 0) {
                return -1;
            }
            return i >> 5;
        }

        public int convertOldPositionToNew(int oldListPosition) {
            if (oldListPosition < 0 || oldListPosition >= this.mOldListSize) {
                throw new IndexOutOfBoundsException("Index out of bounds - passed position = " + oldListPosition + ", old list size = " + this.mOldListSize);
            }
            int i = this.mOldItemStatuses[oldListPosition];
            if ((i & 31) == 0) {
                return -1;
            }
            return i >> 5;
        }

        public void dispatchUpdatesTo(ListUpdateCallback updateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback;
            int i;
            int i2;
            ListUpdateCallback listUpdateCallback = updateCallback;
            if (listUpdateCallback instanceof BatchingListUpdateCallback) {
                ListUpdateCallback listUpdateCallback2 = listUpdateCallback;
                batchingListUpdateCallback = (BatchingListUpdateCallback) listUpdateCallback;
            } else {
                BatchingListUpdateCallback batchingListUpdateCallback2 = new BatchingListUpdateCallback(listUpdateCallback);
                BatchingListUpdateCallback batchingListUpdateCallback3 = batchingListUpdateCallback2;
                batchingListUpdateCallback = batchingListUpdateCallback2;
            }
            ArrayList arrayList = new ArrayList();
            int i3 = this.mOldListSize;
            int i4 = this.mNewListSize;
            for (int size = this.mSnakes.size() - 1; size >= 0; size--) {
                Snake snake = this.mSnakes.get(size);
                int i5 = snake.size;
                int i6 = snake.x + i5;
                int i7 = snake.y + i5;
                if (i6 < i3) {
                    i = i7;
                    dispatchRemovals(arrayList, batchingListUpdateCallback, i6, i3 - i6, i6);
                } else {
                    i = i7;
                }
                if (i < i4) {
                    int i8 = i6;
                    i2 = i5;
                    dispatchAdditions(arrayList, batchingListUpdateCallback, i6, i4 - i, i);
                } else {
                    int i9 = i6;
                    i2 = i5;
                }
                for (int i10 = i2 - 1; i10 >= 0; i10--) {
                    if ((this.mOldItemStatuses[snake.x + i10] & 31) == 2) {
                        batchingListUpdateCallback.onChanged(snake.x + i10, 1, this.mCallback.getChangePayload(snake.x + i10, snake.y + i10));
                    }
                }
                i3 = snake.x;
                i4 = snake.y;
            }
            batchingListUpdateCallback.dispatchLastEvent();
        }

        public void dispatchUpdatesTo(RecyclerView.Adapter adapter) {
            dispatchUpdatesTo((ListUpdateCallback) new AdapterListUpdateCallback(adapter));
        }

        /* access modifiers changed from: package-private */
        public List<Snake> getSnakes() {
            return this.mSnakes;
        }

        private void dispatchRemovals(List<PostponedUpdate> list, ListUpdateCallback updateCallback, int start, int count, int globalIndex) {
            if (!this.mDetectMoves) {
                updateCallback.onRemoved(start, count);
                return;
            }
            for (int i = count - 1; i >= 0; i--) {
                int[] iArr = this.mOldItemStatuses;
                int i2 = iArr[globalIndex + i] & 31;
                switch (i2) {
                    case 0:
                        updateCallback.onRemoved(start + i, 1);
                        for (PostponedUpdate postponedUpdate : list) {
                            postponedUpdate.currentPos--;
                        }
                        break;
                    case 4:
                    case 8:
                        int i3 = iArr[globalIndex + i] >> 5;
                        PostponedUpdate removePostponedUpdate = removePostponedUpdate(list, i3, false);
                        updateCallback.onMoved(start + i, removePostponedUpdate.currentPos - 1);
                        if (i2 != 4) {
                            break;
                        } else {
                            updateCallback.onChanged(removePostponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(globalIndex + i, i3));
                            break;
                        }
                    case 16:
                        list.add(new PostponedUpdate(globalIndex + i, start + i, true));
                        break;
                    default:
                        StringBuilder append = new StringBuilder().append("unknown flag for pos ").append(globalIndex + i).append(" ");
                        String binaryString = Long.toBinaryString((long) i2);
                        Log1F380D.a((Object) binaryString);
                        throw new IllegalStateException(append.append(binaryString).toString());
                }
            }
        }
    }

    public static abstract class ItemCallback<T> {
        public abstract boolean areContentsTheSame(T t, T t2);

        public abstract boolean areItemsTheSame(T t, T t2);

        public Object getChangePayload(T t, T t2) {
            return null;
        }
    }

    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int posInOwnerList2, int currentPos2, boolean removal2) {
            this.posInOwnerList = posInOwnerList2;
            this.currentPos = currentPos2;
            this.removal = removal2;
        }
    }

    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int oldListStart2, int oldListEnd2, int newListStart2, int newListEnd2) {
            this.oldListStart = oldListStart2;
            this.oldListEnd = oldListEnd2;
            this.newListStart = newListStart2;
            this.newListEnd = newListEnd2;
        }
    }

    static class Snake {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;

        Snake() {
        }
    }

    private DiffUtil() {
    }

    public static DiffResult calculateDiff(Callback cb) {
        return calculateDiff(cb, true);
    }

    public static DiffResult calculateDiff(Callback cb, boolean detectMoves) {
        int oldListSize = cb.getOldListSize();
        int newListSize = cb.getNewListSize();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new Range(0, oldListSize, 0, newListSize));
        int abs = oldListSize + newListSize + Math.abs(oldListSize - newListSize);
        int[] iArr = new int[(abs * 2)];
        int[] iArr2 = new int[(abs * 2)];
        ArrayList arrayList3 = new ArrayList();
        while (!arrayList2.isEmpty()) {
            Range range = (Range) arrayList2.remove(arrayList2.size() - 1);
            Snake diffPartial = diffPartial(cb, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, iArr, iArr2, abs);
            if (diffPartial != null) {
                if (diffPartial.size > 0) {
                    arrayList.add(diffPartial);
                }
                diffPartial.x += range.oldListStart;
                diffPartial.y += range.newListStart;
                Range range2 = arrayList3.isEmpty() ? new Range() : (Range) arrayList3.remove(arrayList3.size() - 1);
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (diffPartial.reverse) {
                    range2.oldListEnd = diffPartial.x;
                    range2.newListEnd = diffPartial.y;
                } else if (diffPartial.removal) {
                    range2.oldListEnd = diffPartial.x - 1;
                    range2.newListEnd = diffPartial.y;
                } else {
                    range2.oldListEnd = diffPartial.x;
                    range2.newListEnd = diffPartial.y - 1;
                }
                arrayList2.add(range2);
                Range range3 = range;
                if (!diffPartial.reverse) {
                    range3.oldListStart = diffPartial.x + diffPartial.size;
                    range3.newListStart = diffPartial.y + diffPartial.size;
                } else if (diffPartial.removal) {
                    range3.oldListStart = diffPartial.x + diffPartial.size + 1;
                    range3.newListStart = diffPartial.y + diffPartial.size;
                } else {
                    range3.oldListStart = diffPartial.x + diffPartial.size;
                    range3.newListStart = diffPartial.y + diffPartial.size + 1;
                }
                arrayList2.add(range3);
            } else {
                arrayList3.add(range);
            }
        }
        Collections.sort(arrayList, SNAKE_COMPARATOR);
        ArrayList arrayList4 = arrayList3;
        int[] iArr3 = iArr2;
        int[] iArr4 = iArr;
        return new DiffResult(cb, arrayList, iArr, iArr2, detectMoves);
    }

    private static Snake diffPartial(Callback cb, int startOld, int endOld, int startNew, int endNew, int[] forward, int[] backward, int kOffset) {
        boolean z;
        int i;
        int i2;
        boolean z2;
        int i3;
        Callback callback = cb;
        int[] iArr = forward;
        int[] iArr2 = backward;
        int i4 = endOld - startOld;
        int i5 = endNew - startNew;
        if (endOld - startOld < 1) {
            int i6 = i4;
            return null;
        } else if (endNew - startNew < 1) {
            int i7 = i4;
            return null;
        } else {
            int i8 = i4 - i5;
            int i9 = ((i4 + i5) + 1) / 2;
            Arrays.fill(iArr, (kOffset - i9) - 1, kOffset + i9 + 1, 0);
            Arrays.fill(iArr2, ((kOffset - i9) - 1) + i8, kOffset + i9 + 1 + i8, i4);
            boolean z3 = i8 % 2 != 0;
            for (int i10 = 0; i10 <= i9; i10++) {
                int i11 = -i10;
                while (i11 <= i10) {
                    if (i11 == (-i10) || (i11 != i10 && iArr[(kOffset + i11) - 1] < iArr[kOffset + i11 + 1])) {
                        i3 = iArr[kOffset + i11 + 1];
                        z2 = false;
                    } else {
                        i3 = iArr[(kOffset + i11) - 1] + 1;
                        z2 = true;
                    }
                    int i12 = i3 - i11;
                    while (i3 < i4 && i12 < i5 && callback.areItemsTheSame(startOld + i3, startNew + i12)) {
                        i3++;
                        i12++;
                    }
                    iArr[kOffset + i11] = i3;
                    if (!z3 || i11 < (i8 - i10) + 1 || i11 > (i8 + i10) - 1 || iArr[kOffset + i11] < iArr2[kOffset + i11]) {
                        i11 += 2;
                    } else {
                        Snake snake = new Snake();
                        snake.x = iArr2[kOffset + i11];
                        snake.y = snake.x - i11;
                        snake.size = iArr[kOffset + i11] - iArr2[kOffset + i11];
                        snake.removal = z2;
                        snake.reverse = false;
                        return snake;
                    }
                }
                int i13 = -i10;
                while (i13 <= i10) {
                    int i14 = i13 + i8;
                    if (i14 == i10 + i8 || (i14 != (-i10) + i8 && iArr2[(kOffset + i14) - 1] < iArr2[kOffset + i14 + 1])) {
                        i = iArr2[(kOffset + i14) - 1];
                        z = false;
                    } else {
                        i = iArr2[(kOffset + i14) + 1] - 1;
                        z = true;
                    }
                    int i15 = i - i14;
                    while (true) {
                        if (i > 0 && i15 > 0) {
                            i2 = i4;
                            if (!callback.areItemsTheSame((startOld + i) - 1, (startNew + i15) - 1)) {
                                break;
                            }
                            i--;
                            i15--;
                            i4 = i2;
                        } else {
                            i2 = i4;
                        }
                    }
                    i2 = i4;
                    iArr2[kOffset + i14] = i;
                    if (z3 || i13 + i8 < (-i10) || i13 + i8 > i10 || iArr[kOffset + i14] < iArr2[kOffset + i14]) {
                        i13 += 2;
                        i4 = i2;
                    } else {
                        Snake snake2 = new Snake();
                        snake2.x = iArr2[kOffset + i14];
                        snake2.y = snake2.x - i14;
                        snake2.size = iArr[kOffset + i14] - iArr2[kOffset + i14];
                        snake2.removal = z;
                        snake2.reverse = true;
                        return snake2;
                    }
                }
                int i16 = i4;
            }
            int i17 = i4;
            throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
        }
    }
}
