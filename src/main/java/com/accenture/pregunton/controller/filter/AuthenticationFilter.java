package com.accenture.pregunton.controller.filter;

import com.accenture.pojo.UnauthorizedResponseDto;
import com.accenture.pregunton.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private ObjectMapper objectMapper;

  @Value("${authentication.subdomain.validate}")
  private String tokenValidationUrl;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException {
    try {
      doFilter(request);
      filterChain.doFilter(request, response);
    } catch (UnauthorizedException e) {
      buildErrorResponse(response, HttpStatus.UNAUTHORIZED,
          objectMapper.writeValueAsString(new UnauthorizedResponseDto(e.getPath())));
    } catch (Exception e) {
      buildErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error happens");
    }
  }

  private void doFilter(HttpServletRequest request) {
    String requestUrl = request.getServletPath();
    if (isRestEndpoint(requestUrl)) {
      String token = request.getHeader("Authorization");
      String BEARER = "Bearer ";
      if (Objects.isNull(token) || !token.startsWith(BEARER)) {
        throw new UnauthorizedException(requestUrl);
      }
      boolean isExpired = isTokenExpired(token.replace(BEARER, ""));
      if (isExpired) {
        throw new UnauthorizedException(requestUrl);
      }
    }
  }

  private boolean isTokenExpired(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("token", token);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    URI uri = UriComponentsBuilder.fromHttpUrl(tokenValidationUrl)
        .build()
        .encode()
        .toUri();
    ResponseEntity<Boolean> response = restTemplate.exchange(uri, HttpMethod.POST, request, Boolean.class);

    return Objects.nonNull(response.getBody()) ? response.getBody() : true;
  }

  private void buildErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
    response.setStatus(status.value());
    response.getWriter()
        .write(message);
  }

  private boolean isRestEndpoint(String url) {
    return url.startsWith("/categories") || url.startsWith("/games") || url.startsWith("/players");
  }
}
