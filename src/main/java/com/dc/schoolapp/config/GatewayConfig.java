package com.dc.schoolapp.config;

import com.dc.schoolapp.filter.JwtAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
public class GatewayConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public GatewayConfig(JwtAuthFilter jwtAuthFilter ) {
        this.jwtAuthFilter = jwtAuthFilter;
     }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        corsConfig.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("http://localhost:8081"))

                .route("exam-service", r -> r.path("/exam/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("🔥 API Gateway: Forwarding request to exam-service: "
                                    + exchange.getRequest().getURI());
                            exchange.getRequest().getHeaders()
                                    .forEach((key, value) -> System.out.println(key + " : " + value));
                            return chain.filter(exchange);
                        }))
                        .uri("http://localhost:8082"))

                .route("subject-service", r -> r.path("/subject/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            exchange.getRequest().getHeaders()
                                    .forEach((key, value) -> System.out.println(key + " : " + value));
                            return chain.filter(exchange);
                        }))
                        .uri("http://localhost:8083"))

                .route("class-teacher-service", r -> r.path("/classTeacher/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("🔥 API Gateway: Forwarding request to exam-service: "
                                    + exchange.getRequest().getURI());
                            exchange.getRequest().getHeaders()
                                    .forEach((key, value) -> System.out.println(key + " : " + value));
                            return chain.filter(exchange);
                        }))
                        .uri("http://localhost:8084"))


                .build();
    }

//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("auth-service", r -> r.path("/auth/**")
//                        .uri("http://localhost:8081"))
//
//
//                .route("exam-service", r -> r.path("/exam/**")
//                        .filters(f -> f.addRequestHeader("Authorization", "#{request.getHeader('Authorization')}"))
////                        .filters(f -> f.filter(jwtAuthFilter))
//                        .uri("http://localhost:8082"))
//
//                .route("subject-service", r -> r.path("/subject/**")
//                        .filters(f -> f.addRequestHeader("Authorization", "#{request.getHeader('Authorization')}"))
////                        .filters(f -> f.filter(jwtAuthFilter))
//                        .uri("http://localhost:8083"))
//
//                .build();
//    }

}
