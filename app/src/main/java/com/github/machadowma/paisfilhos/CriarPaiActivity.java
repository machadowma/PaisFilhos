package com.github.machadowma.paisfilhos;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CriarPaiActivity extends AppCompatActivity {
    private EditText editNomePai;
    private Button buttonCriarPai;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_pai);

        editNomePai = (EditText) findViewById(R.id.editNomePai);
        buttonCriarPai = (Button) findViewById(R.id.buttonCriarPai);

        buttonCriarPai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravarPai();
            }
        });
    }

    private void gravarPai(){
        String nome = editNomePai.getText().toString();
        try {
            bancoDados = openOrCreateDatabase("paisfilhos",MODE_PRIVATE,null);
            bancoDados.execSQL("INSERT INTO pai (nome) VALUES ('"+nome+"')");
            bancoDados.close();
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}