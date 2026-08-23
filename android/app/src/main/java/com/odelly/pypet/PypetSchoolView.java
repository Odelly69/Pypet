package com.odelly.pypet;

import android.app.Activity;

/** Legacy compatibility entry point. The old description-dialog Academy is intentionally removed. */
public final class PypetSchoolView {
    private PypetSchoolView() {}
    public static void show(Activity activity) { PypetAcademyActivityView.show(activity); }
}
