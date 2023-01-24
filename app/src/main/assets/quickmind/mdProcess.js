(()=>{
  var mdis$=this||window
  
  //import variable Transform Class
  const {Transformer,Markmap,deriveOptions} =window.markmap;

  const svgClear=(id,options)=>{
    var optionz=options||{}
    var he=(optionz.height!=undefined)?options.height:540
    var wid=(optionz.width!=undefined)?options.width:'100%'
    var svg_root=$(id)

    svg_root.innerHTML=""
    var svg_=`<svg id="markmap" xmlns="http://www.w3.org/2000/svg" class="w-sreen h-sreen leading-none markmap" width="${wid}" height="${he}px"></svg>`
    svg_root.innerHTML=svg_;
  }


  mdis$.transformMd=(transformer,content)=>{
    var enabled = {};

    const result = transformer.transform(content);
    const keys = Object.keys(result.features).filter((key) => !enabled[key]);
    keys.forEach((key) => {
      enabled[key] = true;
    });
    const { styles, scripts } = transformer.getAssets(keys);
    const { markmap } = window;
    if (styles) markmap.loadCSS(styles);
    if (scripts) markmap.loadJS(scripts);
    return result;
  }
  
  mdis$.MarkdownParse=(mds)=>{
    
    // 1. transform Markdown
    const transformer=new Transformer()

    // convert markdown to markmap
    
    const { root, frontmatter } = transformMd(transformer, mds);
    const markmapOptions = frontmatter?.markmap;
    const frontmatterOptions = deriveOptions(markmapOptions);
    

    // 3. create markmap
    const map_=Markmap.create("#markmap",frontmatterOptions, root);

    createToolbar('svg_toolbar',map_)
  }

  mdis$.createToolbar=(id,mm)=>{
    const {Toolbar}=window.markmap
    const el = Toolbar.create(mm);
    el.setAttribute('class','mm-toolbar d-flex align-items-center w-100 justify-content-end border-1 border-bottom')
    $(id).innerHTML=''
    $(id).append(el)
  }
  mdis$.quickPreview=(id,mds,options)=>{
    svgClear(id,options)
    MarkdownParse(mds)
  }


  mdis$.clear=()=>{
    $("markmap").value=''
  }

  mdis$.MarkdownDynamic=(id,obj,options)=>{
    svgClear(id,options)
    if(obj.value.length==0) clear()
    else MarkdownParse(obj.value)
  }
  
})()