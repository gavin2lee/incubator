
function JsPlugin(){
	this.plugins = {};
}

_extend(JsPlugin,{
	loadPlugin:function(url,fn){
		_loadScript(url,fn);
	},
	plugin:function(key,fn){
		this.plugins[key] = fn;
	},
	getPlugin:function(key){
		return this.plugins[key];
	},
	doPlugin:function(key,params){
		this.plugins[key](params);
	}
	
});
var jsPlugin = new JsPlugin;

function _loadScript(url, fn) {
	var _QUIRKS = document.compatMode != 'CSS1Compat';
    var head = document.getElementsByTagName('head')[0] || (_QUIRKS ? document.body : document.documentElement),
	script = document.createElement('script');
    head.appendChild(script);
    script.src = url;
    script.charset = 'utf-8';
    script.onload = script.onreadystatechange = function () {
        if (!this.readyState || this.readyState === 'loaded') {
            if (fn) {
                fn();
            }
            script.onload = script.onreadystatechange = null;
            head.removeChild(script);
        }
    };
}
