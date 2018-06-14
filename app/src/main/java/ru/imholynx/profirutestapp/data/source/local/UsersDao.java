package ru.imholynx.profirutestapp.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.imholynx.profirutestapp.data.User;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM Users")
    List<User> getUsers();

    @Query("SELECT * FROM Users WHERE id = :userId")
    User getUserById(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("DELETE FROM Users")
    void deleteUsers();
}
