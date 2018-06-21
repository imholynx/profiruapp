package ru.imholynx.profirutestapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.users.UsersContract;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private String imageUrl;
    private BitmapLruCache cache;
    //private RecyclerView.Adapter adapter;
    private Bitmap image = null;
    private List<User> mUserList;
    private int position;
    private WeakReference<ImageView> imageView;

    public DownloadImageTask(ImageView view) {
        this.cache = BitmapLruCache.getInstance();
        //this.mUserList = userList;
        //this.position = position;
        imageView = new WeakReference<ImageView>(view);
        //this.adapter = adapter;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        imageUrl = strings[0];
        return getImage(imageUrl);
    }

    private Bitmap getImage(String imageUrl) {
        if (cache.getImageFromCache(imageUrl) == null) {
            try {
                InputStream inputStream = new URL(imageUrl).openStream();
                image = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (result != null) {
            cache.addBitmapToCache(imageUrl, result);
            if(imageView!=null) {
                ImageView imgView = imageView.get();
                if (imgView != null)
                    imgView.setImageBitmap(result);
            }
        }
    }
}
