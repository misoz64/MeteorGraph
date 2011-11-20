package zugec.michal.meteor.graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

public class DataStorage{
	   private static final String DATABASE_NAME = "database.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "observations";
	 
	   private Context context;
	   private SQLiteDatabase db;
	 
	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into "
	      + TABLE_NAME + "(filename, date, time, value) values (?,?,?,?)";
	   
	   private Integer max_value;
	 
	   public DataStorage(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      this.db = openHelper.getWritableDatabase();
	      this.insertStmt = this.db.compileStatement(INSERT);
	   }
	 
	   public long insert(String filename, String date, String time, Integer value) {
	      this.insertStmt.bindString(1, filename);
	      this.insertStmt.bindString(2, date);
	      this.insertStmt.bindString(3, time);
	      this.insertStmt.bindDouble(4, value);
	      return this.insertStmt.executeInsert();
	   }
	 
	   public void deleteAll() {
	      this.db.delete(TABLE_NAME, null, null);
	   }
	 
	   public List<String> selectAll() {
	      List<String> list = new ArrayList<String>();
	      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "filename" },
	        null, null, null, null, "name desc");
	      if (cursor.moveToFirst()) {
	         do {
	            list.add(cursor.getString(0));
	         } while (cursor.moveToNext());
	      }
	      if (cursor != null && !cursor.isClosed()) {
	         cursor.close();
	      }
	      return list;
	   }

	   
	public ArrayList<ArrayList<Integer>> getFileData(String UrlBase, String URL) {
		// TODO: check if there is any record for URL in the database
		// in case of true, fetch and return that data
        Cursor cursor = this.db.rawQuery("SELECT value from observations WHERE filename = \""+URL+"\"", null);
        if(cursor.getCount()==0){
        	return fetchHTTPData(UrlBase, URL);
        } else {
        	// FIXME: fetch data from database
        	return null;
        }
	}

	private ArrayList<ArrayList<Integer>>fetchHTTPData(String UrlBase, String URL){
		ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
		try {
			URL url = new URL(UrlBase+URL);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			data = new ArrayList<ArrayList<Integer>>();
			String line;
			max_value = 0;
			int line_num = -1;
			while((line=br.readLine())!=null){
				line_num++;					
				if (line_num<=0){ continue; }
				String[] array_line=line.split("\\|");

				Integer day = -1;
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
					if(i==0){
						day = val;
					} else{
						String month="00";
						String year="0000";
					    Pattern link = Pattern.compile("[^_]*_([\\d]{2})([\\d]{4})rmob.TXT");;
					    Matcher tagmatch = link.matcher(URL);
					    if (tagmatch.find()) {
					    	month = tagmatch.group(1);
					    	year  = tagmatch.group(2);
					    }
						String date = year+"-"+month+"-"+String.format("%02d", day);
						String time = String.format("%02d:00", i-1);
//						Log.i(URL, "Date:"+date+", time:"+time);
						insert(URL, date, time, val);
						tmp_list.add(val);
					}
				}
				data.add(tmp_list);
			}
			br.close();
		} catch (MalformedURLException e){
			Toast.makeText(context, "Error: maiformed URL", Toast.LENGTH_LONG).show();
			e.printStackTrace();			
		} catch (Exception e) {
			Toast.makeText(context, "Internet connection error:\n"
					              + "    Can't fetch data", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return data;
	}
	   
	   
	   private static class OpenHelper extends SQLiteOpenHelper {
	 
	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }
	 
	      @Override
	      public void onCreate(SQLiteDatabase db) {
	         db.execSQL("CREATE TABLE " + TABLE_NAME
	        		 + " (id INTEGER PRIMARY KEY, filename TEXT, date TEXT, time TEXT, value INTEGER)");
	      }
	 
	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	         onCreate(db);
	      }
	   }

	public Integer getMaxValue() {
		return max_value;
	}

	public void close(){
		db.close();
	}
}
