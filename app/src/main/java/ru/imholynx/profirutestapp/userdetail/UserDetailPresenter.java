package ru.imholynx.profirutestapp.userdetail;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.imholynx.profirutestapp.data.Photo;
import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    private final UsersRepository mUsersRepository;
    private final UserDetailContract.View mUserDetailView;

    @Nullable
    private String mUserId;
   // @NotNull
    //private Bitmap mStartPhoto;

    public UserDetailPresenter(@NotNull String userId,
                               //@NotNull Bitmap startPhoto,
                               @NotNull UsersRepository usersRepository,
                               @NotNull UserDetailContract.View userDetailView) {
        if (userId == null)
            throw new NullPointerException("userId cannot be null");
       // if (startPhoto == null)
        //    throw new NullPointerException("startPhoto cannot be null");
        if (usersRepository == null)
            throw new NullPointerException("usersRepository cannot be null");
        if (userDetailView == null)
            throw new NullPointerException("userDetailView cannot be null");

        mUserId = userId;
       // mStartPhoto = startPhoto;
        mUsersRepository = usersRepository;
        mUserDetailView = userDetailView;

        mUserDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openUser();
    }

    private void openUser() {
        //mUserDetailView.setLoadingIndicator(true);
        //mUserDetailView.showPhoto(mStartPhoto);
        mUsersRepository.getPhoto(mUserId, new UsersDataSource.LoadPhotoCallback() {
            @Override
            public void onPhotoLoaded(Bitmap photo) {
                if(!mUserDetailView.isActive())
                    return;
                mUserDetailView.setLoadingIndicator(false);
                showPhoto(photo);
            }

            @Override
            public void onDataNotAvailable() {
                if(!mUserDetailView.isActive())
                    return;
                mUserDetailView.showMissingUser();
            }
        });
    }

    private void showPhoto(@NotNull Bitmap photo) {
        //String firstName = user.getFirstName();
        //String secondName = user.getSecondName();
        //Bitmap photo = user.getPhoto();
        /*if(firstName.isEmpty() && secondName.isEmpty())
            mUserDetailView.hideDescription();
        else
            mUserDetailView.showDescription(firstName + " " + secondName);*/
        if(photo == null)
            mUserDetailView.hidePhoto();
        else
            mUserDetailView.showPhoto(photo);
    }

    @Override
    public void loadPhoto(String userId) {
        mUsersRepository.getPhoto(userId, new UsersDataSource.LoadPhotoCallback() {
            @Override
            public void onPhotoLoaded(Bitmap photo) {
                if (!mUserDetailView.isActive())
                    return;
                if(photo==null)
                    return;
                mUserDetailView.showPhoto(photo);
            }
            @Override
            public void onDataNotAvailable() {

            }
        });
    }

}
