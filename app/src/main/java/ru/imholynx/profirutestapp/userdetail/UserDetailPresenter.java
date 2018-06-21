package ru.imholynx.profirutestapp.userdetail;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.imholynx.profirutestapp.data.Photo;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    private final UsersRepository usersRepository;
    private final UserDetailContract.View userDetailView;

    @Nullable
    private String userId;

    public UserDetailPresenter(@NotNull String userId,
                               @NotNull UsersRepository usersRepository,
                               @NotNull UserDetailContract.View userDetailView) {
        if (userId == null)
            throw new NullPointerException("userId cannot be null");
        if (usersRepository == null)
            throw new NullPointerException("usersRepository cannot be null");
        if (userDetailView == null)
            throw new NullPointerException("userDetailView cannot be null");

        this.userId = userId;
        this.usersRepository = usersRepository;
        this.userDetailView = userDetailView;
        this.userDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        openUser();
    }

    private void openUser() {
        usersRepository.getPhoto(userId, new UsersDataSource.LoadPhotoCallback() {
            @Override
            public void onPhotoLoaded(Photo photo) {
                if (!userDetailView.isActive())
                    return;
                userDetailView.setLoadingIndicator(false);
                showPhoto(photo);
            }

            @Override
            public void onDataNotAvailable() {
                if (!userDetailView.isActive())
                    return;
                userDetailView.showMissingUser();
            }
        });
    }

    private void showPhoto(@NotNull Photo photo) {
        if (photo == null  || photo.getPhotoUrl().isEmpty())
            userDetailView.hidePhoto();
        else
            userDetailView.showPhoto(photo);
    }

    @Override
    public void loadPhoto(String userId) {
        usersRepository.getPhoto(userId, new UsersDataSource.LoadPhotoCallback() {
                    @Override
                    public void onPhotoLoaded(Photo photo) {
                        if (!userDetailView.isActive())
                            return;
                        if (photo == null)
                            return;
                        userDetailView.showPhoto(photo);
                    }

                    @Override
                    public void onDataNotAvailable() {

            }
        });
    }

}
