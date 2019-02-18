package com.hzb.config;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

/**
 * @author keven
 * @date 2018-08-29 下午11:51
 * @Description
 */
@SuppressWarnings("deprecation")
@EnableWebMvc
@EnableAutoConfiguration
@EnableScheduling
@Configuration
@ComponentScan({ "com.ideveloperx.smartcall" })
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Primary
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}

	/**
	 * 解决内置的 tomcat 上传文件限制问题
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(2024L * 2024L);
		return factory.createMultipartConfig();
	}

	@Bean
	public ServletRegistrationBean<DispatcherServlet> dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<DispatcherServlet>(
				dispatcherServlet);
		registration.getUrlMappings().clear();
		registration.addUrlMappings("*.do");
		registration.addUrlMappings("/*");
		return registration;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/music/**").addResourceLocations("classpath:/static/music/");
		registry.addResourceHandler("/image1/**").addResourceLocations("classpath:/static/images/image1/");
		registry.addResourceHandler("/image2/**").addResourceLocations("classpath:/static/images/image2/");
		registry.addResourceHandler("/image3/**").addResourceLocations("classpath:/static/images/image3/");
		registry.addResourceHandler("/image4/**").addResourceLocations("classpath:/static/images/image4/");
		registry.addResourceHandler("/js3/**").addResourceLocations("classpath:/static/js/js3/");
		registry.addResourceHandler("/css4/**").addResourceLocations("classpath:/static/css/css4/");
		super.addResourceHandlers(registry);
	}

	@Primary
	@Bean
	public CommonsMultipartResolver getCommonsMultipartResolver() {
		return new CommonsMultipartResolver();
	}

	/**
	 * 解决 response 返回中文 乱码问题
	 */
	@Bean
	public HttpMessageConverter<String> responseBodyConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setDefaultCharset(Charset.forName("UTF-8"));
		List<MediaType> mediaTypes = Lists.newArrayList();
		mediaTypes.add(MediaType.ALL);
		converter.setSupportedMediaTypes(mediaTypes);
		return new StringHttpMessageConverter(Charset.forName("UTF-8"));
	}

	@Bean
	public MappingJackson2HttpMessageConverter messageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(getObjectMapper());
		return converter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.add(responseBodyConverter());
		converters.add(messageConverter());
	}

}
