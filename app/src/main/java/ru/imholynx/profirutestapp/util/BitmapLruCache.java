package ru.imholynx.profirutestapp.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class BitmapLruCache {

    private LruCache<String, Bitmap> lruCache;
    private static BitmapLruCache INSTANCE;

    public static BitmapLruCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BitmapLruCache();
        }
        return INSTANCE;
    }

    public void initializeCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                int bitmapByteCount = value.getRowBytes() * value.getHeight();
                return bitmapByteCount / 1024;
            }
        };
    }

    public void addBitmapToCache(String key, Bitmap value) {
        Log.d("cache","put: "+ key);
        if (lruCache != null && lruCache.get(key) == null) {
            lruCache.put(key, value);
        }
    }

    public Bitmap getImageFromCache(String key) {
        Log.d("cache","get: "+key);
        Log.d("cache","hit: " + lruCache.hitCount()+ " missed: "+ lruCache.missCount()+" put: " +lruCache.putCount());
        if (key != null) {
            return lruCache.get(key);
        } else
            return null;
    }

    public void clearCache() {
        if (lruCache != null)
            lruCache.evictAll();
    }
}
