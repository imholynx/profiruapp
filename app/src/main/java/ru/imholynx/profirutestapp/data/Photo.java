package ru.imholynx.profirutestapp.data;

import org.jetbrains.annotations.NotNull;

public class Photo {

    @NotNull
    private final String id;
    @NotNull
    private final String photoId;
    @NotNull
    private final String photoUrl;
    @NotNull
    private final int likes;
    @NotNull
    private final int comments;

    public Photo(@NotNull String id, @NotNull String photoId, @NotNull String photoUrl, @NotNull int likes, @NotNull int comments) {
        this.id = id;
        this.photoId = photoId;
        this.photoUrl = photoUrl;
        this.likes = likes;
        this.comments = comments;
    }

    @NotNull
    public String getPhotoId() {
        return photoId;
    }

    @NotNull
    public String getPhotoUrl() {
        return photoUrl;
    }

    @NotNull
    public int getLikes() {
        return likes;
    }

    @NotNull
    public int getComments() {
        return comments;
    }
}
