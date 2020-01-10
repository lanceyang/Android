package com.lance.library.origin.config;

import android.content.Intent;
import android.net.Uri;

import static android.content.Intent.ACTION_CALL;
import static android.content.Intent.ACTION_DIAL;

public class CallConfig {

    private String phone;

    public CallConfig setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Intent buildIntent() {
        Intent intent = new Intent(ACTION_CALL);
        if (phone != null){

            Uri data = Uri.parse("tel:" + phone);
            intent.setData(data);
        }
        return intent;
    }
}
