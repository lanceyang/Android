package com.lance.library.origin.config;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class VideoConfig {

    private Uri output;

    private int quality = 0;

    private int sizeLimit = 8 * 1024 * 20;

    private int durationLimit = 30;

    public VideoConfig setOutput(Uri output) {
        this.output = output;
        return this;
    }

    public VideoConfig setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public VideoConfig setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    public VideoConfig setDurationLimit(int durationLimit) {
        this.durationLimit = durationLimit;
        return this;
    }

    public Intent buildIntent() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (output != null)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);

        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit);

        return intent;
    }
}
