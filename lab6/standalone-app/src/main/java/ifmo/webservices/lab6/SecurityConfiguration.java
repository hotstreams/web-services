package ifmo.webservices.lab6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request -> request
                   .requestMatchers(HttpMethod.GET, "/products/**").authenticated()
                   .requestMatchers(HttpMethod.POST, "/products/**").authenticated()
                   .requestMatchers(HttpMethod.PUT, "/products/**").authenticated()
                   .requestMatchers(HttpMethod.DELETE, "/products/**").authenticated()
            )
            .httpBasic(customizer -> customizer.authenticationEntryPoint(basicAuthenticationEntryPoint()))
            .csrf(csrf -> csrf.disable())
            .userDetailsService(userDetailsService());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String password = encoder.encode("password");

        UserDetails details = User.builder()
                .username("username")
                .password(password)
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(details);
    }

    @Bean
    public BasicAuthenticationEntryPoint basicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("realm");
        return basicAuthenticationEntryPoint;
    }

    @Bean
    public ThrottlingFilter throttlingFilter() {
        return new ThrottlingFilter();
    }
}
