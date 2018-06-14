package ru.imholynx.profirutestapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity(tableName = "tasks")
public final class User {

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    private final String mId;

    @NotNull
    @ColumnInfo(name = "first_name")
    private final String mFirstName;

    @NotNull
    @ColumnInfo(name = "second_name")
    private final String mSecondName;

    @Nullable
    @ColumnInfo(name = "photo_50")
    private final String mPhotoLink;

    public User(@NotNull String mId, @NotNull String mFirstName, @NotNull String mSecondName, @Nullable String mPhotoLink) {
        this.mId = mId;
        this.mFirstName = mFirstName;
        this.mSecondName = mSecondName;
        this.mPhotoLink = mPhotoLink;
    }


    @NotNull
    public String getId() {
        return mId;
    }

    @NotNull
    public String getFirstName() {
        return mFirstName;
    }

    @NotNull
    public String getSecondName() {
        return mSecondName;
    }

    @Nullable
    public String getPhotoLink() {
        return mPhotoLink;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return mFirstName + " " + mSecondName;
    }
}
