package co.jlabs.jaime.jaime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Screen1 extends Activity {
    TextView new_session;
    ListView listView;
    Database db;
    ArrayList<String> barcodes;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        barcodes=new ArrayList<>();
        db = new Database(this);
        new_session= (TextView) findViewById(R.id.new_session);
        listView = (ListView) findViewById(R.id.listView);
        new_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Screen1.this,Screen2.class);
                startActivity(i);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) adapter.getItem(position);
                Intent i = new Intent(Screen1.this, Screen3.class);
                i.putExtra("session_name", data);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodes.clear();
        barcodes=db.getAllSess_Name();
        adapter = new Adapter(this,barcodes);
        listView.setAdapter(adapter);
    }
}
