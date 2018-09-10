package com.github.machadowma.paisfilhos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PaiActivity extends AppCompatActivity {
    private Integer paiId;
    private TextView textViewNomePai;
    private SQLiteDatabase bancoDados;
    private ListView listaFilhos;
    private ArrayList<Integer> arrayIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pai);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewNomePai = (TextView) findViewById(R.id.textViewNomePai);
        listaFilhos = (ListView) findViewById(R.id.listaFilhos);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                criarFilho();
            }
        });

        listaFilhos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                excluirFilho(arrayIds.get(i));
                return true;
            }
        });

        Intent intent = getIntent();
        paiId = Integer.parseInt(intent.getStringExtra("paiId"));
        carregarPai();

    }

    @Override
    protected void onResume() {
        super.onResume();
        listarFilhos();
    }

    private void carregarPai(){
        try {
            bancoDados = openOrCreateDatabase("paisfilhos", MODE_PRIVATE, null);
            String sql = "SELECT id,nome FROM pai WHERE id = "+paiId.toString();
            Cursor cursor_pai = bancoDados.rawQuery(sql,null);
            if(cursor_pai.moveToFirst()){
                textViewNomePai.setText(cursor_pai.getString(cursor_pai.getColumnIndex("nome")));
                listarFilhos();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarFilhos(){
        try {
            bancoDados = openOrCreateDatabase("paisfilhos", MODE_PRIVATE, null);
            Cursor cursor_pais = bancoDados.rawQuery("SELECT id,nome FROM filho WHERE id_pai = "+paiId.toString(),null);
            ArrayList<String> linhas = new ArrayList<String>();
            arrayIds = new ArrayList<Integer>();
            ArrayAdapter adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listaFilhos.setAdapter(adapter);
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

    private void criarFilho(){
        Intent intentCriarFilho = new Intent(this,CriarFilhoActivity.class);
        intentCriarFilho.putExtra("paiId", Integer.toString(paiId));
        startActivity(intentCriarFilho);
    }

    private void excluirFilho(Integer id){
        try{
            bancoDados = openOrCreateDatabase("paisfilhos", MODE_PRIVATE, null);
            String sql = "DELETE FROM filho WHERE id = " +id;
            bancoDados.execSQL(sql);
            bancoDados.close();
            listarFilhos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
