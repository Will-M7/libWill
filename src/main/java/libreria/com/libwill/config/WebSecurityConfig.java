package libreria.com.libwill.config;

import libreria.com.libwill.security.jwt.JwtAuthEntryPoint;
import libreria.com.libwill.security.jwt.JwtAuthenticationFilter;
import libreria.com.libwill.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {


    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // ⚙️ Define explícitamente el AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    // ⚙️ AuthenticationManager con la configuración correcta
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/uploads/**",
                "/imagenes/**",
                "/imagenes/pagos/**",
                "/static.imagenes/**",
                "/static.imagenes/pagos/**"
        );
    }

    // ⚙️ Seguridad principal
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {}) // Usa la configuración del CorsFilter bean
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
// 🔥 Rutas públicas
                                .requestMatchers("/api/auth/**", "/api/test/public").permitAll()
                                .requestMatchers("/api/admin/**", "/categorias/admin/**").hasRole("ADMIN")
                                .requestMatchers("/categorias/crear", "/categorias/editar/**").hasAnyRole("ADMIN", "VENDEDOR")
                                .requestMatchers("/categorias/eliminar/**").hasRole("ADMIN")
                                // 🔥 PRODUCTOS
                                .requestMatchers(HttpMethod.GET, "/api/productos", "/api/productos/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/productos").hasAnyRole("ADMIN", "VENDEDOR")
                                .requestMatchers(HttpMethod.PUT, "/productos/**").hasAnyRole("ADMIN", "VENDEDOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAnyRole("ADMIN", "VENDEDOR")

                                .requestMatchers(HttpMethod.GET, "/api/auth").permitAll()

                                // 🔥 PEDIDOS
                                .requestMatchers(HttpMethod.POST, "/api/pedidos").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/pedidos/mis-pedidos").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/pedidos/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/{id}/cancelar").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/{id}/estado").hasAnyRole("ADMIN", "VENDEDOR")
                                .requestMatchers(HttpMethod.GET, "/api/pedidos").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/pedidos/estado/**").hasAnyRole("ADMIN", "VENDEDOR")

                                // 🔥 Rutas de admin
                                .requestMatchers("/api/admin/**", "/categorias/admin/**").hasRole("ADMIN")
                                .requestMatchers("/categorias/crear", "/categorias/editar/**").hasAnyRole("ADMIN", "VENDEDOR")
                                .requestMatchers("/categorias/eliminar/**").hasRole("ADMIN")

                                // 🔥 PAGOS
                                .requestMatchers(HttpMethod.POST, "/api/pagos/*/comprobante").authenticated()
                                .requestMatchers(HttpMethod.GET,  "/api/pagos/pedido/*").authenticated()
                                .requestMatchers(HttpMethod.GET,  "/api/pagos/pendientes").hasAnyRole("ADMIN","VENDEDOR")
                                .requestMatchers(HttpMethod.PUT,  "/api/pagos/*/validar").hasAnyRole("ADMIN","VENDEDOR")
                                .requestMatchers(HttpMethod.PUT,  "/api/pagos/*/rechazar").hasAnyRole("ADMIN","VENDEDOR")
                                .requestMatchers(HttpMethod.GET,  "/api/pagos/*").hasAnyRole("ADMIN","VENDEDOR")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
