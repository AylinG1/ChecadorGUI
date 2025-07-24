import java.awt.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Wed Jul 23 16:28:00 GMT-06:00 2025
 */



/**
 * @author aylin
 */
public class Retardos extends JFrame {
    public Retardos() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        label1 = new JLabel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        button1 = new JButton();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {

            //---- textArea1 ----
            textArea1.setBackground(Color.lightGray);
            textArea1.setForeground(Color.pink);
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(325, 175, 170, 85);

        //---- label1 ----
        label1.setText("Motivo del retardo");
        contentPane.add(label1);
        label1.setBounds(325, 145, 150, label1.getPreferredSize().height);

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(table1);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(30, 145, 230, 167);

        //---- radioButton1 ----
        radioButton1.setText("semanal");
        contentPane.add(radioButton1);
        radioButton1.setBounds(50, 55, 180, radioButton1.getPreferredSize().height);

        //---- radioButton2 ----
        radioButton2.setText("diario");
        contentPane.add(radioButton2);
        radioButton2.setBounds(new Rectangle(new Point(55, 80), radioButton2.getPreferredSize()));

        //---- label2 ----
        label2.setText("Mostrar retardos por:");
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(55, 35), label2.getPreferredSize()));

        //---- label3 ----
        label3.setText("Nombre");
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(190, 10), label3.getPreferredSize()));

        //---- label4 ----
        label4.setText("dd/mm/aaaa");
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(360, 5), label4.getPreferredSize()));

        //---- button1 ----
        button1.setText("Hacer un comentario");
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(375, 295), button1.getPreferredSize()));

        //---- label5 ----
        label5.setText("Resumen de checada");
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(45, 115), label5.getPreferredSize()));

        //---- label6 ----
        label6.setText("D\u00eda");
        contentPane.add(label6);
        label6.setBounds(new Rectangle(new Point(30, 130), label6.getPreferredSize()));

        //---- label7 ----
        label7.setText("Hora");
        contentPane.add(label7);
        label7.setBounds(new Rectangle(new Point(165, 130), label7.getPreferredSize()));

        //---- label8 ----
        label8.setText("Entrada/Salida");
        contentPane.add(label8);
        label8.setBounds(new Rectangle(new Point(235, 130), label8.getPreferredSize()));

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
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JLabel label1;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JButton button1;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
