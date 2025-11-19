package xyz.akhiltay.lab4fullstack;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CarRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test authentication with correct credentials")
    public void testAuthentication() throws Exception {
        // Testing authentication with correct credentials
        this.mockMvc
                .perform(post("/login")
                        .content("{\"username\":\"admin\",\"password\":\"admin\"}")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test authentication with incorrect credentials")
    public void testAuthenticationFailure() throws Exception {
        // Testing authentication with incorrect credentials
        this.mockMvc
                .perform(post("/login")
                        .content("{\"username\":\"admin\",\"password\":\"wrongpassword\"}")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test user registration")
    public void testRegistration() throws Exception {
        // Testing user registration
        this.mockMvc
                .perform(post("/register")
                        .content("{\"username\":\"testuser\",\"password\":\"testpass\",\"role\":\"USER\"}")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test accessing protected endpoint without authentication")
    public void testUnauthorizedAccess() throws Exception {
        // Trying to access protected endpoint without token
        this.mockMvc
                .perform(get("/api/cars"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
