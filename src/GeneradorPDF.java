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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class GeneradorPDF {

    public void generarReporteDeRetardos(String nombreEmpleado, Date fechaInicio, Date fechaFin, String comentario) {

        // Formato para las fechas en el PDF
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaInicioStr = formatoFecha.format(fechaInicio);
        String fechaFinStr = formatoFecha.format(fechaFin);

        // Definir la ruta de guardado en la carpeta de Descargas
        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "Downloads" + File.separator + "ReporteRetardos_" + nombreEmpleado + ".pdf";

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // --- Encabezado del documento ---
            // Fecha actual a la derecha
            Paragraph fechaActual = new Paragraph("Fecha de emisión: " + formatoFecha.format(new Date()), new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC));
            fechaActual.setAlignment(Element.ALIGN_RIGHT);
            document.add(fechaActual);

            // Título
            Paragraph titulo = new Paragraph("INFORME DE RETARDOS Y DESCUENTOS", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // --- Datos del Empleado y Fechas ---
            PdfPTable tablaDatos = new PdfPTable(2);
            tablaDatos.setWidthPercentage(100);
            tablaDatos.setSpacingAfter(20);
            tablaDatos.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaDatos.getDefaultCell().setPadding(5);

            // Fila 1: Empleado
            tablaDatos.addCell(new Phrase("Nombre del Empleado: ", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            tablaDatos.addCell(nombreEmpleado);

            // Fila 2: Fechas
            tablaDatos.addCell(new Phrase("Período del informe: ", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            tablaDatos.addCell("Del " + fechaInicioStr + " al " + fechaFinStr);

            document.add(tablaDatos);

            // --- Detalles del Comentario ---
            Paragraph parrafoComentario = new Paragraph("Comentario de la Gerencia:", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD));
            parrafoComentario.setSpacingAfter(5);
            document.add(parrafoComentario);

            Paragraph textoComentario = new Paragraph(comentario, com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 10, com.itextpdf.text.Font.ITALIC));
            textoComentario.setSpacingAfter(20);
            document.add(textoComentario);

            // --- Sección de Retardos (Diseño de ejemplo) ---
            Paragraph subtituloRetardos = new Paragraph("DETALLES DE RETARDOS ACUMULADOS", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 12, com.itextpdf.text.Font.BOLD));
            subtituloRetardos.setSpacingAfter(10);
            document.add(subtituloRetardos);

            PdfPTable tablaRetardos = new PdfPTable(3);
            tablaRetardos.setWidthPercentage(100);
            tablaRetardos.setWidths(new float[]{3, 3, 3});
            tablaRetardos.setSpacingAfter(30);

            // Encabezados de la tabla de retardos
            PdfPCell cell1 = new PdfPCell(new Phrase("Fecha", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            PdfPCell cell2 = new PdfPCell(new Phrase("Hora de Checada", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            PdfPCell cell3 = new PdfPCell(new Phrase("Minutos de Retraso", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD)));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaRetardos.addCell(cell1);
            tablaRetardos.addCell(cell2);
            tablaRetardos.addCell(cell3);

            // Aquí iría el bucle para rellenar la tabla con los datos reales de la base de datos
            // Por ahora, añadimos una fila de ejemplo
            tablaRetardos.addCell("15/07/2025");
            tablaRetardos.addCell("09:10 AM");
            tablaRetardos.addCell("10 min");

            document.add(tablaRetardos);

            // --- Sección de "Enterado" (Diseño formal) ---
            Paragraph parrafoEnterado = new Paragraph("Yo, " + nombreEmpleado + ", con este documento, confirmo que he sido debidamente notificado y " +
                    "estoy enterado de los retardos y descuentos aplicados durante el período señalado. " +
                    "Acepto y entiendo las políticas de la empresa en cuanto a la puntualidad y sus consecuencias.",
                    com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 10, com.itextpdf.text.Font.NORMAL));
            parrafoEnterado.setAlignment(Element.ALIGN_JUSTIFIED);
            parrafoEnterado.setSpacingAfter(40);
            document.add(parrafoEnterado);

            // Firma
            PdfPTable tablaFirma = new PdfPTable(1);
            tablaFirma.setWidthPercentage(50);
            tablaFirma.setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaFirma.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            tablaFirma.addCell(new Phrase("____________________________________", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 10)));
            tablaFirma.addCell(new Phrase("Firma del Empleado", com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 10)));
            document.add(tablaFirma);

            document.close();

            JOptionPane.showMessageDialog(null, "El reporte se ha generado correctamente en: " + filePath, "Reporte Generado", JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}