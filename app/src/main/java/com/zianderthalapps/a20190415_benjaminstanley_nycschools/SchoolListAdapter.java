package com.zianderthalapps.a20190415_benjaminstanley_nycschools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
//Tells the RecyclerView how to populate itself
public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.SchoolViewHolder> {
    private List<School> schools;
    private Context context;
    private onSchoolClickListener onSchoolClickListener;
    //An interface so we can receive clicks in the MainActivity
    interface onSchoolClickListener{
        public void onSchoolClick(int position, String schoolName, String paragraph, String dbn);
    }
    //Constructor method that sets up the Adapter
    public SchoolListAdapter(Context context, List<School> schoolNames, onSchoolClickListener onSchoolClickListener){
        this.schools = schoolNames;
        this.context = context;
        this.onSchoolClickListener = onSchoolClickListener;
    }
    //Allows us to add the data after it's been loaded off the main thread
    public void setSchoolList(List<School> schools){
        this.schools = schools;
        notifyDataSetChanged();
    }
    //Sets up the layout for the list items
    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, viewGroup, false);
        return new SchoolViewHolder(view);
    }
    //Fills in the relevant data
    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder schoolViewHolder, int i) {
        schoolViewHolder.nameTV.setText(schools.get(i).getSchoolName());
    }
    //Tells the RecyclerView how many items it's displaying
    @Override
    public int getItemCount() {
        if(schools == null){
            return 0;
        }
        return schools.size();
    }
    // Custom ViewHolder for list items
    class SchoolViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener{

        TextView nameTV;
        //Identify relevant views that need to be modified when the viewholder is bound with data
        //and adds itself as an onClickListener to the view it's holding
        public SchoolViewHolder(View schoolView){
            super(schoolView);
            schoolView.setOnClickListener(this);
            nameTV = schoolView.findViewById(R.id.list_item_school_name);
        }
        //Calls the onSchoolClick callback in the main activity to tell us when to swap fragments
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onSchoolClickListener.onSchoolClick(position,
                    schools.get(position).getSchoolName(), schools.get(position).getOverviewParagraph(), schools.get(position).getDbn());
        }
    }
}
