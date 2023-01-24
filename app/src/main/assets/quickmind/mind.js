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

const markmap_height=window.screen.availHeight*6/10-32
const markmap_tool_height=45
const fullscreen_height=window.screen.availHeight*8/10
//静态资源组


const helpBtn=()=>
`<button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#${help_modal_id}">
    语法帮助
</button>`
const switchControl=(switch_id)=>{
    $switch(switch_id,{controlEle:$(edit_area_root_id),hintEle:$(hint_control_id)},
    (ele,args)=>{
        args.controlEle.css('display','none')
        args.hintEle.innerHTML='预览模式'
    },
    (ele,args)=>{
        args.controlEle.css('display','block')
        args.hintEle.innerHTML='编辑模式'
    })
}

const previeModeSwitch=()=>
    `<div class="form-check form-switch">
        <input class="form-check-input" type="checkbox" id="flexswitch" onclick="switchControl(this)" isCheck="false">
        <label class="form-check-label" for="flexswitch" id="${hint_control_id}">编辑模式</label>
    </div>`

const Header=()=>
    `<div class="d-flex justify-content-between align-items-center">
        ${helpBtn()}
        ${previeModeSwitch()}
    </div>`




const textClear=(obj)=>{
    $(obj).value=''
    $(svg_root_id).innerHTML=`<div class="text-center pt-5"><h4>暂无内容显示</h4></div>`
    $(svg_toolbar_id).innerHTML=''
    $(edit_fullscreen_btn_id).attr('data-bs-whatever','')

}


const confirmInput=()=>{
    var h=$('input_content').value
    //console.log(typeof h)
    if(h!==null&&typeof parseFloat(h) =='number')$(edit_area_id).css('height',h>48?h+'px':'48px')
}

const inputModal=()=>{
    const modal=
    `<!-- Modal -->
    <div class="modal fade" id="${input_modal_id}" tabindex="-1" aria-labelledby="${input_modal_id}" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>高度调整</h5>
                    <div class="pt-1 mt-1">
                        <p class="text-primary">最少高度为48,不能低于48,否则不会生效!</p>
                        <div class="input-group mb-3">
                            <span class="input-group-text" id="input_name">高度大小</span>
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

const Editor=()=>
`<div class="row mb-3" id="edit_area_root">
    <div class="col-12 p-1">
    <button class="btn btn-white text-primary float-start" data-bs-toggle="modal" data-bs-target="#${FullScreenEditor_id}" id="${edit_fullscreen_btn_id}" data-bs-whatever="">全屏编辑</button>
    <button class="btn text-center text-info" data-bs-toggle="modal" data-bs-target="#${input_modal_id}">高度调整</button>
    <button class="btn btn-white text-danger float-end" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="askFn">一键清除</button>
    </div>
    <div class="mt-1 col-12">
        <textarea class="form-control border-0" placeholder="${edit_area_hint}" style="caret-color:#007AFF;resize:none;height:96px" id="${edit_area_id}" rows="2" oninput="inputListener(this)"></textarea>
        </div>
</div>`
const inputListener=(ele)=>{
    $(edit_fullscreen_btn_id).attr('data-bs-whatever',ele.value)
    MarkdownDynamic(svg_root_id,ele,{height:markmap_height})
}

const reloadEditor=(contents)=>{
    var ele=$(edit_area_id)
    ele.value=contents
    $(edit_fullscreen_btn_id).attr('data-bs-whatever',ele.value)
    MarkdownDynamic(svg_root_id,ele,{height:markmap_height})
}




const confirmEdit=()=>{
    var contentz=$(fullscreen_edit_id).value
    $(edit_fullscreen_btn_id).attr('data-bs-whatever',contentz)
    $(edit_area_id).value=contentz
    MarkdownDynamic(svg_root_id,$(edit_area_id),{height:markmap_height})
}

const FullScreenEditor=()=>{
    const modal=
    `<!-- Modal -->
    <div class="modal fade" id="${FullScreenEditor_id}" tabindex="-1" aria-labelledby="${FullScreenEditor_id}" aria-hidden="true">
        <div class="modal-dialog modal-fullscreen">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="pt-4"></div>
                    <div class="d-flex justify-content-between align-items-center">
                        <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
                        <h5 class="modal-title" id="exampleModalLabel">内容编辑区</h5>
                        <button type="button" class="btn text-primary" data-bs-dismiss="modal" onclick="confirmEdit()">确定</button>
                    </div>
                    <div class="pt-1 mt-1 border-1 border-top d-flex align-items-start">
                    <div style="margin-top:2px;width:1px;height:${fullscreen_height}px;background-color:gray;opacity:.5;"></div>
                    <textarea class="form-control border-0" style="caret-color:#007AFF;resize:none;width:100%;height:${fullscreen_height}px" placeholder="${edit_area_hint}" id="${fullscreen_edit_id}"></textarea>
                    </div>
                </div>
            </div>
        </div>
    </div>`
    return modal
}


const addEven=()=>{
    var exampleModal = $(FullScreenEditor_id)
    exampleModal.addEventListener('show.bs.modal', function (event) {
        // Button that triggered the modal
        var button = event.relatedTarget

        // Extract info from data-bs-* attributes
        var recipient = button.getAttribute('data-bs-whatever')

        var modalEdit = exampleModal.elect('.form-control')
        modalEdit.value = recipient
})
}


const Preview=()=>
    `<!--主界面数据载入区-->
    <div class="row mt-1 p-1">
        <div class="col">
            <!--js载入预处理数据-->
            <div class="p-1 bg-white rounded-top" id="svg_toolbar" style="height:${markmap_tool_height}px"></div>
           <div id="svg_root" class="bg-white rounded-bottom" style="width: 100%;height:${markmap_height}px;">
                <div class="text-center pt-5"><h4>暂无内容显示</h4></div>
           </div>
        </div>
    </div>`



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


const confirmDialogueInit=(clickM)=>{
    var exampleModal = $(confirmDialogue_id)

    exampleModal.on('show.bs.modal',(e)=>{
        var buttonType=e.relatedTarget

        var clickName=buttonType.getAttribute('data-bs-whatever')
        
        if(clickM.has(clickName)){
            var fn=clickM.get(clickName)
            var mbody=exampleModal.elect('.modal-body')
            if(typeof fn=='function') fn(mbody,buttonType)
        }
        //exampleModal.elect('.modal-body').innerHTML=content
    })
    
    exampleModal.on('hidden.bs.modal',(e)=>{
        exampleModal.elect('.modal-body').innerHTML=''
    })
}


//图片文件名    
//图片尺寸数
//图片背景色
//图片质量比
//放大级别数
const ExporterConfig={
    markmapId:'markmap',
    opts:{backgroundColor:'white',encoderOptions:1,scale:4},
}

//const doc_tile=ExporterConfig.editEle.length>0?"_"+ExporterConfig.editEle.split("\n")[0].replace(" ","").replace("#",""):""
const Exporter={
    savePdf:(opts)=>saveSvgAsPdfUri($(ExporterConfig.markmapId),opts),
    saveJpg:(opts)=>saveSvgAsJpgUri($(ExporterConfig.markmapId),opts),
    savePng:(opts)=>saveSvgAsPngUri($(ExporterConfig.markmapId),opts),
}




//document.body.innerHTML+=FullScreenEditor()
$('modal_loading').innerHTML+=FullScreenEditor()+'<br/>'+inputModal()+'<br/>'+confirmDialogue()
addEven()


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

confirmDialogueClickMap.set( 'saveFn',(ele,btn)=>ele.innerHTML=
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
        <button type="button" class="btn text-primary" onclick="saveFn()">确定</button>
    </div>
</div>
<button class="visually-hidden" id="saveFn_input_click" data-bs-dismiss="modal"></button>
`
)


confirmDialogueClickMap.set('askFn',(ele,btn)=>{
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
            AnconfigObj.open(neo_name)

            loadPage(AnconfigObj) 
            
            // 触发对象可以是任何元素或其他事件目标
            $('createFn_input_click').autoclick()
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

confirmDialogueClickMap.set('createFn',(ele,btn)=>{
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
    var more_id=btn.getAttribute('data-bs-id')
    var md_old_name=$(more_id).getAttribute('data-bs-name')

    ele.innerHTML=
    `
    <div class="row">
        <label for="editFn_input_file" class="col-sm-2 col-form-label">文件名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="editFn_input_file" placeholder="请输入新的文件名" value="${md_old_name.replace('.md','')}">
        </div>
        <p id="editFn_input_hint" class="text-danger ps-2 visually-hidden">文件名不能为空！</p>
    </div>
    <div class="row pt-2">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn text-primary"  onclick="editDoc('${more_id}')">确定</button>
        </div>
    </div>
    <button class="visually-hidden" id="editFn_input_click" data-bs-dismiss="modal" ></button>
    `
})

confirmDialogueClickMap.set('delFn',(ele,btn)=>{
    var more_id=btn.getAttribute('data-bs-id')
    ele.innerHTML=
    `
    <div class="row">
        <div class="col">
            是否删除当前选中文件？
        </div>
    </div>
    <div class="row">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn text-primary" data-bs-dismiss="modal"  onclick="delDoc('${more_id}')">确定</button>
        </div>
    </div>
    `
})


const enableClickFn=(thi)=>{
    thi.switch({el:$("exportFn_input_file")},
    (ele,args)=>{
        thi.innerHTML="确定"

        args.el.removeAttribute('disabled')
    },
    (ele,args)=>{
        thi.innerHTML="编辑"
        if(args.el.value.length== 0) args.el.value='新文件名'
        args.el.attr('disabled','')
    })
}



const selectPropFn=(it,ele)=>{

    it.switch({els:$(ele).electAll('li')},
        (ele,args)=>{
            for(let i=0;i<args.els.length;i++)
                args.els[i].setAttribute('class','list-inline-item badpan')
            
            ele.attr('class','list-inline-item badpaned')
        },
        (ele,args)=>{
            for(let i=0;i<args.els.length;i++)
                args.els[i].setAttribute('class','list-inline-item badpan')
        }
    )
}

const confirmExportFn=()=>{
    SideBar.setHexport($("exportFn_input_file").value)

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
    console.log(foargs)
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

    setTimeout(()=>{
        $("exportFn_input_click").autoclick()
    },200)
}
confirmDialogueClickMap.set('exportFn',(ele,btn)=>{
    //图片文件名    
    //图片清晰度 0.5低 0.8中 1高
    //图片背景色 white白色 transparent透明 黑色black
    //放大级别数 1 2 3 4 5
    //图片格式 png jpg pdf 
    const oldame=SideBar.getHeader()
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
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_quality')">低</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_quality')">中</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_quality')">高</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <label class="col-4">图片背景色</label>
        <div class="col-8">
            <ul class="list-inline" id="export_color">
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_color')">白色</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_color')">透明</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_color')">灰色</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <label class="col-4">放大级别数</label>
        <div class="col-8">
            <ul class="list-inline" id="export_zoom">
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_zoom')">1</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_zoom')">3</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_zoom')">5</li>
            </ul>
        </div>
    </div>

    <div class="row">
        <label class="col-4">导出之格式</label>
        <div class="col-8">
            <ul class="list-inline" id="export_format">
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_format')">PNG</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_format')">JPG</li>
                <li class="list-inline-item badpan" isCheck="false" onclick="selectPropFn(this,'export_format')">PDF</li>
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


confirmDialogueInit(confirmDialogueClickMap)

