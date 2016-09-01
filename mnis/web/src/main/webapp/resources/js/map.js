/*  
002  * MAP对象，实现MAP功能  
003  *  
004  * 接口：  
005  * size()     获取MAP元素个数  
006  * isEmpty()    判断MAP是否为空  
007  * clear()     删除MAP所有元素  
008  * put(key, value)   向MAP中增加元素（key, value)   
009  * remove(key)    删除指定KEY的元素，成功返回True，失败返回False  
010  * get(key)    获取指定KEY的元素值VALUE，失败返回NULL  
011  * element(index)   获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL  
012  * containsKey(key)  判断MAP中是否含有指定KEY的元素  
013  * containsValue(value) 判断MAP中是否含有指定VALUE的元素  
014  * values()    获取MAP中所有VALUE的数组（ARRAY）  
015  * keys()     获取MAP中所有KEY的数组（ARRAY）  
016  *  
017  * 例子：  
018  * var map = new Map();  
019  *  
020  * map.put("key", "value");  
021  * var val = map.get("key")  
022  * ……  
023  *  
024  */ 

function Map() {  
    this.elements = new Array();  
    //获取MAP元素个数  
    this.size = function() {  
        return this.elements.length;  
    }  

    //判断MAP是否为空  
    this.isEmpty = function() {  
        return (this.elements.length < 1);  
    }  

    //删除MAP所有元素  
    this.clear = function() {  
        this.elements = new Array();  
    }  

    //向MAP中增加元素（key, value)   
    this.put = function(_key, _value) {  
        this.elements.push( {  
            key : _key,  
            value : _value  
        });  
    }  

    //删除指定KEY的元素，成功返回True，失败返回False  
    this.remove = function(_key) {  
        var bln = false;  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].key == _key) {  
                    this.elements.splice(i, 1);  
                    return true;  
                }  
            }  
        } catch (e) {  
            bln = false;  
        }  
        return bln;  
    }  
   
    //获取指定KEY的元素值VALUE，失败返回NULL  
    this.get = function(_key) {  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].key == _key) {  
                    return this.elements[i].value;  
                }  
            }  
        } catch (e) {  
            return null;  
        }  
    }  

    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL  
    this.element = function(_index) {  
        if (_index < 0 || _index >= this.elements.length) {  
            return null;  
        }  
        return this.elements[_index];  
    }  

    //判断MAP中是否含有指定KEY的元素  
    this.containsKey = function(_key) {  
        var bln = false;  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].key == _key) {  
                    bln = true;  
                }  
            }  
        } catch (e) {  
            bln = false;  
        }  
        return bln;  
    }  

    //判断MAP中是否含有指定VALUE的元素  
    this.containsValue = function(_value) {  
        var bln = false;  
        try {  
            for (i = 0; i < this.elements.length; i++) {  
                if (this.elements[i].value == _value) {  
                    bln = true;  
                }  
            }  
        } catch (e) {  
            bln = false;  
        }  
        return bln;  
    }  

    //获取MAP中所有VALUE的数组（ARRAY）  
    this.values = function() {  
        var arr = new Array();  
        for (i = 0; i < this.elements.length; i++) {  
            arr.push(this.elements[i].value);  
        }  
        return arr;  
    }  

    //获取MAP中所有KEY的数组（ARRAY）  
    this.keys = function() {  
        var arr = new Array();  
        for (i = 0; i < this.elements.length; i++) {  
            arr.push(this.elements[i].key);  
        }  
        return arr;  
    }  
} 