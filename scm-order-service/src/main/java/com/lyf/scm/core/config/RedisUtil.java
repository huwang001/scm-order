package com.lyf.scm.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**
 *
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法
 * 针对所有的List 都是以l开头的方法
 */
@Slf4j
@Service
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
	 * 键,前缀
	 */
    @Value("${scm.redis.key.prefix}")
	private String keyPrefix="";

    /**
     * redis 默认存储时间
     */
    private final static Long DEFAULT_TIME = 3600L;

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(keyPrefix + key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
        	log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(keyPrefix + key,TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){
        try {
            return redisTemplate.hasKey(keyPrefix + key);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    
    /**
     * 删除缓存
     * @param key
     */
    public void del(String key){
    	redisTemplate.delete(keyPrefix + key);
    }

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(keyPrefix + key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(keyPrefix + key, value, DEFAULT_TIME, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(keyPrefix + key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @return
     */
    public long incr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(keyPrefix + key, delta);
    }
    
    /**
     * 递增
     * @param key 键
     * @param delta 增加值
     * @param time 时间(秒)
     * @return 返回增加后的值
     */
    public long incr(String key, long delta, long time){
        if(time < 0){
            throw new RuntimeException("过期时间必须大于0");
        }
		String script = "local s= redis.call('INCRBY',KEYS[1],ARGV[1]); redis.call('EXPIRE',KEYS[1],ARGV[2]); return {s}";
		List<String> vals = new ArrayList<>(2);
		vals.add(delta + "");
		vals.add(time + "");
		List<Long> result = evalScript(key, vals, script);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}else {
			throw new RuntimeException("incr操作失败");
		}
    }

    /**
     * 递减
     * @param key 键
     * @return
     */
    public long decr(String key, long delta){
        if(delta<0){
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(keyPrefix + key, -delta);
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key,String item){
        return redisTemplate.opsForHash().get(keyPrefix + key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(keyPrefix + key);
    }
    
    /**
     * 获取hashKey对应的 hashKeys键的域值
     * @param key
     * @param hashKeys
     * @return
     */
    public List<Object> hfieldsget(String key,List<Object> hashKeys) {
    	return redisTemplate.opsForHash().multiGet(keyPrefix + key, hashKeys);
    }
    
    /**
     * HashSet
     * 注:模型专用,不允许设置过期时间
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String,Object> map){
        try {
            redisTemplate.opsForHash().putAll(keyPrefix+key, map);
            return true;
        } catch (Exception e) {
        	log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String,Object> map, long time){
        try {
            redisTemplate.opsForHash().putAll(keyPrefix + key, map);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
        	log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * 注:模型专用,不允许设置过期时间
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value) {
        try {
            redisTemplate.opsForHash().put(keyPrefix+key, item, value);
            return true;
        } catch (Exception e) {
        	log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key,String item,Object value,long time) {
        try {
            redisTemplate.opsForHash().put(keyPrefix + key, item, value);
            if(time>0){
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item){
        redisTemplate.opsForHash().delete(keyPrefix + key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
        return redisTemplate.opsForHash().hasKey(keyPrefix + key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(keyPrefix + key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item,double by){
        return redisTemplate.opsForHash().increment(keyPrefix + key, item,-by);
    }

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(keyPrefix + key);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(keyPrefix + key, value);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object...values) {
        try {
            Long count = redisTemplate.opsForSet().add(keyPrefix + key, values);
            if(DEFAULT_TIME > 0){
                expire(keyPrefix + key, DEFAULT_TIME);
            }
            return  count;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key 键
     * @param time 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key,long time,Object...values) {
        try {
            Long count = redisTemplate.opsForSet().add(keyPrefix + key, values);
            if(time>0) {expire(key, time);}
            return count;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(keyPrefix + key);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(keyPrefix + key, values);
            return count;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return 0;
        }
    }
    
    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key,long start, long end){
        try {
            return redisTemplate.opsForList().range(keyPrefix + key, start, end);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(keyPrefix + key);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            return redisTemplate.opsForList().index(keyPrefix + key, index);
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(keyPrefix + key, value);
            if (DEFAULT_TIME > 0) {expire(keyPrefix + key, DEFAULT_TIME);}
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(keyPrefix + key, value);
            if (time > 0) {expire(key, time);}
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(keyPrefix + key, value);
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @param time 时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(keyPrefix + key, value);
            if (time > 0) {expire(key, time);}
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index,Object value) {
        try {
            redisTemplate.opsForList().set(keyPrefix + key, index, value);
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key,long count,Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(keyPrefix + key, count, value);
            return remove;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return 0;
        }
    }
    
    /**
     * 将list 从左边取出一个元素并删除
     * 
     * @param key 键
     * @return
     */
    public Object lPop(String key) {
    	return redisTemplate.opsForList().leftPop(keyPrefix+key);
    }
    
    /**
     * 将list放入缓存,从右面
     * 注:模型专用,不允许设置过期时间
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lRightPushSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(keyPrefix+key, value);
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }
    
    /**
     * 将list 从右边取出一个元素并删除,添加到另一个key中左边
     * 
     * @param key 键
     * @param otherKey 键
     * @return
     */
    public Object lRPopLPush(String key, String otherKey) {
    	return redisTemplate.opsForList().rightPopAndLeftPush(keyPrefix+key, keyPrefix+otherKey);
    }
    
    /**
     * 将list放入缓存,从左面
     * 注:模型专用,不允许设置过期时间
     * @param key 键
     * @param value 值
     * @return
     */
    public boolean lLeftPushSet(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(keyPrefix+key, value);
            return true;
        } catch (Exception e) {
           log.error("redis执行命令失败", e);
            return false;
        }
    }
    
    /**
     * 判断某个键是否存在
     * @param key 键
     * @return 
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(keyPrefix+key);

    }
    
    /**
     * 锁,获取（存在死锁风险）
     * 说明,对于做幂等需求的建议用这个方法
     * @param lockKey
     * @param clientId
     * @param time 秒
     * @return
     */
    public boolean tryLock(String lockKey, String clientId, long time) {
    	if(time < 0) {
    		return false;
    	}
    	return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set((keyPrefix+lockKey).getBytes(), clientId.getBytes(), Expiration.from(time, TimeUnit.SECONDS), SetOption.SET_IF_ABSENT));
    }
    
    private static final String DELIMITER = "|";
    
    private static final Long RELEASE_SUCCESS = 1L;
    
    /**
     * 锁,获取
     * 说明,
     * @param lockKey
     * @param clientId
     * @param time 秒
     * @return
     */
    public boolean lock(String lockKey, final String clientId, long time) {
        final long milliseconds = time * 1000L;
        boolean success = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set((keyPrefix+lockKey).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(), Expiration.from(time, TimeUnit.SECONDS), SetOption.SET_IF_ABSENT));
        if (!success) {
            Object oldVal = redisTemplate.opsForValue().get(keyPrefix+lockKey);
//          Object oldVal = redisTemplate.opsForValue().getAndSet(keyPrefix+lockKey, (System.currentTimeMillis() + milliseconds) + DELIMITER + clientId);
            if(oldVal == null) {
                return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set((keyPrefix+lockKey).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(), Expiration.from(time, TimeUnit.SECONDS), SetOption.SET_IF_ABSENT));
            }
            final String[] oldValues = oldVal.toString().split(Pattern.quote(DELIMITER));
            if (Long.parseLong(oldValues[0]) + 1 <= System.currentTimeMillis()) {
                del(lockKey);
                success = redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.set((keyPrefix+lockKey).getBytes(), ((System.currentTimeMillis() + milliseconds) + DELIMITER + clientId).getBytes(), Expiration.from(time, TimeUnit.SECONDS), SetOption.SET_IF_ABSENT));
                return success;
            }
        }
        return success;
    }

    /**
     * 释放锁
     * 没有严格要求del就可以了
     * @param lockKey
     * @param clientId
     * @return
     */
    public boolean unLock(String lockKey, String clientId) {
    	String script ="if redis.call('EXISTS',KEYS[1]) == 1 then " + 
         "if string.sub(redis.call('get', KEYS[1]),ARGV[2]) == ARGV[1] then return {redis.call('del', KEYS[1])} else return {0} end " +
    	 "else return {0} end";
    	List<String> vals = new ArrayList<>(2);
    	vals.add(clientId);
    	vals.add((-clientId.length()) + "");
    	List<Long> result = evalScript(lockKey, vals, script);
    	Long result2 = null;
    	if(result != null && result.size() > 0) {
    		result2 = result.get(0);
    	}
    	if (RELEASE_SUCCESS.equals(result2)) {
            return true;
        }
        return false;
    }
    
    /**
     * 执行脚本
     */
    @SuppressWarnings("rawtypes")
	private static Map<String, RedisScript> mapScript=new ConcurrentHashMap<>();
    
    /**
     * 执行脚本返回List<Long> 保证原子性
     * @param key
     * @param values
     * @param script
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Long> evalScript(String key,List<String> values,String script){
    	DefaultRedisScript<List> rs =(DefaultRedisScript<List>)mapScript.get(script);
    	if(rs==null) {
    		rs = new DefaultRedisScript<List>();
            //设置脚本
            rs.setScriptText(script);
            rs.setResultType(List.class);
            mapScript.put(script,rs);
    	}
    	Object[] params = new Object[values.size()];
        int argCount = values.size();
        for (int i = 0; i < argCount; i++) {
            params[i] = values.get(i);
        }
        List object=redisTemplate.execute(rs, Collections.singletonList(keyPrefix + key), params);
        if(object!=null&&(object instanceof List)&&object.size()>0) {
        	List<Long> result=new ArrayList<Long>();
        	for(Object o:object) {
        		if(o==null) {
    				result.add(null);
    			}else if(o instanceof Long) {
    				result.add((Long)o);
    			}else if("".equals(o.toString().trim())){
    				result.add(null);
    			}else {
    				result.add(Long.valueOf(o.toString().trim()));
    			}
        	}
        	return result;
        }
    	return null;
    }
    
    /**
     * 执行脚本返回List<Object> 保证原子性
     * @param key
     * @param values
     * @param script
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Object> evalScriptObject(String key,List<String> values,String script){
    	DefaultRedisScript<List> rs =(DefaultRedisScript<List>)mapScript.get(script);
    	if(rs==null) {
    		rs = new DefaultRedisScript<List>();
            //设置脚本
            rs.setScriptText(script);
            rs.setResultType(List.class);
            mapScript.put(script,rs);
    	}
    	Object[] params = new Object[values.size()];
        int argCount = values.size();
        for (int i = 0; i < argCount; i++) {
            params[i] = values.get(i);
        }
        return redisTemplate.execute(rs, Collections.singletonList(keyPrefix + key), params);
    }

}
