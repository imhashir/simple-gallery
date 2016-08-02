package com.hashirbaig.developer.phonegalleryapp.MainFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
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

import com.hashirbaig.developer.phonegalleryapp.Helper.PictureUtils;
import com.hashirbaig.developer.phonegalleryapp.HostingActivities.ImagesGridActivity;
import com.hashirbaig.developer.phonegalleryapp.Model.Album;
import com.hashirbaig.developer.phonegalleryapp.Model.AlbumData;
import com.hashirbaig.developer.phonegalleryapp.Model.Photo;
import com.hashirbaig.developer.phonegalleryapp.R;
import com.hashirbaig.developer.phonegalleryapp.Threads.ThumbnailLoader;

import java.util.List;

public class AlbumsGridFragment extends Fragment{

    private static final int REQUEST_STORAGE_PERMISSION = 20;

    private RecyclerView mGridView;
    private AlbumAdapter mAdapter;
    private ThumbnailLoader<AlbumHolder> mThumbnailLoader;

    public static AlbumsGridFragment newInstance() {
        return new AlbumsGridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getLocalStoragePermissions();
        AlbumData.get(getActivity());

        Handler responseHandler = new Handler();
        mThumbnailLoader = new ThumbnailLoader<>(responseHandler);

        mThumbnailLoader.setThumbnailLoadedListener(new ThumbnailLoader.ThumbnailLoaderListener<AlbumHolder>() {
            @Override
            public void onThumbnailDownloaded(AlbumHolder target, Bitmap bitmap) {
                target.finishView(bitmap);
            }
        });

        mThumbnailLoader.start();
        mThumbnailLoader.getLooper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailLoader.quit();
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
            mThumbnailLoader.queueThumbnail(this, album.getPhotos().get(0).getPath());
            mAlbum = album;
        }

        public void finishView(Bitmap bitmap) {
            mAlbumTitle.setText(mAlbum.getTitle());
            mAlbumCover.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            Intent i = ImagesGridActivity.newIntent(getActivity(), mAlbum);
            startActivity(i);
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

    private class FetchImagesInStorage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AlbumData.get(getActivity()).queryAlbums();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUI();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.gallery_grid_menu, menu);
    }

    private void getLocalStoragePermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_data_option:
                new FetchImagesInStorage().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
