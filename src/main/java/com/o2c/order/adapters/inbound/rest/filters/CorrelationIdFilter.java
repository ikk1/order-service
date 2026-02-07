package com.o2c.order.adapters.inbound.rest.filters;

import com.o2c.order.shared.CorrelationId;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorrelationIdFilter implements Filter {

    public static final String CORRELATION_ID = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        String cid = CorrelationId.getOrCreate(http.getHeader(CorrelationId.HEADER));

        MDC.put(CORRELATION_ID, cid);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID);
        }

    }
}
