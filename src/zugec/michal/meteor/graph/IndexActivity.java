package zugec.michal.meteor.graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class IndexActivity extends ListActivity {
	public static String INTENT_BUNDLE_TEXT = "text";
	
	public static String URL = "";
	private String UrlBase = "http://smrst.meteory.sk/rmob/";
    private static ArrayList<String> texts = new ArrayList<String>();

    private static class SelectTextAdapter extends BaseAdapter {
        private final Context mContext;

        public SelectTextAdapter(Context context) {
                super();
                this.mContext = context;
        }        
        public int getCount() {
                return texts.size();
        }
        public Object getItem(int position) {
                return texts.get(position);
        }
        public long getItemId(int position) {
                return position;
        }
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                TextView tv = (TextView)inflater.inflate(R.layout.index_files,  null);
                tv.setText((String)getItem(position));                                
                return tv;
        }

    }

    private void import_data(){
		try {
			URL url = new URL(UrlBase);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while((line=br.readLine())!=null){
				// Copy & pasted (a bit changed) regexp magic
				// FIXME: any hero - please help and reduce this huge code !!!
				if (line.matches(".*<a\\s+href.*rmob.TXT.*")){
				       Pattern link = Pattern.compile("href=\"[^>]*\">.*</[aA]>");;
				        Pattern htmltag = Pattern.compile("<a\\b[^>]*href=\"[^>]*>(.*?)</a>");
				        Matcher tagmatch = htmltag.matcher(line);
				        if (tagmatch.find()) {
				                Matcher matcher = link.matcher(tagmatch.group());
				                matcher.find();
				                String s_link = matcher.group().replaceFirst("[^>]*>", "").replaceFirst("</[aA]>", "");
								texts.add(s_link);
				        }
				}
			}
			br.close();
		} catch (MalformedURLException e){
//			Toast.makeText(context, "Error: maiformed URL", Toast.LENGTH_LONG).show();
			e.printStackTrace();			
		} catch (Exception e) {
//			Toast.makeText(context, "Internet connection error:\n"
//					              + "    Can't fetch data", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
    	
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        import_data();
        
        setListAdapter(new SelectTextAdapter(this));
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     URL = UrlBase + (String)parent.getItemAtPosition(position);
                	 startActivity(new Intent(IndexActivity.this, MeteorGraphActivity.class));
                 }
         });                   
    }

}
