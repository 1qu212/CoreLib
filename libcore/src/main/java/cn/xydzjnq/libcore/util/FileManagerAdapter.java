package cn.xydzjnq.libcore.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import cn.xydzjnq.libcore.R;

public class FileManagerAdapter extends RecyclerView.Adapter {
    private final static int BACKVIEWHOLDER = 0;
    private final static int FILEMANAGERVIEWHOLDER = 1;
    private File[] files;

    public void setFiles(File[] files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file_manager, viewGroup, false);
        if (i == BACKVIEWHOLDER) {
            return new BackViewHolder(view);
        }
        return new FileManagerViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BACKVIEWHOLDER;
        }
        return FILEMANAGERVIEWHOLDER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BackViewHolder) {
            BackViewHolder holder = (BackViewHolder) viewHolder;
            holder.ivFileType.setImageResource(R.drawable.folder);
            holder.tvFileName.setText("返回上一级");
        } else if (viewHolder instanceof FileManagerViewHolder) {
            FileManagerViewHolder holder = (FileManagerViewHolder) viewHolder;
            if (files[i - 1].isDirectory()) {
                holder.ivFileType.setImageResource(R.drawable.folder);
                holder.isDirectory = true;
            } else {
                holder.ivFileType.setImageResource(R.drawable.file);
                holder.isDirectory = false;
            }
            holder.tvFileName.setText(files[i - 1].getName());
            holder.fileName = files[i - 1].getName();
        }
    }

    @Override
    public int getItemCount() {
        return files == null ? 0 : files.length + 1;
    }

    public static class BackViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFileType;
        private TextView tvFileName;

        public BackViewHolder(View itemView) {
            super(itemView);
            ivFileType = itemView.findViewById(R.id.iv_file_type);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
        }
    }

    public static class FileManagerViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivFileType;
        private TextView tvFileName;
        public boolean isDirectory;
        public String fileName;

        public FileManagerViewHolder(View itemView) {
            super(itemView);
            ivFileType = itemView.findViewById(R.id.iv_file_type);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
        }
    }
}
