package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ActividadMenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_menu_principal);
        Intent lanzador = getIntent();
        String usuarioLogeado = lanzador.getStringExtra("usuario_log");
        TextView usuario = (TextView) findViewById(R.id.tv_txt_usuario_menuPrincipal);
        usuario.setText(usuarioLogeado);
    }
}
