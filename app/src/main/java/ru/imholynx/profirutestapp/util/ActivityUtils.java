package ru.imholynx.profirutestapp.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

public class ActivityUtils {
    public static void addFragmentToActivity(@NotNull FragmentManager fragmentManager,
                                             @NotNull Fragment fragment,
                                             int frameId){
        if(fragmentManager == null || fragment == null)
            throw new NullPointerException();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
