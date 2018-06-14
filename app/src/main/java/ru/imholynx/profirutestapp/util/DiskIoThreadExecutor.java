package ru.imholynx.profirutestapp.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DiskIoThreadExecutor implements Executor {
    private final Executor mDiskIO;

    public DiskIoThreadExecutor(){
        mDiskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NotNull Runnable command){
        mDiskIO.execute(command);
    }
}
