// String
String.prototype.startsWith = function(prefix) {
    return (this.substr(0, prefix.length) === prefix);
};
 
String.prototype.endsWith = function(suffix) {
    return (this.substr(this.length - suffix.length) === suffix);
};

String.prototype.contains = function(txt) {
    return (this.indexOf(txt) >= 0);
};

/* method 2:
String.prototype.startsWith = function(prefix) {
    return this.lastIndexOf(prefix, 0) === 0;
}
 

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
*/
/* method 3:
String.prototype.startsWith = function(prefix) {
	return this.slice(0, prefix.length) == prefix;
}

String.prototype.endsWith = function(suffix) {
	return this.slice(-suffix.length) == suffix;
}
*/
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");
};
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");
};
// Map
var Map = function() {
	this._entries = new Array();
};
Map.prototype.getEntries = function() {
    return this._entries;
};
Map.prototype.getEntry = function(key) {
	for (var i = 0; i < this._entries.length; i++) {
		var entry = this._entries[i];
		if (key == entry.key) {
			return entry;
		}
	}
	return null;
};
Map.prototype.get = function(key) {
    for (var i = 0; i < this._entries.length; i++) {
        var entry = this._entries[i];
        if (key == entry.key) {
            return entry.value;
        }
    }
    return null;
};
Map.prototype.put = function(key, value) {
    var entry = this.getEntry(key);
    if (entry) {
        entry.value = value;
    } else {
        this._entries.push(new Map.Entry(key, value));
    }
};
Map.prototype.remove = function(key) {
    for (var i = 0; i < this._entries.length; i++) {
        if (key == this._entries[i].key) {
            var entry = this._entries[i];
            this._entries.splice(i, 1);
            return entry;
        }
    }
    return null;
};

Map.Entry = function(key, value) {
	this.key = key;
	this.value = value;
};
Map.Entry.prototype.getKey = function() {
	return this.key;
};
Map.Entry.prototype.getValue = function() {
	return this.value;
};

// 