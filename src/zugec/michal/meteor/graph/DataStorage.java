package zugec.michal.meteor.graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

public class DataStorage{
	   private static final String DATABASE_NAME = "example.db";
	   private static final int DATABASE_VERSION = 1;
	   private static final String TABLE_NAME = "table1";
	 
	   private Context context;
	   private SQLiteDatabase db;
	 
	   private SQLiteStatement insertStmt;
	   private static final String INSERT = "insert into "
	      + TABLE_NAME + "(name) values (?)";
	   
	   private Integer max_value;
	 
	   public DataStorage(Context context) {
	      this.context = context;
	      OpenHelper openHelper = new OpenHelper(this.context);
	      this.db = openHelper.getWritableDatabase();
	      this.insertStmt = this.db.compileStatement(INSERT);
	   }
	 
	   public long insert(String name) {
	      this.insertStmt.bindString(1, name);
	      return this.insertStmt.executeInsert();
	   }
	 
	   public void deleteAll() {
	      this.db.delete(TABLE_NAME, null, null);
	   }
	 
	   public List<String> selectAll() {
	      List<String> list = new ArrayList<String>();
	      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "name" },
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
	 
	   private static class OpenHelper extends SQLiteOpenHelper {
	 
	      OpenHelper(Context context) {
	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }
	 
	      @Override
	      public void onCreate(SQLiteDatabase db) {
	         db.execSQL("CREATE TABLE " + TABLE_NAME
	        		 + " (id INTEGER PRIMARY KEY, name TEXT)");
	      }
	 
	      @Override
	      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
	         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	         onCreate(db);
	      }
	   }

	public ArrayList<ArrayList<Integer>> getFileData(String uRL) {
		ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
		try {
			URL url = new URL(uRL);
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
		} catch (MalformedURLException e){
			Toast.makeText(context, "Error: maiformed URL", Toast.LENGTH_LONG).show();
			e.printStackTrace();			
		} catch (Exception e) {
			Toast.makeText(context, "Internet connection error:\n"
					              + "    Can't fetch data", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return data;
	}

	public Integer getMaxValue() {
		return max_value;
	}}
