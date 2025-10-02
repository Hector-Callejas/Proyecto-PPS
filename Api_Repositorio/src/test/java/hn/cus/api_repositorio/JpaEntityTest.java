package hn.cus.api_repositorio;

import hn.cus.api_repositorio.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas para verificar el mapeo JPA de las entidades
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class JpaEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testUsuarioEntityMapping() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setActivo(true);

        // Persistir en la base de datos
        entityManager.persist(usuario);
        entityManager.flush();
        entityManager.clear();

        // Verificar que se persistió correctamente
        assertNotNull(usuario.getId(), "ID debe ser generado automáticamente");
        
        // Buscar el usuario persistido
        Usuario foundUsuario = entityManager.find(Usuario.class, usuario.getId());
        assertNotNull(foundUsuario, "Usuario debe ser encontrado en la BD");
        assertEquals("testuser", foundUsuario.getUsername());
        assertEquals("test@example.com", foundUsuario.getEmail());
    }

    @Test
    void testRolEntityMapping() {
        // Crear un rol de prueba con nombre único
        Rol rol = new Rol();
        rol.setNombre("TEST_ROLE_" + System.currentTimeMillis());
        rol.setDescripcion("Rol de prueba para test");

        // Persistir el rol
        entityManager.persist(rol);
        entityManager.flush();
        entityManager.clear();

        // Verificar persistencia
        assertNotNull(rol.getId());
        
        Rol foundRol = entityManager.find(Rol.class, rol.getId());
        assertNotNull(foundRol);
        assertEquals(rol.getNombre(), foundRol.getNombre());
    }

    @Test
    void testUsuarioRolRelationship() {
        // Crear rol con nombre único
        Rol rol = new Rol();
        rol.setNombre("TEST_USER_ROLE_" + System.currentTimeMillis());
        rol.setDescripcion("Rol de usuario para test");
        entityManager.persist(rol);

        // Crear usuario con rol
        Usuario usuario = new Usuario();
        usuario.setUsername("userwithrole" + System.currentTimeMillis());
        usuario.setEmail("user" + System.currentTimeMillis() + "@example.com");
        usuario.setPassword("password123");
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        usuario.setRoles(roles);

        // Persistir usuario
        entityManager.persist(usuario);
        entityManager.flush();
        entityManager.clear();

        // Verificar relación
        Usuario foundUsuario = entityManager.find(Usuario.class, usuario.getId());
        assertNotNull(foundUsuario);
        assertEquals(1, foundUsuario.getRoles().size());
        assertTrue(foundUsuario.getRoles().stream()
            .anyMatch(r -> rol.getNombre().equals(r.getNombre())));
    }

    @Test
    void testDocumentoEntityMapping() {
        // Crear usuario primero
        Usuario usuario = new Usuario();
        usuario.setUsername("docuser" + System.currentTimeMillis());
        usuario.setEmail("doc" + System.currentTimeMillis() + "@example.com");
        usuario.setPassword("password123");
        entityManager.persist(usuario);

        // Crear documento
        Documento documento = new Documento();
        documento.setNombreOriginal("test.pdf");
        documento.setTipoMime("application/pdf");
        documento.setRutaArchivo("/uploads/test.pdf");
        documento.setPublico(false);
        documento.setCreadoPor(usuario);
        documento.setFechaCreacion(LocalDateTime.now());

        // Persistir documento
        entityManager.persist(documento);
        entityManager.flush();
        entityManager.clear();

        // Verificar persistencia
        assertNotNull(documento.getId());
        
        Documento foundDoc = entityManager.find(Documento.class, documento.getId());
        assertNotNull(foundDoc);
        assertEquals("test.pdf", foundDoc.getNombreOriginal());
        assertEquals("application/pdf", foundDoc.getTipoMime());
        assertNotNull(foundDoc.getCreadoPor());
        assertEquals(usuario.getUsername(), foundDoc.getCreadoPor().getUsername());
    }

    @Test
    void testWorkflowEntityMapping() {
        // Crear workflow con nombre único
        Workflow workflow = new Workflow();
        workflow.setNombre("Test Workflow " + System.currentTimeMillis());
        workflow.setDescripcion("Workflow para test de entidades");

        // Persistir workflow
        entityManager.persist(workflow);
        entityManager.flush();
        entityManager.clear();

        // Verificar persistencia
        assertNotNull(workflow.getId());
        
        Workflow foundWorkflow = entityManager.find(Workflow.class, workflow.getId());
        assertNotNull(foundWorkflow);
        assertEquals(workflow.getNombre(), foundWorkflow.getNombre());
        assertEquals(workflow.getDescripcion(), foundWorkflow.getDescripcion());
    }

    @Test
    void testWorkflowEtapaEntityMapping() {
        // Crear workflow primero con nombre único
        Workflow workflow = new Workflow();
        workflow.setNombre("Test Workflow " + System.currentTimeMillis());
        workflow.setDescripcion("Workflow para test de etapas");
        entityManager.persist(workflow);

        // Crear etapa del workflow
        WorkflowEtapa etapa = new WorkflowEtapa();
        etapa.setWorkflow(workflow);
        etapa.setOrden(1);
        etapa.setDescripcion("Primera etapa de test");

        // Persistir etapa
        entityManager.persist(etapa);
        entityManager.flush();
        entityManager.clear();

        // Verificar persistencia
        assertNotNull(etapa.getId());
        
        WorkflowEtapa foundEtapa = entityManager.find(WorkflowEtapa.class, etapa.getId());
        assertNotNull(foundEtapa);
        assertEquals(1, foundEtapa.getOrden());
        assertEquals("Primera etapa de test", foundEtapa.getDescripcion());
        assertNotNull(foundEtapa.getWorkflow());
        assertEquals(workflow.getNombre(), foundEtapa.getWorkflow().getNombre());
    }
}
