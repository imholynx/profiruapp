package ru.imholynx.profirutestapp.userdetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.imholynx.profirutestapp.data.Photo;
import ru.imholynx.profirutestapp.data.User;
import ru.imholynx.profirutestapp.data.source.UsersDataSource;
import ru.imholynx.profirutestapp.data.source.UsersRepository;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDetailPresenterTest {

    public static final String DESCRIPTION_TEST = "Лайков: 42 Комментариев: 60";

    public static final User USER = new User("1","Ivan1","Ivanov1","https://cs.pikabu.ru/assets/favicon.ico","https://cs.pikabu.ru/assets/favicon.ico");

    public static final Photo USER_PHOTO = new Photo("1","444","https://cs.pikabu.ru/assets/favicon.ico",42,60);

    public static final User EMPTY_USER = new User("","","","","");

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserDetailContract.View userDetailView;

    @Captor
    private ArgumentCaptor<UsersDataSource.LoadPhotoCallback> loadPhotoCallbackArgumentCaptor;

    private UserDetailPresenter userDetailPresenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        when(userDetailView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenter_setsPresenterToView(){
        userDetailPresenter = new UserDetailPresenter(USER.getId(),
                usersRepository,
                userDetailView);

        verify(userDetailView).setPresenter(userDetailPresenter);
    }

    @Test
    public void getPhotoFromRepositoryAndLoadIntoView(){
        //ask to open photo
        userDetailPresenter = new UserDetailPresenter(USER.getId(),
                usersRepository,
                userDetailView);
        userDetailPresenter.start();

        verify(usersRepository).getPhoto(eq(USER.getId()),loadPhotoCallbackArgumentCaptor.capture());
        InOrder inOrder = inOrder(userDetailView);
        inOrder.verify(userDetailView).setLoadingIndicator(true);

        loadPhotoCallbackArgumentCaptor.getValue().onPhotoLoaded(USER_PHOTO);

        inOrder.verify(userDetailView).setLoadingIndicator(false);
        verify(userDetailView).showPhoto(USER_PHOTO);
    }

    @Test
    public void getEmptyPhotFromRepositoryAndLoadIntoView(){
        userDetailPresenter = new UserDetailPresenter(EMPTY_USER.getId(),usersRepository,userDetailView);
        userDetailPresenter.start();
        verify(userDetailView).showMissingUser();
    }


}
