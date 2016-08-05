package idv.ron.sqlitedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryActivity extends AppCompatActivity {
    private EditText editText;
    private Button btnSearch;

    final MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);

    ListView item_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        List<Spot> spotList = getAddressList("");
        this.editText = (EditText) findViewById(R.id.editText);
        this.btnSearch = (Button) findViewById(R.id.btnSearch);

        item_list = (ListView) findViewById(R.id.lvSpotList);
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
                List<Spot> spotList = getAddressList(editText.getText().toString());
                openMapActivity(spotList.get(position).getAddress());
            }
        });


        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdapter();
            }
        });

        setAdapter();
    }


    public void openMapActivity(String address) {
        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        i.putExtra("ADDRESS", address);
        startActivity(i);
    }

    public List<Spot> getAddressList(String keyword) {
        List<Spot> spotList = helper.getAllSpots();
        if (keyword == null) {
            keyword = "";
        } else {
            keyword = keyword.toUpperCase();
        }

        List<Spot> spotResult = new ArrayList<Spot>();
        for (Spot spot : spotList) {
            if (spot.getName().toUpperCase().contains(keyword) || spot.getAddress().toUpperCase().contains(keyword)) {
                spotResult.add(spot);
            }
        }

        return spotResult;
    }

    public void setAdapter() {
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<Spot> spotList = getAddressList(this.editText.getText().toString());

        for (Spot spot : spotList) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("Name", spot.getName());
            item.put("Address", spot.getAddress());
            list.add(item);
        }


        final SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                list,
                android.R.layout.simple_list_item_2,
                new String[]{"Name", "Address"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        item_list.setAdapter(adapter);
    }
}