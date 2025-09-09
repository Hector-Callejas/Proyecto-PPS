package hn.cus.api_repositorio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración con la base de datos PostgreSQL real
 * Solo se ejecutan si se especifica la propiedad de sistema 'test.postgres'
 * Usa configuración mínima para evitar problemas de Spring Security
 */
@SpringBootTest(classes = {DataSource.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("default")
@TestPropertySource(locations = "classpath:application.yml")
@EnabledIfSystemProperty(named = "test.postgres", matches = "true")
class DatabaseIntegrationTest {

    @Test
    void testPostgreSQLConnection() throws SQLException {
        // Crear DataSource manualmente para evitar problemas de contexto
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            assertNotNull(connection, "Conexión no debe ser null");
            assertFalse(connection.isClosed(), "Conexión no debe estar cerrada");
            
            // Verificar que la conexión es válida
            assertTrue(connection.isValid(5), "Conexión debe ser válida");
            
            // Verificar metadatos de la conexión
            DatabaseMetaData metaData = connection.getMetaData();
            assertNotNull(metaData, "Metadatos de conexión no deben ser null");
            
            // Verificar información de la base de datos
            assertEquals("PostgreSQL", metaData.getDatabaseProductName(), 
                "Base de datos debe ser PostgreSQL");
            
            // Verificar versión del driver
            assertNotNull(metaData.getDriverVersion(), "Versión del driver no debe ser null");
            
            System.out.println("Conectado a PostgreSQL versión: " + metaData.getDatabaseProductVersion());
            System.out.println("Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
        }
    }

    @Test
    void testPostgreSQLSchema() throws SQLException {
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Obtener todas las tablas del esquema gestdoc_ow
            ResultSet tables = metaData.getTables(null, "gestdoc_ow", "%", new String[]{"TABLE"});
            
            List<String> tableNames = new ArrayList<>();
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                String schemaName = tables.getString("TABLE_SCHEM");
                tableNames.add(schemaName + "." + tableName);
            }
            
            // Verificar que existen las tablas principales en el esquema correcto
            assertTrue(tableNames.contains("gestdoc_ow.usuarios"), "Tabla usuarios debe existir en esquema gestdoc_ow");
            assertTrue(tableNames.contains("gestdoc_ow.roles"), "Tabla roles debe existir en esquema gestdoc_ow");
            assertTrue(tableNames.contains("gestdoc_ow.documentos"), "Tabla documentos debe existir en esquema gestdoc_ow");
            assertTrue(tableNames.contains("gestdoc_ow.workflows"), "Tabla workflows debe existir en esquema gestdoc_ow");
            
            System.out.println("Tablas encontradas en esquema gestdoc_ow: " + tableNames);
        }
    }

    @Test
    void testPostgreSQLTableStructure() throws SQLException {
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar estructura de la tabla usuarios
            ResultSet columns = metaData.getColumns(null, "gestdoc_ow", "usuarios", null);
            
            List<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                String isNullable = columns.getString("IS_NULLABLE");
                String columnSize = columns.getString("COLUMN_SIZE");
                
                columnNames.add(columnName);
                System.out.println("Columna: " + columnName + " - Tipo: " + dataType + 
                    "(" + columnSize + ")" + " - Nullable: " + isNullable);
            }
            
            // Verificar columnas obligatorias
            assertTrue(columnNames.contains("id"), "Columna id debe existir en usuarios");
            assertTrue(columnNames.contains("username"), "Columna username debe existir en usuarios");
            assertTrue(columnNames.contains("email"), "Columna email debe existir en usuarios");
            assertTrue(columnNames.contains("password"), "Columna password debe existir en usuarios");
            assertTrue(columnNames.contains("activo"), "Columna activo debe existir en usuarios");
        }
    }

    @Test
    void testPostgreSQLConstraints() throws SQLException {
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar restricciones únicas
            ResultSet uniqueKeys = metaData.getIndexInfo(null, "gestdoc_ow", "usuarios", true, false);
            
            List<String> uniqueColumns = new ArrayList<>();
            while (uniqueKeys.next()) {
                String columnName = uniqueKeys.getString("COLUMN_NAME");
                String indexName = uniqueKeys.getString("INDEX_NAME");
                if (columnName != null && indexName != null) {
                    uniqueColumns.add(columnName + " (" + indexName + ")");
                }
            }
            
            System.out.println("Restricciones únicas en usuarios: " + uniqueColumns);
            
            // Verificar que username y email son únicos
            assertTrue(uniqueColumns.stream().anyMatch(col -> col.contains("username")), 
                "username debe tener restricción única");
            assertTrue(uniqueColumns.stream().anyMatch(col -> col.contains("email")), 
                "email debe tener restricción única");
        }
    }

    @Test
    void testPostgreSQLForeignKeys() throws SQLException {
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar claves foráneas en documentos
            ResultSet foreignKeys = metaData.getImportedKeys(null, "gestdoc_ow", "documentos");
            
            List<String> fkColumns = new ArrayList<>();
            while (foreignKeys.next()) {
                String fkColumn = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTable = foreignKeys.getString("PKTABLE_NAME");
                String pkSchema = foreignKeys.getString("PKTABLE_SCHEM");
                fkColumns.add(fkColumn + " -> " + pkSchema + "." + pkTable);
            }
            
            System.out.println("Claves foráneas en documentos: " + fkColumns);
            
            // Verificar que documentos tiene FK a usuarios
            boolean hasUserFK = fkColumns.stream()
                .anyMatch(fk -> fk.contains("creado_por") && fk.contains("gestdoc_ow.usuarios"));
            
            assertTrue(hasUserFK, "documentos debe tener FK a usuarios en creado_por");
        }
    }

    @Test
    void testPostgreSQLDataInsertion() throws SQLException {
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            connection.setAutoCommit(false);
            
            try {
                // Insertar datos de prueba
                try (Statement stmt = connection.createStatement()) {
                    // Insertar rol
                    stmt.executeUpdate("INSERT INTO gestdoc_ow.roles (nombre) VALUES ('TEST_ROLE')");
                    
                    // Insertar usuario
                    stmt.executeUpdate("INSERT INTO gestdoc_ow.usuarios (username, email, password, activo) " +
                        "VALUES ('testuser', 'test@test.com', 'password123', true)");
                    
                    // Verificar inserción
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM gestdoc_ow.usuarios WHERE username = 'testuser'");
                    rs.next();
                    assertEquals(1, rs.getInt(1), "Usuario debe haberse insertado correctamente");
                    
                    // Limpiar datos de prueba
                    stmt.executeUpdate("DELETE FROM gestdoc_ow.usuarios WHERE username = 'testuser'");
                    stmt.executeUpdate("DELETE FROM gestdoc_ow.roles WHERE nombre = 'TEST_ROLE'");
                    
                    connection.commit();
                }
                
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    @Test
    void testPostgreSQLPerformance() throws SQLException {
        try (Connection connection = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/gestor_documental",
                "postgres", "0043962")) {
            
            // Verificar rendimiento de consultas básicas
            long startTime = System.currentTimeMillis();
            
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM gestdoc_ow.usuarios");
                rs.next();
                int userCount = rs.getInt(1);
                
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                System.out.println("Consulta COUNT(*) en usuarios tomó: " + duration + "ms");
                System.out.println("Total de usuarios: " + userCount);
                
                // La consulta debe completarse en menos de 1 segundo
                assertTrue(duration < 1000, "Consulta debe completarse en menos de 1 segundo");
            }
        }
    }

    @Test
    void testPostgreSQLConnectionPool() throws SQLException {
        // Verificar que se pueden crear múltiples conexiones
        List<Connection> connections = new ArrayList<>();
        
        try {
            // Crear múltiples conexiones para probar el pool
            for (int i = 0; i < 5; i++) {
                Connection conn = java.sql.DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/gestor_documental",
                    "postgres", "0043962");
                connections.add(conn);
                
                // Verificar que cada conexión es válida
                assertTrue(conn.isValid(5), "Conexión " + i + " debe ser válida");
                
                // Ejecutar una consulta simple
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT 1");
                    assertTrue(rs.next());
                    assertEquals(1, rs.getInt(1));
                }
            }
            
            System.out.println("Se crearon " + connections.size() + " conexiones exitosamente");
            
        } finally {
            // Cerrar todas las conexiones
            for (Connection conn : connections) {
                if (!conn.isClosed()) {
                    conn.close();
                }
            }
        }
    }
}
