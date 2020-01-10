package com.lance.library.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.lance.library.R;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

/**
 * Created by ZF_Develop_PC on 2018/3/16.
 */

public class PdfReaderActivity extends AppCompatActivity implements DownloadFile.Listener {

    private PDFPagerAdapter adapter;
    private RemotePDFViewPager remotePDFViewPager;
    public final static String INTENT_KEY_FILE_URL = "intent_key_file_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);


        String fileUrl = getIntent().getStringExtra(INTENT_KEY_FILE_URL);
        int fileNamePosition = fileUrl.lastIndexOf("pdfFiles") + 9;
        String fileName = fileUrl.substring(fileNamePosition);
        fileName = Uri.encode(fileName);
        fileUrl = fileUrl.substring(0, fileNamePosition) + fileName;

        if (StringUtil.isEmpty(fileUrl)) {
            setResult(RESULT_CANCELED);
            finish();
        }

        remotePDFViewPager =
                new RemotePDFViewPager(this, fileUrl, this);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, destinationPath);
        remotePDFViewPager.setAdapter(adapter);
        setContentView(remotePDFViewPager);
    }

    @Override
    public void onFailure(Exception e) {
        Intent intent = new Intent();
        intent.putExtra("error", e.getMessage());
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null)
            adapter.close();
    }
}
