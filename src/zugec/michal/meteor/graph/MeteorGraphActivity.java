package zugec.michal.meteor.graph;

import zugec.michal.meteor.graph.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MeteorGraphActivity extends Activity {
    /** Called when the activity is first created. */
	private MeteorGraph mg;
	private MeteorGraphView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        view = new MeteorGraphView(this);
        setContentView(view);
    }

    public boolean onCreateOptionsMenu(Menu menu){
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.main_menu, menu);    	
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.text:
            	startActivityForResult(new Intent(this, IndexActivity.class), 42);
                break;
        }
        return true;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	  super.onActivityResult(requestCode, resultCode, data);
    	  if(resultCode==RESULT_OK && requestCode==1){
    		  view.invalidate();
    	  }
    	 }
    
	private class MeteorGraphView extends View {
		
		public MeteorGraphView(Context context) {
			super(context);
	        mg = new MeteorGraph(context);
		}
		
		protected void onDraw (Canvas canvas)
		{
			super.onDraw(canvas);
			setTitle(!IndexActivity.URL.isEmpty() ? IndexActivity.URL : "Meteor Graph");
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int width = metrics.widthPixels;
			int height = metrics.heightPixels;
			int smaller_size = (width > height) ? width : height;

			mg.draw(canvas, smaller_size);
			

		}
    }
	protected void onDestroy(){
		super.onDestroy();
		mg.close();
	}
}