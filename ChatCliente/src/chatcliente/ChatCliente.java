package chatcliente;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import static chatcliente.InterfazCliente.panelMensajes;

public class ChatCliente {

    public static void main(String[] args) {

        InterfazCliente IC = new InterfazCliente();
        IC.setVisible(true);
    }
}

class Recibir extends Thread {

    InputStream is;

    public Recibir(InputStream is) {
        this.is = is;
    }

    @Override
    public void run() {

        while (true) {
            try {
            //Recibimos mensajes
            byte[] recibido = new byte[500];
            is.read(recibido);
            String mensaje = new String(recibido);
            panelMensajes.setText(panelMensajes.getText() + "\n" + mensaje);
            } catch (IOException ex) {
                Logger.getLogger(Recibir.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //estado.setText("Conectado");
        }

    }
}
