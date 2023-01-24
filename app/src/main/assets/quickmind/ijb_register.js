const __getPngUri=(uri)=>{
    const name=SideBar.getHexport()+'.png'
    const msg={command:"vert2Png",params:"{\"name\":\""+name+"\",\"base64\":\""+uri+"\"}"}

    java.ijbridge(JSON.stringify(msg))
}

const __getJpgUri=(uri)=>{
    const name=SideBar.getHexport()+'.jpg'
    const msg={command:"vert2Png",params:"{\"name\":\""+name+"\",\"base64\":\""+uri+"\"}"}

    java.ijbridge(JSON.stringify(msg))
}

const __getPdfUri=(uri)=>{
    const name=SideBar.getHexport()
    const msg={command:"vert2Pdf",params:"{\"name\":\""+name+"\",\"base64\":\""+uri+"\"}"}

    java.ijbridge(JSON.stringify(msg))
}


//删除某个文件
//args格式：根目录名，文件名
//例如：args:["mind","gello","hello,world"]
const anDelData=(file_name)=>{
    if(file_name.length>0) var __name=file_name+'.md'
    const test=['mind',__name,'']
    const args_={"cmd":"delData","args":test}
    
    console.log('args='+JSON.stringify(args_))
    const msg={command:"fileInvoker",params:JSON.stringify(args_)}

    java.ijbridge(JSON.stringify(msg))
}


//对文件进行写入操作
//args格式：根目录名，文件名，文件内容
//例如：args:["mind","gello","hello,world"]
const anPutData=(file_name,content)=>{
    if(file_name.length>0) var __name=file_name
    const test=['mind',__name,content]
    const args_={"cmd":"putData","args":test}
    
    console.log('args='+JSON.stringify(args_))
    const msg={command:"fileInvoker",params:JSON.stringify(args_)}

    java.ijbridge(JSON.stringify(msg))
}


//获取某个文件内容
//args格式：根目录名，文件名
//例如：args:["mind","gello","hello,world"]
const anAskData=(file_name)=>{
    if(file_name.length>0) var __name=file_name+'.md'
    const test=['mind',__name,'']
    const args_={"cmd":"askData","args":test}

    console.log('args='+JSON.stringify(args_))
    const msg={command:"fileInvoker",params:JSON.stringify(args_)}

    java.ijbridge(JSON.stringify(msg))
}

//对配置信息文件进行写入操作
//args格式：根目录名，文件名，文件内容
//例如：args:["mind","gello","hello,world"]
const anPutConfig=(file_name,content)=>{
    if(file_name.length>0) var __name=file_name
    const test=['mind',__name,content]
    const args_={"cmd":"putConfigData","args":test}
    
    console.log('args='+JSON.stringify(args_))
    const msg={command:"fileInvoker",params:JSON.stringify(args_)}

    java.ijbridge(JSON.stringify(msg))
}

//获取配置信息
const __getConfigStrs=(strs)=>{
    console.log('received config\'s data from native:'+strs)
}

//获取内容
const __getData=(strs)=>{
    console.log('received data from native:'+strs)
    //return strs
}



//注册JS当地函数
var __hjb=HJBridgeCmdUtil.getInstance()
__hjb.registerFunc('getPngUri',__getPngUri)
__hjb.registerFunc('getJpgUri',__getJpgUri)
__hjb.registerFunc('getPdfUri',__getPdfUri)
__hjb.registerFunc('getData',__getData)
__hjb.registerFunc('getConfig',__getConfigStrs)