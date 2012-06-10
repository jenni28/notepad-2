package com.notepad;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;

/**
 * Main activity, based off the TabActivity example at http://developer.android.com/reference/android/app/TabActivity.html
 * @author Neville Samuell
 */
public class NotepadActivity extends FragmentActivity {
	TabHost m_tabHost;
	TabManager m_tabManager;
	
    public void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // Setup the TabHost to render the view
        m_tabHost = (TabHost)findViewById(android.R.id.tabhost);
        m_tabHost.setup();
        m_tabManager = new TabManager(this, m_tabHost, R.id.realtabcontent);
        
        // Add some tabs
        m_tabManager.addTab(m_tabHost.newTabSpec("home").setIndicator("Home"),
        		HomeFragment.class, null);
        m_tabManager.addTab(m_tabHost.newTabSpec("note").setIndicator("Note"),
        		NoteFragment.class, null);
        
        // Load the current tab from the bundle
        if (SavedInstance != null) {
        	m_tabHost.setCurrentTabByTag(SavedInstance.getString("tab"));
        }
    }
    
    protected void onSaveInstanceState(Bundle OutState) {
    	super.onSaveInstanceState(OutState);
    	OutState.putString("tab", m_tabHost.getCurrentTabTag());
    }
    
    /**
     * Helper class to bind Fragments to tabs.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
    	private final FragmentActivity m_activity;
    	private final TabHost m_tabHost;
    	private final int m_containerId;
    	private final HashMap<String, TabInfo> m_tabs = new HashMap<String, TabInfo>();
    	TabInfo m_lastTab;
    	
    	static final class TabInfo {
    		private final String m_tag;
    		private final Class<?> m_class;
    		private final Bundle m_args;
    		private Fragment m_fragment;
    		
    		TabInfo(String Tag, Class<?> Class, Bundle Args) {
    			m_tag = Tag;
    			m_class = Class;
    			m_args = Args;
    		}
    	}
    	
    	static class DummyTabFactory implements TabHost.TabContentFactory {
    		private final Context m_context;
    		
    		public DummyTabFactory(Context Context) {
    			m_context = Context;
    		}
    		
    		public View createTabContent(String Tag) {
    			View v = new View(m_context);
    			v.setMinimumHeight(0);
    			v.setMinimumWidth(0);
    			return v;
    		}
    	}
    	
    	public TabManager(FragmentActivity Activity, TabHost TabHost, int ContainerId) {
    		m_activity = Activity;
    		m_tabHost = TabHost;
    		m_containerId = ContainerId;
    		m_tabHost.setOnTabChangedListener(this);
    	}
    	
    	public void addTab(TabHost.TabSpec TabSpec, Class<?> Class, Bundle Args) {
    		TabSpec.setContent(new DummyTabFactory(m_activity));
    		String tag = TabSpec.getTag();
    		
    		TabInfo info = new TabInfo(tag, Class, Args);
    		
    		// Check for previously created fragment and deactivate it if found
    		info.m_fragment = m_activity.getSupportFragmentManager().findFragmentByTag(tag);
    		if (info.m_fragment != null && !info.m_fragment.isDetached()) {
    			FragmentTransaction trans = m_activity.getSupportFragmentManager().beginTransaction();
    			trans.detach(info.m_fragment);
    			trans.commit();
    		}
    		m_tabs.put(tag,  info);
    		m_tabHost.addTab(TabSpec);
    	}
    	
    	public void onTabChanged(String TabId) {
    		// Lookup the tab id, and see if it's actually changed
    		TabInfo newTab = m_tabs.get(TabId);
    		if (m_lastTab != newTab) {
    			// Detach the current fragment, if active
    			FragmentTransaction trans = m_activity.getSupportFragmentManager().beginTransaction();
    			if (m_lastTab != null) {
    				if (m_lastTab.m_fragment != null) {
    					trans.detach(m_lastTab.m_fragment);
    				}
    			}
    			
    			// Switch to the new tab
    			if (newTab != null) {
    				// Instantiate the tab's Fragment, if necessary
    				if (newTab.m_fragment == null) {
    					newTab.m_fragment = Fragment.instantiate(m_activity, newTab.m_class.getName(), newTab.m_args);
    					trans.add(m_containerId, newTab.m_fragment, newTab.m_tag);
    				} else {
    					trans.attach(newTab.m_fragment);
    				}
    			}
    			
    			// Keep track of the current tab and execute the transaction
    			m_lastTab = newTab;
    			trans.commit();
    			m_activity.getSupportFragmentManager().executePendingTransactions();
    		}
    	}
    }
}