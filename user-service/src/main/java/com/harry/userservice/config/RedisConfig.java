package com.harry.userservice.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {
    /*
     * Here we need two things -
     * 1 - JedisConnectionFactory that will be used to connect to Redis and
     * 2 - RedisTemplate that will be used to interact with Redis using jedis
     * connection
     * So we'll need these two beans here.
     */

    // Standalone mode jedisConnectionFactory
    /*
     * @Bean
     * public JedisConnectionFactory jedisConnectionFactory() {
     * RedisStandaloneConfiguration redisStandaloneConfiguration = new
     * RedisStandaloneConfiguration();
     * redisStandaloneConfiguration.setHostName("127.0.0.1");
     * redisStandaloneConfiguration.setPort(6379);
     * //redisStandaloneConfiguration.setPassword("password");
     * 
     * JedisConnectionFactory jedisConnectionFactory = new
     * JedisConnectionFactory(redisStandaloneConfiguration);
     * // here we can set attributes for jedisConnectionFactory - like maxPoolSize,
     * timeouts, etc
     * return jedisConnectionFactory;
     * }
     */

    // Cluster mode jedisConnectionFactory
    // m1
    // @Autowired
    // ClusterConfigurationProperties clusterProperties;

    // m2
    // @Bean
    // public RedisClusterConfiguration redisClusterConfiguration() {
    // Map<String, Object> source = new HashMap<>();
    // source.put("spring.redis.cluster.nodes", clusterNodes);
    // source.put("spring.redis.cluster.timeout", timeout);
    // source.put("spring.redis.cluster.max-redirects", maxRedirects);
    // return new RedisClusterConfiguration(new
    // MapPropertySource("RedisClusterConfiguration", source));
    // }

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.username}")
    private String redisUserName;
    @Value("${spring.data.redis.password}")
    private String redisPassword;

    // @Value("${spring.redis.cluster.timeout}")
    // private Long timeout;

    // @Value("${spring.redis.cluster.max-redirects}")
    // private int maxRedirects;

    // @Value("${redis.maxIdle}")
    // private int maxIdle;
    //
    // @Value("${redis.maxTotal}")
    // private int maxTotal;
    //
    // @Value("${redis.maxWaitMillis}")
    // private long maxWaitMillis;
    //
    // @Value("${redis.testOnBorrow}")
    // private boolean testOnBorrow;

    // Connection With Cluster------------
    // @Bean
    // public JedisConnectionFactory jedisCConnectionFactory() {
    // // JedisConnectionFactory jedisConnectionFactory = new
    // JedisConnectionFactory(
    // // new RedisClusterConfiguration(clusterProperties.getNodes())); // m1

    // // m3
    // RedisClusterConfiguration redisClusterConfiguration = new
    // RedisClusterConfiguration();
    // redisClusterConfiguration.setClusterNodes(getClusterNodes());
    // redisClusterConfiguration.setMaxRedirects(maxRedirects);

    // JedisConnectionFactory jedisConnectionFactory = new
    // JedisConnectionFactory(redisClusterConfiguration);

    // // here we can set attributes for jedisConnectionFactory - like maxPoolSize,
    // // timeouts, etc

    // return jedisConnectionFactory;
    // }

    // private Iterable<RedisNode> getClusterNodes() {
    // String[] hostAndPorts =
    // StringUtils.commaDelimitedListToStringArray(clusterNodes);
    // Set<RedisNode> clusterNodes = new HashSet<>();
    // for (String hostAndPort : hostAndPorts) {
    // int lastScIndex = hostAndPort.lastIndexOf(":");
    // if (lastScIndex == -1)
    // continue;

    // try {
    // String host = hostAndPort.substring(0, lastScIndex);
    // Integer port = Integer.parseInt(hostAndPort.substring(lastScIndex + 1));
    // clusterNodes.add(new RedisNode(host, port));
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    // return clusterNodes;
    // }
    // Connection With Lettuce------------
    // @Bean
    // public LettuceConnectionFactory lettuceConnectionFactory() {
    // RedisStandaloneConfiguration redisStandaloneConfiguration = new
    // RedisStandaloneConfiguration();
    // redisStandaloneConfiguration.setHostName("localhost");
    // redisStandaloneConfiguration.setPort(6379);
    // redisStandaloneConfiguration.setDatabase(0);
    // redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));

    // LettuceConnectionFactory lcf = new
    // LettuceConnectionFactory(redisStandaloneConfiguration);
    // lcf.afterPropertiesSet();
    // return lcf;
    // }

    // https://github.com/huangjian888/jeeweb-mybatis-springboot/blob/v2.0-master/jeeweb-common-core/src/main/java/cn/jeeweb/core/config/RedisConfig.java#L126
    // https://github.com/huangjian888/jeeweb-mybatis-springboot/search?p=2&q=RedisTemplate

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        // redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setUsername(redisUserName);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
        // JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

        JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(60));// 60s
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());

        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}