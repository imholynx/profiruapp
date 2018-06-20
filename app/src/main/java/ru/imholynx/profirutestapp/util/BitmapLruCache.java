package ru.imholynx.profirutestapp.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapLruCache {

    private LruCache<String,Bitmap> lruCache;
    private static BitmapLruCache INSTANCE;
    public static BitmapLruCache getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new BitmapLruCache();
        }

        return INSTANCE;
    }

    public void initializeCache()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() /1024);

        final int cacheSize = maxMemory / 8;

        lruCache = new LruCache<String, Bitmap>(cacheSize)
        {
            protected int sizeOf(String key, Bitmap value)
            {
                int bitmapByteCount = value.getRowBytes() * value.getHeight();

                return bitmapByteCount / 1024;
            }
        };
    }

    public void addBitmapToCache(String key,Bitmap value){
        if(lruCache != null && lruCache.get(key) == null)
        {
            lruCache.put(key, value);
        }
    }

    public Bitmap getImageFromCache(String key){
        if(key != null)
            return lruCache.get(key);
        else
            return null;
    }

    public void clearCache(){
        if(lruCache != null)
            lruCache.evictAll();
    }


}
