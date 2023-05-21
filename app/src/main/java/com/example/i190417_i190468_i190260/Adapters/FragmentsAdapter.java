package com.example.i190417_i190468_i190260.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.i190417_i190468_i190260.Fragments.ExercisesFragment;
import com.example.i190417_i190468_i190260.Fragments.HistoryFragment;
import com.example.i190417_i190468_i190260.Fragments.LeaderboardFragment;
import com.example.i190417_i190468_i190260.Fragments.StatsFragment;


public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new StatsFragment();
        } else if (position == 1) {
            return new ExercisesFragment();
        } else if (position == 2) {
            return new HistoryFragment();
        } else if (position == 3) {
            return new LeaderboardFragment();
        }
        return new StatsFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (position == 0) {
            title = "Stats";
        } else if (position == 1) {
            title = "Exercises";
        } else if (position == 2) {
            title = "History";
        } else if (position == 3) {
            title = "Leaderboard";
        }
        return title;
    }
}
