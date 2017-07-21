package com.subscription.controller;

import com.subscription.Application;
import com.subscription.model.Info;
import com.subscription.model.Message;
import com.subscription.service.SubscriptionManagement;
import com.subscription.service.SubscriptionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ControllersTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private SubscriptionManagement subsManagement;
	
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

    }
    
    @Test
    public void createSubscription() throws Exception {
    	
        this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "testA")
                .param("messageType", "typeA"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeA\":0}}"));
        
        this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "testA")
                .param("messageType", "typeB"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Subscription already exist"));
    }
    
    
    @Test
    public void invalidRequestsToCreateSubscription() throws Exception {
        
        this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "")
                .param("messageType", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid parameters in the request."));
        
        this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "Valid")
                .param("messageType", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid parameters in the request."));

        this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "")
                .param("messageType", "TypeX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid parameters in the request."));
    }
    
    @Test
    public void updateSubscription() throws Exception {
    	
    	this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "testB")
                .param("messageType", "typeB"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeB\":0}}"));
    	
        this.mockMvc.perform(put("/subscriptions/update")
                .contentType(contentType)
                .param("id", "testB")
                .param("messageType", "typeA")
                .param("messageType", "typeB"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeB\":0,\"typeA\":0}}"));
    }
    
    @Test
    public void updateDoesntExistSubscription() throws Exception {
    	
        this.mockMvc.perform(put("/subscriptions/update")
                .contentType(contentType)
                .param("id", "testH")
                .param("messageType", "typeA")
                .param("messageType", "typeB"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Subscription doesn't exist"));
    }
    
    @Test
    public void invalidRequestsToUpdateSubscription() throws Exception {
        
        this.mockMvc.perform(put("/subscriptions/update")
                .contentType(contentType)
                .param("id", "")
                .param("messageType", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid parameters in the request."));
        
        this.mockMvc.perform(put("/subscriptions/update")
                .contentType(contentType)
                .param("id", "Valid")
                .param("messageType", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid parameters in the request."));

        this.mockMvc.perform(put("/subscriptions/update")
                .contentType(contentType)
                .param("id", "")
                .param("messageType", "TypeX"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid parameters in the request."));
    }

    @Test
    public void readSubscriptionAppInfo() throws Exception {
    	
        this.mockMvc.perform(get("/subscriptions/info")
                .contentType(contentType)
                .param("id", "testC"))
                .andExpect(status().isOk())
                .andExpect(content().string(Info.INFO_MSG));
    }
    
    @Test
    public void readSubscription() throws Exception {
    	
    	this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "testC")
                .param("messageType", "typeC"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeC\":0}}"));
    	
        this.mockMvc.perform(get("/subscriptions/read")
                .contentType(contentType)
                .param("id", "testC"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeC\":0}}"));
    }
    
    @Test
    public void readSubscriptionNotFound() throws Exception {
    	
        this.mockMvc.perform(get("/subscriptions/read")
                .contentType(contentType)
                .param("id", "testZ"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Subscription doesn't exist"));
    }

    @Test
    public void sendMessage() throws Exception {
    	Message message = new Message();
    	message.setMessageType("typeA");
    	message.setContent("contentA");
    	
        String messageJson = json(message);
        this.mockMvc.perform(post("/messages/send")
                .contentType(contentType)
                .content(messageJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Message received"));
    }

    @Test
    public void sendInvalidMessage() throws Exception {
    	Message message = new Message();
    	message.setMessageType(null);
    	message.setContent("contentA");
    	
        String messageJson = json(message);
        
        this.mockMvc.perform(post("/messages/send")
                .contentType(contentType)
                .content(messageJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Message and message type are mandatory"));
    }
    
    @Test
    public void readSubscriptionWithMessage() throws Exception {
    	
    	this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "testM")
                .param("messageType", "typeM"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeM\":0}}"));

    	Message message = new Message();
    	message.setMessageType("typeM");
    	message.setContent("contentM");
    	
        String messageJson = json(message);
        this.mockMvc.perform(post("/messages/send")
                .contentType(contentType)
                .content(messageJson))
                .andExpect(status().isCreated());
        
        this.mockMvc.perform(get("/subscriptions/read")
                .contentType(contentType)
                .param("id", "testM"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeM\":1}}"));
    }
    
    @Test
    public void deleteSubscription() throws Exception {
    	
    	this.mockMvc.perform(post("/subscriptions/create")
                .contentType(contentType)
                .param("id", "testK")
                .param("messageType", "typeK"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"numberOfMessagesByType\":{\"typeK\":0}}"));
    	
        this.mockMvc.perform(delete("/subscriptions/delete")
                .contentType(contentType)
                .param("id", "testK"))
                .andExpect(status().isOk())
                .andExpect(content().string("Subscription removed"));
    }
    
    @Test
    public void deleteUnknownSubscription() throws Exception {
    	
        this.mockMvc.perform(delete("/subscriptions/delete")
                .contentType(contentType)
                .param("id", "testQ"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Subscription doesn't exist"));
    }
    
    protected String json(Object object) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                object,
                MediaType.APPLICATION_JSON,
                mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
