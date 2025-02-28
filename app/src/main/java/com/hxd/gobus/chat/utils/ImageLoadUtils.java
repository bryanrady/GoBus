package com.hxd.gobus.chat.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hxd.gobus.chat.utils.keyboard.utils.imageloader.ImageBase;
import com.hxd.gobus.chat.utils.keyboard.utils.imageloader.ImageLoader;

import java.io.IOException;


public class ImageLoadUtils extends ImageLoader {

    public ImageLoadUtils(Context context) {
        super(context);
    }

    @Override
    protected void displayImageFromFile(String imageUri, ImageView imageView) throws IOException {
        String filePath = Scheme.FILE.crop(imageUri);
        Glide.with(imageView.getContext())
                .load(filePath)
                .into(imageView);
    }

    @Override
    protected void displayImageFromAssets(String imageUri, ImageView imageView) throws IOException {
        String uri = Scheme.cropScheme(imageUri);
        Scheme.ofUri(imageUri).crop(imageUri);
        Glide.with(imageView.getContext())
                .load(Uri.parse("file:///android_asset/" + uri))
                .into(imageView);
    }
}
