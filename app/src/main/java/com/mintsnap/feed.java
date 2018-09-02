package com.mintsnap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mintsnap.LoadAdapter;
import com.mintsnap.LoadModel;
import com.mintsnap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 */
public class feed extends Fragment implements LoadAdapter.OnItemClickListener {
    public static final String EXTRA_URL="imageUrl";
    public static final String EXTRA_CREATOR="creatorName";
    public static final String EXTRA_LIKES="likeCount";

    private RecyclerView mRecyclerView;
    private LoadAdapter mLoadAdapter;
    private ArrayList<LoadModel> mLoadModel;
    private RequestQueue mRequestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);



        mRecyclerView= view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLoadModel=new ArrayList<>();
        mRequestQueue= Volley.newRequestQueue(getContext());
        parseJSON();
        return view;
    }
    private void parseJSON() {
        String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
                                   
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response)
            {
                try {
                   JSONArray jsonArray=response.getJSONArray("hits");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject hit= jsonArray.getJSONObject(i);
                        String creatorName=hit.getString("name");
                        String imageUrl=hit.getString("name");
                        String likes=hit.getString("name");
                        mLoadModel.add(new LoadModel(imageUrl,creatorName,likes));
                    }


                    mLoadAdapter=new LoadAdapter(getActivity(),mLoadModel);
                    mRecyclerView.setAdapter(mLoadAdapter);
                    mLoadAdapter.setOnItemClickListener(feed.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position)
    {
        Intent detailIntent=new Intent(getContext(),DetailActivity.class);
        LoadModel clickedItem =mLoadModel.get(position);
        detailIntent.putExtra(EXTRA_URL,clickedItem.getmImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR,clickedItem.getmImageUrl());
        detailIntent.putExtra(EXTRA_LIKES,clickedItem.getmLikes());
        startActivity(detailIntent);
    }

}
