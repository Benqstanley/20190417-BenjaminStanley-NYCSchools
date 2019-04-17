package com.zianderthalapps.a20190415_benjaminstanley_nycschools;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//implements onSchoolClickListener so we can know when a school has been clicked from inside the
//ListFragment's RecyclerView
public class MainActivity extends AppCompatActivity implements SchoolListAdapter.onSchoolClickListener{
    //A member variable to store the state of a recycler view from the ListFragment
    Parcelable listState;
    boolean inDetail;
    boolean hasListState;
    String selectedSchoolName;
    String selectedSchoolParagraph;
    String selectedSchoolDbn;

    //Set up the screen and attach a ListFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("HasState")){
                setListState(savedInstanceState.getParcelable("ListState"));
            }
            if(savedInstanceState.getBoolean("InDetail")) {
                selectedSchoolName = savedInstanceState.getString(DetailFragment.schoolNameKey);
                selectedSchoolDbn = savedInstanceState.getString(DetailFragment.dbnKey);
                selectedSchoolParagraph = savedInstanceState.getString(DetailFragment.paragraghKey);
                onSchoolClick(0, selectedSchoolName, selectedSchoolParagraph, selectedSchoolDbn);
            }
        }else {
            setListFragment();
        }
    }
    //public method so that the ListFragment can store its scroll state
    public void setListState(Parcelable listState){
        hasListState = true;
        this.listState = listState;
    }

    //Setup and swap in a DetailScreen Fragment
    //With more time I'd make a layout for tablets that showed these fragments side by side
    @Override
    public void onSchoolClick(int position, String schoolName, String paragraph, String dbn) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = new DetailFragment();
        selectedSchoolName = schoolName;
        selectedSchoolParagraph = paragraph;
        selectedSchoolDbn = dbn;
        detailFragment.setSchoolName(schoolName);
        detailFragment.setParagraphOverview(paragraph);
        detailFragment.setDbn(dbn);
        fragmentTransaction.replace(R.id.container, detailFragment);
        fragmentTransaction.commit();
        inDetail = true;
    }

    //Swap out fragments and restore the scrollstate when back is pressed
    @Override
    public void onBackPressed() {
        setListFragment();
        inDetail = false;
    }

    //Helper method to setup the List Fragment
    public void setListFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ListFragment listFragment = new ListFragment();
        fragmentTransaction.replace(R.id.container, listFragment);
        fragmentTransaction.commit();
        if(listState != null){
            listFragment.setListState(listState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("InDetail", inDetail);
        outState.putBoolean("HasState", hasListState);
        if(inDetail){
            outState.putString(DetailFragment.schoolNameKey, selectedSchoolName);
            outState.putString(DetailFragment.paragraghKey, selectedSchoolParagraph);
            outState.putString(DetailFragment.dbnKey, selectedSchoolDbn);
        }
        if(hasListState){
            outState.putParcelable("ListState", listState);
        }
    }
}
