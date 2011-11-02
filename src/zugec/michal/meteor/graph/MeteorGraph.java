package zugec.michal.meteor.graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

public class MeteorGraph {
	private ArrayList<ArrayList<Integer>> data;
	private Integer max_value;
	
	public MeteorGraph(Context context){
		import_data(context);
	}
	
	private void import_data(Context context){
		try {
			URL url = new URL("http://radio.data.free.fr/live_datas/Vsetin_112011rmob.TXT");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			data = new ArrayList<ArrayList<Integer>>();
			String line;
			max_value = 0;
			int line_num = -1;
			while((line=br.readLine())!=null){
				line_num++;					
				if (line_num<=0){ continue; }
				String[] array_line=line.split("\\|");

				ArrayList<Integer> tmp_list = new ArrayList<Integer>();
				for(int i=0;i<array_line.length;i++){
					Integer val = 0;
					try{
						val = Integer.valueOf(array_line[i].trim());
						if (val > max_value){
							max_value = val;
						}
					}
					catch(Exception e){
						val = -1;
					}
					tmp_list.add(val);
				}
				data.add(tmp_list);
			}
			br.close();
		} catch (Exception e) {
			Toast.makeText(context, "Internet connection error:\n"
					              + "    Can't fetch data", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	public void draw(Canvas canvas, int size){
		int cell_border = 1;
		int cell_size   = size/(64*cell_border);
		Log.i(new Integer(cell_border).toString(), new Integer(cell_size).toString());

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);

		// make the entire canvas white
		paint.setColor(Color.WHITE);
		canvas.drawPaint(paint);
		
		int canvas_x_offset = 20;
		int canvas_y_offset = 20;
		int canvas_x_size = (31*cell_size)+(32*cell_border);
		int canvas_y_size = (24*cell_size)+(25*cell_border);

		paint.setColor(Color.BLACK);
		canvas.drawRect(canvas_x_offset, canvas_y_offset,
				canvas_x_offset+canvas_x_size, 
				canvas_y_offset+canvas_y_size, paint);
		
		// color palette
		int palette_offset = 5;
		canvas.drawRect(canvas_x_offset+canvas_x_size+palette_offset,
				canvas_y_offset,
				canvas_x_offset+canvas_x_size+palette_offset+(2*cell_border)+cell_size, 
				canvas_y_offset+canvas_y_size, paint);
		int x_pos = canvas_x_offset + canvas_x_size+palette_offset+cell_border;
		int y_pos = 20;
		for(int y=0;y<24;y++){
			y_pos+=cell_border;
			paint.setColor(color_palette[y]);
			canvas.drawRect(x_pos, y_pos, x_pos+cell_size, y_pos+cell_size, paint);
			y_pos+=cell_size;
		}
		paint.setColor(Color.BLACK);
		// hour labels
		canvas.drawText("00", cell_border, canvas_y_offset+cell_size+cell_border, paint);
		canvas.drawText("hod", cell_border, canvas_y_offset*2+1, paint);
		canvas.drawText("06", cell_border, canvas_y_offset+(7*cell_size)+(7*cell_border), paint);
		canvas.drawText("12", cell_border, canvas_y_offset+(13*cell_size)+(13*cell_border), paint);
		canvas.drawText("18", cell_border, canvas_y_offset+(19*cell_size)+(19*cell_border), paint);
		canvas.drawText("23", cell_border, canvas_y_offset+(24*cell_size)+(24*cell_border), paint);
		//day labels
		canvas.drawText("1   Days --->", canvas_x_offset, canvas_y_offset-cell_border, paint);
		canvas.drawText("15", canvas_x_offset+(14*cell_size)+(14*cell_border), canvas_y_offset-cell_border, paint);
		canvas.drawText("31", canvas_x_offset+(30*cell_size)+(30*cell_border), canvas_y_offset-cell_border, paint);
		//color palete labels
		canvas.drawText("0", canvas_x_offset+canvas_x_size+(2*palette_offset)+cell_size+(2*cell_border),
				canvas_y_offset, paint);
		canvas.drawText("mid", canvas_x_offset+canvas_x_size+(2*palette_offset)+cell_size+(2*cell_border),
				canvas_y_offset+(12*cell_size)+(12*cell_border), paint);
		canvas.drawText("max", canvas_x_offset+canvas_x_size+(2*palette_offset)+cell_size+(2*cell_border),
				canvas_y_offset+(23*cell_size)+(23*cell_border), paint);

		paint.setColor(Color.WHITE);
		//canvas cells
		x_pos = canvas_x_offset;
		for(int x=0;x<31;x++){
			y_pos = canvas_y_offset;
			x_pos += cell_border;
			for(int y=0;y<24;y++){
				y_pos+=cell_border;
				int cell_color = Color.BLACK;
				try{
					Integer val = data.get(x).get(y+1);
					if (val>=0){
						cell_color = color_palette[(int)(((float)val/(float)max_value)*23.0)];
					}
				} catch(Exception e){
					
				}
				paint.setColor(cell_color);
				canvas.drawRect(x_pos, y_pos,
						x_pos+cell_size, y_pos+cell_size, paint);
						
				y_pos+=cell_size;
			}
			x_pos+=cell_size;
		}
	}		
	
	int[] color_palette = {
Color.rgb(Integer.decode("#07").intValue(),
		Integer.decode("#43").intValue(),
		Integer.decode("#ff").intValue()),
Color.rgb(Integer.decode("#00").intValue(),
		Integer.decode("#53").intValue(),
		Integer.decode("#ff").intValue()),
Color.rgb(Integer.decode("#07").intValue(),
		Integer.decode("#5c").intValue(),
		Integer.decode("#ff").intValue()),
Color.rgb(Integer.decode("#02").intValue(),
		Integer.decode("#6c").intValue(),
		Integer.decode("#ff").intValue()),
Color.rgb(Integer.decode("#08").intValue(),
		Integer.decode("#89").intValue(),
		Integer.decode("#fe").intValue()),
		
Color.rgb(Integer.decode("#00").intValue(),
		Integer.decode("#a0").intValue(),
		Integer.decode("#f4").intValue()),
Color.rgb(Integer.decode("#00").intValue(),
		Integer.decode("#c1").intValue(),
		Integer.decode("#ff").intValue()),
Color.rgb(Integer.decode("#00").intValue(),
		Integer.decode("#e7").intValue(),
		Integer.decode("#fe").intValue()),
Color.rgb(Integer.decode("#3e").intValue(),
		Integer.decode("#fe").intValue(),
		Integer.decode("#e9").intValue()),
Color.rgb(Integer.decode("#74").intValue(),
		Integer.decode("#f3").intValue(),
		Integer.decode("#c8").intValue()),

Color.rgb(Integer.decode("#6d").intValue(),
		Integer.decode("#ff").intValue(),
		Integer.decode("#b3").intValue()),
Color.rgb(Integer.decode("#79").intValue(),
		Integer.decode("#ff").intValue(),
		Integer.decode("#f4").intValue()),
Color.rgb(Integer.decode("#8c").intValue(),
		Integer.decode("#fb").intValue(),
		Integer.decode("#70").intValue()),
Color.rgb(Integer.decode("#ac").intValue(),
		Integer.decode("#fe").intValue(),
		Integer.decode("#5e").intValue()),
Color.rgb(Integer.decode("#c7").intValue(),
		Integer.decode("#f9").intValue(),
		Integer.decode("#58").intValue()),

Color.rgb(Integer.decode("#ec").intValue(),
		Integer.decode("#fe").intValue(),
		Integer.decode("#4a").intValue()),
Color.rgb(Integer.decode("#fd").intValue(),
		Integer.decode("#e2").intValue(),
		Integer.decode("#00").intValue()),
Color.rgb(Integer.decode("#ff").intValue(),
		Integer.decode("#b6").intValue(),
		Integer.decode("#01").intValue()),
Color.rgb(Integer.decode("#fe").intValue(),
		Integer.decode("#a2").intValue(),
		Integer.decode("#03").intValue()),
Color.rgb(Integer.decode("#ff").intValue(),
		Integer.decode("#8c").intValue(),
		Integer.decode("#00").intValue()),

Color.rgb(Integer.decode("#fa").intValue(),
		Integer.decode("#72").intValue(),
		Integer.decode("#04").intValue()),
Color.rgb(Integer.decode("#f4").intValue(),
		Integer.decode("#5d").intValue(),
		Integer.decode("#02").intValue()),
Color.rgb(Integer.decode("#ff").intValue(),
		Integer.decode("#4e").intValue(),
		Integer.decode("#02").intValue()),
Color.rgb(Integer.decode("#fc").intValue(),
		Integer.decode("#00").intValue(),
		Integer.decode("#01").intValue()),
		
	};

}
