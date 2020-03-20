package com.jf.skinchange.ui.cpx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jf.skinchange.R;

import java.util.ArrayList;
import java.util.List;

public class ChildFragment extends Fragment {

    private ListAdapter adapter;

    private String tag;

    public ChildFragment(String tag) {
        this.tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nested_child, container, false);
        RecyclerView list = root.findViewById(R.id.list_view);
        adapter = new ListAdapter(getContext());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        List<String> datalist = new ArrayList<>();
        datalist.add("item-1"+tag);
        datalist.add("item-2"+tag);
        datalist.add("item-3"+tag);
        datalist.add("item-4"+tag);
        datalist.add("item-5"+tag);
        datalist.add("item-6"+tag);
        datalist.add("item-7"+tag);
        datalist.add("item-8"+tag);
        datalist.add("item-9"+tag);
        datalist.add("item-10"+tag);
        datalist.add("item-11"+tag);
        adapter.setData(datalist);

        return root;
    }

}
