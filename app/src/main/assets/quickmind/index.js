//静态资源
const waitloading_id="waitloading"
const offcanvasLabel_id="offcanvasLabel"
const toolbar_tile="toolbar_tile"
const offcanvasMain_id="offcanvasMain"
const filetype_img_path="../basic/img/filetype-md.svg"

const isDebug=false


/**配置文件操作函数*/
class Anco {

    /**
     * 
     * @param {String} configStr 
     */
    constructor(configStr) {
        var configObj={loadState:'none',tile:'（未打开文件）',list:[]}
        
        if( typeof configStr=='string'&&configStr.length>0){
            let obj
            try{
                obj=JSON.parse(configStr)
            }catch(error){
                obj={loadState:'none',tile:'（未打开文件）',list:[]}
                console.log(error)
            }
            configObj=obj
        }

        const {loadState='none', tile='（未打开文件）', list=[]}=configObj
        this.tile = tile

        const filteredList = list.filter(iet => iet.endsWith('.md'))
        this.list = filteredList

        this.loadState = loadState
    }

    //移除元素
    /**
     * 
     * @param {String} ite 
     */
    rem(ite) {
        if(this.list.includes(ite)) {
            this.list=this.list.filter(ie=>ie!=ite)
            console.log(this.list)

            if(this.tile==ite&&this.loadState=='exist'){
                this.close()
                loadPage(this)
            }
        }
    }

    //添加元素
    /**
     * 
     * @param {String} ite 
     */
    add(ite) {
        this.list.push(ite)
        //创建新文件
        anPutData(ite,'')
    }

    //复制文件
    /**
     * 
     * @param {String} ite 
     * @param {String} contents 
     */
    copy(ite,contents){
        this.list.push(ite)
        //创建新文件
        const __contents=contents||""
        anPutData(ite,__contents)

    }

    //改变标题
    /**
     * 
     * @param {String} ite 
     */
    rename(old_ite,ite) {
        this.tile = ite
        var index=this.list.indexOf(old_ite)
        this.list[index]=ite
        var old_file_contents=java.direader(old_ite,'mind')
        anPutData(ite,old_file_contents)

        console.log(this.list)
    }
    //关闭文件
    close() {
        this.loadState = 'none'
        this.tile = '（未打开文件）'
    }
    
    //打开文件
    /**
     * 
     * @param {String} ite 
     */
    open(ite) {
        this.loadState = 'exist'
        this.tile = ite
    }

    toJson(){
        var jsons={loadState:this.loadState,tile:this.tile,list:this.list}
        console.log(jsons)
        return JSON.stringify(jsons)
    }
}
/**配置文件操作函数*/


const Anconfigs=isDebug?
    JSON.stringify({loadState:"none",tile:'hello.md',list:['hello.md','world.md']}):
    java.dipasser('getConfig','mind')//获取配置参数

const AnconfigObj=new Anco(Anconfigs)
console.log(AnconfigObj)



/*侧边栏组件*/
const SideBar={
    setHeader:(title)=>{
        $(offcanvasLabel_id).innerHTML=title
        $(toolbar_tile).innerHTML=title
    },
    getHeader:()=>$(offcanvasLabel_id).innerHTML,
    setHexport:(til)=>$(toolbar_tile).attr('hexport',til),
    getHexport:()=>$(toolbar_tile).getAttribute('hexport'),
    setMain:(source)=>$(offcanvasMain_id).innerHTML=source
}
/*侧边栏组件*/


/*----侧边栏文件列表组件-----*/
/**
 * 
 * @param {HTMLObjectElement} ele 
 */
const openMdFile=(ele)=>{
    AnconfigObj.open(ele)
    if(AnconfigObj.loadState=='exist'){
        SideBar.setHeader(ele)
        LoadProcessor(AnconfigObj)
    }else{
        loadPage(AnconfigObj)
    }
    isDebug?void 0:anPutConfig('.configs',AnconfigObj.toJson())
}

/**
 * 
 * @param {String} id 
 */
const delDoc=(id)=>{
    var root_ele=$(id)
    const tie_id=id.replace('root_','')
    const tie=$(tie_id).getAttribute('data-bs-name')

    var offcanvasMain=$(offcanvasMain_id)
    offcanvasMain.del(root_ele)
    
    if(offcanvasMain.children.length==0||offcanvasMain.children===undefined){
        offcanvasMain.innerHTML='<h5 class="mt-5 text-center">暂无文件</h5>'
    }

    AnconfigObj.rem(tie)
    isDebug?void 0:anPutConfig('.configs',AnconfigObj.toJson())
}

/**
 * 
 * @param {String} id 
 */
const editDoc=(id)=>{
    var doc_ele=$(id)
    var old_name=doc_ele.getAttribute('data-bs-name')
    var vl=$('editFn_input_file').value
    var neo_name=vl+'.md'

    const checkIsDuplicate=(old,neo)=>{
        if(old==neo) return false
        var isSame=AnconfigObj.list.filter(it=>it==neo)

        return isSame.length>0
    }
    if(vl.length>0){
        
        if(!checkIsDuplicate(old_name,neo_name)){
            
            if(SideBar.getHeader()==old_name) SideBar.setHeader(neo_name)
            doc_ele.setAttribute('data-bs-name',neo_name)
            doc_ele.setAttribute('onclick',"openMdFile('"+neo_name+"')")
            doc_ele.innerHTML=neo_name
        
            AnconfigObj.rename(old_name,neo_name)
            
            // 触发对象可以是任何元素或其他事件目标
            $('editFn_input_click').autoclick()
        }else{
            var hintObj=$('editFn_input_hint')
            hintObj.innerHTML='文件名重复了，请重新输入！'
            hintObj.setAttribute('class','text-danger ps-2')
            
            setTimeout(()=>hintObj.setAttribute('class','visually-hidden'),1000)
        }
    }else{
        var hintObj=$('editFn_input_hint')
        hintObj.innerHTML='文件名不能为空！'
        hintObj.setAttribute('class','text-danger ps-2')

        setTimeout(()=>hintObj.setAttribute('class','visually-hidden'),1000)
    }
    isDebug?void 0:anPutConfig('.configs',AnconfigObj.toJson())
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
                <div class="col-10 docClick ps-2" id="{more_id}" onclick="openMdFile('{doc}')" data-bs-name="{doc}" data-bs-dismiss="offcanvas">{doc}</div>
            </div>
        
        </div>
        <div class="column" style="flex: 3;" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="editFn" data-bs-id="{more_id}">编辑</div>
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
        var temp=docItem.replaceAll('{doc}',args_[i])
        var temp_id=Math.random()*3.14159266
        temp=temp.replaceAll('{more_id}','more_'+temp_id)
        docComponent+=temp
    }
    return docComponent
}

/*----侧边栏文件列表组件-----*/



const main_map=new Map()
main_map.set('none',
`
<div class="p-5"></div>
<div class="d-flex justify-content-center flex-column align-items-center mt-5">
    <h4>未打开文件</h4>
    <button class="btn text-primary mt-4" style="background:white;opacity:.9;letter-spacing:.1rem" data-bs-toggle="modal" data-bs-target="#confirmDialogue" data-bs-whatever="createFn">创建新文件</button>
    <button class="btn text-primary mt-2 visually-hidden" style="background:white;opacity:.9;letter-spacing:.1rem">导入新文件</button>
    <button class="btn text-primary mt-2" style="background:white;opacity:.9;letter-spacing:.1rem" data-bs-toggle="offcanvas" href="#sidebar" role="button" aria-controls="sidebar">打开旧文件</button>
    <p class="mt-3 text-black-50 ps-3">
    <span class="badge bg-info">提示</span>&nbsp;&nbsp;点击列表项，对文件进行编辑或预览。
    </p>
</div>`)

main_map.set('exist',
`${Header()}
${Editor()}
${Preview()}`)

const LoadProcessor=(configs)=>{
    const main_content=main_map.get(configs.loadState)
    
    const fileContent=isDebug?'':(configs.tile=='（未打开文件）'?'':java.direader(configs.tile,'mind'))

    setTimeout(()=>{
        $(waitloading_id).innerHTML=main_content
        if(configs.loadState=='exist')reloadEditor(fileContent)
    },1000)

    
    console.log('fileContent='+fileContent)
}


const SideConfig=(args)=>{
    const docs=args.list||{}
    const tile=args.tile

    const docList=docItemGenerator(docs)

    SideBar.setHeader(tile)
    const docListDiv=docList.length>0?docList:'<p class="p-3">这里什么都没有~orz</p>'
    SideBar.setMain(docListDiv)
    parseSlideItem()
}




const loadPage=(obj)=>{
    LoadProcessor(obj)
    SideConfig(obj)
    anPutConfig('.configs',AnconfigObj.toJson())
}


//关闭页面函数
const closePage=()=>{
    if(AnconfigObj.loadState=='exist') {
        AnconfigObj.close()
        loadPage(AnconfigObj)
        anPutConfig('.configs',AnconfigObj.toJson())
    }
}

//自动保存函数
const autosaveFn=()=>{
    if(AnconfigObj.loadState=='exist'){
        const contents=$('edit_area').value
    
        anPutData(AnconfigObj.tile,contents)
        console.log(contents)
        
        anPutConfig('.configs',AnconfigObj.toJson())
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
loadPage(AnconfigObj)
/**实际代码执行部分*/


