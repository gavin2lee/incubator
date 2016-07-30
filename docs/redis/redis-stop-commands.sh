#!/bin/bash
redis-cli -p 6399 shutdown &
redis-cli -p 6389 -a abcdef shutdown &
redis-cli -p 6379 -a 123456 shutdown &
