package sample;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

public class Graphing extends javax.swing.JFrame {

    GraphingTree tree = new GraphingTree();
    Point offset = new Point();
    Point prevMousePos = new Point(-100, -100);
    double scale = 1;
    BufferedImage bufferedImage;
    Graphics bufferedGraphics;
    double tracerOffset = 0;
    Point mouseOnGraph = new Point();

    public Graphing() {
        initComponents();
        processExpression();
    }

    public void processExpression() {
        String expression = txtEquation.getText();
        expression = InputVarChecker.validate(expression);
        if (expression == null) {
            JOptionPane.showMessageDialog(this, "Invalid input expression.");
            return;
        }
        txtEquation.setText(expression);
        tree.parse(expression);
    }

    public void graph(Graphics g) {
        if (tree == null) {
            return;
        }
        if (bufferedGraphics == null) {
            refreshBufferedImage();
        }
        bufferedGraphics.setColor(Color.WHITE);
        bufferedGraphics.fillRect(0, 0, graphPanel.getWidth(), graphPanel.getHeight());
        bufferedGraphics.setColor(Color.BLACK);
        Point prevPoint = new Point(-100, -100);
        Point center = new Point(graphPanel.getWidth() / 2, graphPanel.getHeight() / 2);
        center.x += offset.x;
        center.y += offset.y;
        bufferedGraphics.drawLine(center.x, 0,
                center.x, graphPanel.getHeight());
        bufferedGraphics.drawLine(0, center.y,
                graphPanel.getWidth(), center.y);
        bufferedGraphics.setColor(Color.RED);
        for (int i = 0; i < graphPanel.getWidth(); i++) {
            double x = (double) (i - center.x) / scale;
            tree.variableValue = x;
            double y = tree.evaluate();
            Point currentPoint = new Point(i, center.y - (int) (y * scale));
            bufferedGraphics.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
            prevPoint = currentPoint;
        }
        yEqualsLabel.setText("");
        xEqualsLabel.setText("");
        if (traceCheck.isSelected()) {
            bufferedGraphics.setColor(Color.BLUE);
            double tracerOff = mouseOnGraph.x - graphPanel.getWidth() / 2;
            double traceX = (float) (tracerOff - offset.x) / scale;
            tree.variableValue = traceX;
            double traceY = tree.evaluate();
            Point tracer = new Point((int) (center.x + traceX * scale),
                    center.y - (int) (traceY * scale));
            int tracerSize = (int) (5 * Math.pow(scale, .5));
            bufferedGraphics.drawLine(tracer.x - tracerSize, tracer.y - tracerSize,
                    tracer.x + tracerSize, tracer.y + tracerSize);
            bufferedGraphics.drawLine(tracer.x - tracerSize, tracer.y + tracerSize,
                    tracer.x + tracerSize, tracer.y - tracerSize);
            yEqualsLabel.setText("y = " + String.format("%.2f", traceY));
            xEqualsLabel.setText("x = " + String.format("%.2f", traceX));
        }
        g.drawImage(bufferedImage, 0, 0, graphPanel);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        graphPanel = new javax.swing.JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                graph(g);
            }
        };
        traceCheck = new javax.swing.JCheckBox();
        yEqualsLabel = new javax.swing.JLabel();
        xEqualsLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnGraph = new javax.swing.JButton();
        txtEquation = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Graph");
        setBackground(new java.awt.Color(255, 255, 255));

        graphPanel.setBackground(new java.awt.Color(255, 255, 255));
        graphPanel.setName("graphPanel"); // NOI18N
        graphPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                graphPanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                graphPanelMouseMoved(evt);
            }
        });
        graphPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                graphPanelMouseWheelMoved(evt);
            }
        });
        graphPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                graphPanelMouseReleased(evt);
            }
        });
        graphPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                graphPanelComponentResized(evt);
            }
        });

        javax.swing.GroupLayout graphPanelLayout = new javax.swing.GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
                graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        graphPanelLayout.setVerticalGroup(
                graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 252, Short.MAX_VALUE)
        );

        traceCheck.setText("Trace");
        traceCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                traceCheckActionPerformed(evt);
            }
        });

        yEqualsLabel.setText("jLabel1");

        xEqualsLabel.setText("jLabel2");

        jLabel1.setText("y=");

        btnGraph.setText("Graph");
        btnGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraphActionPerformed(evt);
            }
        });

        txtEquation.setText("x");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(10, 10, 10))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(yEqualsLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(xEqualsLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                                                .addComponent(traceCheck)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtEquation)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnGraph)
                                                .addContainerGap())))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel1)
                                                .addComponent(btnGraph))
                                        .addComponent(txtEquation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(traceCheck)
                                        .addComponent(yEqualsLabel)
                                        .addComponent(xEqualsLabel))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void graphPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphPanelMouseDragged
        if (prevMousePos.x > 0) {
            offset.x += evt.getX() - prevMousePos.x;
            offset.y += evt.getY() - prevMousePos.y;
        }
        prevMousePos.x = evt.getX();
        prevMousePos.y = evt.getY();
        graph(graphPanel.getGraphics());
    }//GEN-LAST:event_graphPanelMouseDragged

    private void graphPanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_graphPanelMouseWheelMoved
        if (evt.getWheelRotation() < 0) {
            scale *= 1.5f;
            offset.x = (int) (offset.x * 1.5f);
            offset.y = (int) (offset.y * 1.5f);
        } else {
            scale /= 1.5f;
            offset.x = (int) (offset.x / 1.5f);
            offset.y = (int) (offset.y / 1.5f);
        }
        graph(graphPanel.getGraphics());
    }//GEN-LAST:event_graphPanelMouseWheelMoved

    private void graphPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphPanelMouseReleased
        prevMousePos.x = -1;
        prevMousePos.y = -1;
    }//GEN-LAST:event_graphPanelMouseReleased

    private void graphPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_graphPanelComponentResized
        refreshBufferedImage();
        graph(graphPanel.getGraphics());
    }//GEN-LAST:event_graphPanelComponentResized

    private void traceCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_traceCheckActionPerformed

        graph(graphPanel.getGraphics());
    }//GEN-LAST:event_traceCheckActionPerformed

    private void graphPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphPanelMouseMoved
        mouseOnGraph.x = evt.getX();
        mouseOnGraph.y = evt.getY();
        graph(graphPanel.getGraphics());
    }//GEN-LAST:event_graphPanelMouseMoved

    private void btnGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraphActionPerformed
        processExpression();
        graph(graphPanel.getGraphics());
    }//GEN-LAST:event_btnGraphActionPerformed

    private void refreshBufferedImage() {
        bufferedImage = new BufferedImage(graphPanel.getWidth(),
                graphPanel.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        bufferedGraphics = bufferedImage.getGraphics();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Graphing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Graphing graphing = new Graphing();
            graphing.setLocationRelativeTo(null);
            graphing.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGraph;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JCheckBox traceCheck;
    private javax.swing.JTextField txtEquation;
    private javax.swing.JLabel xEqualsLabel;
    private javax.swing.JLabel yEqualsLabel;
    // End of variables declaration//GEN-END:variables
}