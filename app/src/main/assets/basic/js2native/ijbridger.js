class HJBridgeCmdHandler{
    static mMap= null
    static instance= null
    constructor(){
        this.mMap = new Map()
    }

    static getInstance(){
        if(!this.instance) this.instance=new HJBridgeCmdHandler()
        return this.instance
    }

    /**
     * 
     * @param {String} key 
     * @param {Function} func 
     */
    putFunc=(key,func)=>{
            if(this.mMap!==null && typeof func === 'function') {
                if(!this.mMap.has(key)) this.mMap.set(key,func)
            }
        }

        /**
     * 
     * @param {String} key 
     * @param {Function} params 
     */
    execFunc=(key,params)=>{
            if(this.mMap!==null){
                let func=this.mMap.get(key)
                if(typeof func === 'function')func(params)
            }
        }
}

class HJBridgeCmdUtil{
    static __instance=null
    constructor(){
        this.__handler=HJBridgeCmdHandler.getInstance()
    }
    static getInstance(){
        if(!this.__instance) this.__instance=new HJBridgeCmdUtil()
        return this.__instance
    }

    /**
     * 
     * @param {String} key 
     * @param {Function} func 
     */
    registerFunc=(key,func)=>{
        this.__handler.putFunc(key,func)
    }
}

const HJBridgeCmdDispatcher=()=>{
    const __handler = HJBridgeCmdHandler.getInstance()
    const checkCmd = (obj)=> obj!=null&& typeof obj =="string"

    /**
     * 
     * @param {String} jsons 
     */
    const dispatchCmd=(jsons)=>{
                var parsed_obj=JSON.parse(jsons)
                var {command=null,params=""}=parsed_obj
                if(command!=null) __handler.execFunc(command,params)
        }

    return {
        /**
         * 
         * @param {String} jsons 
         * @returns {Function} func
         */
        send:(jsons)=> checkCmd(jsons)?dispatchCmd(jsons):console.log("failed to invoke func!"),
    }
}
/*--end of formal class of jbridge--*/



/*<!---示例-->*/
//test example
//const alertMsg=(msg)=>{
//    console.log(msg)
//}

//注册JS当地函数
//HJBridgeCmdUtil.getInstance().registerFunc('alert',alertMsg)

//Native调用JS函数
//jso="{\"command\":\"alert\",\"params\":\"received msg from native,msg is hello,world\"}"
//HJBridgeCmdDispatcher().send(jso)

//JS调用Native函数使用示例
/*

const msg={command:"infost",params:"{\"messege\":\"hello\"}"}
console.log(JSON.stringify(msg))
const clj=()=>{
    java.ijbridge(JSON.stringify(msg))
}
*/