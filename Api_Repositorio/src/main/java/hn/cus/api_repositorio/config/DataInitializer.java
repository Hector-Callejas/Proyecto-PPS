package hn.cus.api_repositorio.config;

import hn.cus.api_repositorio.entity.Rol;
import hn.cus.api_repositorio.entity.Usuario;
import hn.cus.api_repositorio.repository.RolRepository;
import hn.cus.api_repositorio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (rolRepository.count() == 0) {
            Rol adminRole = new Rol();
            adminRole.setNombre("ADMIN");
            rolRepository.save(adminRole);

            Rol userRole = new Rol();
            userRole.setNombre("USER");
            rolRepository.save(userRole);
        }

        // Crear usuarios si no existen
        if (usuarioRepository.count() == 0) {
            // Usuario admin
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@gestor.com");
            admin.setActivo(true);
            
            Rol adminRole = rolRepository.findByNombre("ADMIN").orElse(null);
            if (adminRole != null) {
                admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
            }
            usuarioRepository.save(admin);

            // Usuario regular
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEmail("user@gestor.com");
            user.setActivo(true);
            
            Rol userRole = rolRepository.findByNombre("USER").orElse(null);
            if (userRole != null) {
                user.setRoles(new HashSet<>(Arrays.asList(userRole)));
            }
            usuarioRepository.save(user);
        }
    }
}
