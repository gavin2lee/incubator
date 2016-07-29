#!/bin/bash
redis-server /opt/redis/redis.conf &
redis-server /opt/redis/redis-server1.conf --slaveof 127.0.0.1 6379 &
redis-server /opt/redis/redis-server2.conf --slaveof 127.0.0.1 6379 &
