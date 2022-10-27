package View;

import Model.RoadTableModel;
import Controller.ControllerMalha;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import Model.ParametrosSimulacao;

/**
 *
 * @author danie
 */
public class ViewSimulacao extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private ControllerMalha meshController;
    
    public ViewSimulacao(ParametrosSimulacao simulationParameters) {
        this.meshController = ControllerMalha.getInstance();
        this.meshController.setTelaSimulacao(this);
        this.meshController.setParametros(simulationParameters);
        super.setFocusable(true);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        initComponents();
        setCarrosEmMovimento();
        setAcaoFinalizar();
        setAcaoEncerrar();
        setPainelPrincipal();
        setTableModel();
        start();
    }
    
    private void setCarrosEmMovimento(){
        this.meshController.setContagemCarrosAndando(runningCarsCountLabel);
    }
    
    private void setAcaoFinalizar(){
        endSimulationButton.addActionListener((ActionEvent e) -> {
            isPausedLabel.setText("FINALIZANDO");
            this.meshController.finalizaSimulacao();
        });
    }
    
    private void setAcaoEncerrar(){
        btEncerrar.addActionListener((ActionEvent e) -> {
            isPausedLabel.setText("ENCERRADO");
            this.meshController.encerraSimulacao();
        });
    }
    
    private void setPainelPrincipal(){
        meshController.setMatrizFromMalha(meshController.getParametros().getMalha());
        jPanelPrincipal.setLayout(new BoxLayout(jPanelPrincipal, BoxLayout.Y_AXIS));
        jPanelPrincipal.setOpaque(true);        
    }
    
    private void setTableModel(){
        jTablePrincipal.setModel(new RoadTableModel());
        jTablePrincipal.setPreferredScrollableViewportSize(jTablePrincipal.getPreferredSize());
        jTablePrincipal.setFillsViewportHeight(true);

        jTablePrincipal.setRowHeight(25);
        jTablePrincipal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTablePrincipal.setIntercellSpacing(new Dimension(0, 0));
        jTablePrincipal.setDefaultRenderer(Object.class, new RenderItemMalha());
        jPanelPrincipal.add(jTablePrincipal);
    }

     public JPanel getPainelPrincipal() {
        return jPanelPrincipal;
    }
     
    private void start() {
        super.pack();
        super.setLocationRelativeTo(null);
        this.meshController.iniciaSimulacao();
    }   
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        runningCarsCountLabel = new javax.swing.JLabel();
        isPausedLabel = new javax.swing.JLabel();
        endSimulationButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btEncerrar = new javax.swing.JButton();
        jPanelPrincipal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePrincipal = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Game");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        runningCarsCountLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        runningCarsCountLabel.setText("Carros em Movimento: 0");

        isPausedLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        isPausedLabel.setText("EXECUTANDO...");

        endSimulationButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        endSimulationButton.setText("Finalizar Simulação");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("STATUS:");

        btEncerrar.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btEncerrar.setText("Encerrar Simulação");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(287, 287, 287)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(isPausedLabel))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(endSimulationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(btEncerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(103, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(runningCarsCountLabel)
                .addGap(327, 327, 327))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(runningCarsCountLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(isPausedLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endSimulationButton, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(btEncerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanelPrincipal.setBorder(new javax.swing.border.MatteBorder(null));

        jScrollPane1.setViewportView(jTablePrincipal);

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(161, 161, 161)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEncerrar;
    private javax.swing.JButton endSimulationButton;
    private javax.swing.JLabel isPausedLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePrincipal;
    private javax.swing.JLabel runningCarsCountLabel;
    // End of variables declaration//GEN-END:variables

}
