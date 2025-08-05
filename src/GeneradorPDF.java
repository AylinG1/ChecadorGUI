import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class GeneradorPDF {

    public void generarReporteDeRetardos(int idEmpleado, String nombreEmpleado, Date fechaInicio, Date fechaFin, String comentario) {

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaInicioStr = formatoFecha.format(fechaInicio);
        String fechaFinStr = formatoFecha.format(fechaFin);

        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "Downloads" + File.separator + "ReporteRetardos_" + nombreEmpleado.replace(" ", "_") + ".pdf";

        Document document = new Document();

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        int totalMinutosRetardo = 0;

        try {
            base = new BaseSQL();

            // --- CONSULTA SQL CORREGIDA: CALCULA 'minutos_diferencia' dinámicamente ---
            String sql = "SELECT rc.fecha, rc.hora, DATEDIFF(minute, " +
                    "CASE " +
                    "    WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                    "    WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                    "    WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                    "END, rc.hora) AS minutos_diferencia " +
                    "FROM Registros_Checada rc " +
                    "JOIN Empleados e ON rc.id_empleado = e.id " +
                    "JOIN Turnos t ON e.id_turno = t.id " +
                    "WHERE rc.id_empleado = ? AND rc.tipo_registro LIKE 'ENTRADA_%' " +
                    "AND rc.fecha BETWEEN ? AND ? " +
                    "AND DATEDIFF(minute, " +
                    "    CASE " +
                    "        WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                    "        WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                    "        WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                    "    END, rc.hora) > 0 " + // Filtra solo los registros que son retardo
                    "ORDER BY rc.fecha ASC";

            ps = base.conn.prepareStatement(sql);
            ps.setInt(1, idEmpleado);
            ps.setDate(2, new java.sql.Date(fechaInicio.getTime()));
            ps.setDate(3, new java.sql.Date(fechaFin.getTime()));
            rs = ps.executeQuery();

            PdfPTable tablaRetardos = new PdfPTable(3);
            tablaRetardos.setWidthPercentage(100);
            tablaRetardos.setWidths(new float[]{3, 3, 3});
            tablaRetardos.setSpacingAfter(30);

            PdfPCell cell1 = new PdfPCell(new Phrase("Fecha", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            PdfPCell cell2 = new PdfPCell(new Phrase("Hora de Checada", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            PdfPCell cell3 = new PdfPCell(new Phrase("Minutos de Retraso", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaRetardos.addCell(cell1);
            tablaRetardos.addCell(cell2);
            tablaRetardos.addCell(cell3);

            boolean hayRetardos = false;
            while(rs.next()) {
                hayRetardos = true;
                Date fecha = rs.getDate("fecha");
                Time horaChecada = rs.getTime("hora");
                int minutosRetraso = rs.getInt("minutos_diferencia");

                totalMinutosRetardo += minutosRetraso;

                tablaRetardos.addCell(formatoFecha.format(fecha));
                tablaRetardos.addCell(horaChecada.toString());
                tablaRetardos.addCell(String.valueOf(minutosRetraso) + " min");
            }

            if (!hayRetardos) {
                JOptionPane.showMessageDialog(null, "No se encontraron retardos para el empleado en el período seleccionado.", "Sin Retardos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (totalMinutosRetardo <= 15) {
                JOptionPane.showMessageDialog(null, "El total de retardos (" + totalMinutosRetardo + " min) no supera el límite de 15 minutos semanales para generar un reporte.", "Límite no Alcanzado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // --- GENERACIÓN DEL PDF (si se cumplen las condiciones) ---
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Paragraph fechaActual = new Paragraph("Fecha de emisión: " + formatoFecha.format(new Date()), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC));
            fechaActual.setAlignment(Element.ALIGN_RIGHT);
            document.add(fechaActual);

            Paragraph titulo = new Paragraph("INFORME DE RETARDOS Y DESCUENTOS", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            PdfPTable tablaDatos = new PdfPTable(2);
            tablaDatos.setWidthPercentage(100);
            tablaDatos.setSpacingAfter(20);
            tablaDatos.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaDatos.getDefaultCell().setPadding(5);
            tablaDatos.addCell(new Phrase("Nombre del Empleado: ", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            tablaDatos.addCell(nombreEmpleado);
            tablaDatos.addCell(new Phrase("Período del informe: ", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            tablaDatos.addCell("Del " + fechaInicioStr + " al " + fechaFinStr);
            document.add(tablaDatos);

            Paragraph parrafoComentario = new Paragraph("Comentario de la Gerencia:", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD));
            parrafoComentario.setSpacingAfter(5);
            document.add(parrafoComentario);

            Paragraph textoComentario = new Paragraph(comentario, com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 10, com.itextpdf.text.Font.ITALIC));
            textoComentario.setSpacingAfter(20);
            document.add(textoComentario);

            Paragraph subtituloRetardos = new Paragraph("DETALLES DE RETARDOS ACUMULADOS", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 12, com.itextpdf.text.Font.BOLD));
            subtituloRetardos.setSpacingAfter(10);
            document.add(subtituloRetardos);

            document.add(tablaRetardos);

            Paragraph parrafoEnterado = new Paragraph("Yo, " + nombreEmpleado + ", con este documento, confirmo que he sido debidamente notificado y " +
                    "estoy enterado de los retardos y descuentos aplicados durante el período señalado. " +
                    "Acepto y entiendo las políticas de la empresa en cuanto a la puntualidad y sus consecuencias.",
                    com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 10, com.itextpdf.text.Font.NORMAL));
            parrafoEnterado.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafoEnterado.setSpacingAfter(40);
            document.add(parrafoEnterado);

            PdfPTable tablaFirma = new PdfPTable(1);
            tablaFirma.setWidthPercentage(50);
            tablaFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaFirma.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaFirma.addCell(new Phrase("____________________________________", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 10)));
            tablaFirma.addCell(new Phrase("Firma del Empleado", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 10)));
            document.add(tablaFirma);

            document.close();

            JOptionPane.showMessageDialog(null, "El reporte se ha generado correctamente en: " + filePath, "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | IOException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close();     } catch (Exception _ex) {}
            try { if (ps != null) ps.close();     } catch (Exception _ex) {}
            try { if (base != null) base.cerrar(); } catch (Exception _ex) {}
        }
    }
}