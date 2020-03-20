package com.jf.skinchange.ui.cpx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jf.skinchange.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class NestedFragment extends Fragment {

    private List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nested, container, false);
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        fragments.add(new ChildFragment("A"));
        fragments.add(new ChildFragment("B"));
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "单元页-"+position;
            }
        });
        TabLayout tableLayout = root.findViewById(R.id.view_tab);
        tableLayout.setupWithViewPager(viewPager);
        return root;
    }
}
