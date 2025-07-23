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
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        separator1 = new JPopupMenu.Separator();
        radioButtonMenuItem1 = new JRadioButtonMenuItem();
        menu2 = new JMenu();
        menuItem1 = new JMenuItem();

        //======== this ========
        setBackground(new Color(0x2385c7));
        var contentPane = getContentPane();
        contentPane.setLayout(null);
        setJMenuBar(menuBar1);

        //======== menu1 ========
        {
            menu1.setText("text");
        }
        contentPane.add(menu1);
        menu1.setBounds(new Rectangle(new Point(10, 20), menu1.getPreferredSize()));
        contentPane.add(separator1);
        separator1.setBounds(10, 50, 120, 3);

        //---- radioButtonMenuItem1 ----
        radioButtonMenuItem1.setText("text");
        contentPane.add(radioButtonMenuItem1);
        radioButtonMenuItem1.setBounds(new Rectangle(new Point(75, 135), radioButtonMenuItem1.getPreferredSize()));

        //======== menu2 ========
        {
            menu2.setText("text");
        }
        contentPane.add(menu2);
        menu2.setBounds(new Rectangle(new Point(30, 70), menu2.getPreferredSize()));

        //---- menuItem1 ----
        menuItem1.setText("text");
        contentPane.add(menuItem1);
        menuItem1.setBounds(new Rectangle(new Point(25, 155), menuItem1.getPreferredSize()));

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
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JPopupMenu.Separator separator1;
    private JRadioButtonMenuItem radioButtonMenuItem1;
    private JMenu menu2;
    private JMenuItem menuItem1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
