package com.zianderthalapps.a20190415_benjaminstanley_nycschools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailFragment extends Fragment {
    static String schoolNameKey = "school_name";
    static String paragraghKey = "paragraph";
    static String dbnKey = "dbn";
    TextView nameTV;
    TextView paragraphTV;
    String schoolName;
    String paragraphOverview;
    String dbn;
    SATScores satScores;
    TextView mathScore;
    TextView readingScore;
    TextView writingScore;
    RelativeLayout sATLayout;
    TextView emptyTextView;
    //Set the school name for this fragment
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    //Set the paragraph overview
    public void setParagraphOverview(String paragraphOverview) {
        this.paragraphOverview = paragraphOverview;
    }
    //Set the unique identifier for this school to be used in our SAT score lookup
    public void setDbn(String dbn) {
        this.dbn = dbn;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        (new SatAsyncTask()).execute();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(schoolNameKey, schoolName);
        outState.putString(paragraghKey, paragraphOverview);
        outState.putString(dbnKey, dbn);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            schoolName = savedInstanceState.getString(schoolNameKey);
            paragraphOverview = savedInstanceState.getString(paragraghKey);
            dbn = savedInstanceState.getString(dbnKey);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment_layout, container, false);
        //Setup our views
        //I didn't use ButterKnife only because I'm less familiar with it and was under the time crunch
        nameTV = rootView.findViewById(R.id.school_name_detail);
        nameTV.setText(schoolName);
        paragraphTV = rootView.findViewById(R.id.paragraph_detail);
        mathScore = rootView.findViewById(R.id.math_score);
        readingScore = rootView.findViewById(R.id.crit_reading_score);
        writingScore = rootView.findViewById(R.id.writing_score);
        sATLayout = rootView.findViewById(R.id.sat_info);
        emptyTextView = rootView.findViewById(R.id.no_sat_information);
        paragraphTV.setText(paragraphOverview);
        //Create the AsyncTask to load SAT data
        return rootView;
    }
    //Async Task to load SAT data
    class SatAsyncTask extends AsyncTask<String, Void, String>{
        //Load the SAT JSON information off of the UI thread
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder baseURL = new StringBuilder(getResources().getString(R.string.api_url_sat));
            baseURL.append("?dbn=").append(dbn);
            return NetworkUtilities.fetchJSONData(baseURL.toString());
        }
        //Parse the JSON Response and update UI
        @Override
        protected void onPostExecute(String result) {
            Log.e("JSONData", result);
            satScores = JSONUtilities.fetchSATScoresFromJSON(result);
            if(satScores != null) {
                Log.e("SAT Scores", " HERE");
                mathScore.setText(satScores.getMathAvg());
                readingScore.setText(satScores.getReadingAvg());
                writingScore.setText(satScores.getWritingAvg());
            }else {
                sATLayout.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
