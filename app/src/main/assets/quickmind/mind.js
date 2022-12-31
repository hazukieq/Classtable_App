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
const fullscreen_height=window.screen.availHeight*8/10
//静态资源组


const helpBtn=()=>
`<button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#${help_modal_id}">
    语法帮助
</button>
`
const switchControl=(switch_id)=>{
    $switch(switch_id,{controlEle:$(edit_area_root_id),hintEle:$(hint_control_id)},
    (ele,args)=>{
        args.controlEle.css('display','none')
        args.hintEle.innerHTML='预览模式'},
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
    `<div class="d-flex justify-content-between align-items-center pt-1">
        ${helpBtn()}
        ${previeModeSwitch()}
    </div>`




const textClear=(obj)=>{
    $(obj).value=''
    $(svg_root_id).innerHTML=`<div class="text-center pt-5"><h4>暂无内容显示</h4></div>`
    $(svg_toolbar_id).innerHTML=''
}


const confirmInput=()=>{
    var h=$('input_content').value
    //console.log(typeof h)
    if(h!==null&&typeof parseFloat(h) =='number')$(edit_area_id).css('height',h>48?h+'px':'48px')
}

const inputModal=()=>{
    const modal=
    `
    <!-- Modal -->
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
    </div>
    `
    return modal
}
const Editor=()=>
`
<div class="row mb-3" id="edit_area_root">
    <div class="col-12 p-1">
    <button class="btn btn-white text-primary float-start" data-bs-toggle="modal" data-bs-target="#${FullScreenEditor_id}" id="${edit_fullscreen_btn_id}" data-bs-whatever="">全屏编辑</button>
    <button class="btn text-center text-info" data-bs-toggle="modal" data-bs-target="#${input_modal_id}">高度调整</button>
    <button class="btn btn-white text-danger float-end" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="askFn">一键清除</button>
    </div>
    <div class="mt-1 col-12">
        <textarea class="form-control border-0" placeholder="${edit_area_hint}" style="caret-color:#007AFF;resize:none;height:96px" id="${edit_area_id}" rows="2" onchange="inputListener(this)"></textarea>
        </div>
</div>
`
const inputListener=(ele)=>{
    $(edit_fullscreen_btn_id).attr('data-bs-whatever',ele.value)
    MarkdownDynamic(svg_root_id,ele,{height:markmap_height})
}
                    




const confirmEdit=()=>{
    $(edit_area_id).value=$(fullscreen_edit_id).value
    MarkdownDynamic(svg_root_id,$(edit_area_id),{height:markmap_height})
}

const FullScreenEditor=()=>{
    const modal=
    `
    <!-- Modal -->
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
    </div>
    `
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
    `
    <!--主界面数据载入区-->
    <div class="row mt-1 p-1">
        <div class="col">
            <!--js载入预处理数据-->
            <div class="p-1 bg-white" style="border-radius: 4px 4px 0px 0px;" id="svg_toolbar"></div>
           <div id="svg_root" style="border-radius:0xp 0px 4px 4px;background-color: white; width: 100%;height:${markmap_height}px;">
                <div class="text-center pt-5"><h4>暂无内容显示</h4></div>
           </div>
        </div>
    </div>
    `



const confirmDialogue_id='confirmDialogue'
const confirmDialogue=()=>
    `
    <!-- Modal -->
    <div class="modal fade" id="${confirmDialogue_id}" tabindex="-1" aria-labelledby="${confirmDialogue_id}" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                <!--inject contents here-->
                </div>
            </div>
        </div>
    </div>
    `


const confirmDialogueInit=(clickM)=>{
    var exampleModal = $(confirmDialogue_id)
    exampleModal.on('show.bs.modal',(e)=>{
        var buttonType=e.relatedTarget
        console.log(buttonType)
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

// const savePng=()=>{
//     var ent=$('editerea').value
//     var editz=ent.length>0?"_"+ent.split("\n")[0].replace(" ","").replace("#",""):""
//     window.java.getname(editz)
    
//     var svgxml=document.getElementById('markmap')
//     var toExport=svgxml.cloneNode(true)

//     saveSvgAsPngUri(svgxml,{backgroundColor:'white',encoderOptions:1,scale:4})
//     }
const ExporterConfig={
    editEle:$(edit_area_root_id),
    markmapId:'markmap',
    opts:{backgroundColor:'white',encoderOptions:1,scale:4},
}
//const doc_tile=ExporterConfig.editEle.length>0?"_"+ExporterConfig.editEle.split("\n")[0].replace(" ","").replace("#",""):""
const Exporter={
    savePdf:()=>saveSvgAsPdfUri($(ExporterConfig.markmapId),ExporterConfig.opts),
    savePng:()=>saveSvgAsPngUri($(ExporterConfig.markmapId),ExporterConfig.opts),
}




//document.body.innerHTML+=FullScreenEditor()
$('modal_loading').innerHTML=FullScreenEditor()+'<br/>'+inputModal()+'<br/>'+confirmDialogue()
addEven()


const confirmDialogueClickMap=new Map()
confirmDialogueClickMap.set( 'saveFn',(ele,btn)=>ele.innerHTML=
`
<div class="row mb-2">
    <div class="col-12">
        <h5>新文件</h5>
    </div>
    <div class="col-12 text-black-50">
        是否保存当前页面数据内容？
    </div>
</div>
<div class="row">
    <label for="saveFn_input_file" class="col-sm-2 col-form-label">文件名</label>
    <div class="col-sm-10">
    <input type="text" class="form-control" id="saveFn_input_file" placeholder="请输入文件名">
    </div>
</div>
<div class="row pt-2">
    <div class="col justify-content-end align-items-center d-flex">
        <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
        <button type="button" class="btn text-primary" data-bs-dismiss="modal">确定</button>
    </div>
</div>

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

confirmDialogueClickMap.set('editFn',(ele,btn)=>{
    var more_id=btn.getAttribute('data-bs-id')
    var md_old_name=btn.getAttribute('data-bs-name')
    ele.innerHTML=
    `
    <div class="row">
        <label for="editFn_input_file" class="col-sm-2 col-form-label">文件名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="editFn_input_file" placeholder="请输入新的文件名" value="${md_old_name}">
        </div>
    </div>
    <div class="row pt-2">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn text-primary" data-bs-dismiss="modal"  onclick="editDoc('${more_id}')">确定</button>
        </div>
    </div>
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
confirmDialogueInit(confirmDialogueClickMap)


const confirmDialogueClickMap=new Map()
confirmDialogueClickMap.set( 'saveFn',(ele,btn)=>ele.innerHTML=
`
<div class="row mb-2">
    <div class="col-12">
        <h5>新文件</h5>
    </div>
    <div class="col-12 text-black-50">
        是否保存当前页面数据内容？
    </div>
</div>
<div class="row">
    <label for="saveFn_input_file" class="col-sm-2 col-form-label">文件名</label>
    <div class="col-sm-10">
    <input type="text" class="form-control" id="saveFn_input_file" placeholder="请输入文件名">
    </div>
</div>
<div class="row pt-2">
    <div class="col justify-content-end align-items-center d-flex">
        <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
        <button type="button" class="btn text-primary" data-bs-dismiss="modal">确定</button>
    </div>
</div>

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

confirmDialogueClickMap.set('editFn',(ele,btn)=>{
    var more_id=btn.getAttribute('data-bs-id')
    var md_old_name=btn.getAttribute('data-bs-name')
    ele.innerHTML=
    `
    <div class="row">
        <label for="editFn_input_file" class="col-sm-2 col-form-label">文件名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="editFn_input_file" placeholder="请输入新的文件名" value="${md_old_name}">
        </div>
    </div>
    <div class="row pt-2">
        <div class="col justify-content-end align-items-center d-flex">
            <button type="button" class="btn text-danger" data-bs-dismiss="modal">取消</button>
            <button type="button" class="btn text-primary" data-bs-dismiss="modal"  onclick="editDoc('${more_id}')">确定</button>
        </div>
    </div>
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
confirmDialogueInit(confirmDialogueClickMap)

