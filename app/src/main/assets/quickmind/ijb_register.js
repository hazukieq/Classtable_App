const __getPngUri=(uri)=>{
    const msg={command:"vert2Png",params:"{\"name\":\"hello\",\"base64\":\""+uri+"\"}"}
    //console.log(`getPngData from currentPage:\n${uri}`)
    //window.java.down(uri)
    java.ijbridge(JSON.stringify(msg))
}

const __getPdfUri=(uri)=>{
    const msg={command:"vert2Pdf",params:"{\"name\":\"hello\",\"base64\":\""+uri+"\"}"}
    //console.log(`getPngData from currentPage:\n${uri}`)
    //window.java.down2Pdf(uri)
    java.ijbridge(JSON.stringify(msg))
}

//注册JS当地函数
var __hjb=HJBridgeCmdUtil.getInstance()
__hjb.registerFunc('getPngUri',__getPngUri)
__hjb.registerFunc('getPdfUri',__getPdfUri)