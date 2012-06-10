package com.notepad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
	public View onCreateView(LayoutInflater Inflater, ViewGroup Group, Bundle SavedInstance) {
		return Inflater.inflate(R.layout.home_fragment, Group, false);
	}
}
