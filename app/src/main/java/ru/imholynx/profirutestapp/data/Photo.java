package ru.imholynx.profirutestapp.data;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Photo {

    @NotNull
    private final String mId;
    @NotNull
    private final String mPhotoId;
    @NotNull
    private final String mPhotoUrl;
    @NotNull
    private final Bitmap mPhoto;


    public Photo(@NotNull String id, @NotNull String photoId, @NotNull String photoUrl, @NotNull Bitmap photo) {
        mId = id;
        mPhotoId = photoId;
        mPhotoUrl = photoUrl;
        mPhoto = photo;
    }

    @NotNull
    public String getPhotoId() {
        return mPhotoId;
    }

    @NotNull
    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    @NotNull
    public Bitmap getPhoto() {
        return mPhoto;
    }

}
