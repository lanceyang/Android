package com.lance.library.origin.config;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class PhotoConfig {

    private Uri output;


    public PhotoConfig setOutput(Uri output) {
        this.output = output;
        return this;
    }

    public Intent buildIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (output != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);

        return intent;
    }

}
