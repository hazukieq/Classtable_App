/*兼容旧版本WebView ES语法*/
String.prototype.replaceAll=function(s1,s2){
    return this.replace(new RegExp(s1,'gm'),s2)
}


HTMLElement.prototype.autoclick=function(){
    const el=new MouseEvent('click')
    this.dispatchEvent(el)
}

/**
 * @param {String} id
 * @returns {Object} obj
 */
const $=(id)=>{
    var __id=typeof id=='string'
    if(__id) return document.getElementById(id)
    else console.log('id is not string type,please make sure passed id is correct!')
}

HTMLElement.prototype.$=function(id){
    var __id=typeof id=='string'
    if(__id) return this.getElementById(id.length>0&&id)
    else console.log('id is not string type,please make sure passed id is correct!')
}




HTMLElement.prototype.css=function(prop,value){
        if(value===undefined){
            if(typeof(prop)=='object'){
                for(let key in prop) this.style[key]=prop[key]
            }
            return this
        }else{
            this.style[prop]=value
            return this
        }
    }

HTMLElement.prototype.attr=function(attr,value){
    if(value===undefined){
        if(typeof(attr)=='object'){
            for(let key in attr) this.style[key]=attr[key]
        }
        return this
    }else{
        this.setAttribute(attr,value)
        return this
    }
}

const $css=(prop,args)=>{
    if(prop===undefined||args.length==0) return
    else{
        var __ele=typeof prop==='object'?prop:$(prop)
        const __args=args||{}
        for(let key in __args)  __ele.style[key]=__args[key]
    }
}


const $attr=(id,args)=>{
    if(id===undefined||args.length==0) return
    else{
        var __ele=typeof id==='object'?id:$(id)
        const __args=args||{}

        for(let key in __args){
            __ele.setAttribute(key,__args[key])
            console.log(`element.setAttribute("${key}","${__args[key]}")`)
        }
    }
}


const $eattr=(id,args)=>{
    const _array=[]
    if(id===undefined||args.length==0) return
    else{
        var __ele=typeof id==='object'?id:$(id)
        const __args=args||{}

        for(let key in __args){
            var __attr=__ele.getAttribute(__args[key])
            console.log(`element.getAttribute("${__args[key]}")`)
            _array.push(__attr)
        }
    }
    return _array.length==1?_array[0]:_array
}


HTMLElement.prototype.on=function(args,func){
    if(typeof func=='function'&&typeof args=='string'){
        this.addEventListener(args,(e)=>typeof func=='function'?func(e):console.log('func is not a function!'))
    }
    return this
}

const $on=(id,args,func)=>{
    const __obj=typeof id=='object'?id:$(id)
    __obj.addEventListener(args,(e)=>func(e))
}




HTMLElement.prototype.elect=function(args){
    if(args===undefined||args.length==0) return this
    else{
        return this.querySelector(args)
    }
}

HTMLElement.prototype.electAll=function(args){
    if(args===undefined||args.length==0) return this
    else{
        return this.querySelectorAll(args)
    }
}

const $elect=(clss)=>{
    if(typeof clss=='string'&&clss.length>0)document.querySelector(clss)
    else console.log('make sure the argument is belong to string!')
}
const $electAll=(clss)=>{
    if(typeof clss=='string'&&clss.length>0)clss.length>0&&document.querySelectorAll(clss)
    else console.log('make sure the argument is belong to string!')
}




HTMLElement.prototype.click=function(args,func){
    if(args===undefined){
        if(typeof func=='function') this.setAttribute('onclick',func(this))
        else console.log('func is not a function !')
    }else{
        if(typeof func=='function') this.setAttribute('onclick',func(args))
        else console.log('func is not a function !')
    }

    return this
}

/**
 *
 * @param {String} id or object
 * @param {Function} func(id)
 */
const $click=(id,func)=>{
    var __element=typeof id==='object'?id:$(id)

    if(func!==null&&typeof func=='function')
            __element
                .setAttribute('onclick',`(${func})(this)`)
}




HTMLElement.prototype.neo=function(ele_name,args,attrs){
    var ele_tag_name=(ele_name!=null&&ele_name!=undefined&&ele_name.length>0&&typeof ele_name=='string')?ele_name:'div'
    var __neoele=document.createElement(ele_tag_name)

    var __args=args||{}
    $css(__neoele,__args)

    var __attrs=attrs||{}
    $attr(__neoele,__attrs)

    return this
}

const $neo=(root_ele,ele_name,args,attrs)=>{
    if(root_ele!=undefined&&ele_tag_name==undefined&&args==undefined&&attrs==undefined){
        ele_name=root_ele
        root_ele=undefined
    }

    var ele_tag_name=(ele_name!=undefined&&ele_name.length>0&&typeof ele_name=='string')?ele_name:'div'
    var __neoele=document.createElement(ele_tag_name)

    var __args=args||{}
    $css(__neoele,__args)

    if(root_ele!=undefined){
        var __rootele= typeof root_ele=='object'?root_ele:$(root_ele)
        __rootele.appendChild(__neoele)
    }

    var __attrs=attrs||{}
    $attr(__neoele,__attrs)
    return __neoele
}




HTMLElement.prototype.del=function(prop){
    var __obj=typeof prop=='object'?prop:$(prop)
    this.removeChild(__obj)
    return this
}

HTMLElement.prototype.dels=function(props){
    if(typeof props=='object'&&typeof props[0]=='object'){
        for(let key in props){
            this.removeChild(props[key])
        }
    }

    return this
}

const $del=(root_ele,id)=>{
    var __rootele= typeof root_ele=='object'?root_ele:$(root_ele)
    var __neoele=typeof id=='object'?id:$(id)
    if(__neoele!==null)
    __rootele.removeChild(__neoele)
}





HTMLElement.prototype.drop=function(callback){
    if(typeof callback=='function'){
        this.ondragover=(e)=>e.preventDefault()
        this.ondrop=(e)=>{
            e.preventDefault()
            callback(e)
        }
    }
    return this
}
const $drop=(ele,callback)=>{
    var __ele=typeof ele==='object'?ele:$(ele)
    __ele.ondragover=(e)=>e.preventDefault()
    __ele.ondrop=(e)=>{
        e.preventDefault()
        callback(e)
    }
}






HTMLElement.prototype.switch=function(args,onFunc,offFunc){
    var __check=this.getAttribute('isCheck')
    console.log(__check)
    if(__check==null||__check===undefined) {
        this.setAttribute('isCheck','true')
        onFunc(this,__args)
    }
    const __args=args||{}
    __check==='true'?offFunc(this,__args):onFunc(this,__args)
    this.setAttribute('isCheck',__check==='true'?'false':'true')


    return this
}

/**
 *
 * @param {Object} ele pass an Object or id of Object
 * @param {Object} args {common_data:''}
 * @param {Function} onFunc onFunc(ele,args)
 * @param {Function} offFunc offFunc(ele,args)
 */
const $switch=(ele,args,onFunc,offFunc)=>{
    var __ele=typeof ele==='object'?ele:$(ele)
    var __check=__ele.getAttribute('isCheck')
    console.log(__check)
    if(__check==null||__check===undefined) {
        __ele.setAttribute('isCheck','true')
        onFunc(__ele,__args)
    }
    const __args=args||{}
    __check==='true'?offFunc(__ele,__args):onFunc(__ele,__args)
    __ele.setAttribute('isCheck',__check==='true'?'false':'true')
}




/**
 *
 * @param {String} context
 * @returns {String} url_parameter
 */
const UrlParamer=(context)=>{
    const searchParams=new URLSearchParams(context)
    return {
        /**
         *
         * @param {String} key
         * @param {String} value
         * @returns {String} url_parameter
         */
        put:(key,value)=>searchParams.append(key,value),

        /**
         *
         * @param {String} key
         * @returns {String} url_parameter
         */
        get:(key)=>searchParams.get(key),

        /**
         *
         * @returns {String} whole_url_parameter
         */
        getAll:()=>searchParams.toString(),

        /**
         *
         * @param {String} key
         * @returns {String} url_parameter
         */
        del:(key)=>searchParams.delete(key)
    }
}

/**
 *
 * @param {String} context
 * @returns {Function} func
 */
const getUrlParamer=(context)=>{
    var instance=null
    if(!instance) instance=UrlParamer(context)
    return instance
}


//封装AJax异步请求函数
var timer;
const $ajax=(url,timeout,success_callback,failed_callback)=>{
    var xhr=new XMLHttpRequest()

    var timedout=false;
    timer=setTimeout(()=>{
        console.log('请求终止！请求地址为:'+url)
        xhr.abort();
        failed_callback();
        timedout=true;
    },timeout);

    xhr.onreadystatechange=()=>{
        if(xhr.readyState!=4) return;
        if(timedout) return;
        clearTimeout(timer)
        if(xhr.readyState==4&&xhr.status==200){
            if(xhr.responseText.startsWith('<')){
                console.log(xhr.responseText)
                console.log('验证返回文本失败，已回调失败处理函数！')
                xhr.abort()
                failed_callback()
                return;
            }
            else success_callback(xhr.responseText);

        }else{
            failed_callback();
        }

    };

    xhr.open('GET',url);
    xhr.setRequestHeader('Content-Type','application/json')
    xhr.send();
}


const isLog=true
const Log=(texts)=>isLog?console.log(texts):void 0