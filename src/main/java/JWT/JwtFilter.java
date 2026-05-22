package JWT;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String path = requestContext.getUriInfo().getPath();

        // ✅ Public endpoints
        if (path.contains("login") || path.contains("registerUser") || path.contains("forgotPassword") || path.contains("resetPassword") || path.contains("getSocietiesByWard")) {
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext, "Missing or invalid token", Response.Status.UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            // ✅ Validate token
            Claims claims = JwtUtil.validateToken(token);

            String role = (String) claims.get("role");
            String username = claims.getSubject();

            // store if needed later
            requestContext.setProperty("role", role);
            requestContext.setProperty("username", username);

            // 🔐 Get method annotation
            Method method = resourceInfo.getResourceMethod();

            if (method != null && method.isAnnotationPresent(Secured.class)) {

                Secured secured = method.getAnnotation(Secured.class);
                String[] allowedRoles = secured.roles();

                boolean allowed = Arrays.stream(allowedRoles)
                        .anyMatch(r -> r.equalsIgnoreCase(role));

                if (!allowed) {
                    abort(requestContext, "Access denied: insufficient role", Response.Status.FORBIDDEN);
                }
            }

        } catch (Exception e) {
            abort(requestContext, "Invalid or expired token", Response.Status.UNAUTHORIZED);
        }
    }

    private void abort(ContainerRequestContext ctx, String msg, Response.Status status) {
        ctx.abortWith(
                Response.status(status)
                        .entity(msg)
                        .build()
        );
    }
}