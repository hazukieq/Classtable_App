"use strict";

var _this = void 0;
(function () {
  var mdis$ = _this || window;

  //import variable Transform Class
  var _window$markmap = window.markmap,
    Transformer = _window$markmap.Transformer,
    Markmap = _window$markmap.Markmap,
    deriveOptions = _window$markmap.deriveOptions;
  var svgClear = function svgClear(id, options) {
    var optionz = options || {};
    var he = typeof optionz.height == 'number' ? options.height + 'px' : '100%';
    var wid = typeof optionz.width == 'string' ? options.width : '100%';
    var svg_root = $(id);
    svg_root.innerHTML = "";
    var svg_ = "<svg id=\"markmap\" xmlns=\"http://www.w3.org/2000/svg\" class=\"w-sreen h-sreen leading-none markmap\" width=\"".concat(wid, "\" height=\"").concat(he, "\"></svg>");
    svg_root.innerHTML = svg_;
  };
  mdis$.transformMd = function (transformer, content) {
    var enabled = {};
    var result = transformer.transform(content);
    var keys = Object.keys(result.features).filter(function (key) {
      return !enabled[key];
    });
    keys.forEach(function (key) {
      enabled[key] = true;
    });
    var _transformer$getAsset = transformer.getAssets(keys),
      styles = _transformer$getAsset.styles,
      scripts = _transformer$getAsset.scripts;
    var _window = window,
      markmap = _window.markmap;
    if (styles) markmap.loadCSS(styles);
    if (scripts) markmap.loadJS(scripts);
    return result;
  };
  mdis$.MarkdownParse = function (id, mds) {
    // 1. transform Markdown
    var transformer = new Transformer();

    // convert markdown to markmap
    var _transformMd = transformMd(transformer, mds),
      root = _transformMd.root,
      frontmatter = _transformMd.frontmatter;
    var markmapOptions = frontmatter === null || frontmatter === void 0 ? void 0 : frontmatter.markmap;
    var frontmatterOptions = deriveOptions(markmapOptions);

    // 3. create markmap
    var map_ = Markmap.create('#markmap', frontmatterOptions, root);
    createToolbar(id + '_toolbar', map_);
  };
  mdis$.createToolbar = function (id, mm) {
    var Toolbar = window.markmap.Toolbar;
    var el = Toolbar.create(mm);
    el.setAttribute('class', 'mm-toolbar d-flex align-items-center w-100 justify-content-end border-1 border-bottom');
    $(id).innerHTML = '';
    $(id).append(el);
  };
  mdis$.quickPreview = function (id, mds, options) {
    svgClear(id, options);
    MarkdownParse(mds);
  };
  mdis$.clear = function (id) {
    $(id + '_toolbar').innerHTML = '';
    $(id).innerHTML = "<div class=\"text-center pt-5\"><h4>\u6682\u65E0\u5185\u5BB9\u663E\u793A</h4></div>";
  };
  mdis$.MarkdownDynamic = function (id, obj, options) {
    svgClear(id, options);
    if (obj.value.length == 0) clear(id);else MarkdownParse(id, obj.value);
  };
})();