package swagger.validation.prototype;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.mockmvc.OpenApiMatchers;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.file.Paths;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;

public abstract class TestUtil {

    private TestUtil() {}

    public static ResultMatcher satisfiesSpec() {
        var matcher = openApi().isValid(validator());
        return result -> {
            try {
                matcher.match(result);
            } catch (OpenApiMatchers.OpenApiValidationException e) {
                throw new AssertionError(e);
            }
        };
    }

    private static OpenApiInteractionValidator validator() {
        var specResource = Paths.get("./openapi.yaml").toUri();
        return OpenApiInteractionValidator
                .createForSpecificationUrl(specResource.toString())
                .withResolveCombinators(true)
                .withResolveRefs(true)
                .withLevelResolver(LevelResolver.create()
                        .withLevel("validation.request", ValidationReport.Level.IGNORE)
                        .build())
                .build();
    }
}
