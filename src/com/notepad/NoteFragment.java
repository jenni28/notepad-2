package com.notepad;

import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class NoteFragment extends Fragment {
	private static final String TAG = "NoteFragment";
	
	public View onCreateView(LayoutInflater Inflater, ViewGroup Group, Bundle SavedInstance) {
		// Inflate the note_fragment layout, adding an OnTouchListener to each EditText to workaround the TabHost
		// stealing input focus when the user selects the content (see http://code.google.com/p/android/issues/detail?id=2516)
		Log.d(TAG, "Creating NoteFragment view, adding OnTouchListener to each EditText view");
		View view = Inflater.inflate(R.layout.note_fragment, Group, false);
		Vector<EditText> texts = new Vector<EditText>();
		texts.add((EditText)view.findViewById(R.id.noteTitle));
		texts.add((EditText)view.findViewById(R.id.noteContent));
		for (final EditText t : texts) {
			if (t != null) {
				t.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View View, MotionEvent Event) {
						t.requestFocusFromTouch();
						return false;
					}
				});
			}
		}
		return view;
	}
}
