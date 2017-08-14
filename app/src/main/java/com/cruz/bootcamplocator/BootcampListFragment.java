package com.cruz.bootcamplocator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Acer on 14/08/2017.
 */

public class BootcampListFragment extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BootcampListAdapter adapter;
    private ArrayList<Bootcamp> bootcamps;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bootcamplist,container,false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        bootcamps = getArguments().getParcelableArrayList("bootCamp");
        adapter = new BootcampListAdapter(rootView.getContext(), bootcamps);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public static BootcampListFragment newInstance(ArrayList<Bootcamp> bootcamps) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("bootCamp",bootcamps);
        BootcampListFragment fragment = new BootcampListFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
