package com.ManiCode.fullstackbackend;

import com.ManiCode.fullstackbackend.controller.TimeController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final TimeController timeController = new TimeController();

    @Test
    public void testIsValidTimeZoneWithValidTimeZone() {
        boolean result = timeController.isValidTimeZone("Europe/London");
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsValidTimeZoneWithInvalidTimeZone() {
        boolean result = timeController.isValidTimeZone("Invalid/TimeZone");
        Assertions.assertFalse(result);
    }

    @Test
    public void testIsUSTimeZoneWithUSTimeZone() {
        boolean result = timeController.isUSTimeZone("America/Los_Angeles");
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsUSTimeZoneWithNonUSTimeZone() {
        boolean result = timeController.isUSTimeZone("Europe/London");
        Assertions.assertFalse(result);
    }


    @Test
    public void testGetTime() throws Exception {
        String timezone = "America/New_York";
        mockMvc.perform(MockMvcRequestBuilders.get("/time")
                        .param("timezone", timezone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timezone").value(timezone))
                .andExpect(MockMvcResultMatchers.jsonPath("$.abbreviation").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.datetime").isString());
    }

    @Test
    public void testGetTimeInvalidTimezone() throws Exception {
        String timezone = "Invalid/Timezone";
        mockMvc.perform(MockMvcRequestBuilders.get("/time")
                        .param("timezone", timezone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid timezone"));
    }

    @Test
    public void testGetTimeNonUSTimezone() throws Exception {
        String timezone = "Europe/Paris";
        mockMvc.perform(MockMvcRequestBuilders.get("/time")
                        .param("timezone", timezone)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Not a US timezone"));

    }


}

