package androidx.appcompat.app;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class AppCompatDialogFragment extends DialogFragment {
    public AppCompatDialogFragment() {
    }

    public AppCompatDialogFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AppCompatDialog(getContext(), getTheme());
    }

    public void setupDialog(Dialog dialog, int style) {
        if (dialog instanceof AppCompatDialog) {
            AppCompatDialog appCompatDialog = (AppCompatDialog) dialog;
            switch (style) {
                case 1:
                case 2:
                    break;
                case 3:
                    dialog.getWindow().addFlags(24);
                    break;
                default:
                    return;
            }
            appCompatDialog.supportRequestWindowFeature(1);
            return;
        }
        super.setupDialog(dialog, style);
    }
}
