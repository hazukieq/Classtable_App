//静态资源组
const hint_control_id='hint_control'
const edit_area_id='edit_area'
const edit_area_hint='请在此输入内容...'
const resizable_area_id='resizable_area'
const edit_area_root_id='edit_area_root'
const svg_root_id='svg_root'
const svg_toolbar_id='svg_toolbar'
const edit_fullscreen_btn_id="edit_fullscreen_btn"
const FullScreenEditor_id="fullscreen_editor"
const fullscreen_edit_id='fullscreen_edit'
const input_modal_id='input_modal'
const help_modal_id='helpModal'

const markmap_height=window.screen.availHeight*6/10
const markmap_tool_height=45
const fullscreen_height=window.screen.availHeight-120

const grammar_help='语法帮助'
const preview_mode_txt='预览模式'
const edit_mode_txt='编辑模式'
const non_contents='暂无内容显示'
//静态资源组


//切换显示模型开关
const switchControl=(switch_id)=>{
    $switch(switch_id,{controlEle:$(edit_area_root_id),hintEle:$(hint_control_id)},
    (_ele,args)=>{
        args.controlEle.css('display','none')
        args.hintEle.innerHTML=preview_mode_txt
    },
    (_ele,args)=>{
        args.controlEle.css('display','block')
        args.hintEle.innerHTML=edit_mode_txt
    })
}

const previeModeSwitch=()=>
    `<div class="form-check form-switch">
        <input class="form-check-input" type="checkbox" id="flexswitch" onclick="switchControl(this)" isCheck="false">
        <label class="form-check-label" for="flexswitch" id="${hint_control_id}">${edit_mode_txt}</label>
    </div>`

const Header=()=>
    `<div class="d-flex justify-content-end align-items-center">
        ${previeModeSwitch()}
    </div>`



//文本清空
const textClear=(obj)=>{
    $(obj).value=''
    $(svg_root_id).innerHTML=`<div class="text-center pt-5"><h4>${non_contents}</h4></div>`
    $(svg_toolbar_id).innerHTML=''
    $(edit_fullscreen_btn_id).attr('data-bs-whatever','')

}

//调整编辑框高度
const confirmInput=()=>{
    var h=$('input_content').value
    if(h!==null&&typeof parseFloat(h) =='number') $(edit_area_id).css('height',h>48?h+'px':'48px')
}

const inputModal=()=>{
    const modal=
    `<!-- Modal -->
    <div class="modal fade" id="${input_modal_id}" tabindex="-1" aria-labelledby="${input_modal_id}" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <h5 class="center-text">高度调整</h5>
                    <div class="pt-1 mt-1">
                        <p class="text-primary ps-2" style="font-size:14px">最少高度为48,不能低于48,否则不会生效!</p>
                        <div class="input-group mb-3">
                            <input type="number" class="form-control" aria-describedby="input_name" id="input_content">
                        </div>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
                        <button type="button" class="btn text-primary" data-bs-dismiss="modal" onclick="confirmInput()">确定</button>
                </div>
                </div>
            </div>
        </div>
    </div>`
    return modal
}

//编辑框
const Editor=()=>
`
<!--编辑框(包括按钮)-->
<div class="row mb-3" id="edit_area_root">
    <div class="col-12 p-1">
    
    <button class="btn btn-white text-primary float-start" data-bs-toggle="modal" data-bs-target="#${FullScreenEditor_id}" id="${edit_fullscreen_btn_id}" data-bs-whatever="">
        <img src="./icons/fullscreen.svg" width="18" height="18"></img>
    </button>
    
    <button class="btn text-center text-info" data-bs-toggle="modal" data-bs-target="#${input_modal_id}">
        <img src="./icons/expand.svg" width="18" height="18"></img>
    </button>
    
    <button class="btn btn-white text-danger float-end" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="askFn">
        <img src="./icons/trash.svg" width="18" height="18"></img>
    </button>
    </div>
    <div class="mt-1 col-12">
        <textarea style="word-break:break-all;" class="form-control border-0" placeholder="${edit_area_hint}" style="caret-color:#007AFF;resize:none;height:96px" id="${edit_area_id}" rows="2" oninput="inputListener(this)"></textarea>
    </div>
</div>`

//编辑内容时同步写入更新
var timeoutID
const inputListener=(ele)=>{    
    clearTimeout(timeoutID)
    
    timeoutID=
    setTimeout(
    ()=>{
        $(edit_fullscreen_btn_id).attr('data-bs-whatever',ele.value)
        _renderMarkmap()
    },
    640)
}


const _renderMarkmap=()=>{
    let ele=$('edit_area')
    MarkdownDynamic(svg_root_id,ele,{height:markmap_height})
        
    //写入文件中
    anPutData(AnconfigObj.tile,ele.value)
}


//全屏编辑
const confirmEdit=()=>{
    var contentz=$(fullscreen_edit_id).value
    updateEditorContents(contentz)
    MarkdownDynamic(svg_root_id,$(edit_area_id),{height:markmap_height})    
    
    //写入文件中
    anPutData(AnconfigObj.tile,contentz)
}

const FullScreenEditor=()=>{
    const modal=
    `<!-- Modal -->
    <div class="modal fade" id="${FullScreenEditor_id}" tabindex="-1" aria-labelledby="${FullScreenEditor_id}" aria-hidden="true">
        <div class="modal-dialog modal-fullscreen">
            <div class="modal-content">
                <div class="modal-body">
                    <!--pt-4=>24px-->
                    <div class="pt-4"></div>
                    <div class="d-flex justify-content-between align-items-center" style="36px">
                        <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
                        <h4 class="modal-title" id="exampleModalLabel">编辑区</h4>
                        <button type="button" class="btn text-primary" data-bs-dismiss="modal" onclick="confirmEdit()">确定</button>
                    </div>
                    <div class="pt-1 mt-1 border-1 border-top d-flex align-items-start">
                    <div style="margin-top:2px;width:1px;height:${fullscreen_height}px;background-color:gray;opacity:.5;"></div>
                    <textarea class="form-control border-0" style="word-break:break-all;caret-color:#007AFF;resize:none;width:100%;height:${fullscreen_height}px" placeholder="${edit_area_hint}" id="${fullscreen_edit_id}"></textarea>
                    </div>
                </div>
            </div>
        </div>
    </div>`
    return modal
}


//全屏编辑对话框监听
const addEven=()=>{
    var fullModal = $(FullScreenEditor_id)
    fullModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget

        // Extract info from data-bs-* attributes
        var recipient = button.getAttribute('data-bs-whatever')

        var modalEdit = fullModal.elect('.form-control')
        modalEdit.value = recipient
})
}


//思维导图预览
const Preview=()=>
    `<!--主界面数据载入区-->
    <div class="row mt-1 p-1" id="mmp_root">
        <div class="col p-0" id="mmp">
            <!--js载入预处理数据-->
            <div class="p-1 bg-white rounded-top" id="svg_root_toolbar" style="height:${markmap_tool_height}px"></div>
           <div id="svg_root" class="bg-white rounded-bottom" style="width: 100%;height:${markmap_height}px;">
                <div class="text-center pt-5"><h4>暂无内容显示</h4></div>
           </div>
        </div>
    </div>`


//定制化对话框，可加载不同布局及响应不同函数
const confirmDialogue_id='confirmDialogue'
const confirmDialogue=()=>
    `<!-- Modal -->
    <div class="modal fade" id="${confirmDialogue_id}" tabindex="-1" aria-labelledby="${confirmDialogue_id}" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                <!--inject contents here-->
                </div>
            </div>
        </div>
    </div>`


/**
 * 初始化一个确认对话框，并在对话框显示时根据按钮绑定的回调函数动态设置内容。
 * @param {Map} clickM - 一个映射表，键为按钮的名称，值为按钮点击时执行的函数。
 */
const confirmDialogueInit = (clickM) => {
    // 获取对话框元素
    var hookedModal = $(confirmDialogue_id)

    // 当对话框显示时触发的事件处理程序
    hookedModal.on('show.bs.modal', (e) => {
        // 获取触发对话框显示的按钮元素
        var buttonType = e.relatedTarget
        // 从按钮获取自定义数据属性值
        var clickName = buttonType.getAttribute('data-bs-whatever')
        
        // 检查映射表中是否存在对应按钮的回调函数
        if (clickM.has(clickName)) {
            // 获取回调函数
            var fn = clickM.get(clickName)
            // 获取对话框的主体部分
            var mbody = hookedModal.elect('.modal-body')
            // 如果回调函数是函数类型，则执行该函数
            if (typeof fn === 'function') fn(mbody, buttonType)
        }
    })
    
    // 当对话框隐藏时，清空对话框主体的内容
    hookedModal.on('hidden.bs.modal', () => hookedModal.elect('.modal-body').innerHTML = '')
}


/**定制化对话框注册 */
const confirmDialogueClickMap=new Map()

const saveFn=()=>{
    var inputObj=$("saveFn_input_file")
    var vl=inputObj.value
    var neo_name=vl+'.md'
    const contents=$('edit_area').value

    const checkIsDuplicate=(neo)=>{
        var isSame=AnconfigObj.list.filter(it=>it==neo)
        return isSame.length>0
    }
    if(vl.length>0){
        if(!checkIsDuplicate(neo_name)){
            AnconfigObj.copy(neo_name,contents)
            
            //重新加载侧边栏数据
            SideConfig(AnconfigObj)

            // 触发对象可以是任何元素或其他事件目标
            $('saveFn_input_click').autoclick()
        }else{
            var hintObj=$('saveFn_input_hint')
            hintObj.innerHTML='文件名重复了，请重新输入！'
            hintObj.setAttribute('class','text-danger ps-2')
            
            setTimeout(()=>hintObj.setAttribute('class','visually-hidden'),1000)
        }
    }else{
        var hintObj=$('saveFn_input_hint')
        hintObj.innerHTML='文件名不能为空！'
        hintObj.setAttribute('class','text-danger ps-2')

        setTimeout(()=>hintObj.setAttribute('class','visually-hidden'),1000)
    }
}

confirmDialogueClickMap.set( 'saveFn',
(ele,_)=>ele.innerHTML=
`<div class="row mb-2">
    <div class="col-12">
        <h5>新文件</h5>
    </div>
    <div class="col-12 text-black-50">
        是否将当前页面数据内容存至新文件内？
    </div>
</div>
<div class="row">
    <label for="saveFn_input_file" class="col-sm-2 col-form-label">文件名</label>
    <div class="col-sm-10">
    <input type="text" class="form-control" id="saveFn_input_file" placeholder="请输入文件名">
    </div>
    <p id="saveFn_input_hint" class="text-danger ps-2 visually-hidden">文件名不能为空！</p>
</div>
<div class="row pt-2">
    <div class="col justify-content-end align-items-center d-flex">
        <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
        <button type="button" class="btn text-primary" onclick="${saveFn.name}()">确定</button>
    </div>
</div>
<button class="visually-hidden" id="saveFn_input_click" data-bs-dismiss="modal"></button>
`
)


confirmDialogueClickMap.set('askFn',(ele,_)=>{
    ele.innerHTML=
    `
    <div class="row">
        <div class="col">
            是否清空当前编辑框全部内容？
        </div>
    </div>
    <div class="row">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn text-primary" data-bs-dismiss="modal"  onclick="textClear('${edit_area_id}')">确定</button>
        </div>
    </div>`
})

var createFn=()=>{
    var inputObj=$("createFn_input_file")
    var vl=inputObj.value
    var neo_name=vl+'.md'
    
    const checkIsDuplicate=(neo)=>{
        var isSame=AnconfigObj.list.filter(it=>it==neo)

        return isSame.length>0
    }
    if(vl.length>0){
        if(!checkIsDuplicate(neo_name)){
            AnconfigObj.add(neo_name)
            // 触发对象可以是任何元素或其他事件目标
            // dismiss current modal
            $('createFn_input_click').autoclick()
            if(window.sideEl){
                window.sideEl.context.addCard(neo_name)
                window.sideEl.context.context.open(neo_name)
            }
        }else{   
            var hintObj=$('createFn_input_hint')
            hintObj.innerHTML='文件名重复了，请重新输入！'
            hintObj.setAttribute('class','text-danger ps-2')
            
            setTimeout(()=>hintObj.setAttribute('class','visually-hidden'),1000)
        }
    }else{
        var hintObj=$('createFn_input_hint')
        hintObj.innerHTML='文件名不能为空！'
        hintObj.setAttribute('class','text-danger ps-2')

        setTimeout(()=>hintObj.setAttribute('class','visually-hidden'),1000)
    }

}

confirmDialogueClickMap.set('createFn',(ele,_)=>{
    var md_old_name="默认"
    ele.innerHTML=
    `
    <div class="col-12 mb-2">
        <h5>新文件</h5>
    </div>
    <div class="row">
        <label for="createFn_input_file" class="col-sm-2 col-form-label">文件名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="createFn_input_file" placeholder="请输入新的文件名" value="${md_old_name}">
        </div>
        
        <p id="createFn_input_hint" class="text-danger ps-2 visually-hidden">文件名不能为空！</p>
    </div>
    <div class="row pt-2">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn text-primary" onclick="createFn()">确定</button>
        </div>
    </div>
    <button class="visually-hidden" id="createFn_input_click" data-bs-dismiss="modal" ></button>
    `
})

confirmDialogueClickMap.set('editFn',(ele,btn)=>{
    var more_hint=btn.getAttribute('data-bs-value')

    const rowEl=$neo('div')
    rowEl.attr("class","row")
    rowEl.innerHTML=`<label for="editFn_input_file" class="col-sm-2 col-form-label">文件名</label>`

    const inputEl=$neo("input")
    inputEl.attr("type","text").attr("class","form-control").attr("placeholder","请输入新的文件名").attr("value",more_hint)
    inputEl.attr("oninput",`(()=>{window.__fnargs.file_name=this.value;})()`)
    rowEl.appendChild(inputEl)

    
    const footEl=$neo("div")
    footEl.attr("class","row pt-2")
    
    const stackEl=$neo("div")
    stackEl.attr("class","col justify-content-end align-items-center d-flex")
    
    const confirmEl=$neo("button")
    confirmEl.attr("class","btn text-primary").innerHTML="确定"
    confirmEl.on("click",()=>{
        if(inputEl.value==""){
            Log("empty!!")
        }
        else {
            requestStatus('1')
            let dismissBtn=$neo("div").attr("data-bs-dismiss","modal")
            ele.appendChild(dismissBtn)
            dismissBtn.autoclick()
            dismissBtn.remove()
        }
    })

    const cancelEl=$neo("button")
    cancelEl.attr("class","btn text-danger").attr("data-bs-dismiss","modal").innerHTML="取消"
    cancelEl.on("click",()=>{requestStatus('0')})
    
    stackEl.appendChild(cancelEl)
    stackEl.appendChild(confirmEl)
    footEl.appendChild(stackEl)


    ele.appendChild(rowEl)
    ele.appendChild(footEl)
})

confirmDialogueClickMap.set('delFn',(ele,_)=>{
    //var more_id=btn.getAttribute('data-bs-id')
    ele.innerHTML=
    `
    <div class="row">
        <div class="col">
            是否删除当前选中文件？
        </div>
    </div>
    <div class="row">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal" onclick=requestStatus('0')>取消</button>
            <button type="button" class="btn text-primary" data-bs-dismiss="modal"  onclick="requestStatus('1')">确定</button>
        </div>
    </div>
    `
})


const enableClickFn=(thi)=>{
    thi.switch({el:$("exportFn_input_file")},
    (_,args)=>{
        thi.innerHTML="确定"

        args.el.removeAttribute('disabled')
    },
    (_,args)=>{
        thi.innerHTML="编辑"
        if(args.el.value.length== 0) args.el.value='新文件名'
        args.el.attr('disabled','')
    })
}



/**
 * 该函数用于切换或添加指定元素下所有li元素的类名，
 * 以及对指定元素本身应用特定类名。
 * @param {HTMLElement} it - 指定的DOM元素
 * @param {string} ele - 选择器字符串，用于选择子元素li
 */
const toggleBadPaneClass = (it, ele) => {
  it["switch"]({
    els: $(ele).electAll('li')
  }, function (ele, args) {
    for (var i = 0; i < args.els.length; i++) args.els[i].setAttribute('class', 'list-inline-item badpan');
    ele.attr('class', 'list-inline-item badpaned');
  }, function (ele, args) {
    for (var i = 0; i < args.els.length; i++) args.els[i].setAttribute('class', 'list-inline-item badpan');
  });
}

const confirmExportFn=()=>{
   setTimeout(()=>{
        $("exportFn_input_click").autoclick()
    },100)

    window.sideEl.context.context.setExportTil($("exportFn_input_file").value)

    let firstargs=$('export_color').electAll('li')
    var firstarg=''
    for(let i=0;i<firstargs.length;i++){
        if(firstargs[i].getAttribute('isCheck')=='true'){
            firstarg=firstargs[i].innerHTML
        }
    }
    
    let secondargs=$('export_quality').electAll('li')
    var secondarg=''
    for(let i=0;i<secondargs.length;i++){
        if(secondargs[i].getAttribute('isCheck')=='true'){
            secondarg=secondargs[i].innerHTML
        }
    }


    let thirdargs=$('export_zoom').electAll('li')
    var thirdarg=''
    for(let j=0;j<thirdargs.length;j++){
        if(thirdargs[j].getAttribute('isCheck')=='true'){
            thirdarg=thirdargs[j].innerHTML
        }
    }


    let foargs=$('export_format').electAll('li')

    var foarg=''
    for(let k=0;k<foargs.length;k++){
        if(foargs[k].getAttribute('isCheck')=='true') foarg=foargs[k].innerHTML
    }
    
    

    const confimap=new Map()
    confimap.set('低',0.5)
    confimap.set('中',0.8)
    confimap.set('高',1.0)

    confimap.set('白色','white')
    confimap.set('透明','transparent')
    confimap.set('灰色','#f1f3f5')

    confimap.set('PNG','png')
    confimap.set('JPG','jpg')
    confimap.set('PDF','pdf')


    const bgColor=confimap.get( firstarg)==undefined?'white':confimap.get(firstarg)
    const encode=confimap.get(secondarg)==undefined?1.0:confimap.get(secondarg)
    const scale_=thirdarg==''?3.0:thirdarg
    const opts={backgroundColor:bgColor,encoderOptions:encode,scale:scale_}
    
    const format_=confimap.get(foarg)==undefined?'png':confimap.get(foarg)
    switch(format_){
        case 'png':
            Exporter.savePng(opts)
            break;
        case 'jpg':
            Exporter.saveJpg(opts)
            break;
        case 'pdf':
            Exporter.savePdf(opts)
            break;
        default:
            Exporter.savePng(opts)
            break;
    }
}

confirmDialogueClickMap.set('exportFn',(ele,_)=>{
    //图片文件名    
    //图片清晰度 0.5低 0.8中 1高
    //图片背景色 white白色 transparent透明 黑色black
    //放大级别数 1 2 3 4 5
    //图片格式 png jpg pdf 
    const oldame=$("sideHeader").innerHTML
    const contents=
    `
    <h5>导出文件</h5>
    <div class="row">
        <label for="exportFn_input_file" class="col-3 col-form-label ps-1 pe-1">文件名</label>
        <div class="col-7 pe-0 ps-0">
            <input id="exportFn_input_file" type="text" class="p-1 form-control border-top-0 border-start-0 border-end-0 rounded-0 bg-white" id="exportFn_input_file" placeholder="请输入新的文件名" value="${oldame.replace('.md','')}" disabled>
        </div>
        <button  class="p-0 m-0 col-2 btn text-danger" onclick="enableClickFn(this)" isCheck="false">编辑</button>
    </div>

    <br/>
    <div class="row">
        <div class="col-4">
            图片清晰度
        </div>
        <div class="col-8">
            <ul class="list-inline" id="export_quality">
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_quality')">低</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_quality')">中</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_quality')">高</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <label class="col-4">图片背景色</label>
        <div class="col-8">
            <ul class="list-inline" id="export_color">
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_color')">白色</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_color')">透明</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_color')">灰色</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <label class="col-4">放大级别数</label>
        <div class="col-8">
            <ul class="list-inline" id="export_zoom">
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_zoom')">1</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_zoom')">3</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_zoom')">5</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <label class="col-4">导出之格式</label>
        <div class="col-8">
            <ul class="list-inline" id="export_format">
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_format')">PNG</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_format')">JPG</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="toggleBadPaneClass(this,'export_format')">PDF</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <button class="visually-hidden" id="exportFn_input_click" data-bs-dismiss="modal"></button>
        <div class="col justify-content-end align-items-center d-flex">
            <button class="btn btn-white text-danger" data-bs-dismiss="modal">取消</button>
            <button class="btn btn-white text-primary" onclick="confirmExportFn()">确定</button>
        </div>
    </div>
    `

    ele.innerHTML=contents
})
/**定制化对话框注册 */



//导出配置
//图片文件名，图片尺寸数，图片背景色，图片质量比，放大级别数
const ExporterConfig={
    markmapId:'markmap',
    opts:{backgroundColor:'white',encoderOptions:1,scale:4},
}
const Exporter={
    savePdf:(opts)=>saveSvgAsPdfUri($(ExporterConfig.markmapId),opts),
    saveJpg:(opts)=>saveSvgAsJpgUri($(ExporterConfig.markmapId),opts),
    savePng:(opts)=>saveSvgAsPngUri($(ExporterConfig.markmapId),opts),
}



//网页内容载入
//document.body.innerHTML+=FullScreenEditor()
$('modal_loading').innerHTML+=FullScreenEditor()+'<br/>'+inputModal()+'<br/>'+confirmDialogue()
addEven()
confirmDialogueInit(confirmDialogueClickMap)
