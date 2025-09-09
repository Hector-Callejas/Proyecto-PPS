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
@TestPropertySource(locations = "classpath:application-test.yml")
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
            assertEquals("H2", metaData.getDatabaseProductName(), 
                "Base de datos debe ser H2 para pruebas");
            
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
            
            // Verificar que existen las tablas principales
            assertTrue(tableNames.contains("USUARIOS"), "Tabla USUARIOS debe existir");
            assertTrue(tableNames.contains("ROLES"), "Tabla ROLES debe existir");
            assertTrue(tableNames.contains("DOCUMENTOS"), "Tabla DOCUMENTOS debe existir");
            assertTrue(tableNames.contains("WORKFLOWS"), "Tabla WORKFLOWS debe existir");
            
            System.out.println("Tablas encontradas: " + tableNames);
        }
    }

    @Test
    void testTableStructure() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar estructura de la tabla USUARIOS
            ResultSet columns = metaData.getColumns(null, null, "USUARIOS", null);
            
            List<String> columnNames = new ArrayList<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                String isNullable = columns.getString("IS_NULLABLE");
                
                columnNames.add(columnName);
                System.out.println("Columna: " + columnName + " - Tipo: " + dataType + " - Nullable: " + isNullable);
            }
            
            // Verificar columnas obligatorias
            assertTrue(columnNames.contains("ID"), "Columna ID debe existir en USUARIOS");
            assertTrue(columnNames.contains("USERNAME"), "Columna USERNAME debe existir en USUARIOS");
            assertTrue(columnNames.contains("EMAIL"), "Columna EMAIL debe existir en USUARIOS");
            assertTrue(columnNames.contains("PASSWORD"), "Columna PASSWORD debe existir en USUARIOS");
        }
    }

    @Test
    void testPrimaryKeys() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar claves primarias
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, "USUARIOS");
            
            List<String> pkColumns = new ArrayList<>();
            while (primaryKeys.next()) {
                String pkColumn = primaryKeys.getString("COLUMN_NAME");
                pkColumns.add(pkColumn);
            }
            
            assertTrue(pkColumns.contains("ID"), "ID debe ser clave primaria en USUARIOS");
            assertEquals(1, pkColumns.size(), "USUARIOS debe tener solo una clave primaria");
        }
    }

    @Test
    void testForeignKeys() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar claves foráneas
            ResultSet foreignKeys = metaData.getImportedKeys(null, null, "DOCUMENTOS");
            
            List<String> fkColumns = new ArrayList<>();
            while (foreignKeys.next()) {
                String fkColumn = foreignKeys.getString("FKCOLUMN_NAME");
                String pkTable = foreignKeys.getString("PKTABLE_NAME");
                fkColumns.add(fkColumn + " -> " + pkTable);
            }
            
            // Verificar que DOCUMENTOS tiene FK a USUARIOS
            boolean hasUserFK = fkColumns.stream()
                .anyMatch(fk -> fk.contains("CREADO_POR") && fk.contains("USUARIOS"));
            
            assertTrue(hasUserFK, "DOCUMENTOS debe tener FK a USUARIOS en CREADO_POR");
            
            System.out.println("Claves foráneas en DOCUMENTOS: " + fkColumns);
        }
    }

    @Test
    void testIndexes() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar índices
            ResultSet indexes = metaData.getIndexInfo(null, null, "USUARIOS", false, false);
            
            List<String> indexNames = new ArrayList<>();
            while (indexes.next()) {
                String indexName = indexes.getString("INDEX_NAME");
                String columnName = indexes.getString("COLUMN_NAME");
                boolean isUnique = !indexes.getBoolean("NON_UNIQUE");
                
                if (indexName != null) {
                    indexNames.add(indexName + " (" + columnName + ") - Unique: " + isUnique);
                }
            }
            
            System.out.println("Índices en USUARIOS: " + indexNames);
            
            // Verificar que existen índices para campos únicos
            assertTrue(indexNames.stream().anyMatch(idx -> idx.contains("USERNAME")), 
                "Debe existir índice en USERNAME");
            assertTrue(indexNames.stream().anyMatch(idx -> idx.contains("EMAIL")), 
                "Debe existir índice en EMAIL");
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
            assertFalse(connection.getAutoCommit(), "AutoCommit debe estar deshabilitado para transacciones");
            
            // Verificar que se pueden hacer rollback
            connection.setAutoCommit(false);
            
            try {
                // Intentar una operación que falle
                var stmt = connection.createStatement();
                stmt.execute("INSERT INTO USUARIOS (ID, USERNAME, EMAIL, PASSWORD) VALUES (999999, 'test', 'test@test.com', 'password')");
                
                // Si llegamos aquí, la inserción fue exitosa, hacer rollback
                connection.rollback();
                
                // Verificar que el rollback funcionó
                var checkStmt = connection.createStatement();
                var rs = checkStmt.executeQuery("SELECT COUNT(*) FROM USUARIOS WHERE ID = 999999");
                rs.next();
                assertEquals(0, rs.getInt(1), "El rollback debe haber eliminado la inserción");
                
            } catch (SQLException e) {
                // Si hay error, hacer rollback y continuar
                connection.rollback();
            }
        }
    }
}
