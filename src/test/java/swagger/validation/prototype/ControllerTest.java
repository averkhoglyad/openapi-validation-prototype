package swagger.validation.prototype;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static swagger.validation.prototype.TestUtil.satisfiesSpec;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void ping_вернет_пуньк() throws Exception {
        mvc.perform(get("/ping"))
                .andExpect(satisfiesSpec())
                .andExpect(jsonPath("$").value("pong"));
    }

    @Test
    void time_вернет_ts() throws Exception {
        mvc.perform(get("/time"))
                .andExpect(satisfiesSpec())
                .andExpect(jsonPath("$.ts").isNotEmpty());
    }

    @Test
    void echo_вернет_детали_запроса() throws Exception {
        var content = "{\"hello\": \"world\"}";
        mvc.perform(post("/echo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(satisfiesSpec())
                .andExpect(jsonPath("$.body").isNotEmpty())
                .andExpect(jsonPath("$.headers").exists())
                .andExpect(jsonPath("$.ts").isNotEmpty());
    }

    @Test
    void valid_отработает() throws Exception {
        var value = "TEST";
        mvc.perform(post("/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mandatory\": \"" + value + "\"}"))
                .andExpect(satisfiesSpec())
                .andExpect(jsonPath("$.result").value(value));
    }

    @Test
    void valid_зафейлится() throws Exception {
        var value = "TEST";
        mvc.perform(post("/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"optional\": \"" + value + "\"}"))
                .andExpect(satisfiesSpec())
                .andExpect(status().isBadRequest());
    }
}