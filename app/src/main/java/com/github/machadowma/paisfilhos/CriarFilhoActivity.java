package com.github.machadowma.paisfilhos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CriarFilhoActivity extends AppCompatActivity {
    private Integer paiId;
    private EditText editNomeFilho;
    private Button buttonCriarFilho;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_filho);

        Intent intent = getIntent();
        paiId = Integer.parseInt(intent.getStringExtra("paiId"));

        editNomeFilho = (EditText) findViewById(R.id.editNomeFilho);
        buttonCriarFilho = (Button) findViewById(R.id.buttonCriarFilho);

        buttonCriarFilho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravarFilho();
                finish();
            }
        });
    }

    private void gravarFilho(){
        String nome = editNomeFilho.getText().toString();
        try {
            bancoDados = openOrCreateDatabase("paisfilhos",MODE_PRIVATE,null);
            bancoDados.execSQL("INSERT INTO filho (nome,id_pai) VALUES ('"+nome+"',"+paiId.toString()+")");
            bancoDados.close();
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
