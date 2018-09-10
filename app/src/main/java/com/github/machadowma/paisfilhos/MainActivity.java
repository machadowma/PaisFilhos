package com.github.machadowma.paisfilhos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ListView listaPais;
    private ArrayList<Integer> arrayIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
                criarPai();
            }
        });

        listaPais = (ListView) findViewById(R.id.listaPais);

        listaPais.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                excluirPai(arrayIds.get(i));
                return true;
            }
        });

        listaPais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirPai(arrayIds.get(i));
            }
        });

        criarBanco();
        listarPais();

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarPais();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void criarBanco(){
        try {
            bancoDados = openOrCreateDatabase("paisfilhos", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS pai(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR)");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS filho(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR" +
                    ", id_pai INTEGER" +
                    ", FOREIGN KEY (id_pai) REFERENCES pai(id))");
            //bancoDados.execSQL("INSERT INTO pai(nome) VALUES ('Joao')");
            //bancoDados.execSQL("INSERT INTO pai(nome) VALUES ('Paulo')");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarPais(){
        try {
            bancoDados = openOrCreateDatabase("paisfilhos", MODE_PRIVATE, null);
            Cursor cursor_pais = bancoDados.rawQuery("SELECT id,nome FROM pai",null);
            ArrayList<String> linhas = new ArrayList<String>();
            arrayIds = new ArrayList<Integer>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listaPais.setAdapter(adapter);
            cursor_pais.moveToFirst();
            while(cursor_pais!=null){
                linhas.add(cursor_pais.getString(1));
                arrayIds.add(cursor_pais.getInt(0));
                cursor_pais.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void criarPai(){
        Intent intentCriarPai = new Intent(this,CriarPaiActivity.class);
        startActivity(intentCriarPai);
    }

    private void excluirPai(Integer id){
        try{
            bancoDados = openOrCreateDatabase("paisfilhos", MODE_PRIVATE, null);
            String sql = "DELETE FROM pai WHERE id = " +id;
            bancoDados.execSQL(sql);
            bancoDados.close();
            listarPais();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirPai(Integer paiId){
        //Toast.makeText(this, "Abrir pai com id = " + Integer.toString(paiId), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,PaiActivity.class);
        intent.putExtra("paiId", Integer.toString(paiId));
        startActivity(intent);
    }
}
