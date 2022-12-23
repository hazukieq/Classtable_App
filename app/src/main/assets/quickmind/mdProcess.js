//import variable Transform Class
/*const {Transformer,Markmap,Toolbar} =window.markmap;

const getMarkdown2Svg=(mds)=>{
  // 1. transform Markdown
  const transformer=new Transformer()

  // convert markdown to markmap
  const { root, features } = transformer.transform(mds);

  // 2. get assets
  const keys = Object.keys(features).filter((key) => !enabled[key]);
  keys.forEach((key) => enabled[key] = true);

  const { styles, scripts } = transformer.getAssets(keys);
  const { markmap,mm} = window;

  if (styles) markmap.loadCSS(styles);
  if (scripts) markmap.loadJS(scripts);

  // 3. create markmap
  Markmap.create("#markmap", "", root);
}

const clearSvg=()=>{
  var svg_root=obj||$('svg_root')
  svg_root.innerHTML=""
  var svg_=`<svg id="markmap" xmlns="http://www.w3.org/2000/svg" class="w-sreen h-sreen leading-none markmap" style="" width="100%" height="540px"></svg>`
  svg_root.innerHTML=svg_;
  }
const getMarkdownz=(obj)=>{
var z_=obj.value
clearSvg()
getMarkdown2Svg(z_)
}*/

(()=>{
  var mdis$=this||window
  //import variable Transform Class
  const {Transformer,Markmap} =window.markmap;

  const svgClear=(id,options)=>{
    var optionz=options||{}
    var he=(optionz.height!=undefined)?options.height:540
    var wid=(optionz.width!=undefined)?options.width:'100%'
    var svg_root=$(id)

    svg_root.innerHTML=""
    var svg_=`<svg id="markmap" xmlns="http://www.w3.org/2000/svg" class="w-sreen h-sreen leading-none markmap" style="" width="${wid}" height="${he}px"></svg>`
    svg_root.innerHTML=svg_;
  }

  mdis$.MarkdownParse=(mds)=>{
    
    // 1. transform Markdown
    const transformer=new Transformer()

    // convert markdown to markmap
    const { root } = transformer.transform(mds);

    // 2. get assets
    //const keys = Object.keys(features).filter((key) => !enabled[key]);
    //keys.forEach((key) => enabled[key] = true);

    //const { styles, scripts } = transformer.getAssets(keys);
    //const { markmap} = window;

    //if (styles) markmap.loadCSS(styles);
    //if (scripts) markmap.loadJS(scripts);

    // 3. create markmap
    const map_=Markmap.create("#markmap", "", root);

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

  mdis$.MarkdownDynamic=(id,obj,options)=>{
    svgClear(id,options)
    MarkdownParse(obj.value)
  }
  
})()