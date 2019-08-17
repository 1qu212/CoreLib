package cn.xydzjnq.libcore.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.xydzjnq.libcore.R;
import cn.xydzjnq.libcore.base.BaseActivity;
import cn.xydzjnq.libcore.util.listener.OnRecyclerItemTapUpListener;

import static cn.xydzjnq.libcore.util.ConfigSPUtils.LASTFILEPATH;

public class FileManagerActivity extends BaseActivity {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    public static final String EXTRAPATH = "extraPath";
    private static final String ROOTPATH = "/storage";
    public static final int RESULTCODE = 0;
    public static final String SELECTEDPATH = "selectedPath";
    private TextView tvPath;
    private RecyclerView rvFileName;
    private TextView tvOk;
    private TextView tvCancel;
    private String extraPath;
    private FileManagerAdapter fileManagerAdapter;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extraPath = getIntent().getStringExtra(EXTRAPATH);
        setContentView(R.layout.activity_file_manager);
        initView();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, WRITE_EXTERNAL_STORAGE_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, true,
                    "需要WRITE_EXTERNAL_STORAGE权限",
                    "为方便从手机导入文件，请授予文件可写权限");
        } else {
            permissonGrant();
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvFileName.addOnItemTouchListener(new OnRecyclerItemTapUpListener(rvFileName) {
            @Override
            public void onItemTapUp(RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof FileManagerAdapter.BackViewHolder) {
                    if (ROOTPATH.equals(path)) {
                        Toast.makeText(FileManagerActivity.this, "已经是根目录了", Toast.LENGTH_SHORT).show();
                    } else {
                        String tempPath = path.substring(0, path.lastIndexOf("/"));
                        File file = new File(tempPath);
                        File[] files = file.listFiles();
                        if (files == null) {
                            Toast.makeText(FileManagerActivity.this, "已经没有上一级目录了", Toast.LENGTH_SHORT).show();
                        } else {
                            path = tempPath;
                            tvPath.setText(path);
                            fileManagerAdapter.setFiles(files);
                        }
                    }
                } else if (viewHolder instanceof FileManagerAdapter.FileManagerViewHolder) {
                    FileManagerAdapter.FileManagerViewHolder holder = (FileManagerAdapter.FileManagerViewHolder) viewHolder;
                    if (holder.isDirectory) {
                        path = path + "/" + holder.fileName;
                        tvPath.setText(path);
                        File file = new File(path);
                        File[] files = file.listFiles();
                        fileManagerAdapter.setFiles(files);
                    } else {
                        ConfigSPUtils.putString(FileManagerActivity.this, LASTFILEPATH, path);
                        path = path + "/" + holder.fileName;
                        Intent intent = new Intent();
                        intent.putExtra(SELECTEDPATH, path);
                        setResult(RESULTCODE, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissonGrant();
        }
    }

    private void initView() {
        tvPath = (TextView) findViewById(R.id.tv_path);
        rvFileName = (RecyclerView) findViewById(R.id.rv_file_name);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
    }

    private void permissonGrant() {
        if (extraPath != null) {
            path = extraPath;
        } else {
            path = ConfigSPUtils.getString(this, LASTFILEPATH) == null
                    ? Environment.getExternalStorageDirectory().getPath()
                    : ConfigSPUtils.getString(this, LASTFILEPATH);
        }
        File tempFile = new File(path);
        if (tempFile.listFiles() == null) {
            path = Environment.getExternalStorageDirectory().getPath();
            ConfigSPUtils.putString(this, LASTFILEPATH, path);
        }
        tvPath.setText(path);
        fileManagerAdapter = new FileManagerAdapter();
        rvFileName.setAdapter(fileManagerAdapter);
        rvFileName.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_default_devider));
        rvFileName.addItemDecoration(dividerItemDecoration);
        File file = new File(path);
        File[] files = file.listFiles();
        fileManagerAdapter.setFiles(files);
    }
}
