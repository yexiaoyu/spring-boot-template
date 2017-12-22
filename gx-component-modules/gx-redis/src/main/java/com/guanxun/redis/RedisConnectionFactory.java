package com.guanxun.redis;

import org.apache.commons.lang.*;
import org.slf4j.*;
import org.springframework.beans.factory.*;
import redis.clients.jedis.*;

import java.util.*;

/**
 * 
 * <b><code>RedisConnectionFactory</code></b>
 * <p>
 * redis 连接器
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:46:02
 * 
 * @author abin.yao
 * @since qlchat 1.0
 */
public class RedisConnectionFactory implements InitializingBean, DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(RedisConnectionFactory.class);

    // 格式为： 127.0.0.1:6379,10.1.2.242:6379 多个之间用逗号分隔
    // 对于server的权重目前没有需求，统一用默认值
    protected String servers;

    private String password;

    private int timeout = 3000; // 3seconds

    private boolean usePool = true;

    protected ShardedJedisPool pool;

    private JedisPoolConfig poolConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>();
        for (String server : servers.split("[,]")) {
            String[] sa = server.split("[:]");
            if (sa.length == 2) {
                String host = sa[0];
                int port = Integer.parseInt(sa[1]);
                JedisShardInfo info = new JedisShardInfo(host, port, timeout);
                if (!StringUtils.isBlank(password)) {
                    info.setPassword(password);
                }
                shardInfoList.add(info);
            }
        }
        pool = new ShardedJedisPool(poolConfig, shardInfoList);
    }

    public ShardedJedisPool getConnectionPool() {
        if (pool == null) {
            try {
                afterPropertiesSet();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return this.pool;
    }

    @Override
    public void destroy() throws Exception {
        if (usePool && pool != null) {
            try {
                pool.destroy();
            } catch (Exception ex) {
                logger.warn("Cannot properly close Jedis pool", ex);
            }
            pool = null;
        }
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isUsePool() {
        return usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
