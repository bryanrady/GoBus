package com.hxd.gobus.chat.adapter;

import android.content.Context;
import android.view.View;

import com.hxd.gobus.R;
import com.hxd.gobus.chat.model.Constants;
import com.hxd.gobus.chat.utils.ImageLoadUtils;
import com.hxd.gobus.chat.utils.keyboard.data.EmoticonEntity;
import com.hxd.gobus.chat.utils.keyboard.data.EmoticonPageEntity;
import com.hxd.gobus.chat.utils.keyboard.interfaces.EmoticonClickListener;

import java.io.IOException;


public class BigEmoticonsAndTitleAdapter extends BigEmoticonsAdapter {

    protected final double DEF_HEIGHTMAXTATIO = 1.6;

    public BigEmoticonsAndTitleAdapter(Context context, EmoticonPageEntity emoticonPageEntity, EmoticonClickListener onEmoticonClickListener) {
        super(context, emoticonPageEntity, onEmoticonClickListener);
        this.mItemHeight = (int) context.getResources().getDimension(R.dimen.dp_60);
        this.mItemHeightMaxRatio = DEF_HEIGHTMAXTATIO;
    }

    protected void bindView(int position, ViewHolder viewHolder) {
        final boolean isDelBtn = isDelBtn(position);
        final EmoticonEntity emoticonEntity = mData.get(position);
        if (isDelBtn) {
            viewHolder.iv_emoticon.setImageResource(R.mipmap.icon_del);
            viewHolder.iv_emoticon.setBackgroundResource(R.drawable.bg_emoticon);
        } else {
            if (emoticonEntity != null) {
                try {
                    ImageLoadUtils.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content.setText(emoticonEntity.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                viewHolder.iv_emoticon.setBackgroundResource(R.drawable.bg_emoticon);
            }
        }

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmoticonClickListener != null) {

                    mOnEmoticonClickListener.onEmoticonClick(emoticonEntity, Constants.EMOTICON_CLICK_BIGIMAGE, isDelBtn);
                }
            }
        });
    }
}