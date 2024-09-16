/*侧边栏组件*/
class SideBarEl{
    constructor(configs){
        const {tile="",docList=[]}=configs
        //var mContext=this
        //公共上下文类
        this.context={
            //标题
            tile:tile,
            //标题组件
            tilEl:null,
            //export title
            exportTil:"默认_",
            //列表数据
            docs:docList,
            //设置标题
            setTile(til){
                this.tile=til
                if(this.tilEl!=undefined&&this.tilEl!=null){
                    this.tilEl.innerHTML=til
                }
            },
            closeSide:()=>{
                let btn=$("offcanvasCloseBtn")
                if(btn) btn.autoclick()
            },
            addDoc(doc){
                if(this.docs) this.docs.push(doc)
            },
            //设置列表数据
            setDocList(docz){
                this.docs=docz
            },

            //实时更新顶层标题、加载状态为初始值
            updateAll(doc,new_doc){
                this.setTile(new_doc)
                AnconfigObj.rename(doc,new_doc)
                this.closeSide()
            },
            //打开文件
            open(doc){
                this.setTile(doc)
                console.log("open doc<"+doc+">")
                AnconfigObj.open(doc)
                openPage(AnconfigObj)
                this.closeSide()
            },
            setExportTil(tile){
                this.exportTil=tile
            },
            export(){
                return this.exportTil
            }
            ,
            //还原顶层标题、加载状态为初始值且关闭文件
            close(){
                this.tile=''
                this.setTile("（未打开文件）")
                this.closeSide()
                return 0
            }
        }

        this.rootEl=$neo("div")
        this.rootEl.attr("class","offcanvas offcanvas-start menuside").attr("tabindex","-1").attr("aria-labelledby","sidebar").attr("id","sidebar")
        this.rootEl.css("width",(window.screen.availWidth/4+80)+"px")
        this.rootEl.appendChild(this.getHead(this.context.tile))
        this.resyncCards()
    }


    getHead(til){
        const headEl=$neo()
        headEl.attr("class","pt-5 border-bottom pb-1 d-flex flex-column align-items-start justify-content-center bg-white")
        headEl.css("padding","1rem 1rem").css("z-index" ,"1045")

        const titleEl=$neo()
        titleEl.attr("class","d-inline-block text-truncate mb-1").css("max-width","160px").css("font-size","18px")
        titleEl.innerHTML=til
        titleEl.attr('id','sideHeader')

        headEl.innerHTML=`            
        <div class="d-flex justify-content-end w-100">
            <button id="offcanvasCloseBtn" type="button" class="btn btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
    
        <div>
            <span class="text-danger" style="font-size:14px;">当前文件&nbsp;</span>
        </div>`

        this.context.tilEl=null
        this.context.tilEl=titleEl

        headEl.appendChild(titleEl)
        return headEl
    }

    setTile(til){
        this.context.tile=til
    }

    setDocList(docs){
        this.context.docs=docs
    }

    addCard(doc){
        const cardListEl=new DocListEl(doc,this.context).get()
        this.cardRoot.appendChild(cardListEl)
    }

    addCardList(){
        if(this.context.docs){
            for (const doc of this.context.docs) this.addCard(doc)
        }
    }

    resyncCards(){
        if(this.cardRoot) this.cardRoot.remove()
        this.cardRoot=$neo('div')
        this.cardRoot.attr("class","offcanvas-body pt-0")
        this.addCardList()
        this.rootEl.appendChild(this.cardRoot)
    }
    get=()=>this.rootEl   
}
