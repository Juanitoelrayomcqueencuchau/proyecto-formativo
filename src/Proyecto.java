import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Proyecto {

    public static void main(String[] args) {
        String usuario = "root";
        String password = "123456789";
        String url = "jdbc:mysql://localhost:3306/proyecto";
        Connection conexion = null;

        try {
            // Establecer conexión con la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);

            // Menú de opciones
            String[] opciones = {"Registrar usuario", "Consultar usuarios", "Actualizar usuario", "Eliminar usuario"};
            int opcionSeleccionada = JOptionPane.showOptionDialog(null, "Seleccione una opción:", "Menú", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            switch (opcionSeleccionada) {
                case 0:
                    // Registrar usuario
                    registrarUsuario(conexion);
                    break;

                case 1:
                    // Consultar usuarios
                    consultarUsuarios(conexion);
                    break;

                case 2:
                    // Actualizar usuario
                    actualizarUsuario(conexion);
                    break;

                case 3:
                    // Eliminar usuario
                    eliminarUsuario(conexion);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida");
                    break;
            }

            // Cerrar conexión con la base de datos al finalizar
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para registrar un nuevo usuario
    public static void registrarUsuario(Connection conexion) throws SQLException {
        JOptionPane.showMessageDialog(null, "A continuación se le pedirán los datos del usuario para poder registrarlo");

        String nombre = JOptionPane.showInputDialog("¿Cuál es tu nombre?");
        String[] opcionesGenero = {"Hombre", "Mujer"};
        String genero = (String) JOptionPane.showInputDialog(null, "¿Cuál es tu género?", "Selección de Género", JOptionPane.QUESTION_MESSAGE, null, opcionesGenero, opcionesGenero[0]);
        String fechaNacimiento = JOptionPane.showInputDialog("¿Cuándo naciste? (Formato: yyyy-mm-dd)");
        String[] opciones = {"Ganar masa muscular", "Perder peso", "Mantenerse en forma"};
        String objetivos = (String) JOptionPane.showInputDialog(null, "¿Cuáles son tus objetivos?", "Selección de Objetivos", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        String correo = JOptionPane.showInputDialog("Ingresa tu correo electrónico");
        Timestamp fechaRegistro = new Timestamp(System.currentTimeMillis());

        String sqlInsertUsuario = "INSERT INTO usuarios (usuario_nombre, usuario_genero, usuario_fecha_nacimiento, usuario_objetivos, usuario_correo, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statementInsertUsuario = conexion.prepareStatement(sqlInsertUsuario);
        statementInsertUsuario.setString(1, nombre);
        statementInsertUsuario.setString(2, genero);
        statementInsertUsuario.setString(3, fechaNacimiento);
        statementInsertUsuario.setString(4, objetivos);
        statementInsertUsuario.setString(5, correo);
        statementInsertUsuario.setTimestamp(6, fechaRegistro);

        int filasInsertadas = statementInsertUsuario.executeUpdate();

        if (filasInsertadas > 0) {
            JOptionPane.showMessageDialog(null, "Usuario registrado correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario");
        }
    }

    // Método para consultar todos los usuarios en la base de datos
    public static void consultarUsuarios(Connection conexion) throws SQLException {
        String sqlConsulta = "SELECT * FROM usuarios";
        PreparedStatement statementConsulta = conexion.prepareStatement(sqlConsulta);
        ResultSet resultSet = statementConsulta.executeQuery();

        StringBuilder resultadoConsulta = new StringBuilder();
        while (resultSet.next()) {
            resultadoConsulta.append("ID: ").append(resultSet.getInt("usuarios_id")).append("\n");
            resultadoConsulta.append("Nombre: ").append(resultSet.getString("usuario_nombre")).append("\n");
            resultadoConsulta.append("Género: ").append(resultSet.getString("usuario_genero")).append("\n");
            resultadoConsulta.append("Fecha de Nacimiento: ").append(resultSet.getString("usuario_fecha_nacimiento")).append("\n");
            resultadoConsulta.append("Objetivos: ").append(resultSet.getString("usuario_objetivos")).append("\n");
            resultadoConsulta.append("Correo: ").append(resultSet.getString("usuario_correo")).append("\n");
            resultadoConsulta.append("Fecha de Registro: ").append(resultSet.getString("fecha_registro")).append("\n\n");
        }

        JOptionPane.showMessageDialog(null, resultadoConsulta.toString());
    }

    // Método para actualizar un usuario existente
    public static void actualizarUsuario(Connection conexion) throws SQLException {
        int usuarioIdActualizar = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el ID del usuario a actualizar"));
        String nuevoNombre = JOptionPane.showInputDialog("¿Cuál es tu nombre?");
        String[] opcionesGenero = {"Hombre", "Mujer"};
        String nuevoGenero = (String) JOptionPane.showInputDialog(null, "¿Cuál es tu género?", "Selección de Género", JOptionPane.QUESTION_MESSAGE, null, opcionesGenero, opcionesGenero[0]);
        String nuevaFechaNacimiento = JOptionPane.showInputDialog("Ingresa tu fecha de nacimiento (Formato: yyyy-mm-dd)");
        String[] nuevasOpciones = {"Ganar masa muscular", "Perder peso", "Mantenerse en forma"};
        String nuevosObjetivos = (String) JOptionPane.showInputDialog(null, "¿Cuáles son tus objetivos?", "Selección de Objetivos", JOptionPane.QUESTION_MESSAGE, null, nuevasOpciones, nuevasOpciones[0]);
        String nuevoCorreo = JOptionPane.showInputDialog("Ingresa tu correo electrónico");

        // Obtener la fecha actual para actualizar la fecha de registro
        Timestamp fechaRegistro = new Timestamp(System.currentTimeMillis());

        String sqlUpdate = "UPDATE usuarios SET usuario_nombre = ?, usuario_genero = ?, usuario_fecha_nacimiento = ?, usuario_objetivos = ?, usuario_correo = ?, fecha_registro = ? WHERE usuarios_id = ?";
        PreparedStatement statementUpdate = conexion.prepareStatement(sqlUpdate);
        statementUpdate.setString(1, nuevoNombre);
        statementUpdate.setString(2, nuevoGenero);
        statementUpdate.setString(3, nuevaFechaNacimiento);
        statementUpdate.setString(4, nuevosObjetivos);
        statementUpdate.setString(5, nuevoCorreo);
        statementUpdate.setTimestamp(6, fechaRegistro);
        statementUpdate.setInt(7, usuarioIdActualizar);

        int filasActualizadas = statementUpdate.executeUpdate();

        if (filasActualizadas > 0) {
            JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "Error al actualizar el usuario");
        }
    }

    // Método para eliminar un usuario existente
    public static void eliminarUsuario(Connection conexion) throws SQLException {
        int usuarioIdEliminar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario a eliminar"));

        String sqlDelete = "DELETE FROM usuarios WHERE usuarios_id = ?";
        PreparedStatement statementDelete = conexion.prepareStatement(sqlDelete);
        statementDelete.setInt(1, usuarioIdEliminar);
        int filasEliminadas = statementDelete.executeUpdate();

        if (filasEliminadas > 0) {
            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar el usuario");
        }
    }
}