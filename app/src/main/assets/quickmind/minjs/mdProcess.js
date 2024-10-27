(()=>{
  var mdis$=this||window
  
  //import variable Transform Class
  const {markmap}=window;
  const {Transformer,Markmap,deriveOptions} =markmap;
  
  //svg图片清空
  const svgClear=(id,options)=>{
    var optionz=options||{}
    var he=(typeof optionz.height=='number')?options.height+'px':'100%'
    var wid=(typeof optionz.width=='string')?options.width+'px':'100%'
    var svg_root=$(id)

    svg_root.innerHTML=""
    var svg_=`<svg id="markmap" xmlns="http://www.w3.org/2000/svg" class="w-sreen h-sreen leading-none markmap" width="${wid}" height="${he}"></svg>`
    svg_root.innerHTML=svg_;
  }

  //转换md文本
  const transformMd=(transformer,content)=>{
    const result=transformer.transform(content)
    const assets = transformer.getAssets()
    const {styles,scripts}=assets

    if (styles) markmap.loadCSS(styles);
    if (scripts) markmap.loadJS(scripts,{
	    getMarkmap: ()=>markmap
    });

    return result;
  }
  
 //md文本渲染
  const MarkdownParse=(id,mds)=>{
    // 1. transform Markdown
    const transformer=new Transformer()

    // convert markdown to markmap
    const { root, frontmatter } = transformMd(transformer, mds);
    const markmapOptions = frontmatter?.markmap;
    const frontmatterOptions = deriveOptions(markmapOptions);
    
    // 3. create markmap
    const map_=Markmap.create('#markmap',frontmatterOptions, root);
    createToolbar(id+'_toolbar',map_)
  }
  
  //状态栏
  const createToolbar=(id,mm)=>{
    const {Toolbar}=window.markmap
    const el = Toolbar.create(mm);
    el.setAttribute('class','mm-toolbar d-flex align-items-center w-100 justify-content-end border-1 border-bottom')
    
    $(id).innerHTML=''
    $(id).append(el)
  }

  //清空
  const clear=(id)=>{
    $(id+'_toolbar').innerHTML=''
    $(id).innerHTML=`<div class="text-center pt-5"><h4>暂无内容显示</h4></div>`
  }


  mdis$.quickPreview=(id,mds,options)=>{
    svgClear(id,options)
    MarkdownParse(mds)
  }

  mdis$.MarkdownDynamic=(id,obj,options)=>{
    svgClear(id,options)

    if(obj.value.length==0) clear(id)
    else MarkdownParse(id,obj.value)
  }
  
})()
