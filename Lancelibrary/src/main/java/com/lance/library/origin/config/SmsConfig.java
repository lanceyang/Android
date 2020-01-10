package com.lance.library.origin.config;

import android.content.Intent;
import android.net.Uri;

public class SmsConfig {

    private String phone;

    public SmsConfig setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Intent buildIntent() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone));
        intent.putExtra("sms_body", " ");
        return intent;
    }

}
