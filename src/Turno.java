public class Turno {
    private int id;
    private String nombre;

    public Turno(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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