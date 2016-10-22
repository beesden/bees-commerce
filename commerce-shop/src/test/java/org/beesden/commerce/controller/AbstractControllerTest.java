package org.beesden.commerce.controller;

import org.junit.Assert;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractControllerTest {

	MediaType contentType = new MediaType( MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName( "utf8" ) );

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	String json( Object o ) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write( o, MediaType.APPLICATION_JSON, mockHttpOutputMessage );
		return mockHttpOutputMessage.getBodyAsString();
	}

	@Autowired
	void setConverters( HttpMessageConverter<?>[] converters ) {

		this.mappingJackson2HttpMessageConverter = Arrays.stream( converters ).filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter ).findAny().get();

		Assert.assertNotNull( "the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter );
	}

	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup( webApplicationContext ).build();
	}
}