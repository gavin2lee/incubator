--分割字符串
function string_split(str, delimiter)
	if str == nil or str == '' or delimiter == nil then
		return nil
	end
	local result = {}
	for id in (str..delimiter):gmatch("(.-)"..delimiter) do
		if (id ~= nil) then
			print(id)
		end
		table.insert(result, id)
	end
	return result
end

--获取IP(代理IP/真实IP)
function getIp(headers)
	local ip = nil
	local proxyIp = ngx.var.remote_addr
	local realIp = headers["X-Real-IP"]
	if realIp == nil then
		realIp = headers["X-Forwarded-For"]
		if realIp ~=nil then
			local result = string_split(realIp, ",")
			realIp = result[1]
		end
	end
	if realIp == nil then
		ip = proxyIp
	else
		ip = proxyIp .. '/' .. realIp
	end
	return ip
end

--解析auth_address-
function getAuthService(authAD)
	local array = {}
	local result = string_split(authAD, "/")
	local domain = result[1] .. "//" .. result[3]
	array[1] = domain
	
	result = string_split(authAD, domain)
	array[2] = result[2]
	return array
end

local apiId = ngx.var.version_id
local request_method = ngx.var.request_method

local result = getAuthService(ngx.var.auth_address)
local authDomain = result[1]
local authUri = result[2]

local headers = ngx.req.get_headers()
local ip = getIp(headers)
local appId = headers["auth-appid"] or 0
local token = headers["auth-token"] or 0

local argument = "auth-appid=" .. appId .. "&auth-token=" .. token .. "&auth-apiid=" .. apiId .. "&ip=" .. ip
local http = require("resty.http")
local httpc = http.new()

httpc:set_timeout(30000)

local authPath = "/auth/checkToken.do?" .. argument
if authUri ~= nil and authUri ~= "/" then
	authPath = authUri .. authPath  
end
local resp = httpc:request_uri(authDomain, {  
    method = "GET",  
    path = authPath,  
})
-- ngx.say(resp.status)
-- ngx.exit(200)

if not resp then
    httpc:close()  
    return ngx.exit(ngx.HTTP_FORBIDDEN)  
end
--ngx.say(resp.status)
--ngx.say(ngx.status)
if resp.status ~= ngx.HTTP_OK then
    ngx.status = ngx.HTTP_FORBIDDEN
    ngx.say(resp.body)
    httpc:close()
    return ngx.exit(ngx.OK)
else
    httpc:close()
end
