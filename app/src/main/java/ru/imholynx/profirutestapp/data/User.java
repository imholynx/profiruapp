package ru.imholynx.profirutestapp.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class User {

    @NotNull
    private final String id;

    @NotNull
    private final String firstName;

    @NotNull
    private final String secondName;

    @Nullable
    private final String photoLink;

    @Nullable
    private final String largePhotoLink;

    public User(@NotNull String id, @NotNull String firstName, @NotNull String secondName,  @Nullable String photoLink, @Nullable String largePhotoLink) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.photoLink = photoLink;
        this.largePhotoLink = largePhotoLink;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getFirstName() {
        return firstName;
    }

    @NotNull
    public String getSecondName() {
        return secondName;
    }

    @Nullable
    public String getPhotoLink() { return photoLink; }

    @Nullable
    public String getLargePhotoLink() { return largePhotoLink;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return firstName + " " + secondName;
    }


}
