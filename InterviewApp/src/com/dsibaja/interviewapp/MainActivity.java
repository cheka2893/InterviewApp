package com.dsibaja.interviewapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dsibaja.interviewapp.HeadlinesFragment.OnHeadlineSelectedListener;

public class MainActivity extends ActionBarActivity implements OnHeadlineSelectedListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_articles);
		
			if(findViewById(R.id.fragment_container) != null){
				if(savedInstanceState != null){
					return;
				}
				
				HeadlinesFragment mFirstFragment = new HeadlinesFragment();
				mFirstFragment.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, mFirstFragment).commit();		
			}
			
			ActionBar bar = getSupportActionBar();
			bar.setDisplayHomeAsUpEnabled(true);
			bar.setTitle("Interview app");
			bar.setIcon(R.drawable.ic_menu_home);
		
	}

	@Override
	public void onArticleSelected(int position) {
		ArticleFragment mArticleFrag = (ArticleFragment)getSupportFragmentManager()
				.findFragmentById(R.id.article_fragment);	
		if(mArticleFrag != null){			
			mArticleFrag.updateArticleView(position);			
		}else{			
			ArticleFragment newFragment = new ArticleFragment();			
			Bundle args = new Bundle();			
			args.putInt(ArticleFragment.ARG_POSITION, position);			
			newFragment.setArguments(args);		
			FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
			mTransaction.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);
			mTransaction.replace(R.id.fragment_container, newFragment);			
			mTransaction.addToBackStack(null);
			mTransaction.commit();	
		}	
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) 
    	   {        
    	      case android.R.id.home:  
	    		 FragmentManager mManager = getSupportFragmentManager();
	    		 if(mManager.getBackStackEntryCount()>0)
	    			 mManager.popBackStack();
    	    	  
    	      default:            
    	         return super.onOptionsItemSelected(item);    
    	   }
    }
}