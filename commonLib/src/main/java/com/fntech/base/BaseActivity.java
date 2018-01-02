package com.fntech.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.fntech.commonlib.R;

public abstract class BaseActivity extends Activity {

	public ImageView navLeft;
	public ImageView navMiddle;
	public ImageView navRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		if(actionBar!=null){
			actionBar.hide();
		}
	}

	public void initView(){
		navLeft = (ImageView) findViewById(R.id.bottom_nav_left);
		navLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveTaskToBack(true);
			}
		});
		
		navMiddle = (ImageView) findViewById(R.id.bottom_nav_middle);
		navMiddle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		navRight = (ImageView) findViewById(R.id.bottom_nav_right);
		navRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleRightClick();
			}
		});
	}
	
	public abstract void handleRightClick();
	
}
