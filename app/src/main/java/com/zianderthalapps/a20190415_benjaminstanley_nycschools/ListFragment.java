package com.zianderthalapps.a20190415_benjaminstanley_nycschools;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
//Uses a Loader to avoid reloading the big dataSet on configuration change
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, View.OnClickListener {
    RecyclerView recyclerView;
    //integer to identify the AsyncTaskLoader
    TextView emptyText;
    static int SCHOOL_TASK_LOADER = 22;
    //Shows when the app is working on getting information
    ProgressBar progressBar;
    //Store the school information
    ArrayList<School> schools;
    //Adapter for the recycler view
    SchoolListAdapter adapter;
    //variable to save the scroll state of the recyclerview so that when we return
    // to this screen we'll be in the right spot
    Parcelable listState;


    public void setListState(Parcelable listState) {
        this.listState = listState;
    }

    //Create the screen for this fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment_layout, container, false);
        //Setup the recycler view passing in the parent activity as an onSchoolClickListener
        emptyText = rootView.findViewById(R.id.empty);
        emptyText.setOnClickListener(this);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.detail_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SchoolListAdapter(getContext(), null,(SchoolListAdapter.onSchoolClickListener) getActivity());
        recyclerView.setAdapter(adapter);
        //Restore scroll state if it's needed
        if(listState != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        progressBar = rootView.findViewById(R.id.progress_circular);
        //initiate the AsyncTaskLoader
        //If I had more time I'd use ViewModel and LiveData instead of this deprecated class
        getActivity().getSupportLoaderManager().initLoader(SCHOOL_TASK_LOADER, null, this);
        return rootView;
    }
    //Create the loader and pass in URL
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new SchoolTaskLoader(getContext(), getResources().getString(R.string.api_url_schools));
    }
    //Once finished, interpret the information, update the recyclerview and hide the progressbar
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if(!s.equals("Error")) {
            schools = (ArrayList<School>) JSONUtilities.extractSchoolsFromJson(s);
            adapter.setSchoolList(schools);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);

    }
    //Get the scroll state and save it in the MainActivity for reuse
    @Override
    public void onStop() {
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        ((MainActivity) getActivity()).setListState(listState);
        super.onStop();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportLoaderManager().initLoader(SCHOOL_TASK_LOADER, null, this);
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
    }

    static class SchoolTaskLoader extends AsyncTaskLoader<String>{
        String url;
        String response;

        public SchoolTaskLoader(Context context, String url){
            super(context);
            this.url = url;
        }
        //If we already have the result, just return it
        //Otherwise load the result
        @Override
        protected void onStartLoading() {
            if(response != null){
                deliverResult(response);
            }else {
                onForceLoad();
            }
        }
        //Cache the result and deliver it
        @Override
        public void deliverResult(@Nullable String data) {
                response = data;
                super.deliverResult(data);
            }
        //Make the network calls off the main thread using our utility class
        @Nullable
        @Override
        public String loadInBackground() {
            return NetworkUtilities.fetchJSONData(getContext().getResources().getString(R.string.api_url_schools));
        }
    }
}
