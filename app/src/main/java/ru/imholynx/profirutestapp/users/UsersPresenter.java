package ru.imholynx.profirutestapp.users;

import android.graphics.Bitmap;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

class UsersPresenter implements UsersContract.Presenter {

    private final UsersRepository usersRepository;
    private final UsersContract.View usersView;
    private boolean firstLoad = true;

    public UsersPresenter(@NotNull UsersRepository usersRepository, @NotNull UsersContract.View usersView) {
        if (usersRepository == null)
            throw new NullPointerException("usersRepository cannot be null");
        if (usersView == null)
            throw new NullPointerException("usersView cannot be null");
        this.usersRepository = usersRepository;
        this.usersView = usersView;
        this.usersView.setPresenter(this);
    }

    @Override
    public void start() {
        loadUsers(false);
    }

    @Override
    public void loadUsers(boolean forcedUpdate) {
        loadUsers(forcedUpdate || firstLoad, true);
        firstLoad = false;
    }

    public void loadUsers(boolean forcedUpdate, final boolean showLoadingUi) {
        if (showLoadingUi)
            usersView.setLoadingIndication(true);
        if (forcedUpdate)
            usersRepository.refreshUsers();

        usersRepository.getUsers(new UsersDataSource.LoadUsersCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                if (!usersView.isActive()) {
                    return;
                }
                if (showLoadingUi) {
                    usersView.setLoadingIndication(false);
                }
                processUsers(users);

            }

            @Override
            public void onDataNotAvailable() {
                if (!usersView.isActive())  {
                    return;
                }
                usersView.showLoadingUsersError();
            }
        });
    }

    private void processUsers(List<User> users) {
        if (users.isEmpty()) {
            usersView.showNoUsers();
        } else {
            usersView.showUsers(users);
        }
    }

    @Override
    public void openUserDetails(@NotNull User requestedUser, View view, Bitmap photo) {
        if (requestedUser == null)
            throw new NullPointerException("requestedUser cannot be null");
        usersView.showUserDetailsUi(requestedUser.getId(), view, photo);
    }
}
