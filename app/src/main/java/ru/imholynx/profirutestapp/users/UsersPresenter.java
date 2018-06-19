package ru.imholynx.profirutestapp.users;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

class UsersPresenter  implements UsersContract.Presenter{

    private final UsersRepository mUsersRepository;
    private final UsersContract.View mUsersView;
    private boolean mFirstLoad = true;

    public UsersPresenter(@NotNull UsersRepository usersRepository,@NotNull UsersContract.View usersView) {
        if(usersRepository == null)
            throw new NullPointerException("usersRepository cannot be null");
        if(usersView == null)
            throw new NullPointerException("usersView cannot be null");
        this.mUsersRepository = usersRepository;
        this.mUsersView = usersView;
        mUsersView.setPresenter(this);
    }

    @Override
    public void start() {
        loadUsers(false);
    }
    //TODO
    @Override
    public void result(int requestCode, int resultCode) {
    }

    @Override
    public void loadUsers(boolean forcedUpdate) {
        loadUsers(forcedUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    public void loadUsers(boolean forcedUpdate, final boolean showLoadingUi) {
        if (showLoadingUi)
            mUsersView.setLoadingIndication(true);
        if (forcedUpdate)
            mUsersRepository.refreshUsers();

        mUsersRepository.getUsers(new UsersDataSource.LoadUsersCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                if(!mUsersView.isActive()){
                    return;
                }
                if(showLoadingUi){
                    mUsersView.setLoadingIndication(false);
                }
                processUsers(users);

            }

            @Override
            public void onDataNotAvailable() {
                if(!mUsersView.isActive()){
                    return;
                }
                mUsersView.showLoadingUsersError();
            }
        });
    }

    private void processUsers(List<User> users){
        if(users.isEmpty())
            mUsersView.showNoUsers();
        else
            mUsersView.showUsers(users);
    }

    @Override
    public void openUserDetails(@NotNull User requestedUser, View view,Bitmap photo) {
        if(requestedUser == null)
            throw new NullPointerException("requestedUser cannot be null");
        mUsersView.showUserDetailsUi(requestedUser.getId(),view, photo);
    }
}
