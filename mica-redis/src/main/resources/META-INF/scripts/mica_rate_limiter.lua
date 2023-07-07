-- 开启单命令复制模式
redis.replicate_commands()
-- lua 下标从 1 开始
-- 限流大小
local max = tonumber(ARGV[1])
-- 超时时间
local ttl = tonumber(ARGV[2])
-- 读取 redis 毫秒数
local time = redis.call("TIME")
local now = tonumber(time[1]) * 1000 + math.floor(tonumber(time[2]) / 1000)
-- 已经过期的时间点
local expired = now - ttl
-- 清除过期的数据,移除指定分数（score）区间内的所有成员
redis.call('zremrangebyscore', KEYS[1], 0, expired)
-- 获取当前流量大小
local currentLimit = tonumber(redis.call('zcard', KEYS[1]))

local nextLimit = currentLimit + 1
if nextLimit > max then
    -- 达到限流大小 返回 0
    return 0;
else
    -- 没有达到阈值 value + 1
    redis.call("zadd", KEYS[1], now, now)
    -- 秒为单位设置 key 的生存时间
    redis.call("pexpire", KEYS[1], ttl)
    return nextLimit
end
