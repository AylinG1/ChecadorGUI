public class Empleado {
    private int id;
    private String nombre;

    public Empleado(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    // Sobrescribimos el método toString() para que el JComboBox
    // sepa qué texto mostrar en pantalla.
    @Override
    public String toString() {
        return nombre;
    }
}