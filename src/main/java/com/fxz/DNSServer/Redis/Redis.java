package com.fxz.DNSServer.Redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fxz.DNSServer.Utils.Params;

@Service
public class Redis {
	@Autowired
	RedisTemplate redisTemplate;
	Logger logger = Logger.getLogger(Redis.class);

	public String getKey(String key) {
		String rez = (String) redisTemplate.boundValueOps(key).get();
		logger.info("redis get key->" + key + "  value->" + rez);
		return rez;
	}

	public boolean setKey(String key, String value, int expr) {
		redisTemplate.boundValueOps(key).set(value);
		if (expr > 0) {
			redisTemplate.expire(key, expr, TimeUnit.MINUTES);
		}
		logger.info("redis set key->" + key + "  value->" + value + "  expr->" + expr + " m");
		return true;
	}

	public Set<String> keys(String patten) {
		return redisTemplate.keys(patten);
	}

	public int dbSize() {
		return redisTemplate.keys("*").size();
	}

	public boolean setKey(String key, String value) {
		redisTemplate.boundValueOps(key).set(value);
		redisTemplate.expire(key, Params.getDnsConfig().getLivetime(), TimeUnit.MINUTES);
		logger.info("redis set key->" + key + "  value->" + value + "  expr->" + Params.getDnsConfig().getLivetime() + " m");
		return true;
	}
}
