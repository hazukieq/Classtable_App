//静态资源
const waitloading_id="waitloading"
const offcanvasLabel_id="offcanvasLabel"
const offcanvasMain_id="offcanvasMain"
const filetype_img_path="../basic/img/filetype-md.svg"


/*侧边栏组件*/
const SideBar={
    setHeader:(title)=>{
        $(offcanvasLabel_id).innerHTML=title
    },
    //setCloseFunc:(func)=>$click('offcanvasCloseBtn',func),
    setMain:(source)=>$(offcanvasMain_id).innerHTML=source
}
/*侧边栏组件*/


/*----侧边栏文件列表组件-----*/
const openMdFile=(ele)=>{
    SideBar.setHeader(ele)
}   
const delDoc=(id)=>{
    var root_ele=$(id)
    $(offcanvasMain_id).del(root_ele)
}

const editDoc=(id)=>{
    var doc_ele=$(id)
    console.log(doc_ele)
}


const docItem=
`
<div class="item" id="root_{more_id}">
    <div class="item-box">
        <div class="column" style="flex:9;width:100%">
            <div class="row g-0">
                <div class="col-2 bg-white">
                    <img src="${filetype_img_path}" class="img-fluid" style="opacity:.76" width="32" height="32" alt="..."></img>
                </div>
                <div class="col-10 docClick ps-2" id="{more_id}" onclick="openMdFile('{doc}')">{doc}</div>
            </div>
        
        </div>
        <div class="column" style="flex: 3;" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="editFn" data-bs-id="{more_id}"  data-bs-name="{doc}">编辑</div>
    </div>
    <div class="item-del" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="delFn" data-bs-id="root_{more_id}">删除</div>
</div>`

const parseSlideItem=()=>{
    var slideItems=document.querySelectorAll('.item')
    if(slideItems.length>0){
        slideItems.forEach(slideIt=>{
            var moveStartX,moveEndX,differX;
            slideIt.on('touchstart',(e)=>moveStartX=e.targetTouches[0].pageX)
            slideIt.on('touchmove',(e)=>{
                        moveEndX=e.targetTouches[0].pageX
                        differX=moveEndX-moveStartX
                        if(differX>50){
                            slideIt.css('margin-left','0')
                            slideIt.elect('.item-del').css('display','none')
                        }else if(differX<-50){
                            slideIt.elect('.item-box').css('margin-left','0')
                            slideIt.css('margin-left','-60px')
                            slideIt.elect('.item-del').css('display','flex')
                        }
                   })
        })
    }
}


const docItemGenerator=(args)=>{
    var docComponent=''
    var args_=typeof args=='string'?JSON.parse(args):args
    for(let i in args_){
        var temp=docItem.replaceAll('{doc}',md_lists[i])
        var temp_id=Math.random()*3.14159266
        temp=temp.replaceAll('{more_id}','more_'+temp_id)
        docComponent+=temp
    }
    return docComponent
}
/*----侧边栏文件列表组件-----*/








/**实际代码执行部分*/
setTimeout(()=>{
    $(waitloading_id).innerHTML=
    `${Header()}
     ${Editor()}
     ${Preview()}`
},1000)

const md_lists=['示例文件名称','期末考试计划','马克思主义原理','假期规划','示例文件名称','期末考试计划','马克思主义原理','假期规划']
var md_docs=docItemGenerator(md_lists)

SideBar.setHeader(`客音方典.md`)
SideBar.setMain(`${md_docs}`)
  parseSlideItem()
/**实际代码执行部分*/
