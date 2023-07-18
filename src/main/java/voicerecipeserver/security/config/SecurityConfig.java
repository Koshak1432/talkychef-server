package voicerecipeserver.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import voicerecipeserver.config.Constants;
import voicerecipeserver.security.filter.JwtFilter;
import voicerecipeserver.security.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers( Constants.BASE_API_PATH +"/registration").permitAll()
                        .requestMatchers(Constants.BASE_API_PATH + "/auth/token").permitAll()
                        .requestMatchers(Constants.BASE_API_PATH +"/login").permitAll()
                        .requestMatchers(Constants.BASE_API_PATH + "/media/**").permitAll()
                        .requestMatchers(Constants.BASE_API_PATH + "/comments/**").permitAll()
                        .requestMatchers(Constants.BASE_API_PATH + "/collections/**").permitAll()
                        .requestMatchers("/", Constants.BASE_API_PATH + "/recipes/**").permitAll() //todo вывод только рецептов
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(LogoutConfigurer::permitAll);
        http.cors(configurer -> configurer.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }


}
