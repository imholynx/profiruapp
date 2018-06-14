package ru.imholynx.profirutestapp.userdetail;

import android.graphics.Bitmap;

import ru.imholynx.profirutestapp.BasePresenter;
import ru.imholynx.profirutestapp.BaseView;
import ru.imholynx.profirutestapp.users.UsersContract;

public interface UserDetailContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showDescription(String description);
        void hideDescription();
        void showPhoto(Bitmap photo);
        void hidePhoto();
        void showMissingUser();
        boolean isActive();
    }

    interface Presenter extends BasePresenter{
    }
}
