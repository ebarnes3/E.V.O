package sample;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MathTraining extends javax.swing.JFrame {

    private class TokenLabelGroup {

        ExpressionTree.Token token;
        JLabel label;
        Point originalLocation;

        public TokenLabelGroup(ExpressionTree.Token token, JLabel label, Point originalLocation) {
            this.token = token;
            this.label = label;
            this.originalLocation = originalLocation;
        }
    }
    final int PADDING_LEFT = 20;
    final int PADDING_TOP = 60;
    final int TOKEN_PADDING = 6;
    final Font LABEL_FONT = new Font("Courier New", Font.BOLD, 50);
    final Color COLOR_VALID_MOVE = new Color(50, 180, 10);
    final Color COLOR_OVERLAP = new Color(100, 255, 10);
    ExpressionTree tree;
    HashMap<JLabel, TokenLabelGroup> labelToTokenGroupMap
            = new HashMap<>();
    HashMap<ExpressionTree.Token, TokenLabelGroup> tokenToTokenGroupMap
            = new HashMap<>();

    Point prevMouse = new Point(-1, -1);
    TokenLabelGroup activeLabel = null;
    TokenLabelGroup potentialLabel = null;
    int maxWidth = 0;
    MouseAdapter labelMouseAdapter;
    MouseMotionAdapter labelMotionAdapter;

    public MathTraining() {
        initComponents();
        labelMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevMouse.x = e.getXOnScreen();
                prevMouse.y = e.getYOnScreen();
                activeLabel = labelToTokenGroupMap.get(e.getSource());
                highLightLabels();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                prevMouse.x = -1;
                prevMouse.y = -1;
                if (activeLabel != null) {
                    if (potentialLabel != null) {
                        tree.merge(activeLabel.token, potentialLabel.token);
                        refreshLabels();
                    } else {
                        activeLabel.label.setLocation(activeLabel.originalLocation);
                    }
                }
                activeLabel = null;
                potentialLabel = null;
                highLightLabels();
            }
        };

        labelMotionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (prevMouse.x > 0) {
                    JLabel label = (JLabel) e.getSource();
                    Point currentMouse = e.getLocationOnScreen();
                    Point newPos = label.getLocation();
                    newPos.x += currentMouse.x - prevMouse.x;
                    newPos.y += currentMouse.y - prevMouse.y;
                    label.setLocation(newPos);
                    prevMouse.x = currentMouse.x;
                    prevMouse.y = currentMouse.y;
                    // This also set the potential label that the current label
                    // is overlapping.
                    highLightLabels();

                }
            }
        };

    }

    private void highLightLabels() {
        potentialLabel = null;
        for (JLabel label : labelToTokenGroupMap.keySet()) {
            label.setForeground(Color.BLACK);
            if (activeLabel != null && label != activeLabel.label) {
                if (tree.isValidMove(activeLabel.token,
                        labelToTokenGroupMap.get(label).token)) {
                    if (isOverlapping(activeLabel.label, label)) {
                        label.setForeground(COLOR_OVERLAP);
                        potentialLabel = labelToTokenGroupMap.get(label);
                    } else {
                        label.setForeground(COLOR_VALID_MOVE);
                    }
                }
            }
        }
    }

    private boolean isOverlapping(JLabel label1, JLabel label2) {
        Rectangle rect1 = label1.getBounds();
        Rectangle rect2 = label2.getBounds();
        // Negate the not overlapping condition.
        // This is easier than finding the overlapping condition.
        return !(rect1.x + rect1.width < rect2.x ||
                rect1.x > rect2.x + rect2.width ||
                rect1.y + rect1.height < rect2.y ||
                rect1.y > rect2.y + rect2.height);
    }

    public void processExpression(String expression) {
        tree = new ExpressionTree();
        tree.parse(expression);
        refreshLabels();
    }

    private void refreshLabels() {
        // Wipe out the labels
        for (JLabel label : labelToTokenGroupMap.keySet()) {
            pnlLabelContainer.remove(label);
        }
        labelToTokenGroupMap.clear();
        tokenToTokenGroupMap.clear();

        // Click and drag adapter for the labels
        int currentX = PADDING_LEFT;
        for (ExpressionTree.Token token : tree.tokens) {
            JLabel newLabel = createLabel(token.tokenString);
            pnlLabelContainer.add(newLabel);
            newLabel.setLocation(currentX, PADDING_TOP);
            Dimension labelSize = newLabel.getPreferredSize();
            newLabel.setBounds(currentX, PADDING_TOP,
                    labelSize.width, labelSize.height);
            newLabel.repaint();
            // Only add drag and drop to leaf nodes.
            if (token.node instanceof ExpressionTree.LeafNode) {
                newLabel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                newLabel.addMouseListener(labelMouseAdapter);
                newLabel.addMouseMotionListener(labelMotionAdapter);
            }
            currentX += newLabel.getWidth() + TOKEN_PADDING;
            TokenLabelGroup newGroup = new TokenLabelGroup(token, newLabel,
                    newLabel.getLocation());
            labelToTokenGroupMap.put(newLabel, newGroup);
            tokenToTokenGroupMap.put(token, newGroup);
        }
        // Set the size of the window to fit the labels.
        int neededWidth = currentX + PADDING_LEFT * 2 + 10;
        /*if (neededWidth > maxWidth) {
            maxWidth = neededWidth;
        }*/
        pnlLabelContainer.repaint();
        // Shrink the window everytime we merge
        this.setSize(neededWidth, this.getPreferredSize().height);
        this.setLocationRelativeTo(null);

        if (tree.tokens.size() == 1) {
            complete();
        }
    }

    private void complete() {
        JOptionPane.showMessageDialog(this, "\\Woo/");
    }

    public JLabel createLabel(String text) {
        JLabel newLabel = new JLabel(text);
        newLabel.setFont(LABEL_FONT);
        return newLabel;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlLabelContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        pnlLabelContainer.setBackground(new java.awt.Color(204, 204, 255));
        pnlLabelContainer.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlLabelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlLabelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlLabelContainer;
    // End of variables declaration//GEN-END:variables
}
