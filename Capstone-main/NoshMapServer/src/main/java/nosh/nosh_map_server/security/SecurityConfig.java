package nosh.nosh_map_server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        // we're not using HTML forms in our app
        //so disable CSRF (Cross Site Request Forgery)
        http.csrf().disable();

        // this configures Spring Security to allow
        //CORS related requests (such as preflight checks)
        http.cors();

        // the order of the antMatchers() method calls is important
        // as they're evaluated in the order that they're added
        http.authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/refresh_token").authenticated()
                .antMatchers("/create_account").permitAll()
                .antMatchers(HttpMethod.GET, "/api/comment/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/comment").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/api/comment/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,"/api/comment/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/meal/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/meal").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/api/meal/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,"/api/meal/**").hasAnyAuthority("USER")
                .antMatchers("/api/profile", "/api/profile/**", "**/profile/**", "**profile**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/profile/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/profile").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/api/profile/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,"/api/profile/**").hasAnyAuthority("USER")
                .antMatchers("/api/rating", "/api/rating/**", "**/rating/**", "**rating**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/rating/**", "api/rating/ratingId/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/rating").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/api/rating/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,"/api/rating/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/restaurant/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/restaurant").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/api/restaurant/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE,"/api/restaurant/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/restaurant/appUser/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.POST, "/api/restaurant/appUser/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.GET, "/api/appUser/**").permitAll()
                // if we get to this point, let's deny all requests

                //.antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authConfig), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}