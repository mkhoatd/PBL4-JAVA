import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

class clientHandler extends Thread {
    public OutputStreamWrapper dos;
    public InputStreamWrapper dis;
    public Socket socket;

    public clientHandler(Socket socket, InputStreamWrapper dis, OutputStreamWrapper dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    public void run() {
        while (true) {
            try {
                String text = this.dis.readUTF();
                String[] texts = text.split("\n");
                String content = "";
                for (int i = 1; i < texts.length; i++) {
                    content += texts[i];
                    content += "\n";
                }
//				content = socket.getPort() + ": " + content;
                if (texts[0].equals("All")) {
                    server.sendMessageToAll(content);
                } else {
                    int portToSend = Integer.parseInt(texts[0]);
                    System.out.println(portToSend);
                    List<Integer> ports = new ArrayList<Integer>();
                    ports.add(portToSend);
                    server.sendMessageToSomeone(content, ports);
                }
                String message = socket.getPort() + " send something!\n";
                server.updateConsole(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


public class server extends JFrame {

    public static JTextArea console;
    public static ServerSocket serverSocket;
    public static List<Integer> clientPorts = new ArrayList<Integer>();
    public static List<clientHandler> setOfClients = new ArrayList<clientHandler>();
    public static String isConnected = "";

    public server() {
        this.setTitle("This is server");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel lb = new JLabel("   Console");
        lb.setPreferredSize(new Dimension(10, 30));
        console = new JTextArea(20, 35);
        mainPanel.add(lb);
        mainPanel.add(console);
        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void updateConsole(String text) {
        console.append(text);
    }

    public static void sendMessageToAll(String text) {
        for (clientHandler client : setOfClients) {
            try {
                client.dos.writeUTF(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendMessageToSomeone(String text, List<Integer> ports) throws IOException {
        for (int i = 0; i < ports.size(); i++) {
            for (int j = 0; j < clientPorts.size(); j++) {
                if (ports.get(i).equals(clientPorts.get(j))) {
                    setOfClients.get(j).dos.writeUTF(text);
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        server myServer = new server();
        serverSocket = new ServerSocket(7000);
        while (true) {
            Socket socket = serverSocket.accept();
            String acceptMessage = socket.getPort() + " connected!\n";
            console.append(acceptMessage);

            var dis = new InputStreamWrapper(socket.getInputStream());
            var dos = new OutputStreamWrapper(socket.getOutputStream());


            sendMessageToAll(acceptMessage);

            isConnected = isConnected + acceptMessage;
            dos.writeUTF(isConnected);
            clientHandler th = new clientHandler(socket, dis, dos);
            clientPorts.add(socket.getPort());
            setOfClients.add(th);
            th.start();
        }
    }

}
