import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

class ClientConnect extends JFrame implements ActionListener {

    public DataInputStream dis;
    public DataOutputStream dos;
    public Socket socket;
    public InetAddress localAddress;
    public Integer localPort = 0;

    public JRadioButton r1;
    public JRadioButton r2;

    public JTextField serverAddressTextField;
    public JTextField serverPortTextField;
    public JTextField localPortTextField;
    public JButton connectButton;

    public ClientConnect() {
        this.setTitle("Client Connect");
        this.setSize(400, 400);
        this.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        JLabel lb1 = new JLabel("Local port: ");
        lb1.setPreferredSize(new Dimension(100, 20));
        localPortTextField = new JTextField(35);
        localPortTextField.setEnabled(false);
        localPortTextField.setText(localPort + "");
        panel1.add(lb1);
        panel1.add(localPortTextField);
        mainPanel.add(panel1);

        var lb10=new JLabel("Server Address");
        lb10.setPreferredSize(new Dimension(100, 20));
        serverAddressTextField=new JTextField(20);
        serverAddressTextField.setText("127.0.0.1");
        serverPortTextField=new JTextField(14);
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

        connectButton.addActionListener(this);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//		int mode;
//		if (r1.isSelected()) {
//			mode = 0;
//		} else {
//			mode = 1;
//		}
        client c = new client(serverAddressTextField.getText(), Integer.parseInt(serverPortTextField.getText()));
        this.setVisible(false);
    }
}

public class client extends JFrame implements ActionListener {

    public Socket socket;
    public DataInputStream dis;
    public DataOutputStream dos;
    public int mode;
    public InetAddress localAddress;
    public Integer localPort = 0;

    public JTextField localPortTextField;
    public JButton connectButton;
    public JTextField portTextField;
    public JTextField addressTextField;
    public JTextField fileTextbox;
    public JFileChooser fileChooser;
    public JButton fileButton;
    public JButton sendButton;
    public JButton broadcastButton;
    public JRadioButton r1;
    public JRadioButton r2;
    public JButton modeButton;
    public JTextArea console;

    public client(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            this.localPort = socket.getLocalPort();
//	        this.mode = mode;
            this.localPort = socket.getLocalPort();
            this.localAddress = socket.getInetAddress();

            var message = dis.readUTF();
            if (message.equals("123456")){
                this.console.setText("Connected to server");
            }
            new ClientRunner(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setTitle("This is client");
        this.setSize(400, 400);
        this.setLayout(new FlowLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel();
        JLabel lb12 = new JLabel("Local port: ");
        lb12.setPreferredSize(new Dimension(100, 20));
        localPortTextField = new JTextField(35);
        localPortTextField.setEnabled(false);
        localPortTextField.setText(localPort + "");
        panel1.add(lb12);
        panel1.add(localPortTextField);
        mainPanel.add(panel1);

        JPanel panel3 = new JPanel();
        JLabel lb2 = new JLabel("Destination: ");
        lb2.setPreferredSize(new Dimension(100, 20));
        portTextField = new JTextField(14);
        addressTextField = new JTextField(20);
        addressTextField.setText("127.0.0.1");
        panel3.add(lb2);
        panel3.add(addressTextField);
        panel3.add(portTextField);
        mainPanel.add(panel3);

        JPanel panel4 = new JPanel();
        JLabel lb3 = new JLabel("File: ");
        lb3.setPreferredSize(new Dimension(100, 20));
        fileTextbox = new JTextField(24);
        fileChooser = new JFileChooser("./");
        fileButton = new JButton("Choose File");
//        fileButton.setPreferredSize(new Dimension(0, 20));
        panel4.add(lb3);
//        panel4.add(fileChooser);
        panel4.add(fileTextbox);
        panel4.add(fileButton);
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

        sendButton.addActionListener(this);
        broadcastButton.addActionListener(this);
        fileButton.addActionListener(this);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String res = "", st;
        while ((st = br.readLine()) != null) {
            res = res + st;
            res = res + "\n";
        }
        return res;
    }

    public void processTable(String text) {
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
                baseFlowMatrix[i][j] = Integer.parseInt(line[j]);
                capacityMatrix[i][j] = Double.parseDouble(line2[j]);
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
        new ClientConnect();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((JButton) e.getSource() == fileButton) {
            JFileChooser fc = new JFileChooser("./");
            int x = fc.showDialog(this, "Choose File");
            if (x == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                fileTextbox.setText(path);
            }
        } else {

            String portText = null;
            String addressText="127.0.0.1";
            String res = null;

            // check event
            if ((JButton) e.getSource() == sendButton) {
                portText = portTextField.getText();
                addressText=addressTextField.getText();
            } else if (e.getSource() == broadcastButton) {
                portText = "All";
            }

            // create file path
            String filePath = fileTextbox.getText();
//            filePath = "./src/final_pbl4/" + filePath;
            try {
                // read file
                res = readFile(filePath);
                res = portText + "\n" + addressText + "\n" + res;
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
        cancelButton = new JButton("Cancel");

        buttonPanel.add(resultsButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
