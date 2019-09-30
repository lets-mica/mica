-- lua 下标从 1 开始
-- 限流 key
local key = KEYS[1]
-- 限流大小
local max = tonumber(ARGV[1])
-- 超时时间
local ttl = tonumber(ARGV[2])
-- 考虑主从策略和脚本回放机制，这个time由客户端获取传入
local now = tonumber(ARGV[3])
-- 已经过期的时间点
local expired = now - ttl
-- 清除过期的数据,移除指定分数（score）区间内的所有成员
redis.call('zremrangebyscore', key, 0, expired)
-- 获取当前流量大小
local currentLimit = tonumber(redis.call('zcard', key))

local nextLimit = currentLimit + 1
if nextLimit > max then
    -- 达到限流大小 返回 0
    return 0;
else
    -- 没有达到阈值 value + 1
    redis.call("zadd", key, now, now)
    -- 秒为单位设置 key 的生存时间
    redis.call("pexpire", key, ttl)
    return nextLimit
end
