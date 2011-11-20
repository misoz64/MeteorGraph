package zugec.michal.meteor.graph;

import zugec.michal.meteor.graph.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class MeteorGraphActivity extends Activity {
    /** Called when the activity is first created. */
	private MeteorGraph mg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MeteorGraphView view = new MeteorGraphView(this);
        setContentView(view);
    }

	private class MeteorGraphView extends View {
		
		public MeteorGraphView(Context context) {
			super(context);
	        mg = new MeteorGraph(context);

			// TODO Auto-generated constructor stub
		}
		
		protected void onDraw (Canvas canvas)
		{
			super.onDraw(canvas);
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