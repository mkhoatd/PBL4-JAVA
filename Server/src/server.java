import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class clientHandler extends Thread {
    public DataOutputStream dos;
    public DataInputStream dis;
    public Socket socket;

    public clientHandler(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    public void run() {
        while (true) {
            try {
                String text = this.dis.readUTF();
                String[] texts = text.split("\n");
                StringBuilder content = new StringBuilder();
                for (int i = 2; i < texts.length; i++) {
                    content.append(texts[i]);
                    content.append("\n");
                }
//				content = socket.getPort() + ": " + content;
                if (texts[0].equals("All")) {
                    server.sendMessageToAll(content.toString());
                } else {
                    int portToSend = Integer.parseInt(texts[0]);
                    var addressToSend = texts[1];
                    System.out.println(portToSend);
                    List<Integer> ports = new ArrayList<Integer>();
                    List<String> addresses = new ArrayList<String>();
                    ports.add(portToSend);
                    addresses.add(addressToSend);
                    server.sendMessageToSomeone(content.toString(), addresses, ports);
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
    public static List<String> clientAddresses = new ArrayList<String>();
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

    public static void sendMessageToSomeone(String text, List<String> addresses, List<Integer> ports) throws IOException {
        for (int i = 0; i < ports.size(); i++) {
            for (int j = 0; j < clientPorts.size(); j++) {
                if ((ports.get(i).equals(clientPorts.get(j))) && (addresses.get(i).equals(clientAddresses.get(j)))) {
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

            var dis = new DataInputStream(socket.getInputStream());
            var dos = new DataOutputStream(socket.getOutputStream());


            sendMessageToAll(acceptMessage);

            isConnected = isConnected + acceptMessage;
            dos.writeUTF(isConnected);
            clientHandler th = new clientHandler(socket, dis, dos);
            clientPorts.add(socket.getPort());
            clientAddresses.add(socket.getInetAddress().toString());
            setOfClients.add(th);
            th.start();
        }
    }

}
