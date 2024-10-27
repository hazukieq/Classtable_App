// java is a js-native bridge,when it dose not exists,we can use this
if(window.java==undefined||window.java===null||window.java=="") window.java={
    ijbridge:(msgs)=>{
        console.log(msgs)
        //const msg={command:"vert2Png",params:mgs}
        //java.ijbridge(JSON.stringify(msg))
        return msgs
    },
    direader:(tile,type)=>{
        console.log("read contents from "+tile+"<"+type+">")
        return "# hello,world!"
    },
    dipasser:()=>"{\"loadState\":\"exist\",\"tile\":\"默认.md\",\"list\":[\"默认.md\"]}"
}

const getFileName=()=>window.sideEl.context.context.export();

/**
 * 将给定的URI转换为指定格式的文件URI。
 * @param {string} uri 需要转换的URI。
 * @param {string} command 转换命令，如"vert2Png"或"vert2Pdf"。
 * @param {string} extension 文件扩展名，如".png"或".jpg".
 * @returns 转换后的文件URI。
 */
const __convertUri = (uri, command, extension) => {
    if (!uri) {
      throw new Error("URI不能为空");
    }
    // 安全地构建消息参数
    const name = getFileName() + extension;
    const params = JSON.stringify({ name, base64: uri });
    const msg = { command, params };
  
    try {
      // 假设java.ijbridge是一个异步方法，返回一个Promise
      return java.ijbridge(JSON.stringify(msg)).then(response => response);
    } catch (error) {
      java.ijbridge(JSON.stringify({command:'infost',params:`转换过程中发生错误: ${error}`}));
      throw error; // 重新抛出异常，以便调用者可以处理
    }
  }
  
  const __getPngUri = (uri) => __convertUri(uri, "vert2Png", ".png");
  
  const __getJpgUri = (uri) => __convertUri(uri, "vert2Png", ".jpg");
  
  const __getPdfUri = (uri) => __convertUri(uri, "vert2Pdf", ".pdf");




/**
 * 对文件进行写入操作的安全封装。
 * @param {string} file_name - 文件名，将进行安全性检查。
 * @param {string} content - 要写入文件的内容。
 * @throws 会抛出错误，如果文件名不合法或写入操作失败。
 */
const anPutData = (file_name, content) => {
    // 安全性检查：确保文件名不包含路径遍历字符或空格
    if (typeof file_name !== 'string' || file_name.includes('..') || file_name.includes('/') || file_name.includes('\\') || file_name.trim() === '') {
        java.ijbridge(JSON.stringify({command:'infost',params:'错误：文件包含非法字符<'+file_name+'>'}))
        throw new Error('Invalid file name.');
    }

    // 使用let声明提高作用域明确性，优化逻辑表达
    let __name = file_name.length > 0 ? file_name : "";

    const file_args = ['mind', __name, content];
    const args_ = {"cmd": "putData", "args": file_args};

    const msg = {command: "fileInvoker", params: JSON.stringify(args_)};

    // 异常处理：对java.ijbridge调用进行错误捕获
    try {
        java.ijbridge(JSON.stringify(msg));
    } catch (error) {
        // 处理异常，例如：记录日志、向用户反馈错误信息等
        java.ijbridge(JSON.stringify({command:'infost',params:'Failed to write file:'+error}));
        throw error; // 重新抛出异常，确保调用者知晓错误发生
    }
}

const anPutConfig=(content)=> anPutData('.configs',content)

//注册JS当地函数
var __hjb=HJBridgeCmdUtil.getInstance()
__hjb.registerFunc('getPngUri',__getPngUri)
__hjb.registerFunc('getJpgUri',__getJpgUri)
__hjb.registerFunc('getPdfUri',__getPdfUri)
