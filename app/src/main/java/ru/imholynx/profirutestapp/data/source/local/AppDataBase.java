package ru.imholynx.profirutestapp.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.imholynx.profirutestapp.data.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;
    public abstract UsersDao userDao();
    private static final Object sLock = new Object();
    public static AppDataBase getInstance(Context context) {
        synchronized (sLock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, "Users.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
