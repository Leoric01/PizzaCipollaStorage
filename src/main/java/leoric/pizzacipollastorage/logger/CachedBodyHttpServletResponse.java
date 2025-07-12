package leoric.pizzacipollastorage.logger;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream cachedBody = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(cachedBody, true);

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new DelegatingServletOutputStream(cachedBody);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public String getContentAsString() {
        return cachedBody.toString(StandardCharsets.UTF_8);
    }
}
