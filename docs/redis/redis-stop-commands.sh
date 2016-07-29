#!/bin/bash
redis-cli -p 6399 shutdown &
redis-cli -p 6389 shutdown &
redis-cli -p 6379 shutdown &
