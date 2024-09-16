/**
 *对话框轮询 
 * @param {String} id 
 */
const requestStatus=(id)=>{    
    if(id==0)window.waitForClick=0
    else if (id==1) {
        window.waitForClick=1
    }
}


//刷新编辑框内容、思维导图渲染
const updateEditorContents=(contents)=>{
    $(edit_area_id).value=contents
    $(edit_fullscreen_btn_id).attr('data-bs-whatever',contents)
    MarkdownDynamic(svg_root_id,$('edit_area'),{height:markmap_height})
}

//update sidebar
const initSide=(args)=>{
    const sideElClazz=new SideBarEl({tile:args.tile,docList:args.list||{}})
    
    window.sideEl={
        context:sideElClazz,
        root:sideElClazz.get()
    }

    $("side").appendChild(sideEl.root)
}


const none_page=
`<div class="p-5"></div>
<div class="d-flex justify-content-center flex-column align-items-center mt-5">
   <h4>未打开文件</h4>
   <button class="btn text-primary mt-4" style="background:white;opacity:.9;letter-spacing:.1rem" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="createFn">创建新文件</button>
   <button class="btn text-primary mt-2 visually-hidden" style="background:white;opacity:.9;letter-spacing:.1rem">导入新文件</button>
   <button class="btn text-primary mt-2" style="background:white;opacity:.9;letter-spacing:.1rem" data-bs-toggle="offcanvas" href="#sidebar" role="button" aria-controls="sidebar">打开旧文件</button>
   <p class="mt-3 text-black-50 ps-3">
   <span class="badge bg-info">提示</span>&nbsp;&nbsp;点击列表项，对文件进行编辑或预览。
   </p>
</div>`
const waitloading_id="waitloading"
const LoadProcessor=(configs)=>{
    const main_content=configs.loadState=='none'?none_page:
        `${Header()}
         ${Editor()}
         ${Preview()}`;

    if(!window.sideEl) initSide(configs)
    const fileContent=configs.getContents()
    setTimeout(()=>{
        $(waitloading_id).innerHTML=main_content
        if(configs.loadState=='exist') updateEditorContents(fileContent)
    },800)
}


//加载页面函数
const openPage=(obj)=> LoadProcessor(obj)

//关闭页面函数
const closePage=()=>{
    if(AnconfigObj.loadState=='exist') {
        AnconfigObj.close()
        anPutData('.configs',AnconfigObj.toJson())
        window.sideEl.context.context.close()
        let transition=1
        let c=setInterval(()=>{
            let main=$(waitloading_id)
            transition=transition>0?(transition-0.1):0
            main.css('opacity',transition)
            if(transition==0){
                console.log(transition)
                main.css('opacity',1).innerHTML=none_page
                clearInterval(c)
            }
        },36)       
    }
}

//自动保存函数
const autosaveFn=()=>{
    if(AnconfigObj.loadState=='exist'){
        const contents=$('edit_area').value
    
        anPutData(AnconfigObj.tile,contents)
        
        anPutConfig(AnconfigObj.toJson())
        java.ijbridge(JSON.stringify({command:'infost',params:"数据保存成功！"}))
    }
}

//另存函数
const anosaveFn=(id)=>{
    if(AnconfigObj.loadState=='exist'){
        $(id).autoclick()
    }
}

//导出函数
const hexportFn=(id)=>{
    if(AnconfigObj.loadState=='exist'){
        $(id).autoclick()
    }
}

/**实际代码执行部分*/
const Anconfigs=java.dipasser('getConfig','mind')//获取配置参数
const AnconfigObj=new Anco(Anconfigs)

openPage(AnconfigObj)
/**实际代码执行部分*/