package org.amcbd.jalsa_registration.interfaces;

import android.view.View;

public interface MemberClickListener {

    void onYesClick(View view, int position);

    void onNoClick(View view, int position);

    void onCrossClick(View view, int position);

}
