package com.bookcoast.post_it;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by Adi on 11/02/17.
 */
public class Tab2 extends Fragment {
    private RecyclerView recylceview;
    private DatabaseReference mDatabase;
    private Query userfEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recylceview = (RecyclerView) rootView.findViewById(R.id.list);
        recylceview.setHasFixedSize(true);
        recylceview.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Query userfEvents = mDatabase.orderByChild("type").equalTo("intern");
        FirebaseRecyclerAdapter<Data,PostviewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Data, PostviewHolder>(
                Data.class,
                R.layout.list_cards,
                PostviewHolder.class,
                userfEvents
        ) {
            @Override
            protected void populateViewHolder(PostviewHolder viewHolder, Data model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setdesc(model.getDescription());
                viewHolder.setelig(model.getEligibility());
                viewHolder.setdate(model.getDate());
                viewHolder.setcontact(model.getContact());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getImgurl());
            }
        };
        recylceview.setAdapter(firebaseRecyclerAdapter);


    }
    public static class PostviewHolder extends RecyclerView.ViewHolder{
        View mview;
        public PostviewHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setTitle(String title){
            TextView posttitle = (TextView) mview.findViewById(R.id.tit_rec);
            posttitle.setText(title);
        }
        public void setdesc(String desc){
            TextView posttitle = (TextView) mview.findViewById(R.id.desc_rec);
            posttitle.setText(desc);
        }
        public void setelig(String elig){
            TextView posttitle = (TextView) mview.findViewById(R.id.eligibility_rec);
            posttitle.setText("Eligibility: "+elig);
        }
        public void setdate(String date){
            TextView posttitle = (TextView) mview.findViewById(R.id.date_rec);
            posttitle.setText("Date: "+date);
        }
        public void setcontact(String contact){
            TextView posttitle = (TextView) mview.findViewById(R.id.contact_rec);
            posttitle.setText("Contact: "+contact);
        }
        public void setImage(Context cts, String imgurl){
            ImageView postimg = (ImageView) mview.findViewById(R.id.img_rec);
            Picasso.with(cts).load(imgurl).into(postimg);

        }
    }
}
