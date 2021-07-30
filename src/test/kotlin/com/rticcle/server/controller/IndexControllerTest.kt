package com.rticcle.server.controller

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import org.assertj.core.api.Assertions.assertThat

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun is_main_page_works_well() {
        // When
        val response: String = restTemplate.getForObject("/", String::class.java)

        // Then
        assertThat(response).contains("login test")
    }
}