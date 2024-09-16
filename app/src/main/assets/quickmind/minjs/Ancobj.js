/**配置文件操作函数*/
const unopen_name="（未打开文件）"
class Anco {
    /**
     * @param {String} configStr 
     */
    constructor(configStr) {
        var configObj
        if( typeof configStr=='string'&&configStr.length>0){
            try{
                configObj=JSON.parse(configStr)
            }catch(error){
                configObj={loadState:'none',tile:unopen_name,list:[]}
                Log(error)
            }
        }

        const {loadState='none', tile=unopen_name, list=[]}=configObj
        const filteredList = list.filter(iet => iet.endsWith('.md'))
        
        this.tile = tile
        this.list = filteredList
        this.loadState = loadState
    }

    //移除元素
    /**
     * @param {String} ite
     */
    rem(ite) {
        if(this.list.includes(ite)) {
            this.list=this.list.filter(ie=>ie!=ite)
            Log(this.list)

            if(this.tile==ite&&this.loadState=='exist'){
                this.close()
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
        anPutConfig(this.toJson())
        java.ijbridge(JSON.stringify({command:'infost',params:"文件重命名成功！"}))
    }

    //关闭文件
    close() {
        this.loadState = 'none'
        this.tile = unopen_name
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

    //获取文件内容
    /**
     * 
     * @param {String} ite 
     */
    getContents() {
        if(this.loadState=='exist'&&this.tile!=unopen_name){
            return java.direader(this.tile,'mind')
        }
        return ''
    }
    //转换JSON
    toJson(){
        var jsons={loadState:this.loadState,tile:this.tile,list:this.list}
        Log(jsons)
        return JSON.stringify(jsons)
    }
}
/**配置文件操作函数*/