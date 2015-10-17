package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActividadConfiguracion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_configuracion);

        Button aceptar = (Button) findViewById(R.id.btn_config_aceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ipEd = (EditText) findViewById(R.id.ed_txt_config_ip);
                EditText portEd = (EditText) findViewById(R.id.ed_txt_config_puerto);
                String ip = ipEd.getText().toString();
                String port = portEd.getText().toString();
                if (!ip.equals("") && !port.equals("")) {
                    Comunicacion.getInstance().setIp(ip);
                    Comunicacion.getInstance().setPuerto(Integer.parseInt(port));
                    Comunicacion.getInstance().reintentar();
                    finish();
                }
            }
        });

        Button cancelar = (Button) findViewById(R.id.btn_config_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
