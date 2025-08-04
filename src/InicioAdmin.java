import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Types;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import javax.swing.JTable;
import java.sql.Date;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;


public class InicioAdmin extends JFrame {
    public InicioAdmin() {
        initComponents(); // generado por JFormDesigner
        agregarEventos();
        mostrarPanel("card6");
        nombre.setText(SesionUsuario.usuarioActual);
    }

    public void cargarRegistros() {
        // === 1. Fecha del día en la UI ===
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        fechaadmin.setText("Fecha: " + dtf.format(fechaActual));

        // === 2. Consulta SQL: obtener checadas de entrada y horas esperadas ===
        String sql =
                "SELECT e.id AS id_empleado, e.nombre, rc.tipo_registro, rc.hora AS hora_checada, " +
                        "  CASE " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                        "  END AS hora_esperada, " +
                        "  DATEDIFF(MINUTE, " +
                        "    CASE " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                        "    END, rc.hora) AS minutos_diferencia, " +
                        "  t.entrada_1, t.salida_1, t.entrada_2, t.salida_2, t.entrada_3, t.salida_3 " +
                        "FROM Empleados e " +
                        "JOIN Turnos t ON e.id_turno = t.id " +
                        "LEFT JOIN Registros_Checada rc ON " +
                        "  rc.id_empleado = e.id AND rc.fecha = ? AND rc.tipo_registro LIKE 'ENTRADA_%' " +
                        "ORDER BY e.id, minutos_diferencia ASC";

        BaseSQL base = null;
        PreparedStatement psCount = null;
        PreparedStatement ps = null;
        ResultSet rsCount = null;
        ResultSet rs = null;

        DefaultTableModel model = new DefaultTableModel(new String[] {
                "Nombre", "Tipo Registro", "Hora Checada", "Hora Esperada", "Min. Dif."
        }, 0);

        int totalEmpleados = 0;
        Map<Integer, Integer> mejoresDiferencias = new HashMap<>();
        Map<Integer, Double> horasEsperadasPorEmpleado = new HashMap<>();

        try {
            base = new BaseSQL();

            // === 3. Contar el total de empleados registrados ===
            psCount = base.conn.prepareStatement("SELECT COUNT(*) FROM Empleados");
            rsCount = psCount.executeQuery();
            if (rsCount.next()) {
                totalEmpleados = rsCount.getInt(1);
            }

            // === 4. Ejecutar consulta con la fecha actual ===
            ps = base.conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(fechaActual));
            rs = ps.executeQuery();

            // === 5. Recorrer cada fila de resultado y acumular datos ===
            while (rs.next()) {
                int id = rs.getInt("id_empleado");
                String nombre = rs.getString("nombre");
                String tipo = rs.getString("tipo_registro");
                Time horaCheque = rs.getTime("hora_checada");
                Time horaEsper = rs.getTime("hora_esperada");
                int dif = rs.getInt("minutos_diferencia");

                // Calcular la mejor diferencia de entrada para cada empleado
                if (!rs.wasNull()) {
                    Integer prev = mejoresDiferencias.get(id);
                    if (prev == null || Math.abs(dif) < Math.abs(prev)) {
                        mejoresDiferencias.put(id, dif);
                    }
                }

                // Calcular y guardar las horas de trabajo esperadas para cada empleado
                if (!horasEsperadasPorEmpleado.containsKey(id)) {
                    Time entrada1 = rs.getTime("entrada_1");
                    Time salida1  = rs.getTime("salida_1");
                    Time entrada2 = rs.getTime("entrada_2");
                    Time salida2  = rs.getTime("salida_2");
                    Time entrada3 = rs.getTime("entrada_3");
                    Time salida3  = rs.getTime("salida_3");

                    double minutosEsperados = 0;
                    if (entrada1 != null && salida1 != null) {
                        minutosEsperados += (salida1.getTime() - entrada1.getTime()) / (60 * 1000);
                    }
                    if (entrada2 != null && salida2 != null) {
                        minutosEsperados += (salida2.getTime() - entrada2.getTime()) / (60 * 1000);
                    }
                    if (entrada3 != null && salida3 != null) {
                        minutosEsperados += (salida3.getTime() - entrada3.getTime()) / (60 * 1000);
                    }
                    horasEsperadasPorEmpleado.put(id, minutosEsperados);
                }

                // === Llenar la fila visible (sin idEmpleado) ===
                model.addRow(new Object[]{ nombre, tipo, horaCheque, horaEsper, dif });
            }

            // === 6. Clasificar asistencias, retardos y faltas ===
            int asist = 0;
            double totalMinutosHorasEsperadas = 0;

            for (Map.Entry<Integer, Integer> entry : mejoresDiferencias.entrySet()) {
                int idEmpleado = entry.getKey();
                int minutos = entry.getValue();

                // Un empleado asistió si no tiene una falta (minutos_diferencia < 15)
                if (minutos <= 15) {
                    asist++;
                    // Sumar las horas esperadas SÓLO para los empleados que asistieron
                    totalMinutosHorasEsperadas += horasEsperadasPorEmpleado.getOrDefault(idEmpleado, 0.0);
                }
            }

            // Los que no marcaron se consideran falta
            int fal = totalEmpleados - mejoresDiferencias.size();

            // === 7. Calcular y mostrar porcentajes ===
            double pctAs = totalEmpleados > 0 ? 100.0 * asist / totalEmpleados : 0.0;
            double pctFal = totalEmpleados > 0 ? 100.0 * fal / totalEmpleados : 0.0;
            double totalHorasEsperadas = totalMinutosHorasEsperadas / 60.0;

            labelAsistenciasTotales.setText(asist + "/" + totalEmpleados);
            labelAsistenciasPorcentaje.setText(String.format("%.1f%%", pctAs));
            labelFaltasTotales.setText(fal + "/" + totalEmpleados);
            labelFaltasPorcentaje.setText(String.format("%.1f%%", pctFal));
            labelhorasTotales.setText(String.format("%.1f", totalHorasEsperadas));

            // El porcentaje de horas trabajadas es sobre el total de horas esperadas para todos los empleados.
            // Para este ejemplo, asumiremos que las horas trabajadas son las mismas que las esperadas para los que asistieron.
            double totalMinutosEsperadosGlobal = horasEsperadasPorEmpleado.values().stream().mapToDouble(Double::doubleValue).sum();
            double pctHoras = totalMinutosEsperadosGlobal > 0 ? 100.0 * totalMinutosHorasEsperadas / totalMinutosEsperadosGlobal : 0.0;
            labelhorasPorcentaje.setText(String.format("%.1f%%", pctHoras));
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar registros: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // === 9. Cierre de recursos en el bloque 'finally' ===
            try { if (rs != null) rs.close(); } catch (Exception _ex) {}
            try { if (ps != null) ps.close(); } catch (Exception _ex) {}
            try { if (rsCount != null) rsCount.close(); } catch (Exception _ex) {}
            try { if (psCount != null) psCount.close(); } catch (Exception _ex) {}
            try { if (base != null) base.cerrar(); } catch (Exception _ex) {}
        }
    }


    public void llenadotabla() {
        // 1. Consulta SQL: Obtiene todas las checadas de entrada para la fecha actual
        String sql =
                "SELECT e.id AS id_empleado, e.nombre, rc.tipo_registro, rc.hora AS hora_checada, " +
                        "  CASE " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                        "  END AS hora_esperada, " +
                        "  DATEDIFF(MINUTE, " +
                        "    CASE " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                        "    END, rc.hora) AS minutos_diferencia " +
                        "FROM Empleados e " +
                        "JOIN Turnos t ON e.id_turno = t.id " +
                        "LEFT JOIN Registros_Checada rc ON " +
                        "  rc.id_empleado = e.id AND rc.fecha = ? AND rc.tipo_registro LIKE 'ENTRADA_%' " +
                        "ORDER BY e.id, minutos_diferencia ASC";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // 2. Modelo de tabla
        DefaultTableModel model = new DefaultTableModel(new String[] {
                "Nombre", "Tipo Registro", "Hora Checada", "Hora Esperada", "Min. Dif."
        }, 0);

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);

            // Declaramos la variable 'fechaActual' aquí
            java.time.LocalDate fechaActual = java.time.LocalDate.now();

            ps.setDate(1, java.sql.Date.valueOf(fechaActual));
            rs = ps.executeQuery();

            // 3. Llenar la tabla con los resultados
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String tipo   = rs.getString("tipo_registro");
                Time horaCheque = rs.getTime("hora_checada");
                Time horaEsper  = rs.getTime("hora_esperada");
                int dif = rs.getInt("minutos_diferencia");

                model.addRow(new Object[]{ nombre, tipo, horaCheque, horaEsper, dif });
            }

            // 4. Asignar el modelo a la JTable
            tabladia.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar registros: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception _ex) {}
            try { if (ps != null) ps.close(); } catch (Exception _ex) {}
            try { if (base != null) base.cerrar(); } catch (Exception _ex) {}
        }
    }


    public void cargarRegistrosadmin() {
        // === 1. SQL para traer los datos de los registros ===
        String sql = "SELECT usuario, accion, fecha, hora FROM bitacora_accesos"; // Suponiendo que la tabla se llama 'Registros'

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // === 2. Modelo para la tabla con las columnas deseadas ===
        DefaultTableModel model = new DefaultTableModel(new String[] {
                "Usuario", "Acción", "Fecha", "Hora"
        }, 0);

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // === 3. Recorrer filas y llenar el modelo ===
            while (rs.next()) {
                String usuario = rs.getString("usuario");
                String accion = rs.getString("accion");
                Date fecha = rs.getDate("fecha");
                Time hora = rs.getTime("hora");

                model.addRow(new Object[] {
                        usuario,
                        accion,
                        fecha,
                        hora
                });
            }

            // === 4. Asignar el modelo a la JTable ===
            if (tablaregistros != null) {
                tablaregistros.setModel(model);
            } else {
                System.err.println("Error: La tabla 'tablaregistros' no está inicializada.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar registros: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close();      } catch (Exception _e) {}
            try { if (ps != null) ps.close();      } catch (Exception _e) {}
            try { if (base != null) base.cerrar(); } catch (Exception _e) {}
        }
    }



    public void cargarSesiones() {
        // === 1. SQL para traer los datos de las sesiones con JOIN a la tabla de Usuarios ===
        String sql = "SELECT u.Usuario, l.FechaHora, l.Accion " +
                "FROM LogsActividad l " +
                "JOIN Usuarios u ON l.UsuarioID = u.UsuarioID " +
                "ORDER BY l.FechaHora DESC";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // === 2. Modelo para la tabla con las columnas deseadas ===
        DefaultTableModel model = new DefaultTableModel(new String[] {
                "Usuario", "Fecha y Hora", "Acción"
        }, 0);

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // === 3. Recorrer filas y llenar el modelo ===
            while (rs.next()) {
                String usuario = rs.getString("Usuario");
                Timestamp fechaHora = rs.getTimestamp("FechaHora");
                String accion = rs.getString("Accion");

                model.addRow(new Object[] {
                        usuario,
                        fechaHora,
                        accion
                });
            }

            // === 4. Asignar el modelo a la JTable ===
            if (tablasesiones != null) {
                tablasesiones.setModel(model);
            } else {
                System.err.println("Error: La tabla 'tablasesiones' no está inicializada.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar sesiones: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close();      } catch (Exception _e) {}
            try { if (ps != null) ps.close();      } catch (Exception _e) {}
            try { if (base != null) base.cerrar(); } catch (Exception _e) {}
        }
    }

    public void cargarTurnosEnComboBox() {
        String sql = "SELECT id, nombre FROM Turnos ORDER BY nombre";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // Limpiar el JComboBox y establecer el tipo de objeto que guardará
            cmbturnoempleado.removeAllItems();

            while (rs.next()) {
                int idTurno = rs.getInt("id");
                String nombreTurno = rs.getString("nombre");

                // Creamos un nuevo objeto 'Turno' y lo agregamos al JComboBox
                cmbturnoempleado.addItem(new Turno(idTurno, nombreTurno));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores
        } finally {
            try { if (rs != null) rs.close();      } catch (Exception _e) {}
            try { if (ps != null) ps.close();      } catch (Exception _e) {}
            try { if (base != null) base.cerrar(); } catch (Exception _e) {}
        }
    }


    public void crearEmpleado() {
        // 1. Leer los campos de la interfaz de usuario
        String nombre2 = txtnombreempleado.getText().trim(); // Nombre del empleado (variable cambiada a 'nombre2')
        String rol = label40.getText().trim();

        // Obtener el objeto 'Turno' completo desde el JComboBox
        Turno turnoSeleccionado = (Turno) cmbturnoempleado.getSelectedItem();

        // Variable para el nombre del usuario, que se obtiene de la JLabel
        // Asume que la variable de la JLabel se llama 'nombre'.
        String nombreUsuario = nombre.getText();

        // Validar que el nombre no esté vacío y que se haya seleccionado un turno
        if (nombre2.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre del empleado no puede estar vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (turnoSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un turno.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idTurno = turnoSeleccionado.getId(); // Obtenemos el ID directamente del objeto

        BaseSQL base = null;
        CallableStatement cs = null;
        PreparedStatement psBitacora = null;

        String accionBitacora = "intentó agregar un nuevo perfil de empleado"; // Mensaje por defecto en caso de error

        try {
            base = new BaseSQL();
            cs = base.conn.prepareCall("{call InsertarEmpleado(?, ?, ?)}");

            cs.setString(1, nombre2); // Usa la nueva variable 'nombre2' para el nombre del empleado
            cs.setInt(2, idTurno);
            cs.setString(3, rol);

            cs.executeUpdate();

            accionBitacora = "agregó un nuevo perfil de empleado: " + nombre2; // Mensaje de éxito

            JOptionPane.showMessageDialog(null, "Empleado creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // -------------------- INICIO: REGISTRO EN BITÁCORA --------------------
            try {
                if (base != null && base.conn != null) {
                    String sqlBitacora = "INSERT INTO bitacora_accesos (usuario, accion, fecha, hora) VALUES (?, ?, ?, ?)";
                    psBitacora = base.conn.prepareStatement(sqlBitacora);

                    psBitacora.setString(1, nombreUsuario); // Usa la variable 'nombreUsuario' para la bitácora
                    psBitacora.setString(2, accionBitacora);
                    psBitacora.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    psBitacora.setTime(4, java.sql.Time.valueOf(java.time.LocalTime.now()));
                    psBitacora.executeUpdate();
                }
            } catch (SQLException ex) {
                System.err.println("Error al registrar en bitácora: " + ex.getMessage());
            }
            // -------------------- FIN: REGISTRO EN BITÁCORA --------------------

            // Cierre de recursos
            try { if (cs != null) cs.close(); } catch (SQLException _ex) { _ex.printStackTrace(); }
            try { if (psBitacora != null) psBitacora.close(); } catch (SQLException _ex) { _ex.printStackTrace(); }
            try { if (base != null) base.cerrar(); } catch (SQLException _ex) { _ex.printStackTrace(); }
        }
    }


    public void cargarEmpleados() {
        // SQL para traer los datos de la tabla Empleados
        String sql = "SELECT id, nombre, id_turno, rol FROM Empleados";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Modelo para la tabla con las columnas de Empleados
        DefaultTableModel model = new DefaultTableModel(new String[] {
                "ID", "Nombre", "ID Turno", "Rol"
        }, 0);

        try {
            base = new BaseSQL();

            // Ejecución del SELECT
            ps = base.conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // Recorrer filas y llenar el modelo de la tabla
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int id_turno = rs.getInt("id_turno");
                String rol = rs.getString("rol");

                // Añadir una nueva fila al modelo con los datos del empleado
                model.addRow(new Object[] {
                        id,
                        nombre,
                        id_turno,
                        rol
                });
            }

            // Asignar el modelo a la JTable de la UI
            if (tablaempleados != null) {
                tablaempleados.setModel(model);
            } else {
                // Manejo de error si la tabla no está inicializada
                System.err.println("Error: El componente 'tablaempleados' no está inicializado.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar empleados: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar recursos en el bloque 'finally' para asegurar que se liberen
            try { if (rs != null) rs.close(); } catch (Exception _e) {}
            try { if (ps != null) ps.close(); } catch (Exception _e) {}
            try { if (base != null) base.cerrar(); } catch (Exception _e) {}
        }
    }




    public void crearTurno() {
        // 1. Leer campos
        String nombre2 = txtnombre.getText().trim(); // Nombre del turno (VARIABLE CAMBIADA A nombre2)
        String e1 = txtentrada1.getText().trim();
        String s1 = txtsalida1.getText().trim();
        String e2 = txtentrada2.getText().trim();
        String s2 = txtsalida2.getText().trim();
        String e3 = txtentrada3.getText().trim();
        String s3 = txtsalida3.getText().trim();
        int tolerancia = 15;

        BaseSQL base = null;
        CallableStatement cs = null;
        PreparedStatement psBitacora = null;

        String accionBitacora = "intentó insertar un nuevo turno";

        // --- CORRECCIÓN AQUÍ ---
        // Se obtiene el nombre del usuario de la JLabel llamada 'nombre'.
        String nombreUsuario = nombre.getText();

        try {
            base = new BaseSQL();
            cs = base.conn.prepareCall("{call InsertarTurno(?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setString(1, nombre2); // Usa la nueva variable 'nombre2' para el nombre del turno
            setHoraNullable(cs, 2, e1);
            setHoraNullable(cs, 3, s1);
            setHoraNullable(cs, 4, e2);
            setHoraNullable(cs, 5, s2);
            setHoraNullable(cs, 6, e3);
            setHoraNullable(cs, 7, s3);
            cs.setInt(8, tolerancia);

            int filas = cs.executeUpdate();

            accionBitacora = "insertó un nuevo turno";

            JOptionPane.showMessageDialog(null, "Turno creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al insertar turno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // -------------------- INICIO: REGISTRO EN BITÁCORA --------------------
            try {
                if (base != null && base.conn != null) {
                    String sqlBitacora = "INSERT INTO bitacora_accesos (usuario, accion, fecha, hora) VALUES (?, ?, ?, ?)";
                    psBitacora = base.conn.prepareStatement(sqlBitacora);

                    psBitacora.setString(1, nombreUsuario); // Usa la variable 'nombreUsuario'
                    psBitacora.setString(2, accionBitacora);
                    psBitacora.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    psBitacora.setTime(4, java.sql.Time.valueOf(java.time.LocalTime.now()));
                    psBitacora.executeUpdate();
                }
            } catch (SQLException ex) {
                System.err.println("Error al registrar en bitácora: " + ex.getMessage());
            }
            // -------------------- FIN: REGISTRO EN BITÁCORA --------------------

            try { if (cs != null) cs.close(); } catch (SQLException _ex) { _ex.printStackTrace(); }
            try { if (psBitacora != null) psBitacora.close(); } catch (SQLException _ex) { _ex.printStackTrace(); }
            try { if (base != null) base.cerrar(); } catch (SQLException _ex) { _ex.printStackTrace(); }
        }
    }

    private void setHoraNullable(CallableStatement cs, int idx, String texto) throws SQLException {
        if (texto.isEmpty()) {
            cs.setNull(idx, Types.TIME);
        } else {
            try {
                LocalTime lt = LocalTime.parse(texto);
                Time ts = Time.valueOf(lt);
                cs.setTime(idx, ts);
            } catch (DateTimeParseException dtpe) {
                throw new SQLException("Formato inválido en campo hora (esperado HH:mm o HH:mm:ss): " + texto);
            }
        }
    }




    public void cargarTurnos() {
        // Variable para obtener el nombre del usuario, asumiendo que lo obtienes de un JLabel
        String nombreUsuario = nombre.getText();

        // === 2. SQL para traer nombre y horarios de entrada/salida ===
        String sqlSelect = "SELECT nombre, entrada_1, salida_1, entrada_2, salida_2, entrada_3, salida_3 " +
                "FROM Turnos";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // === Modelo para la tabla con las columnas deseadas ===
        DefaultTableModel model = new DefaultTableModel(new String[] {
                "Turno", "Entrada 1", "Salida 1", "Entrada 2", "Salida 2", "Entrada 3", "Salida 3"
        }, 0);

        try {
            base = new BaseSQL();

            // === Ejecución del SELECT para cargar los turnos ===
            ps = base.conn.prepareStatement(sqlSelect);
            rs = ps.executeQuery();

            // === 3. Recorrer filas y llenar la tabla ===
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                Time e1 = rs.getTime("entrada_1");
                Time s1 = rs.getTime("salida_1");
                Time e2 = rs.getTime("entrada_2");
                Time s2 = rs.getTime("salida_2");
                Time e3 = rs.getTime("entrada_3");
                Time s3 = rs.getTime("salida_3");

                model.addRow(new Object[] {
                        nombre,
                        e1, s1,
                        e2, s2,
                        e3, s3
                });
            }

            // === 4. Asigna el modelo a la JTable de la UI ===
            tablaturnos.setModel(model);

            // -------------------- INICIO: REGISTRO EN BITÁCORA --------------------
            // Esta sección registra la acción en la tabla 'bitacora_accesos'
            // cada vez que los datos de los turnos son visualizados.
            try {
                // Obtenemos la fecha y hora actuales
                LocalDate fechaActual = LocalDate.now();
                LocalTime horaActual = LocalTime.now();

                // SQL para insertar el registro en la bitácora
                String sqlInsert = "INSERT INTO bitacora_accesos (usuario, accion, fecha, hora) VALUES (?, ?, ?, ?)";

                // Usamos un nuevo PreparedStatement para el INSERT
                try (PreparedStatement psInsert = base.conn.prepareStatement(sqlInsert)) {
                    psInsert.setString(1, nombreUsuario);
                    psInsert.setString(2, "Visualizó datos sobre los empleados");
                    psInsert.setObject(3, fechaActual);
                    psInsert.setObject(4, horaActual);
                    psInsert.executeUpdate();
                }
            } catch (SQLException ex) {
                // Manejar errores si falla el registro en la bitácora
                System.err.println("Error al registrar en bitácora: " + ex.getMessage());
            }
            // -------------------- FIN: REGISTRO EN BITÁCORA --------------------

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar turnos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close();      } catch (Exception _e) {}
            try { if (ps != null) ps.close();      } catch (Exception _e) {}
            try { if (base != null) base.cerrar(); } catch (Exception _e) {}
        }
    }



    private void agregarEventos() {

        label3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card2");
                cargarRegistros();
                llenadotabla();
            }
        });

        label4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3");
                LocalDate hoy = LocalDate.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                fechabitacora.setText("Fecha: " + hoy.format(fmt));
                cargarRegistrosadmin();
                cargarSesiones();
            }
        });

        label5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card4");
            }
        });
        
        label17.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card7");
                LocalDate hoy = LocalDate.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                label31.setText("Fecha: " + hoy.format(fmt));
                cargarTurnos();
            }
        });

        label21.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card8");
                // 1. Mostrar la fecha actual en el label
                LocalDate hoy = LocalDate.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                labelFechaCrearTurno.setText("Fecha: " + hoy.format(fmt));
            }
        });
        label1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card12");
                // 1. Mostrar la fecha actual en el label
                LocalDate hoy = LocalDate.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                labelFechaTurnos.setText("Fecha: " + hoy.format(fmt));
                cargarEmpleados();
            }
        });
        label32.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card9");
                // 1. Mostrar la fecha actual en el label
                LocalDate hoy = LocalDate.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
                labelfe.setText("Fecha: " + hoy.format(fmt));
                cargarTurnosEnComboBox();

            }
        });
    }



    private void mostrarPanel(String nombreCard) {
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, nombreCard);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InicioAdmin().setVisible(true);

        });
    }

    private void label1MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void SeguridadYrolesLbl(MouseEvent e) {
        // TODO add your code here
    }

    private void BitacoraActsLBL(MouseEvent e) {
        // TODO add your code here
    }

    private void ReglasLBL(MouseEvent e) {
        // TODO add your code here
    }

    private void label5MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void label11MouseClicked(MouseEvent e) {
        // TODO add your code here
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Cierra la ventana actual
            new LoginPrincipal().setVisible(true); // Abre la ventana de login
        }
    }

	private void crearturno(ActionEvent e) {

        crearTurno();
    }

    private void crearturno2(ActionEvent e) {
        crearEmpleado();
        // TODO add your code here
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Juan
        panelBase = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        separator2 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        separator3 = new JSeparator();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        label2 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        label14 = new JLabel();
        label15 = new JLabel();
        label16 = new JLabel();
        separator6 = new JSeparator();
        label17 = new JLabel();
        label19 = new JLabel();
        nombre = new JLabel();
        panelInicio = new JPanel();
        panelSeguridadYrol = new JPanel();
        label10 = new JLabel();
        label42 = new JLabel();
        fechaadmin = new JLabel();
        panelasistencias = new JPanel();
        label43 = new JLabel();
        labelAsistenciasTotales = new JLabel();
        labelAsistenciasPorcentaje = new JLabel();
        panelfaltas = new JPanel();
        labelFaltasTotales = new JLabel();
        label45 = new JLabel();
        labelFaltasPorcentaje = new JLabel();
        panelgrafica = new JPanel();
        scrollPane6 = new JScrollPane();
        tabladia = new JTable();
        panelhoras = new JPanel();
        labelhorasTotales = new JLabel();
        label46 = new JLabel();
        labelhorasPorcentaje = new JLabel();
        label47 = new JLabel();
        panelBitácoraAct = new JPanel();
        label7 = new JLabel();
        label24 = new JLabel();
        fechabitacora = new JLabel();
        label37 = new JLabel();
        scrollPane4 = new JScrollPane();
        tablaregistros = new JTable();
        label41 = new JLabel();
        scrollPane5 = new JScrollPane();
        tablasesiones = new JTable();
        panelReglas = new JPanel();
        label9 = new JLabel();
        label8 = new JLabel();
        panelGestionEmp = new JPanel();
        label6 = new JLabel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panelBienvenida = new JPanel();
        panelTurnos = new JPanel();
        label18 = new JLabel();
        scrollPane2 = new JScrollPane();
        tablaturnos = new JTable();
        label20 = new JLabel();
        label21 = new JLabel();
        label31 = new JLabel();
        AsignarTurno = new JPanel();
        label22 = new JLabel();
        label23 = new JLabel();
        labelFechaCrearTurno = new JLabel();
        txtnombre = new JTextField();
        txtentrada1 = new JTextField();
        label25 = new JLabel();
        label26 = new JLabel();
        txtentrada2 = new JTextField();
        txtentrada3 = new JTextField();
        label27 = new JLabel();
        label28 = new JLabel();
        label29 = new JLabel();
        label30 = new JLabel();
        txtsalida3 = new JTextField();
        txtsalida2 = new JTextField();
        txtsalida1 = new JTextField();
        crearturno = new JButton();
        panelempleados = new JPanel();
        labelFechaTurnos = new JLabel();
        label32 = new JLabel();
        label33 = new JLabel();
        label34 = new JLabel();
        scrollPane3 = new JScrollPane();
        tablaempleados = new JTable();
        AsignarEmpleados = new JPanel();
        label35 = new JLabel();
        labelfe = new JLabel();
        txtnombreempleado = new JTextField();
        label36 = new JLabel();
        label38 = new JLabel();
        label39 = new JLabel();
        crearturno2 = new JButton();
        cmbturnoempleado = new JComboBox();
        label40 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panelBase ========
        {
            panelBase.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,panelBase. getBorder( )) ); panelBase. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );
            panelBase.setLayout(new BorderLayout());

            //======== panelMenu ========
            {
                panelMenu.setBackground(Color.white);
                panelMenu.setLayout(null);

                //---- label1 ----
                label1.setText("Gesti\u00f3n de Empleados");
                label1.setForeground(new Color(0xff8d1b));
                label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label1.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                label1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label1MouseClicked(e);
                    }
                });
                panelMenu.add(label1);
                label1.setBounds(65, 230, 140, label1.getPreferredSize().height);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(new Color(0xff892a));
                panelMenu.add(separator1);
                separator1.setBounds(10, 75, 195, 20);

                //---- separator2 ----
                separator2.setForeground(new Color(0xff7c25));
                panelMenu.add(separator2);
                separator2.setBounds(10, 260, 195, 20);

                //---- label3 ----
                label3.setText("Centro Administrativo");
                label3.setForeground(new Color(0xff871d));
                label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label3.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 13));
                label3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SeguridadYrolesLbl(e);
                    }
                });
                panelMenu.add(label3);
                label3.setBounds(65, 85, 150, label3.getPreferredSize().height);

                //---- label4 ----
                label4.setText("Bit\u00e1cora de actividades");
                label4.setForeground(new Color(0xff8420));
                label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label4.setFont(new Font("Microsoft YaHei Light", Font.PLAIN, 13));
                label4.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        BitacoraActsLBL(e);
                    }
                });
                panelMenu.add(label4);
                label4.setBounds(65, 125, 145, 35);

                //---- label5 ----
                label5.setText("Reglas");
                label5.setForeground(new Color(0xff9018));
                label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label5.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                label5.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ReglasLBL(e);
                    }
                });
                panelMenu.add(label5);
                label5.setBounds(65, 180, 121, label5.getPreferredSize().height);

                //---- separator3 ----
                separator3.setForeground(new Color(0xff7814));
                panelMenu.add(separator3);
                separator3.setBounds(0, 209, 205, 20);

                //---- separator4 ----
                separator4.setForeground(new Color(0xff7c17));
                panelMenu.add(separator4);
                separator4.setBounds(0, 117, 205, 20);

                //---- separator5 ----
                separator5.setForeground(new Color(0xff861b));
                panelMenu.add(separator5);
                separator5.setBounds(0, 163, 210, 20);

                //---- label2 ----
                label2.setText("ADMINISTRADOR");
                label2.setForeground(new Color(0xff8327));
                label2.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                panelMenu.add(label2);
                label2.setBounds(30, 50, 155, label2.getPreferredSize().height);

                //---- label11 ----
                label11.setText("Cerrar sesi\u00f3n");
                label11.setForeground(Color.black);
                label11.setBackground(new Color(0xff9999));
                label11.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label5MouseClicked(e);
                        label11MouseClicked(e);
                    }
                });
                panelMenu.add(label11);
                label11.setBounds(15, 390, 125, 16);

                //---- label12 ----
                label12.setIcon(new ImageIcon(getClass().getResource("/lo.jpg")));
                panelMenu.add(label12);
                label12.setBounds(10, 70, 40, 50);

                //---- label13 ----
                label13.setIcon(new ImageIcon(getClass().getResource("/lolo.jpg")));
                panelMenu.add(label13);
                label13.setBounds(new Rectangle(new Point(10, 120), label13.getPreferredSize()));

                //---- label14 ----
                label14.setIcon(new ImageIcon(getClass().getResource("/lii.jpg")));
                panelMenu.add(label14);
                label14.setBounds(new Rectangle(new Point(15, 170), label14.getPreferredSize()));

                //---- label15 ----
                label15.setIcon(new ImageIcon(getClass().getResource("/jl.jpg")));
                panelMenu.add(label15);
                label15.setBounds(new Rectangle(new Point(15, 215), label15.getPreferredSize()));

                //---- label16 ----
                label16.setIcon(new ImageIcon(getClass().getResource("/sal.jpg")));
                panelMenu.add(label16);
                label16.setBounds(105, 380, 45, 40);

                //---- separator6 ----
                separator6.setForeground(new Color(0xff7c25));
                panelMenu.add(separator6);
                separator6.setBounds(15, 310, 195, 20);

                //---- label17 ----
                label17.setText("Gesti\u00f3n de Turnos");
                label17.setForeground(new Color(0xff8d1b));
                label17.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label17.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                label17.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label1MouseClicked(e);
                    }
                });
                panelMenu.add(label17);
                label17.setBounds(65, 275, 140, 18);

                //---- label19 ----
                label19.setIcon(new ImageIcon(getClass().getResource("/loc.jpg")));
                panelMenu.add(label19);
                label19.setBounds(15, 265, 40, 38);

                //---- nombre ----
                nombre.setText("ADMINISTRADOR");
                nombre.setForeground(new Color(0xff8327));
                nombre.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                panelMenu.add(nombre);
                nombre.setBounds(15, 15, 155, 20);

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
            panelBase.add(panelMenu, BorderLayout.WEST);

            //======== panelInicio ========
            {
                panelInicio.setForeground(new Color(0xf8f0de));
                panelInicio.setBackground(new Color(0xf8f0de));
                panelInicio.setLayout(new CardLayout());

                //======== panelSeguridadYrol ========
                {
                    panelSeguridadYrol.setBackground(new Color(0xff9966));
                    panelSeguridadYrol.setLayout(null);

                    //---- label10 ----
                    label10.setText("Centro de Administraci\u00f3n");
                    label10.setForeground(Color.black);
                    label10.setFont(new Font("Inter", Font.BOLD, 20));
                    panelSeguridadYrol.add(label10);
                    label10.setBounds(20, 20, 300, 30);

                    //---- label42 ----
                    label42.setText("Vista general de las m\u00e9tricas y estad\u00edsticas principales.");
                    label42.setForeground(Color.black);
                    panelSeguridadYrol.add(label42);
                    label42.setBounds(50, 60, 415, 16);

                    //---- fechaadmin ----
                    fechaadmin.setText("text");
                    fechaadmin.setForeground(Color.black);
                    panelSeguridadYrol.add(fechaadmin);
                    fechaadmin.setBounds(415, 25, 205, 16);

                    //======== panelasistencias ========
                    {
                        panelasistencias.setBackground(new Color(0xccffcc));
                        panelasistencias.setLayout(null);

                        //---- label43 ----
                        label43.setText("Asistencias");
                        label43.setFont(new Font("Inter", Font.PLAIN, 16));
                        panelasistencias.add(label43);
                        label43.setBounds(10, 5, 105, label43.getPreferredSize().height);

                        //---- labelAsistenciasTotales ----
                        labelAsistenciasTotales.setText("text");
                        panelasistencias.add(labelAsistenciasTotales);
                        labelAsistenciasTotales.setBounds(10, 30, 105, 17);

                        //---- labelAsistenciasPorcentaje ----
                        labelAsistenciasPorcentaje.setText("text");
                        panelasistencias.add(labelAsistenciasPorcentaje);
                        labelAsistenciasPorcentaje.setBounds(10, 50, 100, 17);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panelasistencias.getComponentCount(); i++) {
                                Rectangle bounds = panelasistencias.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panelasistencias.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panelasistencias.setMinimumSize(preferredSize);
                            panelasistencias.setPreferredSize(preferredSize);
                        }
                    }
                    panelSeguridadYrol.add(panelasistencias);
                    panelasistencias.setBounds(40, 90, 130, 75);

                    //======== panelfaltas ========
                    {
                        panelfaltas.setBackground(new Color(0xccffcc));
                        panelfaltas.setLayout(null);

                        //---- labelFaltasTotales ----
                        labelFaltasTotales.setText("text");
                        panelfaltas.add(labelFaltasTotales);
                        labelFaltasTotales.setBounds(10, 30, 105, labelFaltasTotales.getPreferredSize().height);

                        //---- label45 ----
                        label45.setText("Faltas");
                        label45.setFont(new Font("Inter", Font.PLAIN, 16));
                        panelfaltas.add(label45);
                        label45.setBounds(10, 5, 105, 20);

                        //---- labelFaltasPorcentaje ----
                        labelFaltasPorcentaje.setText("text");
                        panelfaltas.add(labelFaltasPorcentaje);
                        labelFaltasPorcentaje.setBounds(10, 50, 105, 16);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panelfaltas.getComponentCount(); i++) {
                                Rectangle bounds = panelfaltas.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panelfaltas.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panelfaltas.setMinimumSize(preferredSize);
                            panelfaltas.setPreferredSize(preferredSize);
                        }
                    }
                    panelSeguridadYrol.add(panelfaltas);
                    panelfaltas.setBounds(225, 90, 130, 75);

                    //======== panelgrafica ========
                    {
                        panelgrafica.setBackground(new Color(0xccffcc));
                        panelgrafica.setLayout(null);

                        //======== scrollPane6 ========
                        {
                            scrollPane6.setViewportView(tabladia);
                        }
                        panelgrafica.add(scrollPane6);
                        scrollPane6.setBounds(15, 10, 560, 285);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panelgrafica.getComponentCount(); i++) {
                                Rectangle bounds = panelgrafica.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panelgrafica.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panelgrafica.setMinimumSize(preferredSize);
                            panelgrafica.setPreferredSize(preferredSize);
                        }
                    }
                    panelSeguridadYrol.add(panelgrafica);
                    panelgrafica.setBounds(25, 225, 590, 310);

                    //======== panelhoras ========
                    {
                        panelhoras.setBackground(new Color(0xccffcc));
                        panelhoras.setLayout(null);

                        //---- labelhorasTotales ----
                        labelhorasTotales.setText("text");
                        panelhoras.add(labelhorasTotales);
                        labelhorasTotales.setBounds(10, 30, 105, labelhorasTotales.getPreferredSize().height);

                        //---- label46 ----
                        label46.setText("Horas trabajadas");
                        label46.setFont(new Font("Inter", Font.PLAIN, 16));
                        panelhoras.add(label46);
                        label46.setBounds(10, 5, 135, 20);

                        //---- labelhorasPorcentaje ----
                        labelhorasPorcentaje.setText("text");
                        panelhoras.add(labelhorasPorcentaje);
                        labelhorasPorcentaje.setBounds(10, 50, 105, 16);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panelhoras.getComponentCount(); i++) {
                                Rectangle bounds = panelhoras.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panelhoras.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panelhoras.setMinimumSize(preferredSize);
                            panelhoras.setPreferredSize(preferredSize);
                        }
                    }
                    panelSeguridadYrol.add(panelhoras);
                    panelhoras.setBounds(415, 90, 150, 75);

                    //---- label47 ----
                    label47.setText("Registro de asistencia en el dia");
                    label47.setForeground(Color.black);
                    label47.setFont(new Font("Inter", Font.BOLD, 20));
                    panelSeguridadYrol.add(label47);
                    label47.setBounds(25, 180, 395, 30);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelSeguridadYrol.getComponentCount(); i++) {
                            Rectangle bounds = panelSeguridadYrol.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelSeguridadYrol.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelSeguridadYrol.setMinimumSize(preferredSize);
                        panelSeguridadYrol.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelSeguridadYrol, "card2");

                //======== panelBitácoraAct ========
                {
                    panelBitácoraAct.setBackground(new Color(0xff9966));
                    panelBitácoraAct.setLayout(null);

                    //---- label7 ----
                    label7.setText("Bit\u00e1cora de actividades");
                    label7.setForeground(Color.black);
                    label7.setFont(new Font("Inter", Font.BOLD, 20));
                    panelBitácoraAct.add(label7);
                    label7.setBounds(15, 10, 300, 30);

                    //---- label24 ----
                    label24.setText("Registro de acciones importantes en el sistema.");
                    label24.setForeground(Color.black);
                    panelBitácoraAct.add(label24);
                    label24.setBounds(30, 45, 365, 25);

                    //---- fechabitacora ----
                    fechabitacora.setText("text");
                    fechabitacora.setForeground(Color.black);
                    panelBitácoraAct.add(fechabitacora);
                    fechabitacora.setBounds(445, 15, 160, 25);

                    //---- label37 ----
                    label37.setText("Registros recientes");
                    label37.setForeground(Color.black);
                    label37.setFont(new Font("Inter", Font.BOLD, 20));
                    panelBitácoraAct.add(label37);
                    label37.setBounds(10, 80, 300, 30);

                    //======== scrollPane4 ========
                    {
                        scrollPane4.setViewportView(tablaregistros);
                    }
                    panelBitácoraAct.add(scrollPane4);
                    scrollPane4.setBounds(10, 120, 595, 190);

                    //---- label41 ----
                    label41.setText("Inicios de Sesi\u00f3n recientes");
                    label41.setForeground(Color.black);
                    label41.setFont(new Font("Inter", Font.BOLD, 20));
                    panelBitácoraAct.add(label41);
                    label41.setBounds(15, 320, 300, 30);

                    //======== scrollPane5 ========
                    {
                        scrollPane5.setViewportView(tablasesiones);
                    }
                    panelBitácoraAct.add(scrollPane5);
                    scrollPane5.setBounds(10, 350, 590, 200);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelBitácoraAct.getComponentCount(); i++) {
                            Rectangle bounds = panelBitácoraAct.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelBitácoraAct.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelBitácoraAct.setMinimumSize(preferredSize);
                        panelBitácoraAct.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelBitácoraAct, "card3");

                //======== panelReglas ========
                {
                    panelReglas.setBackground(new Color(0xff9966));
                    panelReglas.setLayout(null);

                    //---- label9 ----
                    label9.setText("text");
                    panelReglas.add(label9);
                    label9.setBounds(new Rectangle(new Point(525, 320), label9.getPreferredSize()));

                    //---- label8 ----
                    label8.setText("Reglas");
                    label8.setForeground(new Color(0xf2876b));
                    panelReglas.add(label8);
                    label8.setBounds(10, 30, 102, 16);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelReglas.getComponentCount(); i++) {
                            Rectangle bounds = panelReglas.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelReglas.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelReglas.setMinimumSize(preferredSize);
                        panelReglas.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelReglas, "card4");

                //======== panelGestionEmp ========
                {
                    panelGestionEmp.setBackground(new Color(0xff9966));
                    panelGestionEmp.setLayout(null);

                    //---- label6 ----
                    label6.setText("Gesti\u00f3n empleados");
                    label6.setForeground(Color.black);
                    label6.setFont(new Font("MingLiU_HKSCS-ExtB", Font.PLAIN, 13));
                    panelGestionEmp.add(label6);
                    label6.setBounds(35, 60, 145, label6.getPreferredSize().height);

                    //======== scrollPane1 ========
                    {

                        //---- table1 ----
                        table1.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                            },
                            new String[] {
                                "NOMBRE", "ESTATUS"
                            }
                        ));
                        scrollPane1.setViewportView(table1);
                    }
                    panelGestionEmp.add(scrollPane1);
                    scrollPane1.setBounds(105, 105, 300, 238);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelGestionEmp.getComponentCount(); i++) {
                            Rectangle bounds = panelGestionEmp.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelGestionEmp.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelGestionEmp.setMinimumSize(preferredSize);
                        panelGestionEmp.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelGestionEmp, "card5");

                //======== panelBienvenida ========
                {
                    panelBienvenida.setBackground(new Color(0xff9966));
                    panelBienvenida.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelBienvenida.getComponentCount(); i++) {
                            Rectangle bounds = panelBienvenida.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelBienvenida.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelBienvenida.setMinimumSize(preferredSize);
                        panelBienvenida.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelBienvenida, "card6");

                //======== panelTurnos ========
                {
                    panelTurnos.setBackground(new Color(0xff9966));
                    panelTurnos.setLayout(null);

                    //---- label18 ----
                    label18.setText("Gesti\u00f3n de turnos");
                    label18.setForeground(Color.black);
                    label18.setFont(new Font("Inter", Font.PLAIN, 20));
                    panelTurnos.add(label18);
                    label18.setBounds(20, 35, 250, 40);

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setViewportView(tablaturnos);
                    }
                    panelTurnos.add(scrollPane2);
                    scrollPane2.setBounds(15, 200, 540, 230);

                    //---- label20 ----
                    label20.setText("Visualiza y crea turnos");
                    label20.setForeground(Color.black);
                    panelTurnos.add(label20);
                    label20.setBounds(20, 115, 385, label20.getPreferredSize().height);

                    //---- label21 ----
                    label21.setText("(+) Nuevo Turno");
                    label21.setBackground(Color.black);
                    label21.setForeground(Color.blue);
                    panelTurnos.add(label21);
                    label21.setBounds(425, 135, 125, label21.getPreferredSize().height);

                    //---- label31 ----
                    label31.setText("text");
                    label31.setForeground(Color.black);
                    panelTurnos.add(label31);
                    label31.setBounds(385, 25, 130, label31.getPreferredSize().height);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelTurnos.getComponentCount(); i++) {
                            Rectangle bounds = panelTurnos.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelTurnos.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelTurnos.setMinimumSize(preferredSize);
                        panelTurnos.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelTurnos, "card7");

                //======== AsignarTurno ========
                {
                    AsignarTurno.setBackground(new Color(0xff9966));
                    AsignarTurno.setLayout(null);

                    //---- label22 ----
                    label22.setText("Nombre del turno:");
                    label22.setForeground(Color.black);
                    AsignarTurno.add(label22);
                    label22.setBounds(125, 100, 130, label22.getPreferredSize().height);

                    //---- label23 ----
                    label23.setText("Crear un nuevo turno");
                    label23.setForeground(Color.black);
                    label23.setFont(new Font("Inter", Font.PLAIN, 20));
                    AsignarTurno.add(label23);
                    label23.setBounds(15, 40, 325, 17);

                    //---- labelFechaCrearTurno ----
                    labelFechaCrearTurno.setText("text");
                    labelFechaCrearTurno.setForeground(Color.black);
                    AsignarTurno.add(labelFechaCrearTurno);
                    labelFechaCrearTurno.setBounds(370, 20, 185, 17);
                    AsignarTurno.add(txtnombre);
                    txtnombre.setBounds(250, 95, 215, txtnombre.getPreferredSize().height);
                    AsignarTurno.add(txtentrada1);
                    txtentrada1.setBounds(90, 160, 165, 34);

                    //---- label25 ----
                    label25.setText("Entrada 1:");
                    label25.setForeground(Color.black);
                    AsignarTurno.add(label25);
                    label25.setBounds(10, 170, 130, 17);

                    //---- label26 ----
                    label26.setText("Entrada 2:");
                    label26.setForeground(Color.black);
                    AsignarTurno.add(label26);
                    label26.setBounds(10, 210, 130, 17);
                    AsignarTurno.add(txtentrada2);
                    txtentrada2.setBounds(90, 200, 165, 34);
                    AsignarTurno.add(txtentrada3);
                    txtentrada3.setBounds(90, 240, 165, 34);

                    //---- label27 ----
                    label27.setText("Entrada 3:");
                    label27.setForeground(Color.black);
                    AsignarTurno.add(label27);
                    label27.setBounds(10, 250, 130, 17);

                    //---- label28 ----
                    label28.setText("Salida 1:");
                    label28.setForeground(Color.black);
                    AsignarTurno.add(label28);
                    label28.setBounds(280, 170, 130, 17);

                    //---- label29 ----
                    label29.setText("Salida 2:");
                    label29.setForeground(Color.black);
                    AsignarTurno.add(label29);
                    label29.setBounds(280, 210, 130, 17);

                    //---- label30 ----
                    label30.setText("Salida 3:");
                    label30.setForeground(Color.black);
                    AsignarTurno.add(label30);
                    label30.setBounds(280, 250, 130, 17);
                    AsignarTurno.add(txtsalida3);
                    txtsalida3.setBounds(360, 240, 165, 34);
                    AsignarTurno.add(txtsalida2);
                    txtsalida2.setBounds(360, 200, 165, 34);
                    AsignarTurno.add(txtsalida1);
                    txtsalida1.setBounds(360, 160, 165, 34);

                    //---- crearturno ----
                    crearturno.setText("Crear turno");
                    crearturno.addActionListener(e -> crearturno(e));
                    AsignarTurno.add(crearturno);
                    crearturno.setBounds(new Rectangle(new Point(420, 330), crearturno.getPreferredSize()));

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < AsignarTurno.getComponentCount(); i++) {
                            Rectangle bounds = AsignarTurno.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = AsignarTurno.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        AsignarTurno.setMinimumSize(preferredSize);
                        AsignarTurno.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(AsignarTurno, "card8");

                //======== panelempleados ========
                {
                    panelempleados.setBackground(new Color(0xff9966));
                    panelempleados.setLayout(null);

                    //---- labelFechaTurnos ----
                    labelFechaTurnos.setText("text");
                    labelFechaTurnos.setForeground(Color.black);
                    panelempleados.add(labelFechaTurnos);
                    labelFechaTurnos.setBounds(415, 25, 140, labelFechaTurnos.getPreferredSize().height);

                    //---- label32 ----
                    label32.setText("(+) Nuevo Empleado");
                    label32.setBackground(Color.black);
                    label32.setForeground(Color.blue);
                    panelempleados.add(label32);
                    label32.setBounds(410, 150, 140, 17);

                    //---- label33 ----
                    label33.setText("Visualiza, crea y asigna turnos a los empleados.");
                    label33.setForeground(Color.black);
                    panelempleados.add(label33);
                    label33.setBounds(20, 130, 385, 17);

                    //---- label34 ----
                    label34.setText("Gesti\u00f3n de empleados");
                    label34.setForeground(Color.black);
                    label34.setFont(new Font("Inter", Font.PLAIN, 20));
                    panelempleados.add(label34);
                    label34.setBounds(20, 35, 250, 40);

                    //======== scrollPane3 ========
                    {
                        scrollPane3.setViewportView(tablaempleados);
                    }
                    panelempleados.add(scrollPane3);
                    scrollPane3.setBounds(15, 205, 525, 250);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelempleados.getComponentCount(); i++) {
                            Rectangle bounds = panelempleados.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelempleados.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelempleados.setMinimumSize(preferredSize);
                        panelempleados.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelempleados, "card12");

                //======== AsignarEmpleados ========
                {
                    AsignarEmpleados.setForeground(new Color(0xff9966));
                    AsignarEmpleados.setBackground(new Color(0xff9966));
                    AsignarEmpleados.setLayout(null);

                    //---- label35 ----
                    label35.setText("Crear un nuevo perfil de empleado");
                    label35.setForeground(Color.black);
                    label35.setFont(new Font("Inter", Font.PLAIN, 20));
                    AsignarEmpleados.add(label35);
                    label35.setBounds(15, 55, 340, 35);

                    //---- labelfe ----
                    labelfe.setText("text");
                    labelfe.setForeground(Color.black);
                    AsignarEmpleados.add(labelfe);
                    labelfe.setBounds(370, 25, 185, 17);
                    AsignarEmpleados.add(txtnombreempleado);
                    txtnombreempleado.setBounds(210, 110, 215, 34);

                    //---- label36 ----
                    label36.setText("Nombre del empleado:");
                    label36.setForeground(Color.black);
                    AsignarEmpleados.add(label36);
                    label36.setBounds(55, 120, 165, 17);

                    //---- label38 ----
                    label38.setText("Turno del empleado:");
                    label38.setForeground(Color.black);
                    AsignarEmpleados.add(label38);
                    label38.setBounds(55, 180, 145, 17);

                    //---- label39 ----
                    label39.setText("Rol del empleado: ");
                    label39.setForeground(Color.black);
                    AsignarEmpleados.add(label39);
                    label39.setBounds(55, 245, 130, 17);

                    //---- crearturno2 ----
                    crearturno2.setText("Crear perfil");
                    crearturno2.addActionListener(e -> crearturno2(e));
                    AsignarEmpleados.add(crearturno2);
                    crearturno2.setBounds(420, 335, 104, 34);
                    AsignarEmpleados.add(cmbturnoempleado);
                    cmbturnoempleado.setBounds(215, 170, 180, cmbturnoempleado.getPreferredSize().height);

                    //---- label40 ----
                    label40.setText("Empleado");
                    label40.setForeground(Color.black);
                    label40.setFont(new Font("Inter", Font.BOLD, 16));
                    AsignarEmpleados.add(label40);
                    label40.setBounds(220, 245, 130, 17);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < AsignarEmpleados.getComponentCount(); i++) {
                            Rectangle bounds = AsignarEmpleados.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = AsignarEmpleados.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        AsignarEmpleados.setMinimumSize(preferredSize);
                        AsignarEmpleados.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(AsignarEmpleados, "card9");
            }
            panelBase.add(panelInicio, BorderLayout.CENTER);
        }
        contentPane.add(panelBase);
        panelBase.setBounds(-10, -5, 910, 605);

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
    // Generated using JFormDesigner Evaluation license - Juan
    private JPanel panelBase;
    private JPanel panelMenu;
    private JLabel label1;
    private JSeparator separator1;
    private JSeparator separator2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JSeparator separator3;
    private JSeparator separator4;
    private JSeparator separator5;
    private JLabel label2;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label15;
    private JLabel label16;
    private JSeparator separator6;
    private JLabel label17;
    private JLabel label19;
    private JLabel nombre;
    private JPanel panelInicio;
    private JPanel panelSeguridadYrol;
    private JLabel label10;
    private JLabel label42;
    private JLabel fechaadmin;
    private JPanel panelasistencias;
    private JLabel label43;
    private JLabel labelAsistenciasTotales;
    private JLabel labelAsistenciasPorcentaje;
    private JPanel panelfaltas;
    private JLabel labelFaltasTotales;
    private JLabel label45;
    private JLabel labelFaltasPorcentaje;
    private JPanel panelgrafica;
    private JScrollPane scrollPane6;
    private JTable tabladia;
    private JPanel panelhoras;
    private JLabel labelhorasTotales;
    private JLabel label46;
    private JLabel labelhorasPorcentaje;
    private JLabel label47;
    private JPanel panelBitácoraAct;
    private JLabel label7;
    private JLabel label24;
    private JLabel fechabitacora;
    private JLabel label37;
    private JScrollPane scrollPane4;
    private JTable tablaregistros;
    private JLabel label41;
    private JScrollPane scrollPane5;
    private JTable tablasesiones;
    private JPanel panelReglas;
    private JLabel label9;
    private JLabel label8;
    private JPanel panelGestionEmp;
    private JLabel label6;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panelBienvenida;
    private JPanel panelTurnos;
    private JLabel label18;
    private JScrollPane scrollPane2;
    private JTable tablaturnos;
    private JLabel label20;
    private JLabel label21;
    private JLabel label31;
    private JPanel AsignarTurno;
    private JLabel label22;
    private JLabel label23;
    private JLabel labelFechaCrearTurno;
    private JTextField txtnombre;
    private JTextField txtentrada1;
    private JLabel label25;
    private JLabel label26;
    private JTextField txtentrada2;
    private JTextField txtentrada3;
    private JLabel label27;
    private JLabel label28;
    private JLabel label29;
    private JLabel label30;
    private JTextField txtsalida3;
    private JTextField txtsalida2;
    private JTextField txtsalida1;
    private JButton crearturno;
    private JPanel panelempleados;
    private JLabel labelFechaTurnos;
    private JLabel label32;
    private JLabel label33;
    private JLabel label34;
    private JScrollPane scrollPane3;
    private JTable tablaempleados;
    private JPanel AsignarEmpleados;
    private JLabel label35;
    private JLabel labelfe;
    private JTextField txtnombreempleado;
    private JLabel label36;
    private JLabel label38;
    private JLabel label39;
    private JButton crearturno2;
    private JComboBox cmbturnoempleado;
    private JLabel label40;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}