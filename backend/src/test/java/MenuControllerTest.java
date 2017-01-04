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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class MenuControllerTest {
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

    protected void createCategory() {
        String json = "{" + 
                       "\"description\"  : \"This is dessert\"" + 
                       "}";
        String json1 = "{" + 
                       "\"description\"  : \"This is soup\"" + 
                       "}";
        try {
            mockMvc.perform(post("/menu/David/dessert")
                    .contentType(contentType)
                    .content(json));
            mockMvc.perform(post("/menu/David/Soup")
                    .contentType(contentType)
                    .content(json1));
        } catch (Exception e) {
            System.out.println("Failed to setup test cases.");
        }
    }

    protected void deleteCategory() {
        try {
            mockMvc.perform(delete("/menu/David/Soup"));
            mockMvc.perform(delete("/menu/David/dessert"));
        } catch (Exception e) {
            System.out.println("Failed to clean up test cases.");
        }
    }
    @Test
    public void createMenuTest() throws Exception {
        String json = "{" + 
                       "\"description\"  : \"This is Appetizer\"" + 
                       "}";
        String json1 = "{" + 
                       "\"description\"  : \"\"" + 
                       "}";
        String json2 = "{" + 
                       "\"description\"  : \"!~@#\"" + 
                       "}";
        /* test with normal name and category name*/
        /* clean up the record first */
        //mockMvc.perform(delete("/menu/David/Appetizer"));
        //mockMvc.perform(delete("/menu/David/Soup"));
        //mockMvc.perform(delete("/menu/David/dessert"));
        //mockMvc.perform(delete("/menu/David/Suchi"));
        mockMvc.perform(delete("/menu/David"));

        /* start create */
        mockMvc.perform(post("/menu/David/Appetizer")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/menu/David/Soup")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isCreated());
        
        /* testing with unusual user and category name. */
        mockMvc.perform(post("/menu/!David/Appetizer/")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/menu/~David/Appetizer")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/menu/David/@Appetizer")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isBadRequest());

        /* starting with number is acceptable */
        mockMvc.perform(post("/menu/David/6Appetizer")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/menu/David/6Appe tizer")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/menu/")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(post("/menu/Tom/")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isMethodNotAllowed());

        /* testing with duplicate */
        mockMvc.perform(post("/menu/David/Appetizer")
                .contentType(contentType)
                .content(json))
                .andExpect(status().isConflict());

        /* test with different contents */
        mockMvc.perform(post("/menu/David/Suchi")
                .contentType(contentType)
                .content(json1))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/menu/David/dessert")
                .contentType(contentType)
                .content(json2))
                .andExpect(status().isCreated());

        /* clean up */
        mockMvc.perform(delete("/menu/David/Appetizer"));
        mockMvc.perform(delete("/menu/David/Soup"));
        mockMvc.perform(delete("/menu/David/dessert"));
        mockMvc.perform(delete("/menu/David/Suchi"));
        mockMvc.perform(delete("/menu/David/6Appetizer"));
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(delete("/menu/David"));
        /* create categories */
        createCategory();
        /* get the categories */
        mockMvc.perform(get("/menu/David/dessert")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName",is("David")))
                .andExpect(jsonPath("$.category",is("dessert")))
                .andExpect(jsonPath("$.description",is("This is dessert")));
        mockMvc.perform(get("/menu/David/Soup")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName",is("David")))
                .andExpect(jsonPath("$.category",is("Soup")))
                .andExpect(jsonPath("$.description",is("This is soup")));
        mockMvc.perform(get("/menu/David/")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].userName",is("David")))
                .andExpect(jsonPath("$.[0].category",is("dessert")))
                .andExpect(jsonPath("$.[0].description",is("This is dessert")))
                .andExpect(jsonPath("$.[1].userName",is("David")))
                .andExpect(jsonPath("$.[1].category",is("Soup")))
                .andExpect(jsonPath("$.[1].description",is("This is soup")));

        /* getting some non-existing categories */
        mockMvc.perform(get("/menu/JohnNotExist")
                .contentType(contentType))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/menu/David/Appetizer/")
                .contentType(contentType))
                .andExpect(status().isNotFound());

        /* clean up */
        deleteCategory();
    }

    @Test
    public void updateUserTest() throws Exception {
        /* setup */
        createCategory();

        /* update the user info */
        String json = "{" + 
               "\"description\"  : \"This is an updated description\"" +
               "}";
        String json1 = "{" + 
               "\"username\" : \"Cynthia\"," + 
               "\"description\"  : \"This is an updated description2\"" +
               "}";
        mockMvc.perform(put("/menu/David/Soup")
                        .contentType(contentType)
                        .content(json))
                        .andExpect(status().isOk());

        /* verify the content */
        mockMvc.perform(get("/menu/David/Soup")
                        .contentType(contentType))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.userName",is("David")))
               .andExpect(jsonPath("$.category",is("Soup")))
               .andExpect(jsonPath("$.description", 
                           is("This is an updated description")));

        /* This should overate the username with David and get through*/
        mockMvc.perform(put("/menu/David/Soup")
                        .contentType(contentType)
                        .content(json1))
                        .andExpect(status().isOk());
        mockMvc.perform(get("/menu/David/Soup")
                        .contentType(contentType))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userName",is("David")))
                        .andExpect(jsonPath("$.category",is("Soup")))
                        .andExpect(jsonPath("$.description", 
                           is("This is an updated description2")));

        /* update with wrong username */
        mockMvc.perform(put("/menu/Tom/Soup")
                        .contentType(contentType)
                        .content(json))
                        .andExpect(status().isNotFound());

        /* clean up the DB */
        mockMvc.perform(delete("/user/David")
                .contentType(contentType));
    }

    @Test
    public void deleteUserTest() throws Exception {
        /* setup */
        createCategory();

        mockMvc.perform(delete("/menu/David/Soup")
                .contentType(contentType))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/menu/David/dessert")
                .contentType(contentType))
                .andExpect(status().isOk());

        /* duplicate delete */
        mockMvc.perform(delete("/menu/David/Soup")
                .contentType(contentType))
                .andExpect(status().isNotFound());

        /* delete non-existent */
        mockMvc.perform(delete("/account/John")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

}
