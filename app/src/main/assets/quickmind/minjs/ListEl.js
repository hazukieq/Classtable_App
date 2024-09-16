const filetype_img_path="../basic/img/filetype-md.svg"
class DocListEl{
    /**
     * 
     * @param {Object} config 
     */
    constructor(doc,config){
        this.timeCount=0
        this._doc=doc||""
        this._config=config||{}
        //列表标题组件
        this.openEl=$neo("div")

        //INSTANCE指向自身DIV元素
        this.INSTANCE=this.getDocItem()
    }

    registSlideEvent(slideIt){
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
    }
    

    getAutoClick(activeTag){
        //创建一个点击按钮，并返回
        return $neo('div').attr("data-bs-toggle","modal").attr("data-bs-target","#confirmDialogue").attr("data-bs-whatever",`${activeTag}`)
    }



    /**
     * @param cycleup函数用于执行一个函数fn，在指定的时间间隔perTime内循环执行，直至接收到特定的窗口操作（确认或取消）。
     * @param {Function} fn - 待循环执行的函数。
     * @param {number} perTime - 循环执行fn的时间间隔（毫秒）。
     * @param {object} args - 传递给fn的参数对象。
     **/
    cycleup(fn,pertime,args){
        // 判断args是否为对象，是则赋值给全局变量__fnargs，用于传递给fn函数
        if(args!=null&&args!=undefined&& typeof args=='object') window.__fnargs=args
        else window.__fnargs={}   

        // 设置最大循环时间
        var timeMax=pertime*30
        // 开始循环的日志记录
        Log(`Cycle has began, status: (per=${pertime},max=${timeMax})`)

        window.waitForClick=-1
        if(typeof fn=='function') {
            // 定义定时器函数timerFn，用于循环检查窗口操作状态
            const timerFn=(timer_)=>{
                // 当循环时间达到最大值时，自动增加时间间隔并重置循环计时
                if(this.timeCount===timeMax){
                    pertime+=100
                    this.timeCount=0
                    timeMax=pertime*30

                    Log(`Cycle time has too long, now pertime update to (per=${pertime},max=${timeMax}) automatically.`)
                    clearInterval(timer_)
                    var dtimer=setInterval(()=>timerFn(dtimer),pertime)
                }
                
                // 如果接收到确认操作，执行fn函数并终止循环
                if(window.waitForClick==1){
                    Log("break,confirm opts")
                    //dispatchCode()
                    fn(window.__fnargs)
                    this.timeCount=0

                    clearInterval(timer_)
                }
                // 如果接收到取消操作，终止循环
                else if(window.waitForClick==0){
                    this.timeCount=0
                    Log("break,cancel opts")
                    clearInterval(timer_)
                }else {
                    // 若未接收到任何操作，继续循环等待
                    this.timeCount+=pertime
                    Log("wait for dialogue,check status--> no any opts,timeCount="+this.timeCount)
                }

            }
            // 初始化定时器，开始循环检查
            const timer=setInterval(()=>timerFn(timer),pertime)
        }
    }

    
    destroySelf(){
        const clickBtn=this.getAutoClick("delFn")
        
        this.INSTANCE.appendChild(clickBtn)
        
        clickBtn.autoclick()
        this.cycleup((args)=>{
            Log(args)
            this._config.close(this._doc)
            this.INSTANCE.remove()
        },240)

        clickBtn.remove()
    }

    edit(doc){
       const openBtn=this.getAutoClick("editFn")
       openBtn.attr("data-bs-value",this._doc)

       this.INSTANCE.appendChild(openBtn)
       // open the dialog by this btn!
       openBtn.autoclick()

       this.cycleup(
        (args)=>{
            const {file_name=""}=args
            if(file_name==""||file_name==doc) return
            else{
                this._doc=file_name
                this.openEl.innerHTML=file_name
                this._config.updateAll(doc,file_name)
            }
        },250)
        openBtn.remove()
    }
    
    getDocItem(){
        var cardList=$neo("div")
        cardList.classList.add("item")

        var cardBox=$neo("div")
        
        var deletEl=$neo("div")
        deletEl.attr("class","item-del")
        deletEl.innerHTML="删除"
        deletEl.on("click",()=>this.destroySelf())

        var editEl=$neo("div")
        editEl.css("flex","3").attr("class","column")
        editEl.on("click",()=>this.edit(this._doc))
        editEl.innerHTML="编辑"

        cardBox.classList.add("item-box")
        
        const columnEl=$neo("div")
        columnEl.css("flex","9").css("width","100%").attr("class","column")

        const rowEl=$neo("div")
        rowEl.attr("class","row g-0")

        const col2El=$neo("div")
        col2El.attr("class","col-2 bg-white")

        const imgEl=$neo("img")
        imgEl.attr("src",filetype_img_path).css("opacity",".76").attr("width","32").attr("height","32").attr("class","img-fluid")

        //this.openEl=$neo("div")
        this.openEl.attr("class","col-10 docClick ps-2").on("click",()=>{this._config.open(this._doc)})
        this.openEl.innerHTML=this._doc

        col2El.appendChild(imgEl)
        rowEl.appendChild(col2El)
        rowEl.appendChild(this.openEl)

        columnEl.appendChild(rowEl)
        
        cardBox.appendChild(columnEl)
        cardBox.appendChild(editEl)

        cardList.appendChild(cardBox)
        cardList.appendChild(deletEl)

        //需要一个最上层 context，做到实时更新侧边栏标题、主界面标题
        this.registSlideEvent(cardList)
        return cardList
    }

    get(){
        if(this.INSTANCE==null||this.INSTANCE==undefined) this.INSTANCE=this.getDocItem(this._doc) 
        return this.INSTANCE
    }
}