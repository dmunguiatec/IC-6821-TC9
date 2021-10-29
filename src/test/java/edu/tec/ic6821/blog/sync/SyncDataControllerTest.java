package edu.tec.ic6821.blog.sync;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SyncDataController.class)
public class SyncDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SyncDataService syncDataService;

    @Test
    public void sync() throws Exception {
        // given
        given(syncDataService.sync()).willReturn(new SyncDataResult(10, 100));

        // when ... then
        mvc.perform(put("/admin/api/external")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.createdUsersCount", is(10)))
            .andExpect(jsonPath("$.createdPostsCount", is(100)));
    }

}
