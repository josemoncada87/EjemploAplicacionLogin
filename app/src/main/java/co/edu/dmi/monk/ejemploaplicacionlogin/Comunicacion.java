package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

/**
 * Created by josemoncada87 on 14/10/2015. *
 */
public class Comunicacion extends Observable implements Runnable {

    private static final String TAG = "COM-ANDROID";
    private static Comunicacion ref;
    private Socket s;
    private boolean corriendo;

    public Comunicacion() {
        s = null;
        corriendo = true;
        Log.d(TAG, "Comunicacion con el servidor instanciada");



        System.out.println("---------------------------------------------------------------------");
    }

    public static Comunicacion getInstance() {
        if(ref == null){
            ref =  new Comunicacion();
            Thread t =  new Thread(ref);
            t.start();
        }
        return ref;
    }


    @Override
    public void run() {
        Log.d(TAG, "Hilo de comunicacion iniciado");
        while (corriendo) {
            try {
                if (s == null) {
                   conectar();
                } else {
                    recibir();
                }
                Thread.sleep(500);
            } catch (IOException e) {
                e.printStackTrace();
                s = null;
                corriendo = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void conectar() throws IOException {
        InetAddress dirServidor = InetAddress.getByName("10.0.2.2");
        s = new Socket(dirServidor, 5000);
        Log.d(TAG, "Conectado con: " + s.toString());
    }

    private void recibir() throws IOException {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        String recibido = dis.readUTF();
        manejarLogin(recibido);
        manejarSignup(recibido);
    }

    private void manejarSignup(String recibido) {
        if (recibido.contains("signup_resp:")) {
            String[] partes = recibido.split(":");
            int resultado = Integer.parseInt(partes[1]);
            setChanged();
            switch (resultado) {
                case 0:
                    notifyObservers("usuario_existe");
                    break;
                case 1:
                    notifyObservers("usuario_registrado");
                    break;
            }
            clearChanged();
        }
    }

    private void manejarLogin(String recibido) {
        if (recibido.contains("login_resp:")) {
            String[] partes = recibido.split(":");
            int resultado = Integer.parseInt(partes[1]);
            setChanged();
            switch (resultado) {
                case 0:
                    notifyObservers("usuario_no_existe");
                    break;
                case 1:
                    notifyObservers("login_ok");
                    break;
                case 2:
                    notifyObservers("login_no_ok");
                    break;
            }
            clearChanged();
        }
    }

    public void enviar(String msn) {
        if(s != null) {
            try {
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(msn);
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            setChanged();
            notifyObservers("No conectado");
            clearChanged();
        }
    }
}
