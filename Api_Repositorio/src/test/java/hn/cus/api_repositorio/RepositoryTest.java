package hn.cus.api_repositorio;

import hn.cus.api_repositorio.entity.*;
import hn.cus.api_repositorio.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas para los repositorios JPA
 * Utiliza H2 en memoria para pruebas rápidas
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class RepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowEtapaRepository workflowEtapaRepository;

    @Autowired
    private DocumentoWorkflowRepository documentoWorkflowRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    private Usuario testUsuario;
    private Rol testRol;
    private Workflow testWorkflow;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba antes de cada test
        testRol = new Rol();
        testRol.setNombre("TEST_ROLE");
        testRol = rolRepository.save(testRol);

        testUsuario = new Usuario();
        testUsuario.setUsername("testuser");
        testUsuario.setEmail("test@example.com");
        testUsuario.setPassword("password123");
        testUsuario.setActivo(true);
        
        Set<Rol> roles = new HashSet<>();
        roles.add(testRol);
        testUsuario.setRoles(roles);
        testUsuario = usuarioRepository.save(testUsuario);

        testWorkflow = new Workflow();
        testWorkflow.setNombre("Test Workflow");
        testWorkflow.setDescripcion("Workflow de prueba");
        testWorkflow = workflowRepository.save(testWorkflow);
    }

    @Test
    void testUsuarioRepositorySaveAndFind() {
        // Verificar que el usuario se guardó correctamente
        assertNotNull(testUsuario.getId());
        
        // Buscar por ID
        Optional<Usuario> foundUsuario = usuarioRepository.findById(testUsuario.getId());
        assertTrue(foundUsuario.isPresent());
        assertEquals("testuser", foundUsuario.get().getUsername());
        assertEquals("test@example.com", foundUsuario.get().getEmail());
        
        // Buscar por username
        Optional<Usuario> userByUsername = usuarioRepository.findByUsername("testuser");
        assertTrue(userByUsername.isPresent());
        assertEquals(testUsuario.getId(), userByUsername.get().getId());
    }

    @Test
    void testUsuarioRepositoryFindByActivo() {
        // Crear usuario inactivo
        Usuario inactiveUser = new Usuario();
        inactiveUser.setUsername("inactiveuser");
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("password123");
        inactiveUser.setActivo(false);
        inactiveUser = usuarioRepository.save(inactiveUser);

        // Buscar todos los usuarios y filtrar por activo
        List<Usuario> allUsers = usuarioRepository.findAll();
        assertFalse(allUsers.isEmpty());
        
        List<Usuario> activeUsers = allUsers.stream().filter(Usuario::getActivo).toList();
        List<Usuario> inactiveUsers = allUsers.stream().filter(u -> !u.getActivo()).toList();
        
        assertFalse(activeUsers.isEmpty());
        assertFalse(inactiveUsers.isEmpty());
        assertTrue(activeUsers.stream().allMatch(Usuario::getActivo));
        assertTrue(inactiveUsers.stream().noneMatch(Usuario::getActivo));
    }

    @Test
    void testRolRepositorySaveAndFind() {
        // Verificar que el rol se guardó correctamente
        assertNotNull(testRol.getId());
        
        // Buscar por ID
        Optional<Rol> foundRol = rolRepository.findById(testRol.getId());
        assertTrue(foundRol.isPresent());
        assertEquals("TEST_ROLE", foundRol.get().getNombre());
        
        // Buscar por nombre
        Optional<Rol> rolByNombre = rolRepository.findByNombre("TEST_ROLE");
        assertTrue(rolByNombre.isPresent());
        assertEquals(testRol.getId(), rolByNombre.get().getId());
    }

    @Test
    void testDocumentoRepositorySaveAndFind() {
        // Crear documento
        Documento documento = new Documento();
        documento.setNombreOriginal("test.pdf");
        documento.setTipoMime("application/pdf");
        documento.setRutaArchivo("/uploads/test.pdf");
        documento.setPublico(false);
        documento.setCreadoPor(testUsuario);
        documento.setFechaCreacion(LocalDateTime.now());
        
        documento = documentoRepository.save(documento);
        
        // Verificar que se guardó
        assertNotNull(documento.getId());
        
        // Buscar por ID
        Optional<Documento> foundDoc = documentoRepository.findById(documento.getId());
        assertTrue(foundDoc.isPresent());
        assertEquals("test.pdf", foundDoc.get().getNombreOriginal());
        assertEquals("application/pdf", foundDoc.get().getTipoMime());
        
        // Buscar por creador usando el método que existe
        List<Documento> docsByUser = documentoRepository.findByCreadoPor_Id(testUsuario.getId());
        assertFalse(docsByUser.isEmpty());
        assertEquals(1, docsByUser.size());
        assertEquals("test.pdf", docsByUser.get(0).getNombreOriginal());
    }

    @Test
    void testDocumentoRepositoryFindByPublico() {
        // Crear documento público
        Documento publicDoc = new Documento();
        publicDoc.setNombreOriginal("public.pdf");
        publicDoc.setTipoMime("application/pdf");
        publicDoc.setRutaArchivo("/uploads/public.pdf");
        publicDoc.setPublico(true);
        publicDoc.setCreadoPor(testUsuario);
        publicDoc.setFechaCreacion(LocalDateTime.now());
        publicDoc = documentoRepository.save(publicDoc);

        // Crear documento privado
        Documento privateDoc = new Documento();
        privateDoc.setNombreOriginal("private.pdf");
        privateDoc.setTipoMime("application/pdf");
        privateDoc.setRutaArchivo("/uploads/private.pdf");
        privateDoc.setPublico(false);
        privateDoc.setCreadoPor(testUsuario);
        privateDoc.setFechaCreacion(LocalDateTime.now());
        privateDoc = documentoRepository.save(privateDoc);

        // Buscar todos los documentos y filtrar por público/privado
        List<Documento> allDocs = documentoRepository.findAll();
        assertFalse(allDocs.isEmpty());
        
        List<Documento> publicDocs = allDocs.stream().filter(Documento::getPublico).toList();
        List<Documento> privateDocs = allDocs.stream().filter(d -> !d.getPublico()).toList();
        
        assertFalse(publicDocs.isEmpty());
        assertFalse(privateDocs.isEmpty());
        assertTrue(publicDocs.stream().allMatch(Documento::getPublico));
        assertTrue(privateDocs.stream().noneMatch(Documento::getPublico));
    }

    @Test
    void testWorkflowRepositorySaveAndFind() {
        // Verificar que el workflow se guardó correctamente
        assertNotNull(testWorkflow.getId());
        
        // Buscar por ID
        Optional<Workflow> foundWorkflow = workflowRepository.findById(testWorkflow.getId());
        assertTrue(foundWorkflow.isPresent());
        assertEquals("Test Workflow", foundWorkflow.get().getNombre());
        assertEquals("Workflow de prueba", foundWorkflow.get().getDescripcion());
        
        // Buscar todos los workflows y filtrar por nombre
        List<Workflow> allWorkflows = workflowRepository.findAll();
        assertFalse(allWorkflows.isEmpty());
        
        List<Workflow> workflowsByNombre = allWorkflows.stream()
            .filter(w -> w.getNombre().toLowerCase().contains("test"))
            .toList();
        assertFalse(workflowsByNombre.isEmpty());
        assertTrue(workflowsByNombre.stream()
            .anyMatch(w -> w.getNombre().contains("Test")));
    }

    @Test
    void testWorkflowEtapaRepositorySaveAndFind() {
        // Crear etapa del workflow
        WorkflowEtapa etapa = new WorkflowEtapa();
        etapa.setWorkflow(testWorkflow);
        etapa.setOrden(1);
        etapa.setDescripcion("Primera etapa");
        
        etapa = workflowEtapaRepository.save(etapa);
        
        // Verificar que se guardó
        assertNotNull(etapa.getId());
        
        // Buscar por ID
        Optional<WorkflowEtapa> foundEtapa = workflowEtapaRepository.findById(etapa.getId());
        assertTrue(foundEtapa.isPresent());
        assertEquals(1, foundEtapa.get().getOrden());
        assertEquals("Primera etapa", foundEtapa.get().getDescripcion());
        
        // Buscar todas las etapas y filtrar por workflow
        List<WorkflowEtapa> allEtapas = workflowEtapaRepository.findAll();
        assertFalse(allEtapas.isEmpty());
        
        List<WorkflowEtapa> etapasByWorkflow = allEtapas.stream()
            .filter(e -> e.getWorkflow().getId().equals(testWorkflow.getId()))
            .sorted((e1, e2) -> Integer.compare(e1.getOrden(), e2.getOrden()))
            .toList();
        assertFalse(etapasByWorkflow.isEmpty());
        assertEquals(1, etapasByWorkflow.size());
        assertEquals(1, etapasByWorkflow.get(0).getOrden());
    }

    @Test
    void testDocumentoWorkflowRepositorySaveAndFind() {
        // Crear etapa del workflow
        WorkflowEtapa etapa = new WorkflowEtapa();
        etapa.setWorkflow(testWorkflow);
        etapa.setOrden(1);
        etapa.setDescripcion("Primera etapa");
        etapa = workflowEtapaRepository.save(etapa);

        // Crear documento
        final Documento documento = new Documento();
        documento.setNombreOriginal("workflow.pdf");
        documento.setTipoMime("application/pdf");
        documento.setRutaArchivo("/uploads/workflow.pdf");
        documento.setPublico(false);
        documento.setCreadoPor(testUsuario);
        documento.setFechaCreacion(LocalDateTime.now());
        final Documento savedDocumento = documentoRepository.save(documento);

        // Crear documento workflow
        DocumentoWorkflow docWorkflow = new DocumentoWorkflow();
        docWorkflow.setDocumento(savedDocumento);
        docWorkflow.setWorkflow(testWorkflow);
        docWorkflow.setEstadoActual(etapa);
        docWorkflow.setAprobado(false);
        
        docWorkflow = documentoWorkflowRepository.save(docWorkflow);
        
        // Verificar que se guardó
        assertNotNull(docWorkflow.getId());
        
        // Buscar por ID
        Optional<DocumentoWorkflow> foundDocWorkflow = documentoWorkflowRepository.findById(docWorkflow.getId());
        assertTrue(foundDocWorkflow.isPresent());
        assertEquals(savedDocumento.getId(), foundDocWorkflow.get().getDocumento().getId());
        assertEquals(testWorkflow.getId(), foundDocWorkflow.get().getWorkflow().getId());
        
        // Buscar todos los documento workflows y filtrar por documento
        List<DocumentoWorkflow> allDocWorkflows = documentoWorkflowRepository.findAll();
        assertFalse(allDocWorkflows.isEmpty());
        
        List<DocumentoWorkflow> workflowsByDoc = allDocWorkflows.stream()
            .filter(dw -> dw.getDocumento().getId().equals(savedDocumento.getId()))
            .toList();
        assertFalse(workflowsByDoc.isEmpty());
        assertEquals(1, workflowsByDoc.size());
    }

    @Test
    void testMetadataRepositorySaveAndFind() {
        // Crear documento
        final Documento metadataDocumento = new Documento();
        metadataDocumento.setNombreOriginal("metadata.pdf");
        metadataDocumento.setTipoMime("application/pdf");
        metadataDocumento.setRutaArchivo("/uploads/metadata.pdf");
        metadataDocumento.setPublico(false);
        metadataDocumento.setCreadoPor(testUsuario);
        metadataDocumento.setFechaCreacion(LocalDateTime.now());
        final Documento savedMetadataDocumento = documentoRepository.save(metadataDocumento);

        // Crear metadata
        Metadata metadata = new Metadata();
        metadata.setDocumento(savedMetadataDocumento);
        metadata.setClave("author");
        metadata.setValor("Test Author");
        
        metadata = metadataRepository.save(metadata);
        
        // Verificar que se guardó
        assertNotNull(metadata.getId());
        
        // Buscar por ID
        Optional<Metadata> foundMetadata = metadataRepository.findById(metadata.getId());
        assertTrue(foundMetadata.isPresent());
        assertEquals("author", foundMetadata.get().getClave());
        assertEquals("Test Author", foundMetadata.get().getValor());
        
        // Buscar todos los metadata y filtrar por documento
        List<Metadata> allMetadata = metadataRepository.findAll();
        assertFalse(allMetadata.isEmpty());
        
        List<Metadata> metadataByDoc = allMetadata.stream()
            .filter(m -> m.getDocumento().getId().equals(savedMetadataDocumento.getId()))
            .toList();
        assertFalse(metadataByDoc.isEmpty());
        assertEquals(1, metadataByDoc.size());
        assertEquals("author", metadataByDoc.get(0).getClave());
    }

    @Test
    void testAuditLogRepositorySaveAndFind() {
        // Crear audit log
        AuditLog auditLog = new AuditLog();
        auditLog.setUsuario(testUsuario);
        auditLog.setAccion("CREATE");
        auditLog.setDescripcion("Usuario creado");
        auditLog.setFecha(LocalDateTime.now());
        
        auditLog = auditLogRepository.save(auditLog);
        
        // Verificar que se guardó
        assertNotNull(auditLog.getId());
        
        // Buscar por ID
        Optional<AuditLog> foundAuditLog = auditLogRepository.findById(auditLog.getId());
        assertTrue(foundAuditLog.isPresent());
        assertEquals("CREATE", foundAuditLog.get().getAccion());
        assertEquals("Usuario creado", foundAuditLog.get().getDescripcion());
        
        // Buscar todos los audit logs y filtrar por usuario
        List<AuditLog> allLogs = auditLogRepository.findAll();
        assertFalse(allLogs.isEmpty());
        
        List<AuditLog> logsByUser = allLogs.stream()
            .filter(log -> log.getUsuario().getId().equals(testUsuario.getId()))
            .sorted((l1, l2) -> l2.getFecha().compareTo(l1.getFecha()))
            .toList();
        assertFalse(logsByUser.isEmpty());
        assertEquals(1, logsByUser.size());
        assertEquals("CREATE", logsByUser.get(0).getAccion());
        
        // Buscar por acción
        List<AuditLog> logsByAction = allLogs.stream()
            .filter(log -> "CREATE".equals(log.getAccion()))
            .toList();
        assertFalse(logsByAction.isEmpty());
        assertTrue(logsByAction.stream().allMatch(log -> "CREATE".equals(log.getAccion())));
    }

    @Test
    void testRepositoryDelete() {
        // Verificar que se puede eliminar
        Long userId = testUsuario.getId();
        usuarioRepository.deleteById(userId);
        
        // Verificar que se eliminó
        Optional<Usuario> deletedUser = usuarioRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testRepositoryUpdate() {
        // Actualizar usuario
        testUsuario.setEmail("updated@example.com");
        testUsuario = usuarioRepository.save(testUsuario);
        
        // Verificar que se actualizó
        Optional<Usuario> updatedUser = usuarioRepository.findById(testUsuario.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals("updated@example.com", updatedUser.get().getEmail());
    }
}
