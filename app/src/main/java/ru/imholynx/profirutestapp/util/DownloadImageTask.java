package ru.imholynx.profirutestapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private String imageUrl;
    private BitmapLruCache cache;
    private Bitmap image = null;
    private WeakReference<ImageView> imageView;

    public DownloadImageTask(ImageView view) {
        this.cache = BitmapLruCache.getInstance();
        imageView = new WeakReference<>(view);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        imageUrl = strings[0];
        return getImage(imageUrl);
    }

    private Bitmap getImage(String imageUrl) {
        image = cache.getImageFromCache(imageUrl);
        if (image == null) {
            try {
                InputStream inputStream = new URL(imageUrl).openStream();
                image = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cache.addBitmapToCache(imageUrl,image);
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        if (imageView != null) {
            ImageView imgView = imageView.get();
            if (imgView != null)
                if (result != null) {
                    imgView.setImageBitmap(result);
                }
        }
    }
}
