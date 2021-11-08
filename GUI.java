import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends javax.swing.JFrame
{
    private static final long serialVersionUID = 6060077846877793065L;

    private Application application;

    public GUI()
    {
        initComponents();
    }

    private void initComponents()
    {
        titleLabel = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        calculateButton = new javax.swing.JButton();
        primaryTextBox = new javax.swing.JTextField();
        primaryLabel = new javax.swing.JLabel();
        categoryLabel = new javax.swing.JLabel();
        categoryBox = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        relatedLinks = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        authorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });

        primaryTextBox.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        primaryLabel.setFont(new java.awt.Font("Ariel", 0, 18));
        primaryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        primaryLabel.setText("Primary web page");

        categoryLabel.setFont(new java.awt.Font("Ariel", 0, 18));
        categoryLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        categoryLabel.setText("Category:");

        categoryBox.setFont(new java.awt.Font("Ariel", 0, 14));
        categoryBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        relatedLinks.setEditable(false);
        relatedLinks.setColumns(20);
        relatedLinks.setRows(5);
        jScrollPane1.setViewportView(relatedLinks);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(171, 171, 171)
                                                .addComponent(calculateButton)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(primaryTextBox, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(primaryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(10, 10, 10))
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(categoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(categoryBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(primaryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(primaryTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(categoryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(categoryBox, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(calculateButton)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(authorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(titleLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(authorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }


    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        application = new Application(CustomUrl.fixInput(primaryTextBox.getText()));
        application.execute();
        categoryBox.setText(application.getCategory().name());
        String suggestions = "";
        for(String suggestion : application.getSuggestions())
            suggestions += (suggestion +"\n");
        relatedLinks.setText(suggestions);
    }

    public JTextArea getRelatedLinks()
    {
        return relatedLinks;
    }

    public JTextField getPrimaryTextBox()
    {
        return primaryTextBox;
    }

    private javax.swing.JLabel authorLabel;
    private javax.swing.JButton calculateButton;
    private javax.swing.JLabel categoryBox;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel primaryLabel;
    private javax.swing.JTextField primaryTextBox;
    private javax.swing.JTextArea relatedLinks;
    private javax.swing.JLabel titleLabel;
}