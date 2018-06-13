package spittr.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import spittr.data.Spitter;
import spittr.data.SpitterRepository;

public class SpitterControllerTest {

    @Test
    public void shouldShowRegistrationForm() throws Exception{
        SpitterRepository spitterRepository = mock(SpitterRepository.class);
        SpitterController controller = new SpitterController(spitterRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();
        
        mockMvc.perform(get("/spitter/register")).andExpect(view().name("registerForm"));        
    }
    
    @Test
    public void shouldProcessRegistration() throws Exception{
        SpitterRepository mockRepository = mock(SpitterRepository.class);
        Spitter unsaved = new Spitter("jbauer", "24hours", "jack", "bauer", "jbauer@gmail.com");
        Spitter saved = new Spitter(24L, "jbauer", "24hours", "jack", "bauer", "jbauer@gmail.com");
        
        when(mockRepository.save(unsaved)).thenReturn(saved);
        
        SpitterController controller = new SpitterController(mockRepository);
        
        MockMvc mockMvc = standaloneSetup(controller).build();
        
        mockMvc.perform(post("/spitter/register")
            .param("firstName", "jack")
            .param("lastName", "bauer")
            .param("userName", "jbauer")
            .param("password", "24hours")
            .param("email", "jbauer@gmail.com"))
            .andExpect(redirectedUrl("/spitter/jbauer"));
        
        verify(mockRepository, atLeastOnce()).save(unsaved);        
    }
    
    @Test
    public void shouldFailValidationWithNoData() throws Exception{
        SpitterRepository spitterRepository = mock(SpitterRepository.class);
        SpitterController controller = new SpitterController(spitterRepository);
        
        MockMvc mockMvc = standaloneSetup(controller).build();
        
        mockMvc.perform(post("/spitter/register"))
           .andExpect(status().isOk())
           .andExpect(view().name("registerForm"))
           .andExpect(model().errorCount(5))
           .andExpect(model().attributeHasFieldErrors(
                   "spitter", "firstName", "lastName", "userName", "password", "email"));
    }
}
