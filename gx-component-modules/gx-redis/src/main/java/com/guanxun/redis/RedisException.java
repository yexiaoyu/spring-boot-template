package com.guanxun.redis;

/**
 * 
 * <b><code>RedisException</code></b>
 * <p>
 * Comment here.
 * </p>
 * <b>Creation Time:</b> 2016年10月12日 下午5:46:26
 * 
 * @author abin.yao
 * @since qlchat 1.0
 */
public class RedisException extends Exception {

    private static final long serialVersionUID = 7115322404472565037L;

    public RedisException() {
        super();
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }

}
