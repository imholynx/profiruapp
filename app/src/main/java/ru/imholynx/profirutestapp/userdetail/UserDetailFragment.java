package ru.imholynx.profirutestapp.userdetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.imholynx.profirutestapp.R;
import ru.imholynx.profirutestapp.data.User;

public class UserDetailFragment extends Fragment implements UserDetailContract.View{

    @NotNull
    private static final String ARGUMENT_USER_ID = "USER_ID";

    private UserDetailContract.Presenter mPresenter;

    private TextView mDetailDescription;
    private ImageView mDetailPhoto;

    public static UserDetailFragment newInstance(@NotNull String userId){
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_USER_ID, userId);
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.userdetail_frag, container,false);
        mDetailDescription = (TextView) root.findViewById(R.id.user_detail_description);
        mDetailPhoto = (ImageView) root.findViewById(R.id.user_detail_photo);

        return root;
    }

    @Override
    public void setPresenter(UserDetailContract.Presenter presenter) {
        if(presenter == null)
            throw new NullPointerException();
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active){
            mDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void hideDescription() {
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mDetailDescription.setVisibility(View.VISIBLE);
        mDetailDescription.setText(description);
    }

    @Override
    public void hidePhoto() {
        mDetailPhoto.setVisibility(View.GONE);
    }

    @Override
    public void showPhoto(Bitmap photo) {
        mDetailPhoto.setImageBitmap(photo);
    }

    //TODO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            getActivity().finish();
        }
    }

    @Override
    public void showMissingUser() {
        mDetailDescription.setText(getString(R.string.no_data));
    }

     @Override
    public boolean isActive() {
        return isAdded();
    }


}
