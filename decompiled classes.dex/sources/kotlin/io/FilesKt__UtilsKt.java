package kotlin.io;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import mt.Log1F380D;
import okhttp3.HttpUrl;

@Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u001a*\u0010\t\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a*\u0010\r\u001a\u00020\u00022\b\b\u0002\u0010\n\u001a\u00020\u00012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0007\u001a8\u0010\u000e\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\u001a\b\u0002\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u0013\u001a&\u0010\u0016\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0017\u001a\u00020\u0018\u001a\n\u0010\u0019\u001a\u00020\u000f*\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010\u001a\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\n\u0010\u001c\u001a\u00020\u0002*\u00020\u0002\u001a\u001d\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00020\u001d*\b\u0012\u0004\u0012\u00020\u00020\u001dH\u0002¢\u0006\u0002\b\u001e\u001a\u0011\u0010\u001c\u001a\u00020\u001f*\u00020\u001fH\u0002¢\u0006\u0002\b\u001e\u001a\u0012\u0010 \u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0014\u0010\"\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010#\u001a\u00020\u0002*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010$\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0002\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0001\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0002\u001a\u0012\u0010'\u001a\u00020\u000f*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0001\u001a\u0012\u0010(\u001a\u00020\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002\u001a\u001b\u0010)\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010!\u001a\u00020\u0002H\u0002¢\u0006\u0002\b*\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0004\"\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\b\u0010\u0004¨\u0006+"}, d2 = {"extension", "", "Ljava/io/File;", "getExtension", "(Ljava/io/File;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath", "nameWithoutExtension", "getNameWithoutExtension", "createTempDir", "prefix", "suffix", "directory", "createTempFile", "copyRecursively", "", "target", "overwrite", "onError", "Lkotlin/Function2;", "Ljava/io/IOException;", "Lkotlin/io/OnErrorAction;", "copyTo", "bufferSize", "", "deleteRecursively", "endsWith", "other", "normalize", "", "normalize$FilesKt__UtilsKt", "Lkotlin/io/FilePathComponents;", "relativeTo", "base", "relativeToOrNull", "relativeToOrSelf", "resolve", "relative", "resolveSibling", "startsWith", "toRelativeString", "toRelativeStringOrNull", "toRelativeStringOrNull$FilesKt__UtilsKt", "kotlin-stdlib"}, k = 5, mv = {1, 7, 1}, xi = 49, xs = "kotlin/io/FilesKt")
/* compiled from: 0135 */
class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public static final boolean copyRecursively(File $this$copyRecursively, File target, boolean overwrite, Function2<? super File, ? super IOException, ? extends OnErrorAction> onError) {
        Intrinsics.checkNotNullParameter($this$copyRecursively, "<this>");
        Intrinsics.checkNotNullParameter(target, TypedValues.AttributesType.S_TARGET);
        Intrinsics.checkNotNullParameter(onError, "onError");
        if (!$this$copyRecursively.exists()) {
            return onError.invoke($this$copyRecursively, new NoSuchFileException($this$copyRecursively, (File) null, "The source file doesn't exist.", 2, (DefaultConstructorMarker) null)) != OnErrorAction.TERMINATE;
        }
        try {
            Iterator<File> it = FilesKt.walkTopDown($this$copyRecursively).onFail(new FilesKt__UtilsKt$copyRecursively$2(onError)).iterator();
            while (it.hasNext()) {
                File next = it.next();
                if (next.exists()) {
                    String relativeString = FilesKt.toRelativeString(next, $this$copyRecursively);
                    Log1F380D.a((Object) relativeString);
                    File file = new File(target, relativeString);
                    if (file.exists() && (!next.isDirectory() || !file.isDirectory())) {
                        if (!overwrite ? true : file.isDirectory() ? !FilesKt.deleteRecursively(file) : !file.delete()) {
                            if (onError.invoke(file, new FileAlreadyExistsException(next, file, "The destination file already exists.")) == OnErrorAction.TERMINATE) {
                                return false;
                            }
                        }
                    }
                    if (next.isDirectory()) {
                        file.mkdirs();
                    } else if (FilesKt.copyTo$default(next, file, overwrite, 0, 4, (Object) null).length() != next.length() && onError.invoke(next, new IOException("Source file wasn't copied completely, length of destination file differs.")) == OnErrorAction.TERMINATE) {
                        return false;
                    }
                } else if (onError.invoke(next, new NoSuchFileException(next, (File) null, "The source file doesn't exist.", 2, (DefaultConstructorMarker) null)) == OnErrorAction.TERMINATE) {
                    return false;
                }
            }
            return true;
        } catch (TerminateException e) {
            return false;
        }
    }

    public static /* synthetic */ boolean copyRecursively$default(File file, File file2, boolean z, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        if ((i & 4) != 0) {
            function2 = FilesKt__UtilsKt$copyRecursively$1.INSTANCE;
        }
        return FilesKt.copyRecursively(file, file2, z, function2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0077, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r3, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x007b, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x007e, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007f, code lost:
        kotlin.io.CloseableKt.closeFinally(r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0082, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.io.File copyTo(java.io.File r9, java.io.File r10, boolean r11, int r12) {
        /*
            java.lang.String r0 = "<this>"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            java.lang.String r0 = "target"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            boolean r0 = r9.exists()
            if (r0 == 0) goto L_0x0083
            boolean r0 = r10.exists()
            if (r0 == 0) goto L_0x0030
            if (r11 == 0) goto L_0x0028
            boolean r0 = r10.delete()
            if (r0 == 0) goto L_0x0020
            goto L_0x0030
        L_0x0020:
            kotlin.io.FileAlreadyExistsException r0 = new kotlin.io.FileAlreadyExistsException
            java.lang.String r1 = "Tried to overwrite the destination, but failed to delete it."
            r0.<init>(r9, r10, r1)
            throw r0
        L_0x0028:
            kotlin.io.FileAlreadyExistsException r0 = new kotlin.io.FileAlreadyExistsException
            java.lang.String r1 = "The destination file already exists."
            r0.<init>(r9, r10, r1)
            throw r0
        L_0x0030:
            boolean r0 = r9.isDirectory()
            if (r0 == 0) goto L_0x0045
            boolean r0 = r10.mkdirs()
            if (r0 == 0) goto L_0x003d
            goto L_0x0074
        L_0x003d:
            kotlin.io.FileSystemException r0 = new kotlin.io.FileSystemException
            java.lang.String r1 = "Failed to create target directory."
            r0.<init>(r9, r10, r1)
            throw r0
        L_0x0045:
            java.io.File r0 = r10.getParentFile()
            if (r0 == 0) goto L_0x004e
            r0.mkdirs()
        L_0x004e:
            java.io.FileInputStream r0 = new java.io.FileInputStream
            r0.<init>(r9)
            java.io.Closeable r0 = (java.io.Closeable) r0
            r1 = r0
            java.io.FileInputStream r1 = (java.io.FileInputStream) r1     // Catch:{ all -> 0x007c }
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x007c }
            r3.<init>(r10)     // Catch:{ all -> 0x007c }
            java.io.Closeable r3 = (java.io.Closeable) r3     // Catch:{ all -> 0x007c }
            r4 = r3
            java.io.FileOutputStream r4 = (java.io.FileOutputStream) r4     // Catch:{ all -> 0x0075 }
            r5 = 0
            r6 = r1
            java.io.InputStream r6 = (java.io.InputStream) r6     // Catch:{ all -> 0x0075 }
            r7 = r4
            java.io.OutputStream r7 = (java.io.OutputStream) r7     // Catch:{ all -> 0x0075 }
            kotlin.io.ByteStreamsKt.copyTo(r6, r7, r12)     // Catch:{ all -> 0x0075 }
            r4 = 0
            kotlin.io.CloseableKt.closeFinally(r3, r4)     // Catch:{ all -> 0x007c }
            kotlin.io.CloseableKt.closeFinally(r0, r4)
        L_0x0074:
            return r10
        L_0x0075:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0077 }
        L_0x0077:
            r5 = move-exception
            kotlin.io.CloseableKt.closeFinally(r3, r4)     // Catch:{ all -> 0x007c }
            throw r5     // Catch:{ all -> 0x007c }
        L_0x007c:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x007e }
        L_0x007e:
            r2 = move-exception
            kotlin.io.CloseableKt.closeFinally(r0, r1)
            throw r2
        L_0x0083:
            kotlin.io.NoSuchFileException r0 = new kotlin.io.NoSuchFileException
            r5 = 0
            r7 = 2
            r8 = 0
            java.lang.String r6 = "The source file doesn't exist."
            r3 = r0
            r4 = r9
            r3.<init>(r4, r5, r6, r7, r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.FilesKt__UtilsKt.copyTo(java.io.File, java.io.File, boolean, int):java.io.File");
    }

    public static /* synthetic */ File copyTo$default(File file, File file2, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return FilesKt.copyTo(file, file2, z, i);
    }

    @Deprecated(message = "Avoid creating temporary directories in the default temp location with this function due to too wide permissions on the newly created directory. Use kotlin.io.path.createTempDirectory instead.")
    public static final File createTempDir(String prefix, String suffix, File directory) {
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        File createTempFile = File.createTempFile(prefix, suffix, directory);
        createTempFile.delete();
        if (createTempFile.mkdir()) {
            Intrinsics.checkNotNullExpressionValue(createTempFile, "dir");
            return createTempFile;
        }
        throw new IOException("Unable to create temporary directory " + createTempFile + '.');
    }

    public static /* synthetic */ File createTempDir$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempDir(str, str2, file);
    }

    @Deprecated(message = "Avoid creating temporary files in the default temp location with this function due to too wide permissions on the newly created file. Use kotlin.io.path.createTempFile instead or resort to java.io.File.createTempFile.")
    public static final File createTempFile(String prefix, String suffix, File directory) {
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        File createTempFile = File.createTempFile(prefix, suffix, directory);
        Intrinsics.checkNotNullExpressionValue(createTempFile, "createTempFile(prefix, suffix, directory)");
        return createTempFile;
    }

    public static /* synthetic */ File createTempFile$default(String str, String str2, File file, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "tmp";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            file = null;
        }
        return FilesKt.createTempFile(str, str2, file);
    }

    public static final boolean deleteRecursively(File $this$deleteRecursively) {
        Intrinsics.checkNotNullParameter($this$deleteRecursively, "<this>");
        boolean z = true;
        for (File file : FilesKt.walkBottomUp($this$deleteRecursively)) {
            z = (file.delete() || !file.exists()) && z;
        }
        return z;
    }

    public static final boolean endsWith(File $this$endsWith, File other) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        FilePathComponents components = FilesKt.toComponents($this$endsWith);
        FilePathComponents components2 = FilesKt.toComponents(other);
        if (components2.isRooted()) {
            return Intrinsics.areEqual((Object) $this$endsWith, (Object) other);
        }
        int size = components.getSize() - components2.getSize();
        if (size < 0) {
            return false;
        }
        return components.getSegments().subList(size, components.getSize()).equals(components2.getSegments());
    }

    public static final boolean endsWith(File $this$endsWith, String other) {
        Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return FilesKt.endsWith($this$endsWith, new File(other));
    }

    private static final List<File> normalize$FilesKt__UtilsKt(List<? extends File> $this$normalize) {
        List<File> arrayList = new ArrayList<>($this$normalize.size());
        for (File file : $this$normalize) {
            String name = file.getName();
            if (!Intrinsics.areEqual((Object) name, (Object) ".")) {
                if (!Intrinsics.areEqual((Object) name, (Object) "..")) {
                    arrayList.add(file);
                } else if (arrayList.isEmpty() || Intrinsics.areEqual((Object) ((File) CollectionsKt.last(arrayList)).getName(), (Object) "..")) {
                    arrayList.add(file);
                } else {
                    arrayList.remove(arrayList.size() - 1);
                }
            }
        }
        return arrayList;
    }

    private static final FilePathComponents normalize$FilesKt__UtilsKt(FilePathComponents $this$normalize) {
        return new FilePathComponents($this$normalize.getRoot(), normalize$FilesKt__UtilsKt((List<? extends File>) $this$normalize.getSegments()));
    }

    public static final File resolve(File $this$resolve, File relative) {
        Intrinsics.checkNotNullParameter($this$resolve, "<this>");
        Intrinsics.checkNotNullParameter(relative, "relative");
        if (FilesKt.isRooted(relative)) {
            return relative;
        }
        String file = $this$resolve.toString();
        Intrinsics.checkNotNullExpressionValue(file, "this.toString()");
        return ((file.length() == 0) || StringsKt.endsWith$default((CharSequence) file, File.separatorChar, false, 2, (Object) null)) ? new File(file + relative) : new File(file + File.separatorChar + relative);
    }

    public static final File resolve(File $this$resolve, String relative) {
        Intrinsics.checkNotNullParameter($this$resolve, "<this>");
        Intrinsics.checkNotNullParameter(relative, "relative");
        return FilesKt.resolve($this$resolve, new File(relative));
    }

    public static final File resolveSibling(File $this$resolveSibling, File relative) {
        Intrinsics.checkNotNullParameter($this$resolveSibling, "<this>");
        Intrinsics.checkNotNullParameter(relative, "relative");
        FilePathComponents components = FilesKt.toComponents($this$resolveSibling);
        return FilesKt.resolve(FilesKt.resolve(components.getRoot(), components.getSize() == 0 ? new File("..") : components.subPath(0, components.getSize() - 1)), relative);
    }

    public static final File resolveSibling(File $this$resolveSibling, String relative) {
        Intrinsics.checkNotNullParameter($this$resolveSibling, "<this>");
        Intrinsics.checkNotNullParameter(relative, "relative");
        return FilesKt.resolveSibling($this$resolveSibling, new File(relative));
    }

    public static final boolean startsWith(File $this$startsWith, File other) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        FilePathComponents components = FilesKt.toComponents($this$startsWith);
        FilePathComponents components2 = FilesKt.toComponents(other);
        if (Intrinsics.areEqual((Object) components.getRoot(), (Object) components2.getRoot()) && components.getSize() >= components2.getSize()) {
            return components.getSegments().subList(0, components2.getSize()).equals(components2.getSegments());
        }
        return false;
    }

    public static final boolean startsWith(File $this$startsWith, String other) {
        Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        return FilesKt.startsWith($this$startsWith, new File(other));
    }

    private static final String toRelativeStringOrNull$FilesKt__UtilsKt(File $this$toRelativeStringOrNull, File base) {
        FilePathComponents normalize$FilesKt__UtilsKt = normalize$FilesKt__UtilsKt(FilesKt.toComponents($this$toRelativeStringOrNull));
        FilePathComponents normalize$FilesKt__UtilsKt2 = normalize$FilesKt__UtilsKt(FilesKt.toComponents(base));
        if (!Intrinsics.areEqual((Object) normalize$FilesKt__UtilsKt.getRoot(), (Object) normalize$FilesKt__UtilsKt2.getRoot())) {
            return null;
        }
        int size = normalize$FilesKt__UtilsKt2.getSize();
        int size2 = normalize$FilesKt__UtilsKt.getSize();
        File file = $this$toRelativeStringOrNull;
        int i = 0;
        int min = Math.min(size2, size);
        while (i < min && Intrinsics.areEqual((Object) normalize$FilesKt__UtilsKt.getSegments().get(i), (Object) normalize$FilesKt__UtilsKt2.getSegments().get(i))) {
            i++;
        }
        int i2 = i;
        StringBuilder sb = new StringBuilder();
        int i3 = size - 1;
        if (i2 <= i3) {
            while (!Intrinsics.areEqual((Object) normalize$FilesKt__UtilsKt2.getSegments().get(i3).getName(), (Object) "..")) {
                sb.append("..");
                if (i3 != i2) {
                    sb.append(File.separatorChar);
                }
                if (i3 != i2) {
                    i3--;
                }
            }
            return null;
        }
        if (i2 < size2) {
            if (i2 < size) {
                sb.append(File.separatorChar);
            }
            String str = File.separator;
            Intrinsics.checkNotNullExpressionValue(str, "separator");
            CollectionsKt.joinTo$default(CollectionsKt.drop(normalize$FilesKt__UtilsKt.getSegments(), i2), sb, str, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 124, (Object) null);
        }
        return sb.toString();
    }

    public static final String getExtension(File $this$extension) {
        Intrinsics.checkNotNullParameter($this$extension, "<this>");
        String name = $this$extension.getName();
        Intrinsics.checkNotNullExpressionValue(name, "name");
        String substringAfterLast = StringsKt.substringAfterLast(name, '.', HttpUrl.FRAGMENT_ENCODE_SET);
        Log1F380D.a((Object) substringAfterLast);
        return substringAfterLast;
    }

    public static final String getInvariantSeparatorsPath(File $this$invariantSeparatorsPath) {
        Intrinsics.checkNotNullParameter($this$invariantSeparatorsPath, "<this>");
        if (File.separatorChar != '/') {
            String path = $this$invariantSeparatorsPath.getPath();
            Intrinsics.checkNotNullExpressionValue(path, "path");
            String replace$default = StringsKt.replace$default(path, File.separatorChar, '/', false, 4, (Object) null);
            Log1F380D.a((Object) replace$default);
            return replace$default;
        }
        String path2 = $this$invariantSeparatorsPath.getPath();
        Intrinsics.checkNotNullExpressionValue(path2, "path");
        return path2;
    }

    public static final String getNameWithoutExtension(File $this$nameWithoutExtension) {
        Intrinsics.checkNotNullParameter($this$nameWithoutExtension, "<this>");
        String name = $this$nameWithoutExtension.getName();
        Intrinsics.checkNotNullExpressionValue(name, "name");
        String substringBeforeLast$default = StringsKt.substringBeforeLast$default(name, ".", (String) null, 2, (Object) null);
        Log1F380D.a((Object) substringBeforeLast$default);
        return substringBeforeLast$default;
    }

    public static final File normalize(File $this$normalize) {
        Intrinsics.checkNotNullParameter($this$normalize, "<this>");
        FilePathComponents components = FilesKt.toComponents($this$normalize);
        File root = components.getRoot();
        String str = File.separator;
        Intrinsics.checkNotNullExpressionValue(str, "separator");
        String joinToString$default = CollectionsKt.joinToString$default(normalize$FilesKt__UtilsKt((List<? extends File>) components.getSegments()), str, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
        Log1F380D.a((Object) joinToString$default);
        return FilesKt.resolve(root, joinToString$default);
    }

    public static final File relativeTo(File $this$relativeTo, File base) {
        Intrinsics.checkNotNullParameter($this$relativeTo, "<this>");
        Intrinsics.checkNotNullParameter(base, "base");
        String relativeString = FilesKt.toRelativeString($this$relativeTo, base);
        Log1F380D.a((Object) relativeString);
        return new File(relativeString);
    }

    public static final File relativeToOrNull(File $this$relativeToOrNull, File base) {
        Intrinsics.checkNotNullParameter($this$relativeToOrNull, "<this>");
        Intrinsics.checkNotNullParameter(base, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrNull, base);
        Log1F380D.a((Object) relativeStringOrNull$FilesKt__UtilsKt);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return new File(relativeStringOrNull$FilesKt__UtilsKt);
        }
        return null;
    }

    public static final File relativeToOrSelf(File $this$relativeToOrSelf, File base) {
        Intrinsics.checkNotNullParameter($this$relativeToOrSelf, "<this>");
        Intrinsics.checkNotNullParameter(base, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($this$relativeToOrSelf, base);
        Log1F380D.a((Object) relativeStringOrNull$FilesKt__UtilsKt);
        return relativeStringOrNull$FilesKt__UtilsKt != null ? new File(relativeStringOrNull$FilesKt__UtilsKt) : $this$relativeToOrSelf;
    }

    public static final String toRelativeString(File $this$toRelativeString, File base) {
        Intrinsics.checkNotNullParameter($this$toRelativeString, "<this>");
        Intrinsics.checkNotNullParameter(base, "base");
        String relativeStringOrNull$FilesKt__UtilsKt = toRelativeStringOrNull$FilesKt__UtilsKt($this$toRelativeString, base);
        Log1F380D.a((Object) relativeStringOrNull$FilesKt__UtilsKt);
        if (relativeStringOrNull$FilesKt__UtilsKt != null) {
            return relativeStringOrNull$FilesKt__UtilsKt;
        }
        throw new IllegalArgumentException("this and base files have different roots: " + $this$toRelativeString + " and " + base + '.');
    }
}
