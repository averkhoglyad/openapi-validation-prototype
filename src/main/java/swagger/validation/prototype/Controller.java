package swagger.validation.prototype;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static swagger.validation.prototype.HttpRequestUtil.extractHeaders;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class Controller {

    @GetMapping(path = "/ping")
    String ping() {
        return "pong";
    }

    @GetMapping(path = "/time")
    Map<String, Object> time() {
        return Map.of(
                "ts", Instant.now()
        );
    }

    @PostMapping(path = "/echo")
    Map<String, Object> echo(@RequestBody String body, HttpServletRequest request) {
        return Map.of(
                "body", body,
                "headers", extractHeaders(request),
                "ts", Instant.now()
        );
    }

    @PostMapping(path = "/valid")
    Object valid(@RequestBody Map<String, String> body) {
        var value = body.get("mandatory");
        if (StringUtils.hasText(value)) {
            return Map.of("result", value);
        } else {
            return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Field `mandatory` is required");
        }
    }
}
