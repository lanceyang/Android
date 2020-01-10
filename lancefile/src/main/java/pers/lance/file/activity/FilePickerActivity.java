package pers.lance.file.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import pers.lance.file.R;
import pers.lance.file.adapter.FileAdapter;
import pers.lance.file.adapter.NavigationAdapter;
import pers.lance.file.po.ResourcePo;
import pers.lance.file.po.ResourceType;

public class FilePickerActivity extends AppCompatActivity implements FileAdapter.OnItemClickListener, NavigationAdapter.OnItemClickListener {

    public static final String RESULT_DATA_KET = "result_data";

    private NavigationAdapter navigationAdapter;

    private FileAdapter fileAdapter;

    private RecyclerView nView;

    private RecyclerView fView;

    private List<String> nList = new ArrayList<>();

    private List<File[]> fList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filepicker);

        nView = findViewById(R.id.navigation);
        fView = findViewById(R.id.list);

        nView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        navigationAdapter = new NavigationAdapter();
        nView.setAdapter(navigationAdapter);

        fView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        fileAdapter = new FileAdapter();
        fView.setAdapter(fileAdapter);

        File[] files = checkChildFiles(Environment.getExternalStoragePublicDirectory("/"));
        fList.add(files);
        fileAdapter.setDatas(files);

        fileAdapter.setOnItemClickListener(this);
        navigationAdapter.setClickListener(this);
    }

    FileFilter filter = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory())
                return true;
            if (pathname.getName().contains(".doc") || pathname.getName().contains(".ppt") || pathname.getName().contains("xls") ||
                    pathname.getName().contains(".docx") || pathname.getName().contains(".pptx") || pathname.getName().contains(".xlsx"))
                return true;
            return false;
        }
    };

    private File[] checkChildFiles(File file) {
        return file.listFiles(filter);
    }

    public void leftClick(View v) {
        onBackPressed();
    }

    @Override
    public void OnClick(int position, File content) {
        if (content != null && content.isDirectory()) {
            File[] files = checkChildFiles(content);
            fileAdapter.setDatas(files);
            fList.add(content.listFiles());
            nList.add(content.getName());
            navigationAdapter.setDataList(nList);
        } else if (content != null) {

            Uri currUri;

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                currUri = FileProvider.getUriForFile(
                        this, "com.zfzn.tuanbo.provider",
                        content.getAbsoluteFile());
            } else {
                currUri = Uri.fromFile(content.getAbsoluteFile());
            }

            ResourcePo resourcePo = new ResourcePo();
            resourcePo.setContent(String.valueOf(currUri));
            resourcePo.setLocalPath(content.getAbsolutePath());
            resourcePo.setName(content.getName());
            resourcePo.setType(ResourceType.FILE);

            Intent intent = new Intent();
            intent.putExtra(RESULT_DATA_KET, resourcePo);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void OnClick(int position, String content) {
        if (position != fList.size() - 1) {
            fileAdapter.setDatas(fList.get(position));
            nList = nList.subList(0, position);
            navigationAdapter.setDataList(nList);
        }
    }

    public static void jumpHereForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, FilePickerActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
