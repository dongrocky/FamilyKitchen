package forester.familykitchen;

/* Test file for AccountControllerTest */

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.http.MediaType;
import java.nio.charset.Charset;
import static org.hamcrest.Matchers.is;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = forester.familykitchen.Application.class)
@WebAppConfiguration
public class AccountControllerTest {
    private MockMvc mockMvc;
    private static final Logger log = 
        LoggerFactory.getLogger(AccountControllerTest.class);
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        log.debug("Setup test...");
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createAccountTest() throws Exception {
        log.debug("Starting createAccountTest.");
        /* clean up */
        mockMvc.perform(delete("/account/David")
                .contentType(contentType));
        mockMvc.perform(delete("/account/~David")
                .contentType(contentType));
        mockMvc.perform(delete("/account/!David")
                .contentType(contentType));
        mockMvc.perform(delete("/account/6")
                .contentType(contentType));
        mockMvc.perform(delete("/account/")
                .contentType(contentType));

        /* test with normal name */
        String json = "{" + 
                       "\"userName\"  : \"David\"," +
                       "\"email\"     : \"David@example.com\"," +
                       "\"password\"  : \"David123@\"" +   
                       "}";
        mockMvc.perform(post("/account/")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isCreated());
        
        /* testing with unusual account name. */
        String json1 = "{" + 
                       "\"userName\"  : \"!David\"," +
                       "\"email\"     : \"David@example.com\"," +
                       "\"password\"  : \"David123@\"" +   
                       "}";
        mockMvc.perform(post("/account/")
                .contentType(contentType)
                .content(json1))
                .andExpect(status().isCreated());
        String json2 = "{" + 
                       "\"userName\"  : \"~David\"," +
                       "\"email\"     : \"David@example.com\"," +
                       "\"password\"  : \"David123@\"" +   
                       "}";
        mockMvc.perform(post("/account/")
                .contentType(contentType)
                .content(json2))
                .andExpect(status().isCreated());
        /*
         * This test is commented because "" exists in DB 
         * and can not be deleted
         */
        String json3 = "{" + 
                       "\"userName\"  : \"\"," +
                       "\"email\"     : \"David@example.com\"," +
                       "\"password\"  : \"David123@\"" +   
                       "}";
        mockMvc.perform(post("/account/")
                .contentType(contentType)
                .content(json3))
                .andExpect(status().isBadRequest());
        String json4 = "{" + 
                       "\"userName\"  : \"6\"," +
                       "\"email\"     : \"David@example.com\"," +
                       "\"password\"  : \"David123@\"" +   
                       "}";
        mockMvc.perform(post("/account/")
                .contentType(contentType)
                .content(json4))
                .andExpect(status().isCreated());

        /* testing with duplicate */
        mockMvc.perform(post("/account/")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isConflict());
        
        log.debug("createAccountTest finished.");
    }

    /**
     * Note: This test depends on createAccountTest test case 
     */
    @Test
    public void getAccountTest() throws Exception {
        log.debug("Start getAccountTest.");

        mockMvc.perform(get("/account/David")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName",is("David")));

        mockMvc.perform(get("/account/John")
                .contentType(contentType))
                .andExpect(status().isNotFound());

        log.debug("Finish getAccountTest.");
    }

    @Test
    public void updateAccountTest() throws Exception {
        log.debug("Start updateAccountTest.");
        String json = "{" + 
                       "\"userName\"  : \"David_update\"," +
                       "\"email\"     : \"David@example.com\"," +
                       "\"password\"  : \"David123@\"" +   
                       "}";
        /* delete the account in case it exists */
        mockMvc.perform(delete("/account/David_update")
                .contentType(contentType));

        /* create the account first */
        mockMvc.perform(post("/account/")
                        .contentType(contentType)
                        .content(json))
                        .andExpect(status().isCreated());

        /* update the account info */
        json = "{" + 
               "\"userName\"  : \"David_update\"," +
               "\"email\"     : \"David_updated@example.com\"," +
               "\"password\"  : \"David321@\"," +   
               "\"firstName\"  : \"David_first\"," +   
               "\"lastName\"  : \"David_last\"" + 
               "}";
        mockMvc.perform(put("/account/")
                        .contentType(contentType)
                        .content(json))
                        .andExpect(status().isOk());

        /* verify the content */
        mockMvc.perform(get("/account/David_update")
                        .contentType(contentType))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.userName",is("David_update")))
               .andExpect(jsonPath("$.email",is("David_updated@example.com")))
               .andExpect(jsonPath("$.password", is("David321@")))
               .andExpect(jsonPath("$.firstName", is("David_first")))
               .andExpect(jsonPath("$.lastName", is("David_last")));
        /* clean up the DB */
        mockMvc.perform(delete("/account/David_update")
                .contentType(contentType));

        log.debug("Finish updateAccountTest.");
    }

    /**
     * Note: This test case depends on createAccountTest test case 
     */
    @Test
    public void deleteAccountTest() throws Exception {
        log.debug("start deleteAccountTest.");

        mockMvc.perform(delete("/account/David")
                .contentType(contentType))
                .andExpect(status().isOk());

        /* duplicate delete */
        mockMvc.perform(delete("/account/David")
                .contentType(contentType))
                .andExpect(status().isNotFound());

        /* delete non-existent */
        mockMvc.perform(delete("/account/John")
                .contentType(contentType))
                .andExpect(status().isNotFound());

        log.debug("finish deleteAccountTest.");
    }

}
