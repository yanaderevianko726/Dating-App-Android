package com.sugardaddy.citascasualesconocergentenuevacercadeti.modules.centersheet;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * Created by Maravilho Singa on 08/12/19.
 */
public class CenterSheetDialogFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new CenterSheetDialog(getContext(), getTheme());
    }
}
