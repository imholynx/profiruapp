package ru.imholynx.profirutestapp.userdetail;

import ru.imholynx.profirutestapp.BasePresenter;
import ru.imholynx.profirutestapp.BaseView;
import ru.imholynx.profirutestapp.data.Photo;

public interface UserDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showDescription(String description);

        void hideDescription();

        void showPhoto(Photo photo);

        void hidePhoto();

        void showMissingUser();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadPhoto(String userId);
    }
}
