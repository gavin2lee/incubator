
var sexDictionaryData = '[{"code":"M", "name":"男"},' 
	+ '{"code":"F", "name":"女"},'
	+ '{"code":"U", "name":"未知"},'
	+ '{"code":"O", "name":"其它"}]';
var tendLevelData = '[{"code":"0", "name":"特级"},'
    + '{"code":"1", "name":"一级"},'
    + '{"code":"2", "name":"二级"},'
    + '{"code":"3", "name":"三级"}]';
var chargeTypeData = '[{"code":"0", "name":"自费"},'
    + '{"code":"1", "name":"医保"},'
    + '{"code":"2", "name":"公费"}]';
var ageUnitDictionaryData ='[{"code":"Y", "name":"岁"},'
	+ '{"code":"M", "name":"月"},'
	+ '{"code":"W", "name":"周"},'
	+ '{"code":"D", "name":"天"},'
	+ '{"code":"H", "name":"时"}	]';
	
function obj2Map(obj) {
	var map = new Map();
	for (var i=0; i<obj.length; i++) {
		map.put(obj[i].code, obj[i].name);
	}
	return map;
}

function getTendMap() {
	var obj = eval(tendLevelData);
	return obj2Map(obj);
}

function getSexDictionary() {
	var obj = eval(sexDictionaryData);
	return obj2Map(obj);
}

function getAgeUnitDictionary() {
	var obj = eval(ageUnitDictionaryData);
	return obj2Map(obj);
}
function getChargeTypeDictionary() {
    var obj = eval(chargeTypeData);
    return obj2Map(obj);
}

