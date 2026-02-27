package org.example;


// Εισαγωγές για τα γραφικά και τα απαραίτητα events
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConverterFrame extends JFrame {
    UserManager userManager = new UserManager();

    private Mail mail;
    // Δημιουργία βασικών panels για το παράθυρο
    private JPanel windowPanel = new JPanel();
    private JPanel boxContainer = new RoundedPanel(20);
    private JPanel centerPanel = new JPanel();
    private JPanel downPanel = new JPanel();
    private JPanel box1 = new RoundedPanel(20);
    private JPanel box2 = new RoundedPanel(20);
    private JPanel favePanel = new RoundedPanel(20);
    private JPanel favePanelTop = new JPanel();
    private JPanel favePanelCenter = new JPanel();
    private JPanel faveButtonPanel = new JPanel();
    private Color myColor = new Color(13,255,16); // Προσαρμοσμένο χρώμα πράσινο

    private GridBagConstraints gbc = new GridBagConstraints(); // για layout με grid

    private JPanel topBar = new RoundedPanel(20); // Επάνω μπάρα (τίτλος εφαρμογής)

    // Πεδία κειμένου και κουμπιά
    private JTextField baseCurrencyTxt = new JTextField();
    private JTextField quoteCurrencyTxt = new JTextField();
    private JTextField resultField = new JTextField();
    private JTextField baseCurrencyNum = new JTextField();
    private JLabel quoteCurrencyResult = new JLabel("");
    private JLabel emailText = new JLabel("Notify me with email for this price of 1st coin $:");
    private JTextField emailPrice = new JTextField();
    private JButton emailButton = new JButton("Notify me");
    private JLabel faveLabel = new JLabel("Favourites");



    private JButton convertButton = new JButton("Convert"); // Κουμπί μετατροπής
    private JButton switchButton = new JButton("Switch");   // Κουμπί ανταλλαγής νομισμάτων
    private JButton addFave = new JButton("Add");
    private JButton removeFave = new JButton("Remove");

    JComboBox comboBox1;
    JComboBox comboBox2;
    JComboBox comboBox3;

    private String coin1, coin2, coin3;
    private Double amount;

    //ΤΑΒLE
    String[] columnNames = {"Coin 1", "Coin 2","Amount" ,"Result"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable favTable = new JTable(tableModel);

    public ConverterFrame(Requests rq,User user) {

        try {
            mail = new Mail();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to initialize Mail:\n" + e.getMessage(), "Mail Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        //ΔΗΜΙΟΥΡΓΙΑ ΛΙΣΤΑΣ ΝΟΜΙΣΜΑΤΩΝ
        //String[] coins = rq.getAllCoinSymbols().toArray(new String[0]);
        // Δημιουργία και τοποθέτηση gradient panel ως κύριο περιβάλλον
        jPanelGradient windowPanel = new jPanelGradient(20);
        this.setContentPane(windowPanel);
        windowPanel.setLayout(new BorderLayout(10, 10));

        // Προσθήκη panels στο βασικό περιβάλλον (top bar, κεντρικό, sidebar)
        topBar.setBackground(Color.BLACK);
        topBar.setPreferredSize(new Dimension(80, 80));
        windowPanel.add(topBar, BorderLayout.NORTH);

        boxContainer.setBackground(Color.BLACK);
        windowPanel.add(boxContainer, BorderLayout.CENTER);

        favePanel.setBackground(Color.BLACK);
        favePanel.setPreferredSize(new Dimension(300, 80));
        favePanel.setLayout(new BorderLayout());
        windowPanel.add(favePanel, BorderLayout.EAST);

        // Ρύθμιση layout για το container
        boxContainer.setLayout(new BorderLayout());
        boxContainer.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);
        downPanel.setBackground(Color.BLACK);
        downPanel.setBorder(new EmptyBorder(20, 10, 200, 10));
        boxContainer.add(downPanel, BorderLayout.SOUTH);

        //boxContainer.setLayout(new GridBagLayout());

        // Προσθήκη panels και κουμπιών στο grid
        box1.setPreferredSize(new Dimension(200, 200));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(box1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(switchButton, gbc); // Κουμπί για αλλαγή νομισμάτων

        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(convertButton, gbc); // Κουμπί μετατροπής

        // Εμφάνιση αποτελέσματος μετατροπής
        gbc.gridx = 2;
        gbc.gridy = 1;
        quoteCurrencyResult.setFont(new Font("Arial", Font.BOLD, 15));
        quoteCurrencyResult.setForeground(Color.GREEN);
        quoteCurrencyResult.setBorder(BorderFactory.createLineBorder(myColor, 2));
        quoteCurrencyResult.setPreferredSize(new Dimension(100, 30));
        quoteCurrencyResult.setText("myresult"); // Προκαθορισμένο κείμενο
        centerPanel.add(quoteCurrencyResult, gbc);

        box2.setPreferredSize(new Dimension(200, 200));
        gbc.gridx = 2;
        gbc.gridy = 0;
        centerPanel.add(box2, gbc);

        //DOWN PANEL

        emailText.setForeground(myColor);
        emailText.setFont(new Font("Verdana", Font.BOLD, 18));

        emailPrice.setPreferredSize(new Dimension(200, 20));
        emailPrice.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = emailPrice.getText();
                if (!(Character.isDigit(c) || c == '.')) {
                    e.consume();
                }
                if (c == '.' && text.contains(".")) {
                    e.consume();
                }
            }
        });
        downPanel.add(emailText);
        downPanel.add(emailPrice);
        downPanel.add(emailButton);
        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        double targetPrice = Double.parseDouble(emailPrice.getText());
                        double currentPrice = Double.parseDouble(rq.request(coin1, "USD", 1.0));

                        if (currentPrice == targetPrice) {
                            mail.sendMail("Price Alert", "The price of " + coin1 + " is now $" + currentPrice);
                            System.out.println("Email sent!");
                            scheduler.shutdown();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }, 0, 5, TimeUnit.SECONDS);
            }
        });


        //FAVE PANEL
        for (String fav : user.getFavorites()) {
            // Διαχωρίζουμε το "USD-EUR:5.0" => ["USD-EUR", "5.0"]
            String[] parts = fav.split(":");
            if (parts.length == 2) {
                String[] coins = parts[0].split("-");
                if (coins.length == 2) {
                    String coin1 = coins[0];
                    String coin2 = coins[1];
                    String amount = parts[1];
                    String result = rq.request(coin1, coin2, Double.parseDouble(amount)); // Υπολόγισε την τιμή
                    Object[] row = {coin1, coin2, amount, result};
                    tableModel.addRow(row);
                }
            }
        }
        faveLabel.setForeground(myColor);
        faveLabel.setFont(new Font("Verdana", Font.BOLD, 15));
        favePanelTop.add(faveLabel);
        favePanel.add(favePanelTop, BorderLayout.NORTH);
        favePanelTop.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        favePanelTop.setBackground(Color.BLACK);
        favePanelCenter.setBackground(Color.BLACK);
        favePanel.add(favePanelCenter, BorderLayout.CENTER);
        faveButtonPanel.setBackground(Color.black);
        faveButtonPanel.add(addFave);
        faveButtonPanel.add(removeFave);
        favePanel.add(faveButtonPanel, BorderLayout.SOUTH);
        JScrollPane scrollPane = new JScrollPane(favTable);
        favTable.setForeground(myColor); // πράσινο κείμενο
        favTable.setGridColor(myColor);  // πράσινα περιγράμματα κελιών
        favTable.setSelectionBackground(Color.DARK_GRAY);
        favTable.setSelectionForeground(Color.WHITE);
        favTable.setFont(new Font("Verdana", Font.PLAIN, 12));


        favTable.getTableHeader().setBackground(Color.BLACK); // κεφαλίδα μαύρη
        favTable.getTableHeader().setForeground(myColor);     // κεφαλίδα πράσινη
        favTable.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));

        scrollPane.getViewport().setBackground(Color.BLACK); // το background πίσω απ' τα κελιά
        scrollPane.setBorder(BorderFactory.createLineBorder(myColor, 1)); // περιθώριο πράσινο


        favTable.setBackground(Color.black);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        favePanelCenter.setLayout(new BorderLayout()); // αν δεν έχει ήδη layout
        favePanelCenter.add(scrollPane, BorderLayout.CENTER);











        // Ρυθμίσεις για το πρώτο πλαίσιο (box1)
        box1.setBackground(Color.GRAY);

        JLabel chooseTitle1 = new JLabel("Choose 1st coin");
        chooseTitle1.setForeground(myColor);
        chooseTitle1.setFont(new Font("Verdana", Font.BOLD, 13));
        box1.add(chooseTitle1);

        // Επιλογές νομισμάτων
        String[] coins = rq.getAllCoinSymbols().toArray(new String[0]);
        comboBox1 = new JComboBox(coins);
        comboBox1.setEditable(true);
        coin1 = comboBox1.getEditor().getItem().toString();

        comboBox1.addActionListener(e -> {
            coin1 = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
        });
        box1.add(comboBox1);



        // Πεδία εισαγωγής ποσότητας
        JLabel amountTitle1 = new JLabel("Choose amount");
        amountTitle1.setForeground(myColor);
        amountTitle1.setFont(new Font("Verdana", Font.BOLD, 13));
        box1.add(amountTitle1);

        JTextField amountTextField = new JTextField();
        amountTextField.setPreferredSize(new Dimension(35, 20));

        // Έλεγχος ώστε να δέχεται μόνο αριθμούς και τελεία
        amountTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = amountTextField.getText();
                if (!(Character.isDigit(c) || c == '.')) {
                    e.consume();
                }
                if (c == '.' && text.contains(".")) {
                    e.consume();
                }
            }
        });

        amountTextField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String strAmount = amountTextField.getText();
                if (strAmount.isEmpty()) return;
                amount = Double.parseDouble(strAmount);
            }
        });

        box1.add(amountTextField);

        // Ρυθμίσεις για το δεύτερο πλαίσιο (box2)
        JLabel chooseTitle2 = new JLabel("Choose 2nd coin");
        chooseTitle2.setForeground(myColor);
        chooseTitle2.setFont(new Font("Verdana", Font.BOLD, 13));
        box2.setBackground(Color.GRAY);
        box2.add(chooseTitle2);

        comboBox2 = new JComboBox(coins);
        comboBox2.setEditable(true);
        coin2 = comboBox2.getEditor().getItem().toString();

        comboBox2.addActionListener(e -> {
            coin2 = Objects.requireNonNull(comboBox2.getSelectedItem()).toString();
        });
        box2.add(comboBox2);


        // Κουμπί μετατροπής: καλεί τη μέθοδο request από την Requests
        convertButton.addActionListener(e -> {
            if(amount==null){
                JOptionPane.showMessageDialog(null, "Please insert valid amount.", "Error", JOptionPane.ERROR_MESSAGE);

            }
            quoteCurrencyResult.setText(rq.request(coin1, coin2, amount));
        });

        addFave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (coin1 != null && coin2 != null && amount != null) {
                    String fav = coin1 + "-" + coin2 + ":" + amount;

                    // Πρόσθεσε στο αντικείμενο του χρήστη
                    user.getFavorites().add(fav);

                    //Φόρτωσε όλους τους χρήστες
                    List<User> allUsers = userManager.loadUsers();  // φόρτωσε όλους
                    for (User u : allUsers) {
                        if (u.getUsername().equals(user.getUsername())) {
                            u.setFavourites(new ArrayList<>(user.getFavorites())); // κάνε overwrite
                            break;
                        }
                    }
                    userManager.saveUsers(allUsers);


                    // Προσθήκη στο table
                    String resultFav = rq.request(coin1, coin2, amount);
                    Object[] row = {coin1, coin2, amount, resultFav};
                    tableModel.addRow(row);
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields before adding.");
                }
            }
        });
        removeFave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = favTable.getSelectedRow();

                if (selectedRow != -1) { // Αν έχει επιλεχθεί γραμμή
                    String coin1 = (String) tableModel.getValueAt(selectedRow, 0);
                    String coin2 = (String) tableModel.getValueAt(selectedRow, 1);
                    String amount = tableModel.getValueAt(selectedRow, 2).toString();
                    user.getFavorites().remove(coin1+"-"+coin2+":"+amount);

                    List<User> allUsers = userManager.loadUsers(); // Φόρτωσε όλους τους χρήστες
                    for (User u : allUsers) {
                        if (u.getUsername().equals(user.getUsername())) {
                            u.setFavourites(new ArrayList<>(user.getFavorites())); // Κάνε overwrite τη λίστα του
                            break;
                        }
                    }


                    userManager.saveUsers(allUsers);

                    tableModel.removeRow(selectedRow);

                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }

            }
        });





        // Κουμπί αλλαγής νομισμάτων
        switchButton.addActionListener(e -> {
            String temp = coin1;
            comboBox1.setSelectedItem(coin2);
            comboBox2.setSelectedItem(temp);
        });

        // Τίτλος εφαρμογής επάνω
        this.setUndecorated(true);
        topBar.setLayout(new BorderLayout());
        JLabel appNameLabel = new JLabel("Currency Converter");
        appNameLabel.setForeground(myColor);
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        appNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        topBar.add(appNameLabel, BorderLayout.WEST);

        // Κουμπί εξόδου
        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(e -> System.exit(0));
        topBar.add(closeButton, BorderLayout.EAST);

        // Τελικές ρυθμίσεις παραθύρου
        this.setTitle("Currency Converter");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setSize(900, 800);
        this.setVisible(true);
        this.centerFrame();
    }

    // Μέθοδος για κεντράρισμα παραθύρου στην οθόνη
    private void centerFrame() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        this.setLocation(x, y);
    }

    // Κλάση για τη δημιουργία panel με διαβάθμιση χρώματος
    class jPanelGradient extends JPanel {
        private int radius;

        public jPanelGradient(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color1 = new Color(26, 140, 28);
            Color color2 = new Color(8, 23, 1);
            GradientPaint gp = new GradientPaint(0, 0, color1, 180, height, color2);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, 0, width, height, radius, radius);
        }
    }

    // Κλάση για panels με στρογγυλεμένες γωνίες
    public class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }



        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }
}