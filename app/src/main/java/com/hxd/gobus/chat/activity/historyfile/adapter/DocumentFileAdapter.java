package com.hxd.gobus.chat.activity.historyfile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxd.gobus.BuildConfig;
import com.hxd.gobus.R;
import com.hxd.gobus.chat.activity.DownLoadActivity;
import com.hxd.gobus.chat.adapter.StickyListHeadersAdapter;
import com.hxd.gobus.chat.entity.FileItem;
import com.hxd.gobus.chat.entity.SelectedHistoryFileListener;
import com.hxd.gobus.chat.utils.FileHelper;
import com.hxd.gobus.chat.utils.SharePreferenceManager;
import com.hxd.gobus.chat.utils.ViewHolder;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.utils.SqliteUtils;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.content.FileContent;


/**
 * Created by ${chenyn} on 2017/8/29.
 */

public class DocumentFileAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<FileItem> mList;
    private LayoutInflater mInflater;
    private SparseBooleanArray mSelectMap = new SparseBooleanArray();
    private SelectedHistoryFileListener mListener;
    private Context mContext;

    public DocumentFileAdapter(Activity fragment, List<FileItem> documents) {
        this.mList = documents;
        this.mContext = fragment;
        this.mInflater = LayoutInflater.from(fragment);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.section_tv);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.text.setText(mList.get(position).getDate());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FileItem item = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_file_document, null);
        }
        final CheckBox checkBox = ViewHolder.get(convertView, R.id.document_cb);
        TextView title = ViewHolder.get(convertView, R.id.document_title);
        TextView size = ViewHolder.get(convertView, R.id.document_size);
        TextView date = ViewHolder.get(convertView, R.id.document_date);
        LinearLayout ll_document = ViewHolder.get(convertView, R.id.document_item_ll);

        if (SharePreferenceManager.getShowCheck()) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        title.setText(item.getFileName());
        size.setText(item.getFileSize());
        date.setText(SqliteUtils.getNameByWorkNo(mContext,item.getFromeName()) + "  " + item.getDate());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    mSelectMap.put(position, true);
                    mListener.onSelected(item.getMsgId(), item.getMsgId());
                } else {
                    mSelectMap.delete(position);
                    mListener.onUnselected(item.getMsgId(), item.getMsgId());
                }
            }
        });
        final FileContent content = (FileContent) item.getMessage().getContent();

            ll_document.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content.getLocalPath() == null) {
                        org.greenrobot.eventbus.EventBus.getDefault().postSticky(item.getMessage());
                        Intent intent = new Intent(mContext, DownLoadActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        final String newPath = "sdcard/JChatDemo/recvFiles/" + content.getFileName();
                        File file = new File(newPath);
                        if (file.exists() && file.isFile()) {
                            browseDocument(content.getFileName(), newPath);
                        } else {
                            final String finalFileName = content.getFileName();
                            FileHelper.getInstance().copyFile(item.getFileName(), content.getLocalPath(), (Activity) mContext,
                                    new FileHelper.CopyFileCallback() {
                                        @Override
                                        public void copyCallback(Uri uri) {
                                            browseDocument(finalFileName, newPath);
                                        }
                                    });
                        }
                    }
                }
            });

        return convertView;
    }

    private void browseDocument(String fileName, String path) {
        try {
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mime = mimeTypeMap.getMimeTypeFromExtension(ext);
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID +".provider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, mime);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, R.string.file_not_support_hint, Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpdateListener(SelectedHistoryFileListener listener) {
        this.mListener = listener;
    }

    private static class HeaderViewHolder {
        TextView text;
    }

}
