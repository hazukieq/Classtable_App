/**
 * 定义一个名为fullM的函数，用于实现全屏模态窗口的显示和隐藏功能，以及调整markmap图表的高度。
 * 该函数不接受参数，也不返回任何值。
 */
const fullM=()=>{
    // 定义工具栏高度
    const fmarkmap_tool_height=45
    // 计算全屏模态下markmap的高度，占屏幕可用高度的90%
    const fmarkmap_height=window.screen.availHeight-fmarkmap_tool_height-26

    // 获取名为'helpModal'的模态框对象
    var fullModal = $('helpModal')

    // 当模态框显示时的事件处理
    fullModal.on('show.bs.modal',(e)=>{
        // 获取markmap容器和SVG元素
        const mmp=$('mmp')
        const mmp_svg=mmp.querySelector('#markmap')
        // 设置markmap SVG的高度为计算得到的高度
        mmp_svg.style.height=fmarkmap_height

        // 清空'fmmp_root'容器的内容，并将markmap容器移入
        const fmmp_root=$('fmmp_root')
        fmmp_root.innerHTML=''
        fmmp_root.appendChild(mmp)
    })

    // 当模态框隐藏时的事件处理
    fullModal.on('hidden.bs.modal',(e)=>{
        // 获取markmap容器和SVG元素
        const mmp=$('mmp')
        const mmp_svg=mmp.querySelector('#markmap')
        // 还原markmap SVG的高度为初始高度
        mmp_svg.style.height=markmap_height

        // 清空'fmmp_root'容器的内容，并将markmap容器移回原位置
        const mmp_root=$('mmp_root')
        const fmmp_root=$('fmmp_root')    
        fmmp_root.innerHTML=''
        mmp_root.appendChild(mmp)
    })
}


// 调用fullM函数
fullM()