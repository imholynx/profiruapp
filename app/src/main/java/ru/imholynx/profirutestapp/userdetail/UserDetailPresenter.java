package ru.imholynx.profirutestapp.userdetail;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    private final UsersRepository mUsersRepository;
    private final UserDetailContract.View mUserDetailView;

    @Nullable
    private String mUserId;

    public UserDetailPresenter(@NotNull String userId,
                               @NotNull UsersRepository usersRepository,
                               @NotNull UserDetailContract.View userDetailView) {
        if (userId == null)
            throw new NullPointerException("userId cannot be null");
        if (usersRepository == null)
            throw new NullPointerException("usersRepository cannot be null");
        if (userDetailView == null)
            throw new NullPointerException("userDetailView cannot be null");

        mUserId = userId;
        mUsersRepository = usersRepository;
        mUserDetailView = userDetailView;

        mUserDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openUser();
    }

    private void openUser() {
        mUserDetailView.setLoadingIndicator(true);
        mUsersRepository.getUser(mUserId, new UsersDataSource.GetUserCallback() {
            @Override
            public void onUserLoaded(User user) {
                if(!mUserDetailView.isActive())
                    return;
                mUserDetailView.setLoadingIndicator(false);
                showUser(user);
            }

            @Override
            public void onDataNotAvailable() {
                if(!mUserDetailView.isActive())
                    return;
                mUserDetailView.showMissingUser();
            }
        });
    }

    private void showUser(@NotNull User user) {
        String firstName = user.getFirstName();
        String secondName = user.getSecondName();
        Bitmap photo = user.getPhoto();
        if(firstName.isEmpty() && secondName.isEmpty())
            mUserDetailView.hideDescription();
        else
            mUserDetailView.showDescription(firstName + " " + secondName);

        if(photo == null)
            mUserDetailView.hidePhoto();
        else
            mUserDetailView.showPhoto(photo);
    }

}
