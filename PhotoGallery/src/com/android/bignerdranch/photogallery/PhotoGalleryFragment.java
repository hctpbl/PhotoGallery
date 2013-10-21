package com.android.bignerdranch.photogallery;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class PhotoGalleryFragment extends Fragment {
	private static final String TAG = "PhotoGalleryFragment";
	
	GridView mGridView;
	ArrayList<GalleryItem> mItems;
	
	int mResultPage = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		new FetchItemsTask().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
		
		mGridView = (GridView)v.findViewById(R.id.gridView);
		
		setupAdapter();
		
		mGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mGridView.getLastVisiblePosition() + 1 == totalItemCount) {
					mResultPage++;
					new FetchItemsTask().execute();
				}
			}
		});
		
		return v;
	}
	
	void setupAdapter() {
		if (getActivity() == null || mGridView == null) return;
		
		if (mItems != null) {
			int position = mGridView.getFirstVisiblePosition();
			mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(),
					android.R.layout.simple_gallery_item, mItems));
			mGridView.setSelection(position);
		} else {
			mGridView.setAdapter(null);
		}
	}
	
	private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {
		@Override
		protected ArrayList<GalleryItem> doInBackground(Void... params) {
			return new FlickerFetchr().fetchItems(mResultPage, getActivity());
		}
		
		@Override
		protected void onPostExecute(ArrayList<GalleryItem> items) {
			if (mItems == null ) {
				mItems = items;
			} else {
				mItems.addAll(items);
			}
			setupAdapter();
		}
	}

}
