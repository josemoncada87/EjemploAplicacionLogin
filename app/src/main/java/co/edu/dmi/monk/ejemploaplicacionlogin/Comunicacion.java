package co.edu.dmi.monk.ejemploaplicacionlogin;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Created by josemoncada87 on 14/10/2015. *
 */
public class Comunicacion extends Observable implements Runnable {

    private static final String TAG = "Comunicacion";
    private static Comunicacion ref;
    private Socket s;
    private boolean corriendo;

    private Comunicacion() {
        s = null;
        corriendo = true;
        Log.d(TAG, "[ INSTACIA DE COMUNICACIÓN CONSTRUIDA ]");
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
        Log.d(TAG, "[ HILO DE COMUNICACIÓN INICIADO ]");
        while (corriendo) {
            try {
                if (s == null) {
                    if (!conectar()) {
                        setChanged();
                        notifyObservers("No conectado");
                        clearChanged();
                    }
                } else {
                    recibir();
                }
                Thread.sleep(500);
            } catch (IOException e) {
                //e.printStackTrace();
                Log.d(TAG, "[ SE PERDIÓ LA CONEXIÓN CON EL SERVIDOR ]");
                corriendo = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            s = null;
        }
    }

    private boolean conectar() {
        try {
            InetAddress dirServidor = InetAddress.getByName("10.0.2.2");
            s = new Socket(dirServidor, 5000);
            Log.d(TAG, "[ CONECTADO CON: " + s.toString() + " ]");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            notifyObservers("no_conectado");
            clearChanged();
        }
    }
}
