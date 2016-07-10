package forester.familykitchen;

/* Test file for UserControllerTest */

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.http.MediaType;
import java.nio.charset.Charset;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = forester.familykitchen.Application.class)
@WebAppConfiguration
public class UserControllerTest {
    private MockMvc mockMvc;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createUserTest() throws Exception {
        /* clean up */
        mockMvc.perform(delete("/user/David")
                .contentType(contentType));
        mockMvc.perform(delete("/user/~David")
                .contentType(contentType));
        mockMvc.perform(delete("/user/!David")
                .contentType(contentType));
        mockMvc.perform(delete("/user/6")
                .contentType(contentType));

        /* test with normal name */
        mockMvc.perform(post("/user/David")
                .contentType(contentType))
                .andExpect(status().isCreated());
        
        /* testing with unusual user name. */
        mockMvc.perform(post("/user/!David")
                .contentType(contentType))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/user/~David")
                .contentType(contentType))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/user/.")
                .contentType(contentType))
                .andExpect(status().isUnprocessableEntity());
        mockMvc.perform(post("/user/")
                .contentType(contentType))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(post("/user/6")
                .contentType(contentType))
                .andExpect(status().isCreated());

        /* testing with duplicate */
        mockMvc.perform(post("/user/David")
                .contentType(contentType))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/user/David")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName",is("David")));
        mockMvc.perform(get("/user/John")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/user/David")
                .contentType(contentType))
                .andExpect(status().isNoContent());

        /* duplicate delete */
        mockMvc.perform(delete("/user/David")
                .contentType(contentType))
                .andExpect(status().isNotFound());

        /* delete non-existent */
        mockMvc.perform(delete("/user/John")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

}
