package io.crstudio.tutor

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class RedisTest {
    /**
     * Somehow, retrieving Long value directly doesn't cause any problems,
     * But allocating to a variable does???
     */
    @Test
    @DisplayName("redis template test?")
    fun testSerializer(
        @Autowired
        connectionFactory: RedisConnectionFactory
    ) {
        val template = RedisTemplate<String, Long>()
        template.connectionFactory = connectionFactory
        template.setDefaultSerializer(RedisSerializer.json())
        template.afterPropertiesSet()
        val ops = template.opsForValue()
        ops["asdf"] = 1
        assertDoesNotThrow {
            println(ops["asdf"])
        }
        assertThrows<Exception> {
            val result = ops["asdf"]
            println(result)
        }
    }

    @AfterEach
    fun cleanUp(
        @Autowired
        connectionFactory: RedisConnectionFactory
    ) {
        val template = RedisTemplate<String, Long>()
        template.connectionFactory = connectionFactory
        template.setDefaultSerializer(RedisSerializer.json())
        template.afterPropertiesSet()
        val ops = template.opsForValue()
        ops.operations.delete("asdf")
    }
}
