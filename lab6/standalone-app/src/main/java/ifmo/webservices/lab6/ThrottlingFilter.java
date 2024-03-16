package ifmo.webservices.lab6;

import ifmo.webservices.lab6.exceptions.ThrottlingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class ThrottlingFilter extends OncePerRequestFilter {
    private final int MAX_REQUESTS = 5;
    private final Semaphore semaphore = new Semaphore(MAX_REQUESTS);

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (semaphore.tryAcquire()) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                semaphore.release();
            }
        } else {
            resolver.resolveException(request, response, null, new ThrottlingException("Max requests reached"));
        }
    }
}
