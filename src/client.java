import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class client extends JFrame implements ActionListener {

    public static DataInputStream dis;
    public static DataOutputStream dos;
    public static Socket socket;
    public static int localPort;

    public static JTextField iPTextbox;
    public static JButton connectButton;
    public static JTextField portTextbox;
    public static JTextField fileTextbox;
    public static JButton sendButton;
    public static JButton broadcastButton;
    public static JTextArea console;

    public client() {
        this.setTitle("This is client");
        this.setSize(400, 400);
        this.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        JLabel lb1 = new JLabel("My Local Port: ");
        lb1.setPreferredSize(new Dimension(100, 20));
        iPTextbox = new JTextField(35);
        iPTextbox.setText(localPort + "");
        panel1.add(lb1);
        panel1.add(iPTextbox);
        mainPanel.add(panel1);

        var lb10=new JLabel("Server Address");
        lb10.setPreferredSize(new Dimension(100, 20));
        var serverAddressTextField=new JTextField(20);
        serverAddressTextField.setText("127.0.0.1");
        var serverPortTextField=new JTextField(15);
        serverPortTextField.setText("7000");
        var panel10=new JPanel();
        panel10.add(lb10);
        panel10.add(serverAddressTextField);
        panel10.add(serverPortTextField);
        mainPanel.add(panel10);

        JPanel panel2 = new JPanel();
        connectButton = new JButton("Connect");
        panel2.add(connectButton);
        mainPanel.add(panel2);

        JPanel panel3 = new JPanel();
        JLabel lb2 = new JLabel("Port: ");
        lb2.setPreferredSize(new Dimension(100, 20));
        portTextbox = new JTextField(35);
        panel3.add(lb2);
        panel3.add(portTextbox);
        mainPanel.add(panel3);

        JPanel panel4 = new JPanel();
        JLabel lb3 = new JLabel("Folder: ");
        lb3.setPreferredSize(new Dimension(100, 20));
        fileTextbox = new JTextField(35);
        panel4.add(lb3);
        panel4.add(fileTextbox);
        mainPanel.add(panel4);

        JPanel panel5 = new JPanel();
        sendButton = new JButton("Send");
        broadcastButton = new JButton("Broadcast");
        panel5.add(sendButton);
        panel5.add(broadcastButton);
        mainPanel.add(panel5);

        JLabel lb4 = new JLabel("Console");
        lb4.setPreferredSize(new Dimension(1, 30));
        console = new JTextArea(20, 35);
        mainPanel.add(lb4);
        mainPanel.add(console);


        connectButton.addActionListener(this);
        sendButton.addActionListener(this);
        broadcastButton.addActionListener(this);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String res = "", st;
        while ((st = br.readLine()) != null) {
            res = res + st;
            res = res + "\n";
        }
        return res;
    }

    public static void processTable(String text) {
        String[] lines = text.split("\n");
        if (lines[0].trim().split(" ").length > 1) {
            console.append(text);
            return;
        }
        int sizeOfPack = Integer.parseInt(lines[0].trim());
        String[] characters = lines[1].trim().split(" ");
        int n = characters.length;
        int[][] baseFlowMatrix = new int[n][n];
        double[][] capacityMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            String[] line = lines[i + 2].trim().split(" ");
            String[] line2 = lines[i + n + 2].trim().split(" ");
            for (int j = 0; j < n; j++) {
                baseFlowMatrix[i][j] = Integer.valueOf(line[j]);
                capacityMatrix[i][j] = Double.valueOf(line2[j]);
            }
        }
        List<String> routes = new ArrayList<String>();
        List<Integer> Ld = new ArrayList<Integer>();
        List<Double> C = new ArrayList<Double>();
        List<Double> mC = new ArrayList<Double>();
        List<Double> weights = new ArrayList<Double>();

        int sumOfPack = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (capacityMatrix[i][j] > 0) {
                    routes.add(characters[i] + characters[j]);
                    Ld.add(baseFlowMatrix[i][j]);
                    sumOfPack += baseFlowMatrix[i][j];
                    C.add(capacityMatrix[i][j]);
                    mC.add(capacityMatrix[i][j] / sizeOfPack * 1000);
                }
            }
        }
        for (Integer integer : Ld) {
            weights.add((double) integer / (double) sumOfPack);
        }

        String note = "Bang phan tich mang con su dung kich thuoc trung binh cua goi la " + sizeOfPack + " bits ";

        var resultTable = new Table(characters,routes, Ld, C, mC, weights, note);

    }

    public static void main(String[] args) throws IOException {
        System.setProperty("org.graphstream.ui", "swing");
        socket = new Socket(Inet4Address.getLocalHost().getHostAddress(), 7000);
        localPort = socket.getLocalPort();

        new client();

        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());

        while (true) {
            String newText = dis.readUTF();
            processTable(newText);
//			console.append(newText);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((JButton) e.getSource() == connectButton) {

        } else {
            String text = null;
            String res = null;

            // check event
            if ((JButton) e.getSource() == sendButton) {
                text = portTextbox.getText();
            } else if ((JButton) e.getSource() == broadcastButton) {
                text = "All";
            }

            // create file path
            String filePath = fileTextbox.getText();
            filePath = "./" + filePath;
            try {
                // read file
                res = readFile(filePath);
                res = text + "\n" + res;
                // send
                dos.writeUTF(res);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}

class Table extends JFrame implements ActionListener {
    public String[] columnNames;
    public String[] nodes;
    public List<String> routes = new ArrayList<String>();
    public List<Integer> Ld = new ArrayList<Integer>();
    public List<Double> C = new ArrayList<Double>();
    public List<Double> mC = new ArrayList<Double>();
    public List<Double> weights = new ArrayList<Double>();
    public int n;
    public String[][] data;
    public String note;
    public JTable table = null;
    public JButton resultsButton;
    public JButton tableButton;
    public JButton cancelButton;


    public Table(String[] nodes,List<String> routes, List<Integer> Ld, List<Double> C, List<Double> mC, List<Double> weights, String note) {
        this.nodes=nodes;
        columnNames = new String[]{"STT", "Line", "Ld", "C", "mC", "Weight"};
        this.routes = routes;
        this.Ld = Ld;
        this.C = C;
        this.mC = mC;
        this.weights = weights;
        this.note = note;
        n = routes.size();

        data = new String[n][6];
        for (int i = 0; i < n; i++) {
            data[i][0] = Integer.toString(i);
            data[i][1] = routes.get(i);
            data[i][2] = Integer.toString(Ld.get(i));
            data[i][3] = Double.toString(C.get(i));
            data[i][4] = Double.toString(mC.get(i));
            data[i][5] = Double.toString(weights.get(i));
        }

        initFrame();
        resultsButton.addActionListener(e-> new ResultForm(this));

    }

    public void initFrame() {

        this.setTitle("Table");
        this.setSize(400, 400);
        this.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);


        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);
        JScrollPane pane = new JScrollPane(table);
        mainPanel.add(pane);

        JLabel label = new JLabel(note);
        mainPanel.add(label);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        resultsButton = new JButton("Result");
        tableButton = new JButton("Table");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(resultsButton);
        buttonPanel.add(tableButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
