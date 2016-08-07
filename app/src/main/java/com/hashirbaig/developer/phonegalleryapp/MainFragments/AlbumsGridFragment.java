package com.hashirbaig.developer.phonegalleryapp.MainFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hashirbaig.developer.phonegalleryapp.HostingActivities.ImagesGridActivity;
import com.hashirbaig.developer.phonegalleryapp.Model.Album;
import com.hashirbaig.developer.phonegalleryapp.Model.AlbumData;
import com.hashirbaig.developer.phonegalleryapp.R;
import java.util.List;

public class AlbumsGridFragment extends Fragment{

    private static final int REQUEST_STORAGE_PERMISSION = 20;

    private RecyclerView mGridView;
    private AlbumAdapter mAdapter;

    public static AlbumsGridFragment newInstance() {
        return new AlbumsGridFragment();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlbumData.get(getActivity()).queryAlbumDatabase();
                    new FetchAlbumsInStorage().execute();
                }
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
        } else {
            new QueryAlbumsDatabase().execute();
            new FetchAlbumsInStorage().execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateUI() {
        if(mAdapter == null) {
            mAdapter = new AlbumAdapter(AlbumData.get(getActivity()).getAlbumList());
            mGridView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_grid_layout, container, false);
        mGridView = (RecyclerView) v.findViewById(R.id.grid_view);
        mGridView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.no_of_cols)));
        updateUI();
        return v;
    }

    public class AlbumHolder extends RecyclerView.ViewHolder
                                        implements View.OnClickListener{

        private ImageView mAlbumCover;
        private TextView mAlbumTitle;
        private Album mAlbum;

        public AlbumHolder(View v) {
            super(v);
            mAlbumCover = (ImageView) v.findViewById(R.id.album_thumb_container);
            mAlbumTitle = (TextView) v.findViewById(R.id.album_name);
            v.setOnClickListener(this);
        }

        public void bindHolder(Album album) {
            mAlbum = album;
            new QueryPhotosDatabase().execute(album, this);
            new FetchPhotosInStorage().execute(album);
            /*
            Photo photo = album.getPhotos().get(0);
            Glide.with(getActivity())
                    .load(new ColorDrawable(getResources().getColor(android.R.color.white)))
                    .into(mAlbumCover);
            */
            mAlbumTitle.setText(mAlbum.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent i = ImagesGridActivity.newIntent(getActivity(), mAlbum);
            startActivity(i);
        }

        public ImageView getAlbumCover() {
            return mAlbumCover;
        }
    }

    public class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        private List<Album> mAlbumList;

        public AlbumAdapter(List<Album> albumList) {
            mAlbumList = albumList;
        }

        @Override
        public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.single_album_layout, parent, false);
            return new AlbumHolder(v);
        }

        @Override
        public void onBindViewHolder(AlbumHolder holder, int position) {
            holder.bindHolder(AlbumData.get(getActivity()).getAlbumList().get(position));
        }

        @Override
        public int getItemCount() {
            return mAlbumList.size();
        }
    }

    private class QueryAlbumsDatabase extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AlbumData.get(getActivity()).queryAlbumDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUI();
        }
    }

    private class FetchAlbumsInStorage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AlbumData.get(getActivity()).queryAlbums();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new QueryAlbumsDatabase().execute();
            updateUI();
        }
    }

    private class QueryPhotosDatabase extends AsyncTask<Object,Void,Void> {

        private AlbumHolder holder;
        private Album mAlbum;
        @Override
        protected Void doInBackground(Object... params) {
            mAlbum = (Album) params[0];
            holder = (AlbumHolder) params[1];
            AlbumData.get(getActivity()).queryPhotoDatabase(mAlbum);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUI();

        }
    }

    private class FetchPhotosInStorage extends AsyncTask<Album, Void, Void> {

        private Album mAlbum;
        @Override
        protected Void doInBackground(Album... alba) {
            mAlbum = alba[0];
            AlbumData.get(getActivity()).pathLookUp(mAlbum);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new QueryPhotosDatabase().execute(mAlbum);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gallery_grid_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_data_option:
                new FetchAlbumsInStorage().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
