package hn.cus.api_repositorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas completas para la conexión a la base de datos
 * Incluye validación de esquema, tablas y relaciones
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws SQLException {
        // Verificar que el DataSource está configurado
        assertNotNull(dataSource, "DataSource no debe ser null");
        
        // Verificar que se puede obtener una conexión
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Conexión no debe ser null");
            assertFalse(connection.isClosed(), "Conexión no debe estar cerrada");
            
            // Verificar que la conexión es válida
            assertTrue(connection.isValid(5), "Conexión debe ser válida");
            
            // Verificar metadatos de la conexión
            DatabaseMetaData metaData = connection.getMetaData();
            assertNotNull(metaData, "Metadatos de conexión no deben ser null");
            
            // Verificar información de la base de datos
            assertEquals("PostgreSQL", metaData.getDatabaseProductName(), 
                "Base de datos debe ser PostgreSQL para pruebas");
            
            // Verificar versión del driver
            assertNotNull(metaData.getDriverVersion(), "Versión del driver no debe ser null");
        }
    }

    @Test
    void testDatabaseSchema() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Obtener todas las tablas del esquema
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            
            List<String> tableNames = new ArrayList<>();
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            
            // Verificar que existen las tablas principales (en minúsculas para PostgreSQL)
            assertTrue(tableNames.contains("usuarios"), "Tabla usuarios debe existir");
            assertTrue(tableNames.contains("roles"), "Tabla roles debe existir");
            assertTrue(tableNames.contains("documentos"), "Tabla documentos debe existir");
            assertTrue(tableNames.contains("workflows"), "Tabla workflows debe existir");
            
            System.out.println("Tablas encontradas: " + tableNames);
        }
    }

    @Test
    void testTableStructure() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar estructura de la tabla usuarios (minúsculas para PostgreSQL)
            ResultSet columns = metaData.getColumns(null, null, "usuarios", null);
            
            List<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                String isNullable = columns.getString("IS_NULLABLE");
                
                columnNames.add(columnName);
                System.out.println("Columna: " + columnName + " - Tipo: " + dataType + " - Nullable: " + isNullable);
            }
            
            // Verificar columnas obligatorias (minúsculas para PostgreSQL)
            assertTrue(columnNames.contains("id"), "Columna id debe existir en usuarios");
            assertTrue(columnNames.contains("username"), "Columna username debe existir en usuarios");
            assertTrue(columnNames.contains("email"), "Columna email debe existir en usuarios");
            assertTrue(columnNames.contains("password"), "Columna password debe existir en usuarios");
        }
    }

    @Test
    void testPrimaryKeys() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar claves primarias
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, "usuarios");
            
            List<String> pkColumns = new ArrayList<>();
            while (primaryKeys.next()) {
                String pkColumn = primaryKeys.getString("COLUMN_NAME");
                pkColumns.add(pkColumn);
            }
            
            assertTrue(pkColumns.contains("id"), "id debe ser clave primaria en usuarios");
            assertEquals(1, pkColumns.size(), "usuarios debe tener solo una clave primaria");
        }
    }

    @Test
    void testForeignKeys() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar claves foráneas
            ResultSet foreignKeys = metaData.getImportedKeys(null, null, "documentos");
            
            List<String> fkColumns = new ArrayList<>();
            while (foreignKeys.next()) {
                String fkColumn = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTable = foreignKeys.getString("PKTABLE_NAME");
                fkColumns.add(fkColumn + " -> " + pkTable);
            }
            
            // Verificar que documentos tiene FK a usuarios
            boolean hasUserFK = fkColumns.stream()
                .anyMatch(fk -> fk.contains("creado_por") && fk.contains("usuarios"));
            
            assertTrue(hasUserFK, "documentos debe tener FK a usuarios en creado_por");
            
            System.out.println("Claves foráneas en documentos: " + fkColumns);
        }
    }

    @Test
    void testIndexes() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar índices
            ResultSet indexes = metaData.getIndexInfo(null, null, "usuarios", false, false);
            
            List<String> indexNames = new ArrayList<>();
            while (indexes.next()) {
                String indexName = indexes.getString("INDEX_NAME");
                String columnName = indexes.getString("COLUMN_NAME");
                boolean isUnique = !indexes.getBoolean("NON_UNIQUE");
                
                if (indexName != null) {
                    indexNames.add(indexName + " (" + columnName + ") - Unique: " + isUnique);
                }
            }
            
            System.out.println("Índices en usuarios: " + indexNames);
            
            // Verificar que existen índices para campos únicos
            assertTrue(indexNames.stream().anyMatch(idx -> idx.contains("username")), 
                "Debe existir índice en username");
            assertTrue(indexNames.stream().anyMatch(idx -> idx.contains("email")), 
                "Debe existir índice en email");
        }
    }

    @Test
    void testMultipleConnections() throws SQLException {
        // Verificar que se pueden crear múltiples conexiones
        try (Connection conn1 = dataSource.getConnection();
             Connection conn2 = dataSource.getConnection()) {
            
            assertNotNull(conn1);
            assertNotNull(conn2);
            assertNotSame(conn1, conn2, "Las conexiones deben ser diferentes");
            
            // Verificar que ambas conexiones son válidas
            assertTrue(conn1.isValid(5));
            assertTrue(conn2.isValid(5));
            
            // Verificar que pueden ejecutar consultas independientemente
            try (var stmt1 = conn1.createStatement();
                 var stmt2 = conn2.createStatement()) {
                
                var rs1 = stmt1.executeQuery("SELECT 1 as test");
                var rs2 = stmt2.executeQuery("SELECT 2 as test");
                
                assertTrue(rs1.next());
                assertTrue(rs2.next());
                
                assertEquals(1, rs1.getInt("test"));
                assertEquals(2, rs2.getInt("test"));
            }
        }
    }

    @Test
    void testTransactionSupport() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            // Verificar que la base de datos soporta transacciones
            // En PostgreSQL, AutoCommit puede estar habilitado por defecto
            boolean autoCommit = connection.getAutoCommit();
            System.out.println("AutoCommit inicial: " + autoCommit);
            
            // Verificar que se pueden hacer rollback
            connection.setAutoCommit(false);
            
            try {
                // Intentar una operación que falle
                var stmt = connection.createStatement();
                stmt.execute("INSERT INTO usuarios (id, username, email, password) VALUES (999999, 'test', 'test@test.com', 'password')");
                
                // Si llegamos aquí, la inserción fue exitosa, hacer rollback
                connection.rollback();
                
                // Verificar que el rollback funcionó
                var checkStmt = connection.createStatement();
                var rs = checkStmt.executeQuery("SELECT COUNT(*) FROM usuarios WHERE id = 999999");
                rs.next();
                assertEquals(0, rs.getInt(1), "El rollback debe haber eliminado la inserción");
                
            } catch (SQLException e) {
                // Si hay error, hacer rollback y continuar
                connection.rollback();
            }
        }
    }
}
