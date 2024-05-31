package swagger.validation.prototype;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class HttpRequestUtil {

    private HttpRequestUtil() {
    }

    public static Map<String, String> extractHeaders(HttpServletRequest request) {
        var headers = new HashMap<String, String>();
        for (var name : (Iterable<String>) request.getHeaderNames()::asIterator) {
            headers.put(name, headerValue(request.getHeaders(name).asIterator()));
        }
        return headers;
    }

    private static String headerValue(Iterator<String> values) {
        var sb = new StringBuilder();
        values.forEachRemaining(it -> {
            if (!sb.isEmpty()) {
                sb.append(";");
            }
            sb.append(it);
        });
        return sb.toString();
    }

}
