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
        this2 = new JFrame();
        panel1 = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        separator2 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        panel6 = new JPanel();
        panel3 = new JPanel();
        panel5 = new JPanel();

        //======== this ========
        setBackground(new Color(0x2385c7));
        var contentPane = getContentPane();
        contentPane.setLayout(null);

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

        //======== this2 ========
        {
            var this2ContentPane = this2.getContentPane();
            this2ContentPane.setLayout(null);

            //======== panel1 ========
            {
                panel1.setLayout(new BorderLayout());

                //======== panelMenu ========
                {
                    panelMenu.setBackground(new Color(0xf2876b));
                    panelMenu.setLayout(null);

                    //---- label1 ----
                    label1.setText("Asistencia");
                    label1.setForeground(Color.white);
                    panelMenu.add(label1);
                    label1.setBounds(15, 125, 125, label1.getPreferredSize().height);

                    //---- separator1 ----
                    separator1.setBackground(Color.white);
                    separator1.setForeground(Color.white);
                    panelMenu.add(separator1);
                    separator1.setBounds(0, 115, 145, 3);

                    //---- label2 ----
                    label2.setText("USUARIO");
                    label2.setForeground(Color.white);
                    panelMenu.add(label2);
                    label2.setBounds(20, 80, 105, 16);

                    //---- separator2 ----
                    separator2.setForeground(Color.white);
                    panelMenu.add(separator2);
                    separator2.setBounds(0, 151, 145, 3);

                    //---- label3 ----
                    label3.setText("Horario");
                    label3.setForeground(Color.white);
                    panelMenu.add(label3);
                    label3.setBounds(15, 165, 115, 16);

                    //---- label4 ----
                    label4.setText("Retardos");
                    label4.setForeground(Color.white);
                    panelMenu.add(label4);
                    label4.setBounds(15, 200, 135, 16);

                    //---- separator4 ----
                    separator4.setForeground(Color.white);
                    panelMenu.add(separator4);
                    separator4.setBounds(0, 190, 145, 3);

                    //---- separator5 ----
                    separator5.setForeground(Color.white);
                    panelMenu.add(separator5);
                    separator5.setBounds(0, 225, 145, 3);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelMenu.getComponentCount(); i++) {
                            Rectangle bounds = panelMenu.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelMenu.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelMenu.setMinimumSize(preferredSize);
                        panelMenu.setPreferredSize(preferredSize);
                    }
                }
                panel1.add(panelMenu, BorderLayout.WEST);

                //======== panel6 ========
                {
                    panel6.setLayout(new BorderLayout());

                    //======== panel3 ========
                    {
                        panel3.setLayout(new BorderLayout());
                    }
                    panel6.add(panel3, BorderLayout.CENTER);
                }
                panel1.add(panel6, BorderLayout.CENTER);
            }
            this2ContentPane.add(panel1);
            panel1.setBounds(0, 0, 745, 370);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < this2ContentPane.getComponentCount(); i++) {
                    Rectangle bounds = this2ContentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = this2ContentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                this2ContentPane.setMinimumSize(preferredSize);
                this2ContentPane.setPreferredSize(preferredSize);
            }
            this2.pack();
            this2.setLocationRelativeTo(this2.getOwner());
        }

        //======== panel5 ========
        {
            panel5.setLayout(new BorderLayout());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JFrame this2;
    private JPanel panel1;
    private JPanel panelMenu;
    private JLabel label1;
    private JSeparator separator1;
    private JLabel label2;
    private JSeparator separator2;
    private JLabel label3;
    private JLabel label4;
    private JSeparator separator4;
    private JSeparator separator5;
    private JPanel panel6;
    private JPanel panel3;
    private JPanel panel5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
