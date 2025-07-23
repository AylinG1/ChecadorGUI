import java.awt.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Wed Jul 23 10:49:29 GMT-06:00 2025
 */



/**
 * @author aylin
 */
public class PrincipaloUsuario extends JFrame {
    public PrincipaloUsuario() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        separator1 = new JPopupMenu.Separator();
        separator2 = new JPopupMenu.Separator();
        separator3 = new JPopupMenu.Separator();
        separator4 = new JPopupMenu.Separator();

        //======== this ========
        setBackground(new Color(0x2385c7));
        var contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(separator1);
        separator1.setBounds(0, 70, 85, separator1.getPreferredSize().height);
        contentPane.add(separator2);
        separator2.setBounds(0, 100, 85, 9);
        contentPane.add(separator3);
        separator3.setBounds(0, 135, 85, 9);
        contentPane.add(separator4);
        separator4.setBounds(0, 170, 85, 9);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPopupMenu.Separator separator1;
    private JPopupMenu.Separator separator2;
    private JPopupMenu.Separator separator3;
    private JPopupMenu.Separator separator4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
