package c.sqlite;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Text> texts;
    private Button add,edit,delete;

    private TextView textId;
    private EditText textEdit;
    private ListView listView;
    DataBaseAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBaseAdapter(this);
        db.open();
        add = (Button) findViewById(R.id.add);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        textId = (TextView) findViewById(R.id.textView);
        textEdit = (EditText) findViewById(R.id.editText);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                long insertId = db.insertText(text);
                Toast.makeText(getApplicationContext(),"Dodałem wpis " + insertId, Toast.LENGTH_LONG).show();
                textId.setText("Id: " + insertId);
                addTextsToList();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textid = textId.getText().toString();
                if( textid.equals("") || (textid.trim()).equals("Id:")){
                    return;
                }
                long id = Long.parseLong(textid.replaceAll("\\D", ""));
                String text = textEdit.getText().toString();
                if(db.updateText(id,text)){
                    Toast.makeText(getApplicationContext(),"Zedytowałem wpis " + id, Toast.LENGTH_LONG).show();
                }
                addTextsToList();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textid = textId.getText().toString();
                if( textid.equals("") || (textid.trim()).equals("Id:")){
                    return;
                }
                long id = Long.parseLong(textid.replaceAll("\\D", ""));
                if(db.deleteText(id)){
                    Toast.makeText(getApplicationContext(),"Usunąłem wpis " + id, Toast.LENGTH_LONG).show();
                    textId.setText("Id: ");
                    textEdit.setText("");
                }
                addTextsToList();
            }
        });
    }

    private void getTextList(){
        Cursor cursor = db.getAll();
        texts = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()){
            while(cursor.isAfterLast()==false){
                Long id = cursor.getLong(0);
                String text = cursor.getString(1);
                texts.add(new Text(text,id));
                cursor.moveToNext();
            }
        }
    }
    private void addTextsToList(){
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        listView = (ListView) findViewById(R.id.listView);
        adapter.clear();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Text text = texts.get(position);
                textEdit.setText(text.getText());
                textId.setText("Id: " + text.getId());
                Log.d("test", "test pos" + position + " id" + id);
            }
        });
        getTextList();
        for(Text text:texts){
            adapter.add(text.getId() + ": " + text.getText());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addTextsToList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
