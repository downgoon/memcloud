/**
 * @param {Object} holder 可选参数，枚举范围：{top.location, parent.location或location}
 * 默认值是：location
 */
function QS(holder){
    this.qs = {};
	var realHolder = location;//默认Holder
	if(holder) {//如果holder传递了
		realHolder = holder;
	} 
	var s =  realHolder.search.replace( /^\?|#.*$/g, '' );
    if( s ) {
        var qsParts = s.split('&');
        var i, nv;
        for (i = 0; i < qsParts.length; i++) {
            nv = qsParts[i].split('=');
            this.qs[nv[0]] = nv[1];
        }
    }
}

QS.prototype.add = function( name, value ) {
    if( arguments.length == 1 && arguments[0].constructor == Object ) {
        this.addMany( arguments[0] );
        return;
    }
    this.qs[name] = value;
}

QS.prototype.addMany = function( newValues ) {
    for( nv in newValues ) {
        this.qs[nv] = newValues[nv];
    }
}

QS.prototype.remove = function( name ) {
    if( arguments.length == 1 && arguments[0].constructor == Array ) {
        this.removeMany( arguments[0] );
        return;
    }
    delete this.qs[name];
}

QS.prototype.removeMany = function( deleteNames ) {
    var i;
    for( i = 0; i < deleteNames.length; i++ ) {
        delete this.qs[deleteNames[i]];
    }
}

QS.prototype.getQueryString = function() {
    var nv, q = [];
    for( nv in this.qs ) {
        q[q.length] = nv+'='+this.qs[nv];
    }
    return q.join( '&' );
}
/** 
 * 获取指定的参数
 * @param {Object} name
 */
QS.prototype.get = function(name) {
	if( arguments.length == 1 && arguments[0].constructor == Array ) {
        return this.getMany( arguments[0] );
    }
    return this.qs[name];
}

QS.prototype.getMany = function( getNames ) {
	var r = null;
    var i;
    for( i = 0; i < getNames.length; i++ ) {
        r += (this.qs[deleteNames[i]]);
    }
	return r;
}

QS.prototype.toString = QS.prototype.getQueryString;

////examples
////instantiation
//var qs = new QS;
//alert( qs );
//
////add a sinle name/value
//qs.add( 'new', 'true' );
//alert( qs );
//
////add multiple key/values
//qs.add( { x: 'X', y: 'Y' } );
//alert( qs );
//
////remove single key
//qs.remove( 'new' )
//alert( qs );
//
////remove multiple keys
//qs.remove( ['x', 'bogus'] )
//alert( qs );

//examples
//instantiation
//var qs = new QS(top.location);
//alert(qs.get("name"));
//if(!qs.get("name22")) {
//	alert("参数:name22不存在");
//} else {
//	alert(qs.get("name22"));
//}
